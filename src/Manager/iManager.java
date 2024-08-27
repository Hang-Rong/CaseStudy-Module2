package Manager;

import java.util.List;

public interface iManager<T> {
    void add(T item);
    void removeById(int id, T item);
    void update(int id, T item);
    T getById(int id);
    T getByName(String name);
    List<T> getAll();
}