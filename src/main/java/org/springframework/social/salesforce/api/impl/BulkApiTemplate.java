package org.springframework.social.salesforce.api.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.salesforce.api.ApiVersion;
import org.springframework.social.salesforce.api.BulkApiException;
import org.springframework.social.salesforce.api.BulkApiOperations;
import org.springframework.social.salesforce.api.Salesforce;
import org.springframework.social.support.URIBuilder;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.sforce.async.AsyncApiException;
import com.sforce.async.BatchInfo;
import com.sforce.async.BatchStateEnum;
import com.sforce.async.BulkConnection;
import com.sforce.async.CSVReader;
import com.sforce.async.ContentType;
import com.sforce.async.JobInfo;
import com.sforce.async.JobStateEnum;
import com.sforce.async.OperationEnum;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;

/**
 * Default implementation of BulkApiOperations.
 * 
 * @author Brian Taylor
 */
public class BulkApiTemplate extends AbstractSalesForceOperations<Salesforce> implements BulkApiOperations {
  
  private final Logger logger = LoggerFactory.getLogger(BulkApiTemplate.class);

  private RestTemplate restTemplate;
  private String accessToken;
  private String apiVersion = "29.0";
  private boolean HTTP_DEBUGGING = false;
  private List<String> errors = new ArrayList<String>();

  public BulkApiTemplate(Salesforce api, RestTemplate restTemplate, String accessToken) {
    super(api);
    this.restTemplate = restTemplate;
    this.accessToken = accessToken;
  }

  public List<ApiVersion> getVersions() {
    URI uri = URIBuilder.fromUri(api.getBaseUrl()).build();
    JsonNode dataNode = restTemplate.getForObject(uri, JsonNode.class);
    return api.readList(dataNode, ApiVersion.class);
  }

  public String doBulkOperation(String sObjectType, OperationEnum operation, File csvFile, boolean deleteCsvFile) 
    throws BulkApiException {
    requireAuthorization();
    errors.clear();
    JsonNode node = restTemplate.getForObject(api.getIdentityServiceUrl(), JsonNode.class);
    String soapEndpoint = node.get("urls").findValuesAsText("enterprise").get(0);
    String bulkUrl = soapEndpoint.substring(0, soapEndpoint.indexOf("Soap/")) + "async/" + apiVersion;
    BulkConnection connection;
    JobInfo jobInfo = new JobInfo();
    try {
      connection = getBulkConnection(bulkUrl);
      jobInfo = createJob(sObjectType, operation, connection);
      List<BatchInfo> batchInfoList = createBatchesFromCSVFile(connection, jobInfo, csvFile);
      closeJob(connection, jobInfo.getId());
      awaitCompletion(connection, jobInfo, batchInfoList);
      checkResults(connection, jobInfo, batchInfoList);
      if (deleteCsvFile) {
        csvFile.delete();
      }
    } catch (ConnectionException e) {
      throw new BulkApiException("Error connecting to bulk api: " + e.getMessage(), e);
    } catch (AsyncApiException e) {
      throw new BulkApiException("Bulk api exception: " + e.getMessage(), e);
    } catch (IOException e) {
      throw new BulkApiException("IO exception: " + e.getMessage(), e);
    }
    return jobInfo.getId();
  }

