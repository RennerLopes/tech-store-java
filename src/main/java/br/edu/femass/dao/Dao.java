package br.edu.femass.dao;

import java.util.List;

public interface Dao<T> {
    public List<T> retrieve() throws Exception;
    public void create(T value) throws Exception;
    public void update(T value) throws Exception;
    public void delete(T value) throws Exception;
}
