
package br.com.doit.rxworld.service.rxworld.pojo;

import java.math.BigDecimal;

import javax.json.bind.annotation.JsonbProperty;

public class RxWorldProduct {
	@JsonbProperty("expiryDate")
	public String expirationDate;
	@JsonbProperty("lotNumber")
	public String lotNumber;
	@JsonbProperty("productCode")
	public String ndc;
	@JsonbProperty("discountedPrice")
	public BigDecimal price;
	@JsonbProperty("packagePrice")
	public String priceWithoutDiscount;
	@JsonbProperty("quantity")
	public Integer quantity;
	@JsonbProperty("skuCode")
	public String sku;
	@JsonbProperty("status")
	public String status;
}