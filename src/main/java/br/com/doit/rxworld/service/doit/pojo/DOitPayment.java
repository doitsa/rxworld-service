package br.com.doit.rxworld.service.doit.pojo;

import java.math.BigDecimal;

import br.com.doit.rxworld.enums.Method;
import br.com.doit.rxworld.enums.PaymentStatus;

public class DOitPayment {
	public Integer sequentialCode;
    public Integer externalId;
    public String typeClass;
    public Integer type;
    public Method method;
    public BigDecimal amount;
    public String creditCardBrand;
    public Integer creditCardExpirationMonth;
    public Integer creditCardExpirationYear;
    public String creditCardHolder;
    public Integer creditCardInstallments;
    public String creditCardNumber;
    public String creditCardSecurityCode;
    public PaymentStatus status;
    public String creditCardNsu;
    public String boletoUrl;
    public String boletoBarCode;
    public String creditCardAcquirerReturnCode;
    public String creditCardAcquirerReturnMessage;
    public String transactionId;
}
