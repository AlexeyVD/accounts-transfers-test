# accounts-transfers-test
Simple api for money transfers between accounts
The project is designed using Kotlin language, Vertx framework and Spock framework for tests

## API End Points
All methods require Content-Type = "application/json"
1. Method: "/account/create", Type: PUT, Params: {"balance":100}, Response: {"id":1}
2. Method: "/account/get", Type: POST, Params: {"id":1}, Response: {"id":1, "balance":100}
3. Method: "/transfer", Type: POST, Params: {"from":1, "to":2, "amount":30}, Response: {}


## Build
You can open project and run Strater.kt

To build artifact and run service execute this commands:
```shell
./gradlew shadowJar
java -jar /build/libs/account-transfers-test-1.0-SNAPSHOT-fat.jar
```
## Tests
To run tests execute this commands:
```shell
./gradlew test
```
