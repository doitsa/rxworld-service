
package br.com.doit.rxworld.service.rxworld.pojo;

import java.util.List;

import javax.json.bind.annotation.JsonbProperty;

public class ShippingInformation {
	@JsonbProperty("orderNumber")
	public Integer orderNumber;
	@JsonbProperty("trackingNumber")
	public String trackingNumber;
	@JsonbProperty("items")
	public List<ShippingItem> items;
}