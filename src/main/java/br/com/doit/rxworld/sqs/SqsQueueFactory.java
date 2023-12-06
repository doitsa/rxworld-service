package br.com.doit.rxworld.sqs;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import software.amazon.awssdk.services.sqs.SqsClient;

@Dependent
public class SqsQueueFactory {
    @Inject
    SqsClient sqsClient;

    public SqsQueue create(String queueName) {
        return new SqsQueue(sqsClient, queueName);
    }
}