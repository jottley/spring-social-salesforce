/**
 * Copyright (C) 2019-2026 https://github.com/jottley/spring-social-salesforce
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
package org.springframework.salesforce.api;

/**
 * Exception thrown when attempting to use a deprecated API version that 
 * returns HTTP 410 GONE from Salesforce. This typically occurs when trying
 * to access API versions older than v31.0 which have been retired.
 */
public class DeprecatedApiVersionException extends ApiException {

    private final String deprecatedVersion;
    private final String minimumSupportedVersion;
    private final String currentVersion;

    public DeprecatedApiVersionException(String deprecatedVersion, 
                                       String minimumSupportedVersion, 
                                       String currentVersion) {
        super(buildMessage(deprecatedVersion, minimumSupportedVersion, currentVersion));
        this.deprecatedVersion = deprecatedVersion;
        this.minimumSupportedVersion = minimumSupportedVersion;
        this.currentVersion = currentVersion;
    }

    public DeprecatedApiVersionException(String deprecatedVersion, 
                                       String minimumSupportedVersion, 
                                       String currentVersion, 
                                       Throwable cause) {
        super(buildMessage(deprecatedVersion, minimumSupportedVersion, currentVersion), cause);
        this.deprecatedVersion = deprecatedVersion;
        this.minimumSupportedVersion = minimumSupportedVersion;
        this.currentVersion = currentVersion;
    }

    private static String buildMessage(String deprecatedVersion, 
                                     String minimumSupportedVersion, 
                                     String currentVersion) {
        return String.format(
            "API version '%s' has been deprecated and is no longer supported by Salesforce. " +
            "Please upgrade to a supported version (minimum: %s, current: %s). " +
            "See https://help.salesforce.com/s/articleView?id=000389618 for more information.",
            deprecatedVersion, minimumSupportedVersion, currentVersion
        );
    }

    public String getDeprecatedVersion() {
        return deprecatedVersion;
    }

    public String getMinimumSupportedVersion() {
        return minimumSupportedVersion;
    }

    public String getCurrentVersion() {
        return currentVersion;
    }
}