# rxworld-service

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
mvn compile quarkus:dev
```

## Running Localstack Locally
The RxWorld Service requires some AWS services to work properly. You can run the command below to start a container with all the required AWS services running.

```shell
$ docker-compose up
```

## API
This service has APIs that allows to configure new web stores and update some product without the necessity to send SQS/SNS messages.

To check the documentation of these APIs, you can check it:
- Development (http://localhost:8080/q/swagger-ui/)
- Staging (https://staging-rxworld-service.doit.com.br/q/swagger-ui/)
- Production (https://rxworld-service.doit.com.br/q/swagger-ui/)