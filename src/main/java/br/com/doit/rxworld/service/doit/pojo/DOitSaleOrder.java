package br.com.doit.rxworld.service.doit.pojo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.json.bind.annotation.JsonbDateFormat;

public class DOitSaleOrder {
	public Integer sequentialCode;
    public DOitAddress billingAddress;
    public DOitPermanentParty customer;
    @JsonbDateFormat("yyyy-MM-dd HH:mm:ss")
    public Date date;
    public BigDecimal discount;
    public String externalId;
    public String federalDocument;
    public List<DOitSaleOrderItem> items;
    public String paymentInfo;
    public String externalPaymentInfo;
    public String reference;
    public DOitAddress shippingAddress;
    public String shippingDescription;
    public BigDecimal shippingCost;
    public BigDecimal subTotal;
    public BigDecimal total;
    public DOitWebStore webStore;
    public List<DOitPayment> payments;
    public String clientIp;
    public String controlledOrderResponse;
    public String controlledTransactionId;
    public String csosTrackingId;
    public String customerDeaNumber;
    public String customerDeaUsername;
    public boolean isSigned;
    public String signedStatus;
    @JsonbDateFormat("yyyy-MM-dd HH:mm:ss")
    public Date updatedSignedOrder;
    public String webStorePayment;
}
