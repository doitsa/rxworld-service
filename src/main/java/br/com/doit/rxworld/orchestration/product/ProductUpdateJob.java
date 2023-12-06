package br.com.doit.rxworld.orchestration.product;

import static io.quarkus.scheduler.Scheduled.ConcurrentExecution.SKIP;

import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import br.com.doit.rxworld.sqs.SqsQueue;
import br.com.doit.rxworld.sqs.SqsQueueFactory;
import io.quarkus.scheduler.Scheduled;
import software.amazon.awssdk.services.sqs.model.Message;

public class ProductUpdateJob {
    SqsQueue sqsQueue;

    @Inject
    ProductOrchestrator productOrchestrator;

    @Inject
    public ProductUpdateJob(SqsQueueFactory sqsQueueFactory, @ConfigProperty(name = "rxworld-service.queue.update-product") String queueName) {
        sqsQueue = sqsQueueFactory.create(queueName);
    }

    @Scheduled(every = "1s", concurrentExecution = SKIP)
    public void run() {
        sqsQueue.receiveMessages().forEach(m -> processMessage(m));
    }

    private void processMessage(Message message) {
        var json = message.body();
        var command = DOitProductCommand.fromJson(json);

        productOrchestrator.upsertProduct(command);
    }
}