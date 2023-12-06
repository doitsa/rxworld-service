package br.com.doit.rxworld.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

import br.com.doit.rxworld.model.WebStore;

import java.util.Optional;

@ApplicationScoped
public class WebStoreRepository implements PanacheRepository<WebStore> {
    public Optional<WebStore> findByWebStoreAndOrganization(Integer webStoreId, String organization) {
        return find("doit_web_store_id = ?1 and organization = ?2", webStoreId, organization).singleResultOptional();
    }
}