  public String doBulkOperation(String sObjectType, OperationEnum operation, List<Map<String, Object>> records) throws BulkApiException {

    File csvFile;
    Writer writer = null;
    try {
      csvFile = File.createTempFile("sfBulkOperation", ".csv");
      writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFile)));
      int i = 0;
      for (Map<String, Object> row : records) {
        if (i == 0) {
          writeHeader(writer, row.keySet());
          i++;
        }
        writeData(writer, row);
      }
    } catch (IOException e) {
      throw new BulkApiException("IO exception creating csv file: " + e.getMessage(), e);
    } finally {
      try {
        writer.close();
      } catch (IOException e) {
        throw new BulkApiException("IO exception closing writer: " + e.getMessage(), e);
      }
    }
    return doBulkOperation(sObjectType, operation, csvFile, true);
  }

  private void writeHeader(Writer writer, Set<String> keys) throws IOException {
    String header = "";
    for (String key : keys) {
      header += key + ",";
    }
    header = header.substring(0, header.lastIndexOf(",")) + "\n";
    writer.write(header);
  }

  private void writeData(Writer writer, Map<String, Object> map) throws IOException {
    Set<String> keys = map.keySet();
    String data = "";
    for (String key : keys) {
      data += map.get(key) + ",";
    }
    data = data.substring(0, data.lastIndexOf(",")) + "\n";
    writer.write(data);
  }

  private List<BatchInfo> createBatchesFromCSVFile(BulkConnection connection, JobInfo jobInfo, File csvFile)
      throws IOException, AsyncApiException {
    logger.info("Creating batch job with jobId: {}", jobInfo.getId());
    List<BatchInfo> batchInfos = new ArrayList<BatchInfo>();
    BufferedReader rdr = new BufferedReader(new InputStreamReader(new FileInputStream(csvFile)));
    // read the CSV header row
    byte[] headerBytes = (rdr.readLine() + "\n").getBytes("UTF-8");
    int headerBytesLength = headerBytes.length;
    File tmpFile = File.createTempFile("bulkAPIInsert", ".csv");

    // Split the CSV file into multiple batches
    try {
      FileOutputStream tmpOut = new FileOutputStream(tmpFile);
      int maxBytesPerBatch = 10000000; // 10 million bytes per batch
      int maxRowsPerBatch = 10000; // 10 thousand rows per batch
      int currentBytes = 0;
      int currentLines = 0;
      String nextLine;
      while ((nextLine = rdr.readLine()) != null) {
        byte[] bytes = (nextLine + "\n").getBytes("UTF-8");
        // Create a new batch when our batch size limit is reached
        if (currentBytes + bytes.length > maxBytesPerBatch || currentLines > maxRowsPerBatch) {
          createBatch(tmpOut, tmpFile, batchInfos, connection, jobInfo);
          currentBytes = 0;
          currentLines = 0;
        }
        if (currentBytes == 0) {
          tmpOut = new FileOutputStream(tmpFile);
          tmpOut.write(headerBytes);
          currentBytes = headerBytesLength;
          currentLines = 1;
        }
        tmpOut.write(bytes);
        currentBytes += bytes.length;
        currentLines++;
      }
      // Finished processing all rows
      // Create a final batch for any remaining data
      if (currentLines > 1) {
        createBatch(tmpOut, tmpFile, batchInfos, connection, jobInfo);
      }
    } finally {
      rdr.close();
      tmpFile.delete();
    }
    return batchInfos;
  }

  private void createBatch(FileOutputStream tmpOut, File tmpFile, List<BatchInfo> batchInfos,
      BulkConnection connection, JobInfo jobInfo) throws IOException, AsyncApiException {
    tmpOut.flush();
    tmpOut.close();
    FileInputStream tmpInputStream = new FileInputStream(tmpFile);
    try {
      BatchInfo batchInfo = connection.createBatchFromStream(jobInfo, tmpInputStream);
      batchInfos.add(batchInfo);

    } finally {
      tmpInputStream.close();
    }
  }

  private BulkConnection getBulkConnection(String bulkUrl) throws ConnectionException, AsyncApiException {
    ConnectorConfig config = new ConnectorConfig();
    config.setSessionId(accessToken);
    config.setRestEndpoint(bulkUrl);
    // This should only be false when doing debugging.
    config.setCompression(HTTP_DEBUGGING);
    // Set this to true to see HTTP requests and responses on stdout
    config.setTraceMessage(HTTP_DEBUGGING);
    BulkConnection connection = new BulkConnection(config);
    return connection;
  }

  private JobInfo createJob(String sobjectType, OperationEnum operation, BulkConnection connection)
      throws AsyncApiException {
    JobInfo job = new JobInfo();
    job.setObject(sobjectType);
    job.setOperation(operation);
    job.setContentType(ContentType.CSV);
    job = connection.createJob(job);

    return job;
  }

  private void closeJob(BulkConnection connection, String jobId) throws AsyncApiException {
    JobInfo job = new JobInfo();
    job.setId(jobId);
    job.setState(JobStateEnum.Closed);
    connection.updateJob(job);
  }

  private void awaitCompletion(BulkConnection connection, JobInfo job,
      List<BatchInfo> batchInfoList)
      throws AsyncApiException {
    long sleepTime = 0L;
    Set<String> incomplete = new HashSet<String>();
    for (BatchInfo bi : batchInfoList) {
      incomplete.add(bi.getId());
    }
    while (!incomplete.isEmpty()) {
      try {
        Thread.sleep(sleepTime);
      } catch (InterruptedException e) {
      }
      logger.info("Awaiting results...{}}", incomplete.size());
      sleepTime = 10000L;
      BatchInfo[] statusList =
          connection.getBatchInfoList(job.getId()).getBatchInfo();
      for (BatchInfo b : statusList) {
        if (b.getState() == BatchStateEnum.Completed
            || b.getState() == BatchStateEnum.Failed) {
          if (incomplete.remove(b.getId())) {
            logger.info("BATCH STATUS:\n{}", b);
          }
        }
      }
    }
  }

  private void checkResults(BulkConnection connection, JobInfo job, List<BatchInfo> batchInfoList) throws AsyncApiException, 
  IOException, BulkApiException {
    // batchInfoList was populated when batches were created and submitted
    for (BatchInfo b : batchInfoList) {
      CSVReader rdr = new CSVReader(connection.getBatchResultStream(job.getId(), b.getId()));
      List<String> resultHeader = rdr.nextRecord();
      int resultCols = resultHeader.size();

      List<String> row;
      while ((row = rdr.nextRecord()) != null) {
        Map<String, String> resultInfo = new HashMap<String, String>();
        for (int i = 0; i < resultCols; i++) {
          resultInfo.put(resultHeader.get(i), row.get(i));
        }
        boolean success = Boolean.valueOf(resultInfo.get("Success"));
        String id = resultInfo.get("Id");
        String error = resultInfo.get("Error");
        if (!success) {
          String message = "Row with Id: " + id + " failed with error: " + error;
          errors.add(message);
          logger.error(message);
        }
      }
      if (!errors.isEmpty()) {
        throw new BulkApiException("Row errors occurred during bulk operation - JobId: " + job.getId(), errors);
      }
    }
  }
}
