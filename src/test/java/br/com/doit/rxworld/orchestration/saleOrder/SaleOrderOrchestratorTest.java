package br.com.doit.rxworld.orchestration.saleOrder;

import static com.github.tomakehurst.wiremock.client.WireMock.exactly;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.putRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static java.time.LocalDate.now;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.sql.Date;
import java.time.LocalDateTime;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import com.github.tomakehurst.wiremock.WireMockServer;

import br.com.doit.rxworld.model.RxWorldConfig;
import br.com.doit.rxworld.model.WebStore;
import br.com.doit.rxworld.orchestration.OrchestratorProfile;
import br.com.doit.rxworld.repository.SaleOrderRepository;
import br.com.doit.rxworld.repository.WebStoreRepository;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import software.amazon.awssdk.services.sqs.SqsClient;

@QuarkusTest
@Transactional
@TestProfile(OrchestratorProfile.class)
public class SaleOrderOrchestratorTest {

	@Inject
	SqsClient sqsClient;

	WireMockServer wireMock;

	@Inject
	WebStoreRepository webStoreRepo;

	@Inject
	SaleOrderRepository saleOrderRepo;

	@Inject
	SaleOrderOrchestrator saleOrderOrchestrator;
	
	WebStore webStore;

	@BeforeEach
	public void setup() {
		var rxWorldConfig = new RxWorldConfig();
		rxWorldConfig.clientId = "U5z9Fy0f6zjCLim";
		rxWorldConfig.clientSecret = "cCakpaFxTiQIS9N";
		rxWorldConfig.tokenExpiresAt = LocalDateTime.MAX;
		rxWorldConfig.currentBearerToken = "";
		
		rxWorldConfig.persistAndFlush();
		
		webStore = new WebStore();
		
		webStore.doitWebStoreId = 1;
		webStore.organization = "Acme Corp";
		webStore.rxWorldUrl = "http://localhost:9090";
		webStore.password = "password";
		webStore.username = "username";
		webStore.clientId = "U5z9Fy0f6zjCLim";
		webStore.clientSecret = "cCakpaFxTiQIS9N";
		webStore.rxWorldConfig = rxWorldConfig;
	}

	@AfterEach
	public void tearDown() {
		saleOrderRepo.findAll().stream().forEach(saleOrderRepo::delete);
		webStoreRepo.findAll().stream().forEach(webStoreRepo::delete);

		wireMock.resetRequests();
		wireMock.resetScenarios();
	}

	@Test
	@Timeout(4)
	public void insertSaleOrder() throws Exception {
		webStore.setOrderCriterias("orderplaced");

		webStoreRepo.persistAndFlush(webStore);
		
		saleOrderOrchestrator.handleSaleOrders();
		
		wireMock.verify(exactly(1), getRequestedFor(urlEqualTo("/api/orders/v1/recentorders?status=orderplaced")));
		
		wireMock.verify(exactly(1), getRequestedFor(urlEqualTo("/api/orders/v1/order?ordernumber=727")));
		
		wireMock.verify(exactly(1), postRequestedFor(urlEqualTo("/ra/CommercialInvoice")));
		
		wireMock.verify(exactly(1), putRequestedFor(urlEqualTo("/api/orders/v1/updateorderstatus")));
		
		var webStore = webStoreRepo.findByWebStoreAndOrganization(1, "Acme Corp").get();
		
		var saleOrderReference = saleOrderRepo.findByRxworldIdAndWebStoreId("727", 1).get();
		
		assertThat(saleOrderReference.dateCreated, is(Date.valueOf(now().toString())));
		assertThat(saleOrderReference.dateUpdated, is(Date.valueOf(now().toString())));
		assertThat(saleOrderReference.webStore, is(webStore));
		assertThat(saleOrderReference.doitId, is("3000001"));
		assertThat(saleOrderReference.rxworldId, is("727"));
	}
	
	@Test
	@Timeout(4)
	void doNotInsertSaleOrderForUnknownProduct() throws InterruptedException {
		webStore.setOrderCriterias("unknown-product");

		webStoreRepo.persistAndFlush(webStore);
		
		saleOrderOrchestrator.handleSaleOrders();
		
		wireMock.verify(exactly(1), getRequestedFor(urlEqualTo("/api/orders/v1/recentorders?status=unknown-product")));
		wireMock.verify(exactly(1), getRequestedFor(urlEqualTo("/api/orders/v1/order?ordernumber=728")));
		wireMock.verify(exactly(1), postRequestedFor(urlEqualTo("/ra/CommercialInvoice")));
		wireMock.verify(exactly(0), putRequestedFor(urlEqualTo("/api/orders/v1/updateorderstatus")));
	}
	
	@Test
	@Timeout(4)
	void doNotInsertSaleOrderThatAlreadyExists() throws InterruptedException {
		webStore.setOrderCriterias("duplicated");
		
		webStoreRepo.persistAndFlush(webStore);
		
		saleOrderOrchestrator.handleSaleOrders();
		
		wireMock.verify(exactly(1), getRequestedFor(urlEqualTo("/api/orders/v1/recentorders?status=duplicated")));
		wireMock.verify(exactly(1), getRequestedFor(urlEqualTo("/api/orders/v1/order?ordernumber=729")));
		wireMock.verify(exactly(1), postRequestedFor(urlEqualTo("/ra/CommercialInvoice")));
		wireMock.verify(exactly(0), putRequestedFor(urlEqualTo("/api/orders/v1/updateorderstatus")));
	}
	
	@Test
	public void getSalesOrdersWithDifferentStatus() throws Exception {
		webStore.setOrderCriterias("Order Placed, other");

		webStoreRepo.persistAndFlush(webStore);
		
		saleOrderOrchestrator.handleSaleOrders();
		
		wireMock.verify(getRequestedFor(urlEqualTo("/api/orders/v1/recentorders?status=orderplaced")));
		wireMock.verify(getRequestedFor(urlEqualTo("/api/orders/v1/recentorders?status=other")));
	}
}