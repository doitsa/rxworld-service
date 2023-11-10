package br.com.doit.rxworld.mapper;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

import br.com.doit.rxworld.service.doit.pojo.DOitProduct;
import br.com.doit.rxworld.service.rxworld.pojo.RxWorldProduct;
import br.com.doit.rxworld.service.rxworld.pojo.ShippingInformation;

public class RxWorldMapper {
	private static final String ALWAYS_AVAILABLE = "ALWAYS_AVAILABLE";
	private static final String MANAGED_BY_INVENTORY = "MANAGED_BY_INVENTORY";
	private static final String UNAVAILABLE = "UNAVAILABLE";
	
	public static RxWorldProduct toRxWorldProduct(DOitProduct doitProduct) {
		var product = new RxWorldProduct();

		var form = DateTimeFormatter.ofPattern("MM/yyyy");
		
		product.expirationDate = doitProduct.batchExpirationDate.format(form);
		product.lotNumber = doitProduct.batchNumber;
		product.ndc = doitProduct.ndc;
		product.price = doitProduct.salePrice;
		product.quantity = doitProduct.availableQuantity.intValue();
		product.sku = doitProduct.sku;
		product.status = toRxWorldStockStatus(doitProduct.status, doitProduct.availableQuantity);
		
		return product;
	}
	
	public static ShippingInformation toRxWorldShippingInformation() {
		var shippingInformation = new ShippingInformation();
		
		// TODO
		
		return shippingInformation;
	}
	
	public static boolean isAvailable(BigDecimal availableQuantity) {
		return availableQuantity != null && availableQuantity.compareTo(BigDecimal.ZERO) > 0;
	}
	
	private static String toRxWorldStockStatus(String status, BigDecimal availableQuantity) {
		switch (status) {
		case UNAVAILABLE:
			return "inactive";
		case MANAGED_BY_INVENTORY:
			return isAvailable(availableQuantity) ? "active" : "inactive";
		case ALWAYS_AVAILABLE:
			return "active";
		default:
			return "active";
		}
	}
}
