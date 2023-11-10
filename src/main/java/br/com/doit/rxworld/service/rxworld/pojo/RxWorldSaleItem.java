
package br.com.doit.rxworld.service.rxworld.pojo;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.json.bind.annotation.JsonbProperty;

public class RxWorldSaleItem {
	@JsonbProperty("DiscountPercentage")
	public BigDecimal discountPercentage;
	@JsonbProperty("PackagePrice")
	public BigDecimal packagePrice;
	@JsonbProperty("Quantity")
	public Integer quantity;
	@JsonbProperty("SkuCode")
	public String sku;
	@JsonbProperty("CardTotal")
	public BigDecimal total;
	
	public BigDecimal totalPriceWithDiscount() {
		return applyPercentage(packagePrice, discountPercentage);
	}
	
	public static BigDecimal applyPercentage(BigDecimal value, BigDecimal percentage) {
        return value.multiply(percentage).divide(new BigDecimal(100), 2, RoundingMode.HALF_EVEN);
    }
}