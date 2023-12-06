package br.com.doit.rxworld.repository;

import java.util.HashMap;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;

import br.com.doit.rxworld.model.SaleOrder;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class SaleOrderRepository implements PanacheRepository<SaleOrder> {
    public Optional<SaleOrder> findByRxworldIdAndWebStoreId(String rxworldId, Integer webStoreId) {
    	var query = new StringBuilder("rxworldId = :rxworld_id AND webStore.doitWebStoreId = :web_store_id");
    	var params = new HashMap<String, Object>();
    	
    	params.put("rxworld_id", rxworldId);
    	params.put("web_store_id", webStoreId);
    	
        return find(query.toString(), params).singleResultOptional();
    }
}
