package br.com.doit.rxworld.mapper;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;

import br.com.doit.commons.time.DateUtils;
import br.com.doit.rxworld.orchestration.OrchestratorProfile;
import br.com.doit.rxworld.repository.StateRepository;
import br.com.doit.rxworld.service.rxworld.pojo.RxWorldOrder;
import br.com.doit.rxworld.service.rxworld.pojo.RxWorldSaleItem;
import br.com.doit.rxworld.service.rxworld.pojo.RxWorldAddress;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusTest
@Transactional
@TestProfile(OrchestratorProfile.class)
public class DOitMapperTest {
	@Inject
	StateRepository stateRepository;

	@Test
	public void fromRxWorldOrderToDOitSaleOrder() throws Exception {
		var item = new RxWorldSaleItem();
		item.sku = "SAMPLE01";
		item.discountPercentage = new BigDecimal("50");
		item.name = "Product Name";
		item.packagePrice = new BigDecimal("2");
		item.quantity = 10;
		
		var rxWorldShippingAddress = new RxWorldAddress();
		rxWorldShippingAddress.addressLine1 = "20 Austin Road, Suite 24";
		rxWorldShippingAddress.addressLine2 = "Shipping";
		rxWorldShippingAddress.city = "Edision";
		rxWorldShippingAddress.state = "NJ";
		rxWorldShippingAddress.zipCode = "08820";
		rxWorldShippingAddress.country = "USA";
		
		var rxWorldBillingAddress = new RxWorldAddress();
		rxWorldBillingAddress.addressLine1 = "21 Olsen Road, Suite 25";
		rxWorldBillingAddress.addressLine2 = "Billing";
		rxWorldBillingAddress.city = "Manhattan";
		rxWorldBillingAddress.state = "NY";
		rxWorldBillingAddress.zipCode = "08821";
		rxWorldBillingAddress.country = "USA";

		var rxWorldOrder = new RxWorldOrder();
		rxWorldOrder.items = new ArrayList<RxWorldSaleItem>();
		rxWorldOrder.orderNumber = 123;
		rxWorldOrder.buyerName = "John Doe";
		rxWorldOrder.buyerId = 12;
		rxWorldOrder.orderDate = DateUtils.toDateTime(LocalDateTime.of(2023, 6, 20, 11, 40));
		rxWorldOrder.sellerReceivedPayment = "Yes";
		rxWorldOrder.sellerAmountCreditedDate = DateUtils.toDateTime(LocalDateTime.of(2023, 10, 10, 16, 10));
		rxWorldOrder.sellerTransRefNumbers = "TR9876";
		rxWorldOrder.amountPaidToSeller = new BigDecimal("15");
		rxWorldOrder.shippingMethod = "Ground shipping";
		rxWorldOrder.buyerEmail = "johndoe@doit.com";
		rxWorldOrder.buyerPhone = "(333) 333-3333";
		rxWorldOrder.cartTotal = new BigDecimal("15");
		
		rxWorldOrder.items.add(item);
		rxWorldOrder.billingAddress = rxWorldBillingAddress;
		rxWorldOrder.shippingAddress = rxWorldShippingAddress;

		var doitOrder = DOitMapper.toDOitSaleOrder(rxWorldOrder, stateRepository);

		assertFalse(doitOrder.items.isEmpty());
		assertThat(doitOrder.items.get(0).product.sku, is("SAMPLE01"));
        assertThat(doitOrder.items.get(0).product.name, is("Product Name"));
        assertThat(doitOrder.items.get(0).quantity, comparesEqualTo(new BigDecimal("10")));
        assertThat(doitOrder.items.get(0).unitPrice, comparesEqualTo(new BigDecimal("1")));
        assertThat(doitOrder.items.get(0).total, comparesEqualTo(new BigDecimal("10")));
        assertThat(doitOrder.date, is(DateUtils.toDateTime(LocalDateTime.of(2023, 6, 20, 11, 40))));
        assertThat(doitOrder.paymentInfo, is("Payment date: 10/10/2023\nPayment transaction number: TR9876"));
        assertThat(doitOrder.externalId, is("123"));
        assertThat(doitOrder.shippingCost, is(nullValue()));
        assertThat(doitOrder.subTotal, comparesEqualTo(new BigDecimal("10")));
        assertThat(doitOrder.total, comparesEqualTo(new BigDecimal("15")));
        assertThat(doitOrder.reference, is("123"));
        assertThat(doitOrder.customer.name, is("John Doe"));
		assertThat(doitOrder.customer.email, is("johndoe@doit.com"));
		assertThat(doitOrder.customer.externalId, is("12"));
		assertThat(doitOrder.customer.phone, is("(333) 333-3333"));
        assertThat(doitOrder.shippingDescription, is("Ground shipping\nJohn Doe"));
        assertThat(doitOrder.shippingAddress.streetName, is("20 Austin Road, Suite 24"));
        assertThat(doitOrder.shippingAddress.more, is("Shipping"));
        assertThat(doitOrder.shippingAddress.city, is("Edision"));
        assertThat(doitOrder.shippingAddress.state, is("NJ"));
        assertThat(doitOrder.shippingAddress.zipCode, is("08820"));
        assertThat(doitOrder.shippingAddress.country, is("United States"));
        assertThat(doitOrder.billingAddress.streetName, is("21 Olsen Road, Suite 25"));
        assertThat(doitOrder.billingAddress.more, is("Billing"));
        assertThat(doitOrder.billingAddress.city, is("Manhattan"));
        assertThat(doitOrder.billingAddress.state, is("NY"));
        assertThat(doitOrder.billingAddress.zipCode, is("08821"));
        assertThat(doitOrder.billingAddress.country, is("United States"));
	}
	
