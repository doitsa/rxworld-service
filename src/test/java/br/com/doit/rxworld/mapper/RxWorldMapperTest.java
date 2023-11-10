package br.com.doit.rxworld.mapper;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.doit.rxworld.orchestration.OrchestratorProfile;
import br.com.doit.rxworld.service.doit.pojo.DOitProduct;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusTest
@Transactional
@TestProfile(OrchestratorProfile.class)
public class RxWorldMapperTest {
	private static final String ALWAYS_AVAILABLE = "ALWAYS_AVAILABLE";
	private static final String MANAGED_BY_INVENTORY = "MANAGED_BY_INVENTORY";
	private static final String UNAVAILABLE = "UNAVAILABLE";

	private DOitProduct doitProduct = new DOitProduct();

	@BeforeEach
	private void setup() {
		doitProduct.availableQuantity = BigDecimal.ZERO;
		doitProduct.batchExpirationDate = LocalDate.of(2026, 10, 10);
		doitProduct.batchNumber = "LOT123A";
		doitProduct.ndc = "00123-4567-89";
		doitProduct.price = new BigDecimal("12");
		doitProduct.salePrice = BigDecimal.TEN;
		doitProduct.sku = "00123456789";
		doitProduct.status = ALWAYS_AVAILABLE;
	}

	@Test
	public void fromDOitProductToRxWorldProduct() throws Exception {
		var rxWorldProduct = RxWorldMapper.toRxWorldProduct(doitProduct);

		assertThat(rxWorldProduct.expirationDate, is("10/2026"));
		assertThat(rxWorldProduct.lotNumber, is("LOT123A"));
		assertThat(rxWorldProduct.ndc, is("00123-4567-89"));
		assertThat(rxWorldProduct.price, is(BigDecimal.TEN));
		assertThat(rxWorldProduct.quantity, is(0));
		assertThat(rxWorldProduct.sku, is("00123456789"));
		assertThat(rxWorldProduct.status, is("active"));
	}

	@Test
	public void returnStatusForProduct() throws Exception {
		var statuses = new HashMap<String, String>();

		statuses.put(UNAVAILABLE, "inactive");
		statuses.put(MANAGED_BY_INVENTORY, "inactive");
		statuses.put(ALWAYS_AVAILABLE, "active");
		statuses.put("default", "active");

		statuses.forEach((doitStatus, rxWorldStatus) -> {
			doitProduct.status = doitStatus;

			var rxWorldProduct = RxWorldMapper.toRxWorldProduct(doitProduct);

			assertThat(rxWorldProduct.status, is(rxWorldStatus));
		});
	}

	@Test
	public void returnActiveStatusWhenDOitProductIsManagedByInventoryAndHasAvailableQuantity() throws Exception {
		doitProduct.availableQuantity = BigDecimal.ONE;
		doitProduct.status = MANAGED_BY_INVENTORY;

		var rxWorldProduct = RxWorldMapper.toRxWorldProduct(doitProduct);

		assertThat(rxWorldProduct.status, is("active"));
	}
}
