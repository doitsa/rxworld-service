package br.com.doit.rxworld.enums;

public enum Method {
    BOLETO("BoletoPayment"),
    CREDIT_CARD("CreditCardPayment");

    private final String typeClass;

    private Method(String typeClass) {
        this.typeClass = typeClass;
    }

    public String getTypeClass() {
        return typeClass;
    }
}