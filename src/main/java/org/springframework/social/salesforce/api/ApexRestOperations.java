package org.springframework.social.salesforce.api;

import java.util.Map;

public interface ApexRestOperations {
  
  /**
   * Executes a POST on the desired Apex Rest Service
   */
  String post(String apexRestService, String json);
  
  /**
   * Executes a GET on the desired Apex Rest Services
   */
  String get(String apexRestService, Map<String, ?> urlVariables);  
}
