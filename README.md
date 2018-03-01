# CallCenter Almundo Rest Spring Boot API

## Overview

A Spring Boot application used to expose a CallCenter Almundo as RESTful API

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

Employee attributes:
  - String name
  - String type
  - Integer priority
  
```

## Main Classes

```
HomeController => Main App Controller with four API Post Methods:

  - /operator    <== Create a new Operator in System (high priority - 1)
  - /supervisor  <== Create a new Supervisor in System (priority - 2)
  - /director    <== Create a new Director in System (priority - 3)
  - /call        <== Simulate have a calling 

EmployeeService => PriorityBlockingQueue Manager (Employee has Comparable interface)

Dispatcher      => Process calls with a ConcurrentTaskExecutor <= ThreadPoolTaskExecutor 

ThreadPoolTaskExecutor => callcenter.numThreads = 10 (Max Pool Size and Core Size)

Application     =>  Spring Boot Application Configuration

HomeControllerTest  =>  Tests the correct functioning of the required functionality

```

## Build

To build callcenter-almundo as a war file, run:

```
mvn clean package
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

Use the included Postman collection to test adding and executing tasks

## Tip

For this example, the almundo-callcenter library is included in a local repository. It can be
installed using mvn install:

```
mvn install:install-file -Dfile=<path>/spring-boot-call-center-0.5.0.jar -DgroupId=com.almundo.callcenter -DartifactId=spring-boot-call-center -Dversion=0.5.0 -Dpackaging=jar -DlocalRepositoryPath=repo
```
