package br.com.doit.rxworld.orchestration.saleOrder;

import static java.time.LocalDate.now;

import java.sql.Date;
import java.util.ArrayList;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.eclipse.microprofile.rest.client.RestClientDefinitionException;
import org.jboss.logging.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;

import br.com.doit.rxworld.mapper.DOitMapper;
import br.com.doit.rxworld.model.SaleOrder;
import br.com.doit.rxworld.repository.SaleOrderRepository;
import br.com.doit.rxworld.repository.StateRepository;
import br.com.doit.rxworld.repository.WebStoreRepository;
import br.com.doit.rxworld.service.doit.DOitException;
import br.com.doit.rxworld.service.doit.DOitServiceProvider;
import br.com.doit.rxworld.service.doit.pojo.DOitWebStore;
import br.com.doit.rxworld.service.rxworld.RxWorldService;
import br.com.doit.rxworld.service.rxworld.RxWorldServiceProvider;
import br.com.doit.rxworld.service.rxworld.pojo.RxWorldOrder;

@ApplicationScoped
@Transactional
public class SaleOrderOrchestrator {
	private static final String ORDER_CONFIRMED = "Order Confirmed";

	private static final Logger log = Logger.getLogger(SaleOrderOrchestrator.class);

	@Inject
	DOitServiceProvider doitServiceProvider;

	@Inject
	RxWorldServiceProvider rxWorldServiceProvider;

	@Inject
	SaleOrderRepository saleOrderRepository;

	@Inject
	WebStoreRepository webStoreRepository;

	@Inject
	StateRepository stateRepository;

	public void handleSaleOrders() {
		var webStores = webStoreRepository.listAll();

		webStores.forEach(webStore -> {
			RxWorldService rxWorldService;

			try {
				rxWorldService = rxWorldServiceProvider.get(webStore);
			} catch (JsonProcessingException | IllegalStateException | RestClientDefinitionException exception) {
				log.error("The authentication generation failed. " + exception.getMessage());
				return;
			}

			try {
				var rxWorldOrders = new ArrayList<RxWorldOrder>();

				webStore.orderCriterias().forEach(criteria -> {
					var orderIdsRequest = rxWorldService.getOrdersByCriteria(criteria.trim());

					if (orderIdsRequest.isSuccessful()) {
						orderIdsRequest.result.forEach(id -> {
							var orderResponse = rxWorldService.getOrderById(id);

							if (orderResponse.isSuccessful()) {
								rxWorldOrders.add(orderResponse.result);
							}
						});
					} else {
						log.error("There was an error while getting the order IDs. " + orderIdsRequest.statusMessage);
					}
				});

				rxWorldOrders.forEach(rxWorldOrder -> {
					log.infof("Get order %s on RxWorld.", rxWorldOrder.orderNumber);

					var doitSaleOrder = DOitMapper.toDOitSaleOrder(rxWorldOrder, stateRepository);

					var doitWebStore = new DOitWebStore();
					doitWebStore.id = webStore.doitWebStoreId;

					doitSaleOrder.webStore = doitWebStore;

					var doitService = doitServiceProvider.get(webStore.doitUrl);

					try {
						var postedSaleOrder = doitService.postSaleOrder(doitSaleOrder);

						log.infof("Post sale order %s on DOit.", postedSaleOrder.sequentialCode);

						updateStatusFor(rxWorldService, rxWorldOrder);

						var saleOrder = new SaleOrder();

						saleOrder.dateCreated = Date.valueOf(now().toString());
						saleOrder.dateUpdated = Date.valueOf(now().toString());
						saleOrder.webStore = webStore;
						saleOrder.doitId = postedSaleOrder.sequentialCode.toString();
						saleOrder.rxworldId = rxWorldOrder.orderNumber.toString();

						saleOrder.persist();

						log.infof("Finished order post %s for web store %s.", postedSaleOrder.sequentialCode, webStore.doitWebStoreId);
					} catch (DOitException exception) {
						log.error(exception.getMessage());
					}
				});
			} catch (Exception e) {
				log.infof("There was an error while getting the order for %s.", webStore.organization);
			}
		});
	}

	private void updateStatusFor(RxWorldService rxWorldService, RxWorldOrder rxWorldOrder) {
		var updatedStatusOrder = new RxWorldOrder();
		updatedStatusOrder.status = ORDER_CONFIRMED;
		updatedStatusOrder.orderNumber = rxWorldOrder.orderNumber;

		rxWorldService.updateOrderStatus(updatedStatusOrder);
	}
}