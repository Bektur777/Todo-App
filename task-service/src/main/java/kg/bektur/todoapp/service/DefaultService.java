package kg.bektur.todoapp.service;

import org.springframework.security.core.Authentication;

import java.util.List;

public interface DefaultService<T, C> {

    List<T> findAll(Authentication authentication);

    T find(Long id, Authentication authentication);

    T create(C c, Authentication authentication);

    T update(C c, Long id, Authentication authentication);

    void deleteById(Long id, Authentication authentication);
}
