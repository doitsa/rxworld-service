# Set up SNS and SQS to update product data
awslocal sqs create-queue --queue-name dev_rxworld_update_product
awslocal sns create-topic --name dev_web_store_product
awslocal sns subscribe --topic-arn arn:aws:sns:us-east-1:000000000000:dev_web_store_product --protocol sqs --notification-endpoint http://localhost:4566/000000000000/dev_rxworld_update_product --attributes RawMessageDelivery=true