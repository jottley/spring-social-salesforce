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
package org.springframework.social.salesforce.api.impl;

import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.social.salesforce.api.CustomApiOperations;
import org.springframework.social.salesforce.api.Salesforce;
import org.springframework.web.client.RestTemplate;

/**
 * Defines operations for calling Apex based custom rest apis.
 *
 * @author Sanchit Agarwal
 *
 */
public class CustomApiTemplate extends AbstractSalesForceOperations<Salesforce> implements CustomApiOperations  {

	private RestTemplate restTemplate;

	public CustomApiTemplate(Salesforce api, RestTemplate restTemplate) {
		super(api);
		this.restTemplate = restTemplate;
	}

	protected String createUriPath(String uriPath) {
		return this.api.getInstanceUrl() + "/services/apexrest" + uriPath;
	}

	@Override
	public <T> T postForApexObject(String uriPath, Object request, Class<T> responseType) {
		requireAuthorization();
		return this.restTemplate.postForObject(this.createUriPath(uriPath), request, responseType);
	}

	@Override
	public <T> T postForApexObject(String uriPath, Object request, Class<T> responseType, Map<String, ?> uriVariables) {
		requireAuthorization();
		return this.restTemplate.postForObject(this.createUriPath(uriPath), request, responseType, uriVariables);
	}

	@Override
	public <T> T getForApexObject(String uriPath, Class<T> responseType) {
		requireAuthorization();
		return this.restTemplate.getForObject(this.createUriPath(uriPath), responseType);
	}

	@Override
	public <T> T getForApexObject(String uriPath, Class<T> responseType, Map<String, ?> uriVariables) {
		requireAuthorization();
		return this.restTemplate.getForObject(this.createUriPath(uriPath), responseType, uriVariables);
	}

	@Override
	public <T> T putForApexObject(String uriPath, Object request, Class<T> responseType) {
		requireAuthorization();
		ResponseEntity<T> entity = this.restTemplate.exchange(this.createUriPath(uriPath), HttpMethod.PUT, request, responseType);
		return entity.getBody();
	}

	@Override
	public <T> T putForApexObject(String uriPath, Object request, Class<T> responseType, Map<String, ?> uriVariables) {
		requireAuthorization();				
		ResponseEntity<T> entity = this.restTemplate.exchange(this.createUriPath(uriPath), HttpMethod.PUT, request, responseType, uriVariables);
		return entity.getBody();
	}

	@Override
	public <T> T patchForApexObject(String uriPath, Object request, Class<T> responseType) {
		requireAuthorization();
		return this.restTemplate.patchForObject(this.createUriPath(uriPath), request, responseType);
	}

	@Override
	public <T> T patchForApexObject(String uriPath, Object request, Class<T> responseType, Map<String, ?> uriVariables) {
		requireAuthorization();
		return this.restTemplate.patchForObject(this.createUriPath(uriPath), request, responseType, uriVariables);
	}

	@Override
	public <T> T deleteForApexObject(String uriPath, Class<T> responseType) {
		requireAuthorization();
		ResponseEntity<T> entity =  this.restTemplate.exchange(this.createUriPath(uriPath), HttpMethod.DELETE, HttpEntity.EMPTY, responseType);
		return entity.getBody();
	}

	@Override
	public <T> T deleteForApexObject(String uriPath, Class<T> responseType, Map<String, ?> uriVariables) {
		requireAuthorization();
		ResponseEntity<T> entity = this.restTemplate.exchange(this.createUriPath(uriPath), HttpMethod.DELETE, HttpEntity.EMPTY, responseType, uriVariables);
		return entity.getBody();
	}

	@Override
	public <T> ResponseEntity<T> executeApexApi(String uriPath, HttpMethod method, HttpEntity<?> request, Class<T> responseType) {
		requireAuthorization();
		return this.restTemplate.exchange(this.createUriPath(uriPath), method, request, responseType);		
	}	

	@Override
	public <T> ResponseEntity<T> executeApexApi(String uriPath, HttpMethod method, HttpEntity<?> request, Class<T> responseType, Map<String, ?> uriVariables) {
		requireAuthorization();
		return this.restTemplate.exchange(this.createUriPath(uriPath), method, request, responseType, uriVariables);		
	}
}
