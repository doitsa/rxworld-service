package br.com.doit.rxworld.orchestration.product;

import static com.github.tomakehurst.wiremock.client.WireMock.exactly;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static java.time.LocalDate.now;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.sql.Date;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import com.github.tomakehurst.wiremock.WireMockServer;

import br.com.doit.rxworld.model.Product;
import br.com.doit.rxworld.model.WebStore;
import br.com.doit.rxworld.orchestration.OrchestratorProfile;
import br.com.doit.rxworld.repository.ProductRepository;
import br.com.doit.rxworld.repository.WebStoreRepository;
import br.com.doit.rxworld.utils.SqsQueueUtils;
import br.com.doit.rxworld.utils.WireMockUtils;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import software.amazon.awssdk.services.sqs.SqsClient;

@QuarkusTest
@Transactional
@TestProfile(OrchestratorProfile.class)
public class UpdateProductOrchestratorTest {

	@Inject
	SqsClient sqsClient;

	WireMockServer wireMock;

	@Inject
	WebStoreRepository webStoreRepo;

	@Inject
	ProductRepository productRepository;

	@BeforeEach
	public void setup() {
		SqsQueueUtils.createQueue(sqsClient, "test_rxworld_update_product");

		var webStore = new WebStore();
		webStore.doitWebStoreId = 1;
		webStore.organization = "Acme Corp";
		webStore.rxWorldUrl = "http://localhost:9090";
		
		webStoreRepo.persistAndFlush(webStore);
		
		var product = new Product();
		product.sku = "SAMPLE01";
		product.webStore = webStore;
		product.doitProductId = 194;

		productRepository.persistAndFlush(product);
	}

	@AfterEach
	public void tearDown() {
		// productRepo.deleteAll doesn't cascade child entities, causing constraint violation errors
		// We must delete one by one
		productRepository.findAll().stream().forEach(productRepository::delete);
		webStoreRepo.findAll().stream().forEach(webStoreRepo::delete);

		SqsQueueUtils.deleteQueue(sqsClient, "test_rxworld_update_product");

		wireMock.resetRequests();
		wireMock.resetScenarios();
	}
	
	@Test
	@Timeout(4)
	void updateRxWorldProductWhenReceivingCommand() throws InterruptedException {
		SqsQueueUtils.sendMessage(sqsClient, "test_rxworld_update_product", "{\"system\": \"Acme Corp\", \"sku\": \"SAMPLE01\", \"webStore\": 1, \"flow\": \"UPDATE_PRODUCT\"}");

		while (WireMockUtils.noRequestMatching(wireMock, "/ra/WebStores/1/Products/SAMPLE01")) {
			Thread.sleep(100);
		}
		
		wireMock.verify(exactly(1), getRequestedFor(urlEqualTo("/ra/WebStores/1/Products/SAMPLE01")));
		
		while (WireMockUtils.noRequestMatching(wireMock, "/api/products/v1/product")) {
			Thread.sleep(100);
		}
		
		wireMock.verify(exactly(1), postRequestedFor(urlEqualTo("/api/products/v1/product")));
		
		while(productRepository.findBySkuAndWebStore("SAMPLE01", 1).isEmpty()) {
			Thread.sleep(400);
		}
		
		var productReference = productRepository.findBySkuAndWebStore("SAMPLE01", 1).get();
		
		assertThat(productReference.dateUpdated, is(Date.valueOf(now().toString())));
	}
	
	@Test
	@Timeout(4)
	void doNotUpdateProductWhenWebStoreIsNotFound() throws InterruptedException {
		SqsQueueUtils.sendMessage(sqsClient, "test_rxworld_update_product", "{\"system\": \"Acme Corp\", \"sku\": \"SAMPLE01\", \"webStore\": 2, \"flow\": \"UPDATE_PRODUCT\"}");
		
		wireMock.verify(exactly(0), getRequestedFor(urlEqualTo("/ra/WebStores/2/Products/SAMPLE01")));
		wireMock.verify(exactly(0), postRequestedFor(urlEqualTo("/api/products/v1/product")));
	}
	
	@Test
	@Timeout(4)
	void doNotUpdateProductWhenSomethingIsWrongOnJsonBody() throws InterruptedException {
		SqsQueueUtils.sendMessage(sqsClient, "test_rxworld_update_product", "{\"system\": \"Acme Corp\", \"sku\": \"WRONGSAMPLE\", \"webStore\": 1, \"flow\": \"UPDATE_PRODUCT\"}");
		
		while (WireMockUtils.noRequestMatching(wireMock, "/ra/WebStores/1/Products/WRONGSAMPLE")) {
			Thread.sleep(100);
		}
		
		wireMock.verify(exactly(1), getRequestedFor(urlEqualTo("/ra/WebStores/1/Products/WRONGSAMPLE")));
		
		wireMock.verify(exactly(0), postRequestedFor(urlEqualTo("/api/products/v1/product")));
	}
}