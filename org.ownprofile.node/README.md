## Build

Environment:
* JDK 8
* Maven 3

Build followed by unit and integration tests:
```sh
mvn clean verify
```


## Run

```sh
mvn exec:java -Dexec.mainClass="org.ownprofile.JettyLauncher" -Dexec.args="config.properties"
```

Logging settings are in `src/main/resources/logback.xml`.


## Run multiple nodes

To run multiple nodes in parallel on the same computer, use a separate configuration file for each node. 