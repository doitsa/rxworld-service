package br.com.doit.rxworld.mapper;

import static java.math.BigDecimal.ZERO;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;

import br.com.doit.rxworld.repository.StateRepository;
import br.com.doit.rxworld.service.doit.pojo.DOitAddress;
import br.com.doit.rxworld.service.doit.pojo.DOitPermanentParty;
import br.com.doit.rxworld.service.doit.pojo.DOitProduct;
import br.com.doit.rxworld.service.doit.pojo.DOitSaleOrder;
import br.com.doit.rxworld.service.doit.pojo.DOitSaleOrderItem;
import br.com.doit.rxworld.service.rxworld.pojo.RxWorldOrder;
import br.com.doit.rxworld.service.rxworld.pojo.RxWorldAddress;

public class DOitMapper {
	public static DOitSaleOrder toDOitSaleOrder(RxWorldOrder rxWorldOrder, StateRepository stateRepository) {
		var doitOrder = new DOitSaleOrder();

		var items = new ArrayList<DOitSaleOrderItem>(rxWorldOrder.items.size());

		rxWorldOrder.items.forEach(rxWorldItem -> {
			var product = new DOitProduct();

			product.sku = rxWorldItem.sku;
			product.name = rxWorldItem.name;

			var doitItem = new DOitSaleOrderItem();

			doitItem.product = product;
			doitItem.quantity = new BigDecimal(rxWorldItem.quantity);
			doitItem.unitPrice = nullableBigDecimal(rxWorldItem.unitPriceWithDiscountApplied());
			doitItem.total = nullableBigDecimal(rxWorldItem.subTotal());

			items.add(doitItem);
		});

		doitOrder.items = items;
		doitOrder.date = rxWorldOrder.orderDate;
		doitOrder.paymentInfo = toPaymentInfo(rxWorldOrder);
		doitOrder.externalId = rxWorldOrder.orderNumber.toString();
		doitOrder.subTotal = rxWorldOrder.items.stream().map(item -> nullableBigDecimal(item.subTotal())).reduce(ZERO, BigDecimal::add);
		doitOrder.total = nullableBigDecimal(rxWorldOrder.cartTotal);
		doitOrder.reference = rxWorldOrder.orderNumber.toString();
		doitOrder.billingAddress = toDOitAddress(rxWorldOrder.billingAddress, stateRepository);
		doitOrder.shippingAddress = toDOitAddress(rxWorldOrder.shippingAddress, stateRepository);

		var customer = new DOitPermanentParty();

		customer.name = rxWorldOrder.buyerName;
		customer.email = rxWorldOrder.buyerEmail;
		customer.phone = rxWorldOrder.buyerPhone;

		var customerExternalId = rxWorldOrder.buyerId;

		if (customerExternalId != null) {
			customer.externalId = customerExternalId.toString();
		}

		doitOrder.customer = customer;

		doitOrder.shippingDescription = toShippingDescription(rxWorldOrder);

		return doitOrder;
	}

	public static DOitAddress toDOitAddress(RxWorldAddress rxWorldAddress, StateRepository stateRepository) {
		var doitAddress = new DOitAddress();

		doitAddress.streetName = rxWorldAddress.addressLine1;
		doitAddress.more = rxWorldAddress.addressLine2;
		doitAddress.city = rxWorldAddress.city;
		doitAddress.zipCode = rxWorldAddress.zipCode;
		doitAddress.state = extractState(rxWorldAddress.state, stateRepository);
		doitAddress.country = extractCountry(rxWorldAddress.country);

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

			var formatter = new SimpleDateFormat("MM/dd/yyyy");

			buffer.append("Payment date: ");
			buffer.append(formatter.format(order.sellerAmountCreditedDate));

			buffer.append("\n");

			buffer.append("Payment transaction number: ");
			buffer.append(order.sellerTransRefNumbers);

			return buffer.toString();
		}

		return "";
	}
	
	public static BigDecimal nullableBigDecimal(BigDecimal nullableBigDecimal) {
		return nullableBigDecimal == null ? BigDecimal.ZERO : nullableBigDecimal;
	}
}
