package io.github.wferdinando.quarkussocial.domain.repository;



import io.github.wferdinando.quarkussocial.domain.model.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped // Cria uma instancia dentro do contexto da aplicação para utilizar em qualquer lugar da aplicação
public class UserRepository implements PanacheRepository<User> {



}
