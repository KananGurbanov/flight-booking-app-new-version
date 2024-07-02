package dao.repository;

import java.util.List;
import java.util.Optional;

public interface Repository<T> {
    void save(T t);

    void delete(T t);

    List<T> findAll();

    Optional<T> findById(Long id);

    void update(Long id, T t);
}