	@Test
	public void zeroDiscountOrder() throws Exception {
		var item = new RxWorldSaleItem();
		item.discountPercentage = BigDecimal.ZERO;
		item.packagePrice = new BigDecimal("2");
		item.quantity = 10;
		
		var rxWorldShippingAddress = new RxWorldAddress();
		rxWorldShippingAddress.addressLine1 = "20 Austin Road, Suite 24";
		rxWorldShippingAddress.addressLine2 = "Shipping";
		rxWorldShippingAddress.city = "Edision";
		rxWorldShippingAddress.state = "NJ";
		rxWorldShippingAddress.zipCode = "08820";
		rxWorldShippingAddress.country = "USA";
		
		var rxWorldBillingAddress = new RxWorldAddress();
		rxWorldBillingAddress.addressLine1 = "21 Olsen Road, Suite 25";
		rxWorldBillingAddress.addressLine2 = "Billing";
		rxWorldBillingAddress.city = "Manhattan";
		rxWorldBillingAddress.state = "NY";
		rxWorldBillingAddress.zipCode = "08821";
		rxWorldBillingAddress.country = "USA";
		
		var rxWorldOrder = new RxWorldOrder();
		rxWorldOrder.items = new ArrayList<RxWorldSaleItem>();
		rxWorldOrder.orderNumber = 123;
		rxWorldOrder.cartTotal = new BigDecimal("20");
		
		rxWorldOrder.items.add(item);
		rxWorldOrder.billingAddress = rxWorldBillingAddress;
		rxWorldOrder.shippingAddress = rxWorldShippingAddress;

		var doitOrder = DOitMapper.toDOitSaleOrder(rxWorldOrder, stateRepository);

		assertFalse(doitOrder.items.isEmpty());
        assertThat(doitOrder.items.get(0).quantity, comparesEqualTo(new BigDecimal("10")));
        assertThat(doitOrder.items.get(0).unitPrice, comparesEqualTo(new BigDecimal("2")));
        assertThat(doitOrder.items.get(0).total, comparesEqualTo(new BigDecimal("20")));
        assertThat(doitOrder.subTotal, comparesEqualTo(new BigDecimal("20")));
        assertThat(doitOrder.total, comparesEqualTo(new BigDecimal("20")));
	}
	
