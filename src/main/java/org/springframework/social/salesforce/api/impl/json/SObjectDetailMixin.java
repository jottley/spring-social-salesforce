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
package org.springframework.social.salesforce.api.impl.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.social.salesforce.api.Field;
import org.springframework.social.salesforce.api.RecordTypeInfo;
import org.springframework.social.salesforce.api.Relationship;
import org.springframework.social.salesforce.api.SObjectDetail;

import java.util.List;

/**
 * {@link SObjectDetail}
 *
 * @author Umut Utkan
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SObjectDetailMixin extends SObjectSummaryMixin {

    @JsonProperty("fields")
    List<Field> fields;

    @JsonProperty("childRelationships")
    List<Relationship> childRelationships;

    @JsonProperty("listviewable")
    boolean listviewable;

    @JsonProperty("lookupLayoutable")
    boolean lookupLayoutable;

    @JsonProperty("recordTypeInfos")
    List<RecordTypeInfo> recordTypeInfos;

    @JsonProperty("searchLayoutable")
    boolean searchLayoutable;

}
