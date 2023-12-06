package br.com.doit.rxworld.repository;

import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;

import br.com.doit.rxworld.model.State;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class StateRepository implements PanacheRepository<State> {
    public Optional<State> findByName(String name) {
        return find("name", name).singleResultOptional();
    }
}
