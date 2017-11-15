/**
 * Copyright (C) 2017 https://github.com/jottley/spring-social-salesforce
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
package org.springframework.social.salesforce.config.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.xml.ApiHelper;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.salesforce.api.Salesforce;

/**
 * 
 * @author Jared Ottley
 *
 */
public class SalesforceApiHelper implements ApiHelper<Salesforce>
{
    private final UsersConnectionRepository usersConnectionRepository;

    private final UserIdSource userIdSource;

    private SalesforceApiHelper(UsersConnectionRepository usersConnectionRepository, UserIdSource userIdSource) {
        this.usersConnectionRepository = usersConnectionRepository;
        this.userIdSource = userIdSource;       
    }
    
    
    @Override
    public Salesforce getApi()
    {
        Connection<Salesforce> connection = usersConnectionRepository.createConnectionRepository(userIdSource.getUserId()).findPrimaryConnection(Salesforce.class);
        if (logger.isDebugEnabled() && connection == null) {
            logger.debug("No current connection; Returning default TwitterTemplate instance.");
        }
        return connection != null ? connection.getApi() : null;
    }

    private final static Log logger = LogFactory.getLog(SalesforceApiHelper.class);
    
}
