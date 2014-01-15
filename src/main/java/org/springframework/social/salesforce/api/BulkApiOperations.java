package org.springframework.social.salesforce.api;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.sforce.async.OperationEnum;

/**
 * Defines operations for getting info on the API.
 *
 * @author Umut Utkan
 */
public interface BulkApiOperations {

    List<ApiVersion> getVersions();

    String doBulkOperation(String sObjectType, OperationEnum operation, File csvFile, boolean deleteCsvFile) throws BulkApiException;
    
    String doBulkOperation(String sObjectType, OperationEnum operation, List<Map<String, Object>> records) throws BulkApiException;

}
