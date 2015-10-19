package org.springframework.social.salesforce.api.impl;

import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.social.salesforce.api.ApexRestOperations;
import org.springframework.social.salesforce.api.Salesforce;
import org.springframework.web.client.RestTemplate;

public class ApexRestTemplate extends AbstractSalesForceOperations<Salesforce> implements ApexRestOperations {

  private RestTemplate restTemplate;
  
  public ApexRestTemplate(Salesforce api, RestTemplate restTemplate) {
    super(api);
    this.restTemplate = restTemplate;
  }

  @Override
  public String post(String apexRestService, String json) {
    requireAuthorization();
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);     
    return restTemplate.postForObject(api.getInstanceUrl()+"/services/apexrest/"+apexRestService, new HttpEntity<String>(json,headers), String.class);
  }

  @Override
  public String get(String apexRestService, Map<String, ?> urlVariables) {
    requireAuthorization();
    return restTemplate.getForObject(api.getInstanceUrl()+"/services/apexrest/"+apexRestService, String.class, urlVariables);
  }  
}