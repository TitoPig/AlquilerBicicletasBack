package com.microservice.Estaciones.Repositorios;

import com.microservice.Estaciones.Entidades.Estaciones;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstacionesRepository extends JpaRepository<Estaciones, Long> {
}
