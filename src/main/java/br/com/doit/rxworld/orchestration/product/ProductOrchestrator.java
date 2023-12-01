package br.com.doit.rxworld.orchestration.product;

import static java.time.LocalDate.now;

import java.sql.Date;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.eclipse.microprofile.rest.client.RestClientDefinitionException;
import org.jboss.logging.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;

import br.com.doit.rxworld.mapper.RxWorldMapper;
import br.com.doit.rxworld.model.WebStore;
import br.com.doit.rxworld.repository.ProductRepository;
import br.com.doit.rxworld.repository.WebStoreRepository;
import br.com.doit.rxworld.service.doit.DOitServiceProvider;
import br.com.doit.rxworld.service.doit.pojo.DOitProduct;
import br.com.doit.rxworld.service.rxworld.RxWorldService;
import br.com.doit.rxworld.service.rxworld.RxWorldServiceProvider;

@ApplicationScoped
@Transactional
public class ProductOrchestrator {
	private static final Logger log = Logger.getLogger(ProductOrchestrator.class);

	@Inject
	DOitServiceProvider doitServiceProvider;

	@Inject
	RxWorldServiceProvider rxWorldServiceProvider;

	@Inject
	WebStoreRepository webStoreRepository;

	@Inject
	ProductRepository productRepository;

	public void upsertProduct(DOitProductCommand command) {
		var system = command.system;
		var webStoreId = command.webStore;
		var sku = command.sku;

		var webStore = webStoreRepository.findByWebStoreAndOrganization(webStoreId, system);

		if (webStore.isPresent()) {
			var doitService = doitServiceProvider.get(system);

			log.infof("Get product %s on DOit.", sku);

			var doitProduct = doitService.getProduct(webStoreId, sku);

			upsertProductInWoo(webStore.get(), doitProduct, system);
		} else {
			log.infof("No web store " + webStoreId + " of organization " + system + " found. Discarding the message.");
		}
	}

	private void upsertProductInWoo(WebStore webStore, DOitProduct doitProduct, String system) {
		RxWorldService rxWorldService;

		try {
			rxWorldService = rxWorldServiceProvider.get(webStore);
		} catch (JsonProcessingException | IllegalStateException | RestClientDefinitionException exception) {
			log.error("Error thrown while trying to get authorization: " + exception.getMessage());
			return;
		}

		var rxWorldProduct = RxWorldMapper.toRxWorldProduct(doitProduct);

		var productReference = productRepository.findBySkuAndWebStore(rxWorldProduct.sku, webStore.doitWebStoreId);

		try {
			if (productReference.isPresent()) {
				log.infof("Update product %s on RxWorld.", doitProduct.sku);

				var updateProductRequest = rxWorldService.postProduct(rxWorldProduct);

				if (updateProductRequest.isSuccessful()) {
					productReference.get().dateUpdated = Date.valueOf(now().toString());

					productReference.get().persistAndFlush();

					log.infof("Finished product update %s for web store %s.", doitProduct.sku, webStore.doitWebStoreId);
				} else {
					log.error("There was an error while updating the product. " + updateProductRequest.statusMessage);
				}
			} else {
				log.infof("Insert product %s on RxWorld.", doitProduct.sku);

				var postProductRequest = rxWorldService.postProduct(rxWorldProduct);

				if (postProductRequest.isSuccessful()) {
					var newProductReference = new br.com.doit.rxworld.model.Product();

					newProductReference.doitProductId = doitProduct.sequentialCode;
					newProductReference.dateCreated = Date.valueOf(now().toString());
					newProductReference.dateUpdated = Date.valueOf(now().toString());
					newProductReference.sku = rxWorldProduct.sku;
					newProductReference.webStore = webStore;

					newProductReference.persistAndFlush();

					log.infof("Finished product insert %s for web store %s.", doitProduct.sku, webStore.doitWebStoreId);
				} else {
					log.error("There was an error while posting the product. " + postProductRequest.statusMessage);
				}
			}
		} catch (Exception exception) {
			log.error(exception.getMessage());
		}
	}
}