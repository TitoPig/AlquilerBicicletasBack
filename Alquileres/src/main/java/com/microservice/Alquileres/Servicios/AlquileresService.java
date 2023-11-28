package com.microservice.Alquileres.Servicios;

import com.microservice.Alquileres.Entidades.Alquileres;
import com.microservice.Alquileres.Entidades.Estaciones;
import com.microservice.Alquileres.dto.AlquileresResponseDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface AlquileresService extends Service<Alquileres, Long> {

    public List<Alquileres> findByMontoGreaterThan(Long monto);

    Alquileres iniciar(String idCliente, Long idEstacion);

    AlquileresResponseDTO finalizar(Long estacionAleatoria, Long idAlquiler, String moneda);

}
