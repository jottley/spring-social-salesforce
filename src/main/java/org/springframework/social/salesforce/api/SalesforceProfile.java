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
package org.springframework.social.salesforce.api;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Umut Utkan
 * @author Alexandra Leahu
 * @author Jared ottley
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SalesforceProfile implements Serializable {
    protected String id;

    protected String email;

    protected String firstName;

    protected String lastName;

    protected Photo photo;
    
    protected String name;


    /*public SalesforceProfile(String id, String firstName, String lastName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }*/


    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Photo getPhoto() {
        return this.photo;
    }

    public String getName() {
        return this.name;
    }

    public String getUsername() {
        return this.id;
    }
}
