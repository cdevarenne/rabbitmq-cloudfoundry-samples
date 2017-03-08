This is a very simple Java and Spring application demonstrating the use of RabbitMQ on Cloud Foundry.

## Deploying to Cloud Foundry ##

After installing in the 'cf' [command-line interface](http://docs.cloudfoundry.com/docs/using/managing-apps/cf/) for Cloud Foundry, targeting a Cloud Foundry instance, and logging in,
the application can be pushed using these commands (example using CloudFoundry on https://run.pivotal.io:

    $ mvn package
    $ cf create-service cloudamqp lemur yourCreativeServiceNameHere
    $ cf bind-service yourAppName yourCreativeServiceNameHere
    $ cf push
    ...
    Push successful! App 'rabbitmq-spring' available at http://rabbitmq-spring-12345.cfapps.io

The provided `manifest.yml` file will be used to provide the application parameters to Cloud Foundry. A unique URL will be assigned to the application, which is shown at the end of the output from 'cf push'. The `manifest.yml` file specifies a RabbitMQ services that is available on the [run.pivotal.io](http://docs.cloudfoundry.com/docs/dotcom/getting-started.html) Cloud Foundry services marketplace. You may need to change the details of the RabbitMQ service to push to a different Cloud Foundry instance.


You can use the user interface to send and receive a message. To send a message as a GET method do: http://your_app_route.cfapps.io/produce?message=Hello1

## Example scripts to run things from the command line  ##
###### producer.sh

    #!/bin/bash
    counter=1
    while true; do curl -X GET http://app_route.pcf.io/produce?message=msg$counter; ((counter++)); sleep 2; 
    done

###### consumer.sh

    #!/bin/bash
    while true; do curl -X POST http://app_route.pcf.io/get; sleep 2; done
