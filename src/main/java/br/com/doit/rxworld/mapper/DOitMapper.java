package br.com.doit.rxworld.mapper;

import static java.math.BigDecimal.ZERO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.lang.StringUtils;

import br.com.doit.rxworld.repository.StateRepository;
import br.com.doit.rxworld.service.doit.pojo.DOitAddress;
import br.com.doit.rxworld.service.doit.pojo.DOitPermanentParty;
import br.com.doit.rxworld.service.doit.pojo.DOitProduct;
import br.com.doit.rxworld.service.doit.pojo.DOitSaleOrder;
import br.com.doit.rxworld.service.doit.pojo.DOitSaleOrderItem;
import br.com.doit.rxworld.service.rxworld.pojo.RxWorldOrder;

public class DOitMapper {
	public static DOitSaleOrder toDOitSaleOrder(RxWorldOrder rxWorldOrder, StateRepository stateRepository) {
		var doitOrder = new DOitSaleOrder();

		var items = new ArrayList<DOitSaleOrderItem>(rxWorldOrder.items.size());

		rxWorldOrder.items.forEach(rxWorldItem -> {
			var product = new DOitProduct();

			product.sku = rxWorldItem.sku;

			var doitItem = new DOitSaleOrderItem();

			doitItem.product = product;
			doitItem.quantity = new BigDecimal(rxWorldItem.quantity);
			doitItem.unitPrice = rxWorldItem.packagePrice;
			doitItem.total = rxWorldItem.packagePrice;

			items.add(doitItem);
		});

		doitOrder.items = items;
		doitOrder.date = rxWorldOrder.orderDate;
		doitOrder.paymentInfo = toPaymentInfo(rxWorldOrder);
		doitOrder.externalId = rxWorldOrder.orderNumber.toString();
//		doitOrder.shippingCost = new BigDecimal(rxWorldOrder.shippingTotal);
		doitOrder.subTotal = rxWorldOrder.items.stream().map(item -> item.packagePrice).reduce(ZERO, BigDecimal::add);
		doitOrder.total = rxWorldOrder.amountPaidToSeller;
		doitOrder.discount = rxWorldOrder.items.stream().map(item -> item.totalPriceWithDiscount()).reduce(ZERO, BigDecimal::add);
		doitOrder.reference = rxWorldOrder.orderNumber.toString();
		doitOrder.billingAddress = toDOitAddress(rxWorldOrder.shippingAddress, stateRepository);

		var customer = new DOitPermanentParty();

		customer.name = rxWorldOrder.buyerName;

//		customer.email = rxWorldOrder.shipping.email;
//
//		var customerExternalId = rxWorldOrder.customerId;
//
//		if (customerExternalId != null) {
//			customer.externalId = customerExternalId.toString();
//		}
//
//		customer.phone = rxWorldOrder.shipping.phone;
		doitOrder.customer = customer;

		doitOrder.shippingDescription = toShippingDescription(rxWorldOrder);

		return doitOrder;
	}

	public static DOitAddress toDOitAddress(String wooAddress, StateRepository stateRepository) {
		var split = Arrays.asList(wooAddress.replaceAll("\\s", "").split(","));
		
		var doitAddress = new DOitAddress();

		doitAddress.streetName = split.get(0).trim();
		doitAddress.more = split.get(1).trim();
		doitAddress.city = split.get(2).trim();
//		doitAddress.zipCode = // TODO: ???;
		doitAddress.state = extractState(split.get(3).trim(), stateRepository);
		doitAddress.country = extractCountry(split.get(4).trim());

		return doitAddress;
	}

	private static String extractState(String address, StateRepository stateRepository) {
		var state = stateRepository.findByName(address.trim());

		if (state.isPresent()) {
			return state.get().acronym;
		}

		return address;
	}

	private static String extractCountry(String address) {
		switch (StringUtils.upperCase(address)) {
		case "BR":
			return "Brasil";
		case "US":
		case "USA":
			return "United States";
		default:
			return address;
		}
	}

	private static String toShippingDescription(RxWorldOrder order) {
		var buffer = new StringBuffer();

		buffer.append(order.shippingMethod);
		
		buffer.append("\n");

		buffer.append(order.buyerName);

		return buffer.toString();
	}
	
	private static String toPaymentInfo(RxWorldOrder order) {
		if ("Yes".equals(order.sellerReceivedPayment)) {
			var buffer = new StringBuffer();
			
			buffer.append("Seller received payment on ");
			buffer.append(order.sellerAmountCreditedDate);

			buffer.append("\n");
			
			buffer.append(order.buyerName);
			
			return buffer.toString();
		}
		
		return "";
	}
}
