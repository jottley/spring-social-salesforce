/**
 * Copyright (C) 2021 https://github.com/jottley/spring-social-salesforce
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

import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

/**
 * Defines operations for calling custom apex exposed rest apis.
 *
 * @author Sanchit Agarwal
 *
 */
public interface CustomApiOperations {

	<T> T postForApexObject(String uriPath, Object request, Class<T> responseType);

	<T> T postForApexObject(String uriPath, Object request, Class<T> responseType, Map<String, ?> uriVariables);

	<T> T getForApexObject(String uriPath, Class<T> responseType);

	<T> T getForApexObject(String uriPath, Class<T> responseType, Map<String, ?> uriVariables);

	<T> T putForApexObject(String uriPath, Object request, Class<T> responseType);

	<T> T putForApexObject(String uriPath, Object request, Class<T> responseType, Map<String, ?> uriVariables);

	<T> T patchForApexObject(String uriPath, Object request, Class<T> responseType);

	<T> T patchForApexObject(String uriPath, Object request, Class<T> responseType, Map<String, ?> uriVariables);

	<T> T deleteForApexObject(String uriPath, Class<T> responseType);

	<T> T deleteForApexObject(String uriPath, Class<T> responseType, Map<String, ?> uriVariablesMap);

	// for custom api - (to get headers and body)
	<T> ResponseEntity<T> executeApexApi(String uriPath, HttpMethod method, HttpEntity<?> request, Class<T> responseType, Map<String, ?> uriVariables);

	<T> ResponseEntity<T> executeApexApi(String uriPath, HttpMethod method, HttpEntity<?> request, Class<T> responseType);	
}