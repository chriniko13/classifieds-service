### Classifieds Counter Service

```
 

       .__                       .__  _____.__           .___
  ____ |  | _____    ______ _____|__|/ ____\__| ____   __| _/______
_/ ___\|  | \__  \  /  ___//  ___/  \   __\|  |/ __ \ / __ |/  ___/
\  \___|  |__/ __ \_\___ \ \___ \|  ||  |  |  \  ___// /_/ |\___ \
 \___  >____(____  /____  >____  >__||__|  |__|\___  >____ /____  >
     \/          \/     \/     \/                  \/     \/    \/

                          .__
  ______ ______________  _|__| ____  ____
 /  ___// __ \_  __ \  \/ /  |/ ___\/ __ \
 \___ \\  ___/|  | \/\   /|  \  \__\  ___/
/____  >\___  >__|    \_/ |__|\___  >___  >
     \/     \/                    \/    \/

```

##### Assignee: Nikolaos Christidis (nick.christidis@yahoo.com)


#### How to run service
* Two options:
    * Execute: 
        * `mvn clean install -DskipUTs=true -DskipITs`
        * `java -jar -Dspring.profiles.active=dev -DLog4jContextSelector=org.apache.logging.log4j.core.async.AsyncLoggerContextSelector target/classifieds-service-1.0-SNAPSHOT.jar`
                
    * Execute:
        * `mvn spring-boot:run -Dspring.profiles.active=dev -DLog4jContextSelector=org.apache.logging.log4j.core.async.AsyncLoggerContextSelector`
        
        
#### Execute Unit Tests
* Execute: `mvn clean test`


#### Execute Integration Tests
* Execute: `mvn clean integration-test -DskipUTs=true` or `mvn clean verify -DskipUTs=true`


#### Test Coverage (via JaCoCo)
* In order to generate reports execute: `mvn clean verify`
    * In order to see unit test coverage open with browser: `target/site/jacoco-ut/index.html`
    * In order to see integration test coverage open with browser: `target/site/jacoco-it/index.html`
    

#### Dependencies Used:
* Spring Boot Web with Undertow
* Log4j2
* Lombok
* Jsoup (for html tags stripping)
* Spring Boot Test
* JsonAssert