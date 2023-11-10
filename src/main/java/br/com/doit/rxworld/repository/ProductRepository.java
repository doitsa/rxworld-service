package br.com.doit.rxworld.repository;

import java.util.HashMap;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;

import br.com.doit.rxworld.model.Product;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class ProductRepository implements PanacheRepository<Product> {
    public Optional<Product> findBySkuAndWebStore(String productSku, Integer webStoreId) {
    	var query = new StringBuilder("sku = :product_sku AND webStore.doitWebStoreId = :web_store_id");
    	var params = new HashMap<String, Object>();
    	
    	params.put("product_sku", productSku);
    	params.put("web_store_id", webStoreId);
    	
        return find(query.toString(), params).singleResultOptional();
    }
}
