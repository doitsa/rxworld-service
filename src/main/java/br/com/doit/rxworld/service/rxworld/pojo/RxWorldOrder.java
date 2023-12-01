package br.com.doit.rxworld.service.rxworld.pojo;

import java.math.BigDecimal;
import java.util.List;

import javax.json.bind.annotation.JsonbDateFormat;
import javax.json.bind.annotation.JsonbProperty;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RxWorldOrder {
	@JsonbProperty("AmountPaidToSeller")
	public BigDecimal amountPaidToSeller;
	@JsonbProperty("Id")
	public Integer id;
	@JsonbProperty("BuyerID")
	public Integer buyerId;
	@JsonbProperty("BuyerName")
	public String buyerName;
	@JsonbProperty("BuyerPhone")
	public String buyerPhone;
	@JsonbProperty("BuyerEmail")
	public String buyerEmail;
	@JsonbProperty("BuyerDEANumber")
	public String buyerDeaNumber;
	@JsonbProperty("CartTotal")
	public BigDecimal cartTotal;
	@JsonbProperty("DiscountTotal")
	public BigDecimal discountTotal;
	@JsonbProperty("Items")
	public List<RxWorldSaleItem> items;
	@JsonbDateFormat("yyyy-MM-dd HH:mm:ss")
	@JsonbProperty("OrderDate")
	public java.util.Date orderDate;
	@JsonbProperty("OrderNumber")
	public Integer orderNumber;
	@JsonbProperty("SellerReceivedPayment")
	public String sellerReceivedPayment;
	@JsonbProperty("SellerTransRefNumbers")
	public String sellerTransRefNumbers;
	@JsonbProperty("BillingAddress")
	public RxWorldAddress billingAddress;
	@JsonbProperty("ShippingAddress")
	public RxWorldAddress shippingAddress;
	@JsonbProperty("SellerAmountCreditedDate")
	public java.util.Date sellerAmountCreditedDate;
	@JsonbProperty("ShippingAmountPaidByBuyer")
	public BigDecimal shippingAmountPaidByBuyer;
	@JsonbProperty("Status")
	public String status;
	@JsonbProperty("RxWorldFee")
	public BigDecimal rxWorldFee;
	@JsonbProperty("ShippingMethod")
	public String shippingMethod;
}