
package br.com.doit.rxworld.service.rxworld.pojo;

import javax.json.bind.annotation.JsonbProperty;

public class ShippingItem {
	@JsonbProperty("expiryDate")
	public String expirationDate;
	@JsonbProperty("lotNumber")
	public String lotNumber;
	@JsonbProperty("productCode")
	public String ndc;
	@JsonbProperty("quantity")
	public Integer quantity;
	@JsonbProperty("skuCode")
	public String sku;
}