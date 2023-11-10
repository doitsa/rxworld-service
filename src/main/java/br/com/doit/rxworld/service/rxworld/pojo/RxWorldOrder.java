package br.com.doit.rxworld.service.rxworld.pojo;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import javax.json.bind.annotation.JsonbDateFormat;
import javax.json.bind.annotation.JsonbProperty;

public class RxWorldOrder {
	@JsonbProperty("AmountPaidToSeller")
	public BigDecimal amountPaidToSeller;
	@JsonbProperty("Id")
	public Integer id;
	@JsonbProperty("BuyerName")
	public String buyerName;
	@JsonbProperty("CartTotal")
	public BigDecimal cartTotal;
	@JsonbProperty("Items")
	public List<RxWorldSaleItem> items;
	@JsonbDateFormat("yyyy-MM-dd HH:mm:ss")
	@JsonbProperty("OrderDate")
	public Date orderDate;
	@JsonbProperty("OrderNumber")
	public Integer orderNumber;
	@JsonbProperty("SellerReceivedPayment")
	public String sellerReceivedPayment;
	@JsonbProperty("SellerTransRefNumbers")
	public String sellerTransRefNumbers;
	@JsonbProperty("ShippingAddress")
	public String shippingAddress;
	@JsonbProperty("SellerAmountCreditedDate")
	public Date sellerAmountCreditedDate;
	@JsonbProperty("ShippingAmountPaidByBuyer")
	public BigDecimal shippingAmountPaidByBuyer;
	@JsonbProperty("Status")
	public String status;
	@JsonbProperty("RxWorldFee")
	public BigDecimal rxWorldFee;
	@JsonbProperty("ShippingMethod")
	public String shippingMethod;
}