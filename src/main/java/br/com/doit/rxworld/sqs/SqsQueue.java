package br.com.doit.rxworld.sqs;

import static java.util.Collections.emptyList;

import java.util.List;
import java.util.function.Consumer;

import org.jboss.logging.Logger;

import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;

public class SqsQueue {
    public class MessageStream {
        private final List<Message> messages;

        public MessageStream(List<Message> messages) {
            this.messages = messages;
        }

        public void forEach(Consumer<Message> messageConsumer) {
            for (var message : messages) {
                log.info("SQS message received: " + message.body());

                try {
                    messageConsumer.accept(message);

                    deleteSqsMessage(message);
                } catch (Throwable exception) {
                    log.error("An error has occurred while processing the SQS message: " + message.body(), exception);
                }
            }
        }
    }

    private static final Logger log = Logger.getLogger(SqsQueue.class);

    private final SqsClient sqsClient;
    private final String queueName;

    public SqsQueue(SqsClient sqsClient, String queueName) {
        this.sqsClient = sqsClient;
        this.queueName = queueName;
    }

    public MessageStream receiveMessages() {
        var messages = receiveSqsMessages();

        return new MessageStream(messages);
    }

    private List<Message> receiveSqsMessages() {
        try {
            var receiveMessageRequest = ReceiveMessageRequest.builder()
                                                             .queueUrl(queueUrl())
                                                             .maxNumberOfMessages(10) // TODO: Parametrizar
                                                             .build();

            return sqsClient.receiveMessage(receiveMessageRequest).messages();
        } catch (Throwable exception) {
            if (!log.isDebugEnabled()) {
            	System.out.println("exception::: " + exception);
                log.error("An error has occurred while receiving SQS messages. Turn on debug for more information.");
            } else {
                log.debug("An error has occurred while receiving SQS messages.", exception);
            }

            return emptyList();
        }
    }

    private void deleteSqsMessage(Message message) {
        log.info("Deleting SQS message: " + message.body());

        var deleteMessageRequest = DeleteMessageRequest.builder()
                                                       .queueUrl(queueUrl())
                                                       .receiptHandle(message.receiptHandle())
                                                       .build();

        sqsClient.deleteMessage(deleteMessageRequest);
    }

    private String queueUrl() {
        var queueUrlRequest = GetQueueUrlRequest.builder()
                                                .queueName(queueName)
                                                .build();
        
        return sqsClient.getQueueUrl(queueUrlRequest).queueUrl();
    }
}