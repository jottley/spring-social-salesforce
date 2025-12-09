# Spring Social Salesforce [![Build Status](https://travis-ci.org/jottley/spring-social-salesforce.svg?branch=master)](https://travis-ci.org/jottley/spring-social-salesforce) [![](https://img.shields.io/static/v1?label=Download&message=1.2.3.RELEASE&color=green)](https://repo.repsy.io/mvn/jottley/spring-social-salesforce/org/springframework/social/spring-social-salesforce/1.2.3.RELEASE)

Spring Social Salesforce is a Spring Social extension that provides connection support and API binding for Salesforce
REST API.

To check out the project and build from source, do the following:

    git clone git://github.com/jottley/spring-social-salesforce.git
    cd spring-social-salesforce
    mvn clean install
    
## Maven
To include in your Maven project, use the following repository and dependency

    <repositories>
    ...
        <repository>
            <id>repsy</id>
            <url>https://repo.repsy.io/mvn/jottley/spring-social-salesforce</url>
        </repository>
    ...
    </repositories>
    
    <dependencies>
    ...
        <dependency>
            <groupId>org.springframework.social</groupId>
            <artifactId>spring-social-salesforce</artifactId>
            <version>1.2.3.RELEASE</version>
        </dependency>
    ...
    </dependencies>
    
## Quickstart
There is a Spring Boot quickstart app available at https://github.com/jottley/spring-social-salesforce-quickstart

## Supported Operations
 - Retrieve all available API versions
 - Retrieve services supported by a specific version of the API
 - Retrieve the list of sObjects
 - Retrieve summary metadata of a sObject
 - Retrieve full metadata of a sObject
 - Retrieve a row from a sObject
 - Retrieve a blob from a row in a sObject
 - Create a new sObject
 - Update an existing sObject
 - Retrieve recent changes feed
 - Execute a SOSL search and retrieve the results (with paging or all)
 - Run a SOQL query and retrieve the results (with paging or all)
   - query results can optionally include deleted records
 - Retrieve user profile
 - Retrieve user status
 - Update user status
 - List the limits of an org
 - Check the current API limit and usage
 - Get a list of Communities (Digital Experience)
 - Get a list of Community (Digital Experience) users 
