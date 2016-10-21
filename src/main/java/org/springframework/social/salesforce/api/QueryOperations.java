/**
 * Copyright (C) 2016 https://github.com/jottley/spring-social-salesforce
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.social.salesforce.api;

/**
 * Defines operations for executing SOQL queries.
 *
 * @author Umut Utkan
 */
public interface QueryOperations {

    /**
     * Execute SOQL query and return the first page of results
     */
    QueryResult query(String query);

    /*
     * Retrieve next page of results based on argument from url (usually from query result "nextRecordsUrl")
     */
    QueryResult nextPage(String urlOrToken);
    
    /**
     * Execute SOQL query and return all results.
     */
    QueryResult queryAll(String query);

}
