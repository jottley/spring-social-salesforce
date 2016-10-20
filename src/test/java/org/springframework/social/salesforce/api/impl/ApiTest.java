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
package org.springframework.social.salesforce.api.impl;

import org.junit.Ignore;
import org.springframework.social.salesforce.api.ApiVersion;
import org.springframework.social.salesforce.api.QueryResult;
import org.springframework.social.salesforce.api.ResultItem;
import org.springframework.social.salesforce.api.Salesforce;
import org.springframework.social.salesforce.client.BaseSalesforceFactory;
import org.springframework.social.salesforce.client.SalesforceFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * This is a test that fully test the API over a real account.
 *
 * @author Umut Utkan
 */
@Ignore
public class ApiTest {

    private static final String AUTH_URL = "https://na7.salesforce.com/services/oauth2/token";


    public static void main(String args[]) throws IOException {
        BufferedReader br = null;
        if (args.length > 0) {
            br = new BufferedReader(new FileReader(args[0]));
        } else {
            br = new BufferedReader(new InputStreamReader(System.in));
        }

        System.out.println("Enter your SF client id: ");
        final String clientid = br.readLine();
        System.out.println("Entered: " + clientid);

        System.out.println("Enter your SF client secret: ");
        final String clientSecret = br.readLine();
        System.out.println("Entered: " + clientSecret);

        System.out.println("Enter your SF username: ");
        final String username = br.readLine();
        System.out.println("Entered: " + username);

        System.out.println("Enter your SF password: ");
        final String password = br.readLine();
        System.out.println("Entered: " + password);

        System.out.println("Enter your secret token: ");
        final String secretToken = br.readLine();
        System.out.println("Entered: " + secretToken);

        SalesforceFactory factory = new BaseSalesforceFactory(clientid, clientSecret);
        Salesforce template = factory.create(username, password, secretToken);

        testMetaApiOperations(template);

        System.out.println("\n\n");

        testChatterOperations(template);

        System.out.println("\n\n");

        testSObjectsOperations(template);
    }

    public static void testMetaApiOperations(Salesforce api) {
        System.out.println("Supported API Versions:");

        for (ApiVersion apiVersion : api.apiOperations().getVersions()) {

            System.out.println(apiVersion);
            System.out.println("Services supported by this version:");
            System.out.println(api.apiOperations().getServices(apiVersion.getVersion()));
        }
    }

    public static void testSObjectsOperations(Salesforce api) {
        System.out.println("SObjects:");

        for (Map sobject : api.sObjectsOperations().getSObjects()) {

            System.out.println(sobject);

            String sobjectName = sobject.get("name").toString();

            System.out.println(sobjectName + " : Calling describe");
            System.out.println(sobjectName + " : " + api.sObjectsOperations().describeSObject(sobjectName));

            if ((Boolean) sobject.get("queryable") && (Boolean) sobject.get("replicateable")) {
                try {
                    QueryResult queryResult = api.queryOperations().query("SELECT Id from " + sobjectName);
                    if (queryResult.getTotalSize() > 0) {
                        ResultItem first = queryResult.getRecords().get(0);

                        System.out.println(sobjectName + " : The id the of the first row is " + first.getAttributes().get("Id"));

                        Map row = api.sObjectsOperations().getRow(first.getType(), (String) first.getAttributes().get("Id"));

                        System.out.println(sobjectName + " : Printing the first row.");
                        System.out.println(sobjectName + " : " + row);
                    }

                } catch (Exception e) {
                    System.out.println("WARNING: Possibly " + sobjectName + " does not allow queries without filter.");
                }
            }
        }
    }

    public static void testChatterOperations(Salesforce template) {
        System.out.println("Chatter API:");
        System.out.println("Current user's latest status:");
        System.out.println(template.chatterOperations().getStatus());

        System.out.println("Updated current user's status, new status:");
        System.out.println(template.chatterOperations().updateStatus("Updated status via #spring-social-salesforce!"));
    }

}
