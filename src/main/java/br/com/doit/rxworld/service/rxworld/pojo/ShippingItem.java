
package br.com.doit.rxworld.service.rxworld.pojo;

import javax.json.bind.annotation.JsonbProperty;

public class ShippingItem {
	@JsonbProperty("ExpiryDate")
	public String expirationDate;
	@JsonbProperty("LotNumber")
	public String lotNumber;
	@JsonbProperty("ProductCode")
	public String ndc;
	@JsonbProperty("Quantity")
	public Integer quantity;
	@JsonbProperty("SKUCode")
	public String sku;
}