# Schema Endpoint module for Karyon2

[![Build Status](https://travis-ci.org/scm-spain/karyon2-schema.svg)](https://travis-ci.org/scm-spain/karyon2-schema)

This module for the Netflix framework Karyon adds an endpoint ```[GET] /schema``` that is used to Swagger Documentation.

## Documentation

Simply add this module into Modules specification for your AppServer.

```
@KaryonBootstrap(name = "AppServer", healthcheck = AlwaysHealthyHealthCheck.class)
@Modules(include = {
    ...
    SchemaEndPointModule.class
})
public interface AppServerForTesting {
    ...
}
```

Then, create a **Schema.json** in your resources folder and fill with your micro service's Swagger Documentation. 

## Gradle

Add dependency as follows:

```
    compile 'com.scmspain:karyon2-schema:0.1.0'
```

## AppServer.properties

Make sure you do not set a too restrictive base package in order to get SchemaController also included and enabled.

```
com.scmspain.karyon.rest.property.packages=com.scmspain
```