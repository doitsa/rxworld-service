package br.com.doit.rxworld.service.doit.pojo;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.json.bind.annotation.JsonbDateFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DOitProduct {
	public BigDecimal availableQuantity;
	@JsonbDateFormat("yyyy-MM-dd hh:mm:ss")
	public LocalDate batchExpirationDate;
	public String batchNumber;
	public String cest;
	public String description;
	public String ean;
	public Boolean isControlled;
	public Boolean isT3;
	public Boolean isDirectPurchase;
	public DOitParty manufacturer;
	public String name;
	public String ncm;
	public String ndc;
	public String type;
	public BigDecimal price;
	public BigDecimal quantity;
	public BigDecimal maxQuantityPerOrder;
	public BigDecimal salePrice;
	public Integer sequentialCode;
	public String serialNumber;
	public String shortDescription;
	public String size;
	public String sku;
	public String status;
	public String unitOfMeasure;
	public String upc;
	public String manufacturerApiStrategy;
	public BigDecimal wholesaleAcquisitionCost;
	public BigDecimal maximumAllowableCost;
	public BigDecimal averageWholesalePrice;
	public BigDecimal weight;
}
