package repository;

import java.util.UUID;

public interface Repository<T> {
    T save(UUID uuid, T t);
    void delete(T t);
    T update(T t);
    T findById(UUID id);
}
