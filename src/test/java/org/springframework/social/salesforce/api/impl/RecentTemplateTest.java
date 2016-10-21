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

import org.junit.Test;
import org.springframework.social.salesforce.api.ResultItem;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.social.test.client.RequestMatchers.method;
import static org.springframework.social.test.client.RequestMatchers.requestTo;
import static org.springframework.social.test.client.ResponseCreators.withResponse;

/**
 * @author Umut Utkan
 */
public class RecentTemplateTest extends AbstractSalesforceTest {

    @Test
    public void search() {
        mockServer.expect(requestTo("https://na7.salesforce.com/services/data/" + AbstractSalesForceOperations.API_VERSION + "/recent"))
                .andExpect(method(GET))
                .andRespond(withResponse(loadResource("recent.json"), responseHeaders));
        List<ResultItem> items = salesforce.recentOperations().recent();
        assertEquals(9, items.size());
        assertEquals("User", items.get(0).getType());
        assertEquals("/services/data/" + AbstractSalesForceOperations.API_VERSION + "/sobjects/User/005A0000001cRuvIAE", items.get(0).getUrl());
        assertEquals("005A0000001cRuvIAE", items.get(0).getAttributes().get("Id"));
        assertEquals("Umut Utkan", items.get(0).getAttributes().get("Name"));
    }

}
