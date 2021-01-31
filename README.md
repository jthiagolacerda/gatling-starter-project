# gatling-starter-project

Gatling starter project to execute performance test using maven.

To execute is necessary to inform the class path(gatling.simulationClass) and environment(environment_test) to test.  
```
mvn gatling:test -Dgatling.simulationClass=load.LoadTestExample -Denviroment_test=qa 
```
After the execution a report will be generate at "./build/reports/gatling/"