/**
 * Copyright (C) 2019 https://github.com/jottley/spring-social-salesforce
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

import org.apache.commons.lang3.StringUtils;
import org.springframework.social.salesforce.api.LimitsOperations;
import org.springframework.social.salesforce.api.LimitsResults;
import org.springframework.social.salesforce.api.Salesforce;
import org.springframework.web.client.RestTemplate;

/**
 * Default implementation of the Limits API. Also includes tracking of the
 * current API usage as returned from salesforce in each API call in the
 * Sforce-Limit-Info header
 * 
 * @author Jared Ottley
 */
public class LimitsOperationsTemplate extends AbstractSalesForceOperations<Salesforce> implements LimitsOperations {

    private RestTemplate restTemplate;
    private int currentMax = 0;
    private int currentUsed = 0;

    private final static String API_USAGE = "api-usage";

    public LimitsOperationsTemplate(Salesforce api, RestTemplate restTemplate) {
        super(api);
        this.restTemplate = restTemplate;
    }

    @Override
    public LimitsResults getLimits() {
        requireAuthorization();
        LimitsResults limitResults = restTemplate.getForObject(api.getBaseUrl() + "/{version}/limits",
                LimitsResults.class, getVersion());
        return limitResults;
    }

    @Override
    public int getDailyApiLimit() {
        return currentMax;
    }

    @Override
    public int getDailyApiUsed() {
        return currentUsed;
    }

    public void setCurrentDailyApiLimits(String currentUsage) {
        if (StringUtils.isNotBlank(currentUsage) && currentUsage.startsWith(API_USAGE)) {
            String rates = currentUsage.split("=")[1];
            String[] ratesSplit = rates.split("/");

            this.currentUsed = Integer.parseInt(ratesSplit[0]);
            this.currentMax = Integer.parseInt(ratesSplit[1]);
        }
    }

}