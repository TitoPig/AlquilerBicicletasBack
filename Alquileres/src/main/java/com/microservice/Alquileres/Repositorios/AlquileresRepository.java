package com.microservice.Alquileres.Repositorios;

import com.microservice.Alquileres.Entidades.Alquileres;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlquileresRepository extends JpaRepository<Alquileres, Long> {
    public List<Alquileres> findByMontoGreaterThan(Long monto);

    Optional<Alquileres> findByIdClienteAndEstado(String idCliente, int idEstado);

    Alquileres findByIdAndEstado(long id, int estado);

}
