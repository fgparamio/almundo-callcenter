# CallCenter Almundo Spring Boot Rest API

## Overview

A Spring Boot application used to expose a CallCenter Almundo as RESTful API

Application built with:

```
Java version "1.8.0_121"
Apache Maven 3.2.3 
Spring Tool Suite Version: 3.9.2.RELEASE

```

## 

Configure your threads implementation application.yaml file

```
callcenter.numThreads = 10
callcenter.minCallTime = 5 
callcenter.maxCallTime = 10
```

## Model UML Basic

```
Operator   <extends>  Employee 
Supervisor <extends>  Employee
Director   <extends>  Employee 

PriorityType(Integer priority) => OPERATOR(1), SUPERVISOR(2), DIRECTOR(3)

Employee attributes:
  - String name
  - PriorityType priorityType
  
```

## Main Classes

```
AlmundoCallCenterController => Main App Controller with the following methods:

  - (POST)    /operator    <== Create a new Operator in System (high priority - 1)
  - (POST)    /supervisor  <== Create a new Supervisor in System (priority - 2)
  - (POST)    /director    <== Create a new Director in System (priority - 3)
  - (POST)    /call        <== Simulate have a calling 
  - (DELETE)  /employees   <== Delete all employees from System
 

EmployeeService => PriorityBlockingQueue Manager (Employee has Comparable interface)

Dispatcher      => Process calls with a ConcurrentTaskExecutor <= ThreadPoolTaskExecutor 

ThreadPoolTaskExecutor => callcenter.numThreads = 10 (Max Pool Size and Core Size)

Application     =>  Spring Boot Application Configuration

AlmundoCallCenterControllerTest  =>  Tests the correct functioning of the required functionality

```

## API Rest Examples

- Insert a new Operator

```
POST /almundo/v1/callcenter/operator
Accept: application/json
Content-Type: application/json

{"name":"OPERATOR-1"}

RESPONSE: HTTP 201 (Created)
```

- Insert a new Supervisor

```
POST /almundo/v1/callcenter/supervisor
Accept: application/json
Content-Type: application/json

{"name":"SUPERVISOR-1"}

RESPONSE: HTTP 201 (Created)
```

- Insert a new Director

```
POST /almundo/v1/callcenter/director
Accept: application/json
Content-Type: application/json

{"name":"DIRECTOR-1"}

RESPONSE: HTTP 201 (Created)
```

- Simulate a Call 

```
POST /almundo/v1/callcenter/call
Content-Type: application/x-www-form-urlencoded

message=LLamando a Almundo CallCenter

RESPONSE: HTTP 200 (OK)
```

- Delete all Employees

```
DELETE /almundo/v1/callcenter/employees

RESPONSE: HTTP 200 (OK)
```


## Build

To build callcenter-almundo as a war file, run:

```
mvn clean package;

java -jar target/spring-boot-call-center-0.5.0.war (To run spring-boot App)
```

## Run

To start the application, run:

```
mvn spring-boot:run -Drun.arguments="spring.profiles.active=test"

```

### Verify Swagger

To verify the application is running, visit:

```
http://localhost:8090/swagger-ui.html
```

## Testing

To test Almundo CallCenter Application

```
mvn test

mvn test -Dlogging.level.com.almundo=DEBUG  (for DEBUG mode)
```

## Tip

For this example, the almundo-callcenter library is included in a local repository. It can be
installed using mvn install:

```
mvn install:install-file -Dfile=<path>/spring-boot-call-center-0.5.0.jar 
-DgroupId=com.almundo.callcenter -DartifactId=spring-boot-call-center 
-Dversion=0.5.0 -Dpackaging=jar -DlocalRepositoryPath=repo
```