	@Test
	public void moreItemsOrder() throws Exception {
		var item1 = new RxWorldSaleItem();
		item1.discountPercentage = new BigDecimal("50");
		item1.packagePrice = new BigDecimal("2");
		item1.quantity = 10;
		
		var item2 = new RxWorldSaleItem();
		item2.discountPercentage = new BigDecimal("50");
		item2.packagePrice = BigDecimal.TEN;
		item2.quantity = 2;
		
		var rxWorldShippingAddress = new RxWorldAddress();
		rxWorldShippingAddress.addressLine1 = "20 Austin Road, Suite 24";
		rxWorldShippingAddress.addressLine2 = "Shipping";
		rxWorldShippingAddress.city = "Edision";
		rxWorldShippingAddress.state = "NJ";
		rxWorldShippingAddress.zipCode = "08820";
		rxWorldShippingAddress.country = "USA";
		
		var rxWorldBillingAddress = new RxWorldAddress();
		rxWorldBillingAddress.addressLine1 = "21 Olsen Road, Suite 25";
		rxWorldBillingAddress.addressLine2 = "Billing";
		rxWorldBillingAddress.city = "Manhattan";
		rxWorldBillingAddress.state = "NY";
		rxWorldBillingAddress.zipCode = "08821";
		rxWorldBillingAddress.country = "USA";
		
		var rxWorldOrder = new RxWorldOrder();
		rxWorldOrder.items = new ArrayList<RxWorldSaleItem>();
		rxWorldOrder.orderNumber = 123;
		rxWorldOrder.cartTotal = new BigDecimal("25");
		
		rxWorldOrder.items.add(item1);
		rxWorldOrder.items.add(item2);
		rxWorldOrder.billingAddress = rxWorldBillingAddress;
		rxWorldOrder.shippingAddress = rxWorldShippingAddress;

		var doitOrder = DOitMapper.toDOitSaleOrder(rxWorldOrder, stateRepository);

		assertFalse(doitOrder.items.isEmpty());
        assertThat(doitOrder.items.get(0).quantity, comparesEqualTo(new BigDecimal("10")));
        assertThat(doitOrder.items.get(0).unitPrice, comparesEqualTo(new BigDecimal("1")));
        assertThat(doitOrder.items.get(0).total, comparesEqualTo(new BigDecimal("10")));
        
        assertThat(doitOrder.items.get(1).quantity, comparesEqualTo(new BigDecimal("2")));
        assertThat(doitOrder.items.get(1).unitPrice, comparesEqualTo(new BigDecimal("5")));
        assertThat(doitOrder.items.get(1).total, comparesEqualTo(new BigDecimal("10")));
        
        assertThat(doitOrder.subTotal, comparesEqualTo(new BigDecimal("20")));
        assertThat(doitOrder.total, comparesEqualTo(new BigDecimal("25")));
	}
	
	@Test
    void fromRxWorldAddressToDOitAddress() {
		var shippingAddress = new RxWorldAddress();
		shippingAddress.addressLine1 = "88 Austin Road, Suite 18";
		shippingAddress.addressLine2 = "RxWorld";
		shippingAddress.city = "Edision";
		shippingAddress.state = "New Jersey";
		shippingAddress.zipCode = "08820";
		shippingAddress.country = "US";
    	
		var rxWorldOrder = new RxWorldOrder();
		rxWorldOrder.items = new ArrayList<RxWorldSaleItem>();
		rxWorldOrder.orderNumber = 123;
		rxWorldOrder.buyerName = "John Doe";
		rxWorldOrder.orderDate = DateUtils.toDateTime(LocalDateTime.of(2023, 6, 20, 11, 40));
		rxWorldOrder.sellerReceivedPayment = "Yes";
		rxWorldOrder.sellerAmountCreditedDate = DateUtils.toDateTime(LocalDateTime.of(2023, 10, 10, 16, 10));
		rxWorldOrder.sellerTransRefNumbers = "TR9876";
		rxWorldOrder.amountPaidToSeller = new BigDecimal("15");
		rxWorldOrder.shippingMethod = "Ground shipping";
		rxWorldOrder.buyerEmail = "johndoe@doit.com";
		rxWorldOrder.buyerPhone = "(333) 333-3333";
		rxWorldOrder.cartTotal = new BigDecimal("13");
		rxWorldOrder.rxWorldFee = new BigDecimal("2");
		
		rxWorldOrder.shippingAddress = shippingAddress;
    	
    	var doitShippingAddress = DOitMapper.toDOitAddress(rxWorldOrder.shippingAddress, stateRepository);
    	
    	assertThat(doitShippingAddress.streetName, is("88 Austin Road, Suite 18"));
        assertThat(doitShippingAddress.more, is("RxWorld"));
        assertThat(doitShippingAddress.city, is("Edision"));
        assertThat(doitShippingAddress.state, is("NJ"));
        assertThat(doitShippingAddress.zipCode, is("08820"));
        assertThat(doitShippingAddress.country, is("United States"));
    }
}