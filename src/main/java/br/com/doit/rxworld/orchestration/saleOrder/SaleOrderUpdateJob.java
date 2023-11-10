package br.com.doit.rxworld.orchestration.saleOrder;

import static io.quarkus.scheduler.Scheduled.ConcurrentExecution.SKIP;

import javax.inject.Inject;

import io.quarkus.scheduler.Scheduled;

public class SaleOrderUpdateJob {
	@Inject
	SaleOrderOrchestrator saleOrderOrchestrator;

	@Scheduled(every = "60s", concurrentExecution = SKIP)
	public void run() {
		saleOrderOrchestrator.handleSaleOrders();
	}
}