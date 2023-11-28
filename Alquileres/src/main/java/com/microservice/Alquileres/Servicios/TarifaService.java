package com.microservice.Alquileres.Servicios;

import com.microservice.Alquileres.Entidades.Tarifa;

import java.util.List;

public interface TarifaService extends Service<Tarifa,Long>{
    public List<Tarifa> findAllByDiaSemanaIsNotNull();

    public List<Tarifa> findAllByDiaMesIsNotNull();
}
