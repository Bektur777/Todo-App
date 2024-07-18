package kg.bektur.todoapp.service;

import java.util.List;

public interface DefaultService<T, C> {

    List<T> findAll();

    T find(Long id);

    T create(C c);

    T update(C c, Long id);

    void deleteById(Long id);
}
