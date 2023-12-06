package br.com.doit.rxworld.mapper;

import java.time.format.DateTimeFormatter;

import br.com.doit.rxworld.service.doit.pojo.DOitProduct;
import br.com.doit.rxworld.service.rxworld.pojo.RxWorldProduct;

public class RxWorldMapper {
	public static RxWorldProduct toRxWorldProduct(DOitProduct doitProduct) {
		var product = new RxWorldProduct();

		var formatter = DateTimeFormatter.ofPattern("MM/yyyy");
		
		product.lotNumber = doitProduct.batchNumber;
		product.ndc = doitProduct.ndc;
		product.price = doitProduct.salePrice != null ? doitProduct.salePrice : doitProduct.price;
		product.quantity = doitProduct.availableQuantity.intValue();
		product.sku = doitProduct.sku;
		product.inventoryStatus = doitProduct.status;
		product.name = doitProduct.name;
		product.packaging = doitProduct.size;
		
		if (doitProduct.batchExpirationDate != null) {
			product.expirationDate = doitProduct.batchExpirationDate.format(formatter);
		}
		
		if (doitProduct.manufacturer != null) {
			product.manufacturer = doitProduct.manufacturer.fullName;
		}
		
		return product;
	}
}
