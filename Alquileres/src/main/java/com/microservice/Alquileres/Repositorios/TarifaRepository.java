package com.microservice.Alquileres.Repositorios;

import com.microservice.Alquileres.Entidades.Tarifa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TarifaRepository extends JpaRepository<Tarifa, Long> {

    public List<Tarifa> findAllByDiaSemanaIsNotNull();

    public List<Tarifa> findAllByDiaMesIsNotNull();

}
