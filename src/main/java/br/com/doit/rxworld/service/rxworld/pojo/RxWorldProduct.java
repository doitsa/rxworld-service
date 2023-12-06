
package br.com.doit.rxworld.service.rxworld.pojo;

import java.math.BigDecimal;

import javax.json.bind.annotation.JsonbProperty;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RxWorldProduct {
	@JsonbProperty("ExpiryDate")
	public String expirationDate;
	@JsonbProperty("LotNumber")
	public String lotNumber;
	@JsonbProperty("ProductCode")
	public String ndc;
	@JsonbProperty("ProductName")
	public String name;
	@JsonbProperty("DiscountedPrice")
	public BigDecimal price;
	@JsonbProperty("PackagePrice")
	public String priceWithoutDiscount;
	@JsonbProperty("Quantity")
	public Integer quantity;
	@JsonbProperty("SKUCode")
	public String sku;
	@JsonbProperty("Status")
	public String status;
	@JsonbProperty("Manufacturer")
	public String manufacturer;
	@JsonbProperty("Packaging")
	public String packaging;
	@JsonbProperty("InventoryLevelType")
	public String inventoryStatus;
}