#Smart Meter REST API

-----------

I have created a RESTFUL web service to display various smart meter details which are read from a database.

###Progress
The REST web service currently gets reading details based on an account number and returns it in the desired JSON format.

An optional task was also completed to allow submission of new meter reads for an account. This includes some validation of values and also allows for single-fuel and multi-fuel submissions (and multiple readings for a single submission).

For the next stage I would liked to have done more in the way of integration testing via new technologies such as Cucumber framework.

###Data Structure
In memory database (H2) was used for testing. Structure of tables can be found in src/main/resources/data.sql where there is table definitions.

How to run
--------
can start project from SmartmeterApplication.java

mvn clean package -> java -jar target/smartmeter-0.0.1-SNAPSHOT.jar






