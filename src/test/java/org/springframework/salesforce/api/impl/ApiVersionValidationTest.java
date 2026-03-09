/**
 * Copyright (C) 2017-2026 https://github.com/jottley/spring-social-salesforce
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
package org.springframework.salesforce.api.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Test;
import org.springframework.salesforce.api.InvalidSalesforceApiVersionException;

/**
 * Test class to validate the new API version validation logic
 * that enforces minimum version of v31.0.
 */
public class ApiVersionValidationTest extends AbstractSalesforceTest {

    @Test
    public void testValidVersionsAccepted() throws InvalidSalesforceApiVersionException {
        // Test minimum version
        salesforce.apiOperations().setVersion("v31.0");
        assertEquals("v31.0", salesforce.apiOperations().getVersion());
        
        // Test current version
        salesforce.apiOperations().setVersion("v66.0");
        assertEquals("v66.0", salesforce.apiOperations().getVersion());
        
        // Test newer version (hypothetical future version)
        salesforce.apiOperations().setVersion("v67.0");
        assertEquals("v67.0", salesforce.apiOperations().getVersion());
    }

    @Test
    public void testDeprecatedVersionsRejected() {
        // Test version below minimum
        try {
            salesforce.apiOperations().setVersion("v30.0");
            fail("Should have thrown InvalidSalesforceApiVersionException for v30.0");
        } catch (InvalidSalesforceApiVersionException e) {
            assertEquals("v30.0 is not a valid Salesforce Api version.", e.getMessage());
        }
        
        // Test very old version
        try {
            salesforce.apiOperations().setVersion("v20.0");
            fail("Should have thrown InvalidSalesforceApiVersionException for v20.0");
        } catch (InvalidSalesforceApiVersionException e) {
            assertEquals("v20.0 is not a valid Salesforce Api version.", e.getMessage());
        }
    }

    @Test
    public void testInvalidFormatRejected() {
        try {
            salesforce.apiOperations().setVersion("62.0");
            fail("Should have thrown InvalidSalesforceApiVersionException for invalid format");
        } catch (InvalidSalesforceApiVersionException e) {
            assertEquals("62.0 is not a valid Salesforce Api version.", e.getMessage());
        }
    }

    @Test
    public void testDefaultVersionIsSet() {
        // Default version should be v66.0
        String version = salesforce.apiOperations().getVersion();
        assertEquals("v66.0", version);
    }
}