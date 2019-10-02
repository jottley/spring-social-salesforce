# Spring Social Salesforce [![Build Status](https://travis-ci.org/jottley/spring-social-salesforce.svg?branch=master)](https://travis-ci.org/jottley/spring-social-salesforce) [ ![Download](https://api.bintray.com/packages/jottley/jottley/spring-social-salesforce/images/download.svg?version=1.2.2.RELEASE) ](https://bintray.com/jottley/jottley/spring-social-salesforce/1.2.2.RELEASE/link)

Spring Social Salesforce is a Spring Social extension that provides connection support and api binding for the Salesforce
REST API.

To check out the project and build from source, do the following:

    git clone git://github.com/jottley/spring-social-salesforce.git
    cd spring-social-salesforce
    mvn clean install
    
## Maven
To include in your maven project use the following repository and dependency

    <repositories>
    ...
		<repository>
			<id>jcenter</id>
			<url>https://jcenter.bintray.com</url>
		</repository>
    ...
	</repositories>
    
    <dependencies>
    ...
        <dependency>
			<groupId>org.springframework.social</groupId>
			<artifactId>spring-social-salesforce</artifactId>
			<version>1.2.2.RELEASE</version>
		</dependency>
    ...
    </dependencies>
    
## Quickstart
There is a spring boot quickstart app available at https://github.com/jottley/spring-social-salesforce-quickstart

## Supported Operations
 - Retrieve all available api versions
 - Retrieve services supported by a specific version of the api
 - Retrieve the list of sObject's
 - Retrieve summary-metadata of an sObject
 - Retrieve full-metadata of an sObject
 - Retrieve a row from an sObject
 - Retrieve a blob from a row in an sObject
 - Create a new sObject
 - Update an existing sObject
 - Retrieve recent changes feed
 - Execute a SOSL search and retrieve the results (with paging or all)
 - Run a SOQL query and retrieve the results (with paging or all)
   - query results can optionally included deleted records
 - Retrieve user profile
 - Retrieve user status
 - Update user status
 - List the limits of an org
 - Check the current API limit and usage
