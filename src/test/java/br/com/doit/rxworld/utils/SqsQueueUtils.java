package br.com.doit.rxworld.utils;

import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;

import java.lang.UnsupportedOperationException;
import java.util.Map;

import static software.amazon.awssdk.services.sqs.model.QueueAttributeName.APPROXIMATE_NUMBER_OF_MESSAGES;

public class SqsQueueUtils {
    public static void createQueue(SqsClient sqsClient, String queueName) {
        var queueRequest = CreateQueueRequest.builder()
                                             .queueName(queueName)
                                             .attributesWithStrings(Map.of("VisibilityTimeout", "1"))
                                             .build();

        sqsClient.createQueue(queueRequest);
    }

    public static void deleteQueue(SqsClient sqsClient, String queueName) {
        var queueUrl = queueUrl(sqsClient, queueName);
        var deleteQueueRequest = DeleteQueueRequest.builder()
                                                   .queueUrl(queueUrl)
                                                   .build();

        sqsClient.deleteQueue(deleteQueueRequest);
    }

    public static void sendMessage(SqsClient sqsClient, String queueName, String message) {
        var queueUrl = queueUrl(sqsClient, queueName);
        var sendMessageRequest = SendMessageRequest.builder()
                                                   .queueUrl(queueUrl)
                                                   .messageBody(message)
                                                   .build();

        sqsClient.sendMessage(sendMessageRequest);
    }

    public static String queueUrl(SqsClient sqsClient, String queueName) {
        var queueUrlRequest = GetQueueUrlRequest.builder()
                                                .queueName(queueName)
                                                .build();

        return sqsClient.getQueueUrl(queueUrlRequest).queueUrl();
    }

    public static Integer numberOfMessages(SqsClient sqsClient, String queueName) {
        var queueAttributesRequest = GetQueueAttributesRequest.builder()
                                                              .queueUrl(queueUrl(sqsClient, queueName))
                                                              .attributeNames(APPROXIMATE_NUMBER_OF_MESSAGES)
                                                              .build();

        var queueAttributes = sqsClient.getQueueAttributes(queueAttributesRequest);

        return Integer.valueOf(queueAttributes.attributes().get(APPROXIMATE_NUMBER_OF_MESSAGES));
    }

    private SqsQueueUtils() {
        throw new UnsupportedOperationException(SqsQueueUtils.class.getSimpleName() + " cannot be instantiated.");
    }
}
