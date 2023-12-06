
package br.com.doit.rxworld.service.rxworld.pojo;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.json.bind.annotation.JsonbProperty;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RxWorldSaleItem {
	@JsonbProperty("DiscountPercentage")
	public BigDecimal discountPercentage;
	@JsonbProperty("Name")
	public String name;
	@JsonbProperty("PackagePrice")
	public BigDecimal packagePrice;
	@JsonbProperty("Quantity")
	public Integer quantity;
	@JsonbProperty("SKUCode")
	public String sku;
	
	public BigDecimal unitPriceWithDiscountApplied() {
		if (discountPercentage != null && discountPercentage.compareTo(BigDecimal.ZERO) > 0) {
			return applyPercentage(packagePrice, discountPercentage);
		}
		
		return packagePrice;
	}
	
	public BigDecimal subTotal() {
		return unitPriceWithDiscountApplied().multiply(BigDecimal.valueOf(quantity));
	}
	
	private static BigDecimal applyPercentage(BigDecimal value, BigDecimal percentage) {
        var discountPrice = value.multiply(percentage).divide(new BigDecimal(100), 2, RoundingMode.HALF_EVEN);
        
        return value.subtract(discountPrice);
    }
}