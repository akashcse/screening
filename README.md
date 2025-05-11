# Screening App

## Implemented feature on this application
* Spring boot reactive framework
  - Java streams works better with spring boot reactive
  - Implemented register battery API as stream response
* Java stream API
  - Used java stream with spring reactive steam troughout the application
* Postgres DB and Liquibase 
  - Used postgres with liquibase versioning to track all the DB changes
* Entity auditing
* Filter
  - Used filter to add requestId in header for tracing errors properly
* Logback configuration
  - Added log back configuration to log application logs and archive log every day
* APIs
  - Implemented two required APIs. Battery register API produce stream response.
* Exception Handler
  - Implemented global exception handle to manage all exception in one place
* Comment
  - Added minimal java docs, comments, logs in application
* Swagger documentation
  - Added swagger documentation to expose API definitions to client
* Blockhound
  - Used to find any block/non reactive code
* Junit tests
  - Added junit tests for all service methods
* Integration test
  - Testcontainer used in integration test and test cases cover all implemented APIs
* Test report
  - Added jacoco for test case reporting, the report will be generated after testcase run in **target/site/jacoco** directory
* Docker
  - Added docker script to run this application

### How to Run test cases
To run testcase run following command from application directory
```
mvn clean test
```
As Testcontainer is implemented, as a dependency need to run docker locally before run test cases.
The report will be generated on following location
```
target/site/jacoco
```
### How to Run the Application
#### Option 1 : Docker
From project directory run following command
````
docker compose up
````
#### Option 2 : Build and run jar file
Project has dependency on Java 17, Maven and Postgres DB, Before run need to configure all those in local system.
After that add following env variable to local system
```
POWERLEDGER_API_DB_HOST
POWERLEDGER_API_DB_PORT
POWERLEDGER_API_DB_NAME
POWERLEDGER_API_DB_USER
POWERLEDGER_API_DB_PASSWORD
```
Then from application directory run following command
```
mvn clean -DskipTests=true package
java -jar target/screening.jar
```
### cURL command to access APIs
Following cURL command used in Windows, it may need modification in differnet env

For register batteries
```
curl -XPOST -H "Content-type: application/json" -d "[{\"name\":\"Cannington\",\"postcode\":\"6107\",\"capacity\":13500},{\"name\":\"Midland\",\"postcode\":\"6107\",\"capacity\":50500},{\"name\":\"Hay Street\",\"postcode\":\"6000\",\"capacity\":23500}]" "localhost:4005/batteries"
```
for filter battery
```
curl -XPOST -H "Content-type: application/json" -d "{\"postcodeRange\": {\"from\": \"000\",\"to\": \"28000\"},\"minimumWatt\": 500}" "localhost:4005/batteries/filter"
```
### Swagger API documentation
* Run the Application
* From Browser Visit : {domain(or url:port)}/{application-context-path}/webjars/swagger-ui/index.html
* For this application : http://localhost:4005/webjars/swagger-ui/index.html
* Note : Above example doesn't have any application-context-path
