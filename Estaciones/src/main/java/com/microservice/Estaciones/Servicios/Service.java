package com.microservice.Estaciones.Servicios;


import java.util.List;

public interface Service<T, ID>{

    T add(T entity);
    T update(T entity);
    T delete(ID id);
    T getById(ID id);

    List<T> getAll();

}