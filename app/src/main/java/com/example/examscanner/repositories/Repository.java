package com.example.examscanner.repositories;

import java.util.List;
import java.util.function.Predicate;

public interface Repository<T> {
    public int getId();//TODO - need to remove
    public T get(long id);
    public List<T> get(Predicate<T> criteria);
    public void create(T t) throws RepositoryException;
    public void update(T t);
    public void delete(int id);
    public void removeFromCache(long id);
}
