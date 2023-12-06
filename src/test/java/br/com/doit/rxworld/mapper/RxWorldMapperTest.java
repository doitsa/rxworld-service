package br.com.doit.rxworld.mapper;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.doit.rxworld.orchestration.OrchestratorProfile;
import br.com.doit.rxworld.service.doit.pojo.DOitParty;
import br.com.doit.rxworld.service.doit.pojo.DOitProduct;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusTest
@Transactional
@TestProfile(OrchestratorProfile.class)
public class RxWorldMapperTest {
	private static final String ALWAYS_AVAILABLE = "ALWAYS_AVAILABLE";

	private DOitProduct doitProduct = new DOitProduct();

	@BeforeEach
	private void setup() {
		var manufacturer = new DOitParty();
		manufacturer.fullName = "Manufacturer";
		
		doitProduct.availableQuantity = BigDecimal.ZERO;
		doitProduct.batchExpirationDate = LocalDate.of(2026, 10, 10);
		doitProduct.batchNumber = "LOT123A";
		doitProduct.name = "Product Name";
		doitProduct.ndc = "00123-4567-89";
		doitProduct.price = new BigDecimal("12");
		doitProduct.salePrice = BigDecimal.TEN;
		doitProduct.size = "10ML";
		doitProduct.sku = "00123456789";
		doitProduct.status = ALWAYS_AVAILABLE;
		doitProduct.manufacturer = manufacturer;
	}

	@Test
	public void fromDOitProductToRxWorldProduct() throws Exception {
		var rxWorldProduct = RxWorldMapper.toRxWorldProduct(doitProduct);

		assertThat(rxWorldProduct.lotNumber, is("LOT123A"));
		assertThat(rxWorldProduct.ndc, is("00123-4567-89"));
		assertThat(rxWorldProduct.price, is(BigDecimal.TEN));
		assertThat(rxWorldProduct.quantity, is(0));
		assertThat(rxWorldProduct.sku, is("00123456789"));
		assertThat(rxWorldProduct.inventoryStatus, is("ALWAYS_AVAILABLE"));
		assertThat(rxWorldProduct.name, is("Product Name"));
		assertThat(rxWorldProduct.packaging, is("10ML"));
		assertThat(rxWorldProduct.expirationDate, is("10/2026"));
		assertThat(rxWorldProduct.manufacturer, is("Manufacturer"));
	}
}
