package com.microservice.Estaciones.Servicios;

import com.microservice.Estaciones.Entidades.Estaciones;
import com.microservice.Estaciones.Repositorios.EstacionesRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstacionesServiceImpl implements EstacionesService {

    private final EstacionesRepository estacionesRepository;


    public EstacionesServiceImpl(EstacionesRepository estacionesRepository) {
        this.estacionesRepository = estacionesRepository;
    }

    @Override
    public Estaciones add(Estaciones entity) {
        Estaciones estacion = estacionesRepository.save(entity);
        return estacion;
    }

    @Override
    public Estaciones update(Estaciones entity) {
        Estaciones estacion = estacionesRepository.save(entity);
        return estacion;
    }

    @Override
    public Estaciones delete(Long id) {
        Estaciones estacion = this.getById(id);
        this.estacionesRepository.delete(estacion);
        return estacion;
    }

    @Override
    public Estaciones getById(Long id) {
        return this.estacionesRepository.findById(id).orElseThrow();
    }

    @Override
    public List<Estaciones> getAll() {
        return this.estacionesRepository.findAll();
    }

    public Estaciones obtenerEstacionMasCercana(Double latitud, Double longitud) {
        List<Estaciones> estaciones = estacionesRepository.findAll();

        Estaciones estacionMasCercana = null;
        double distanciaMinima = Double.MAX_VALUE;

        for (Estaciones estacion : estaciones) {
            double distancia = calcularDistancia(latitud, longitud, estacion.getLatitud(), estacion.getLongitud());

            if (distancia < distanciaMinima) {
                distanciaMinima = distancia;
                estacionMasCercana = estacion;
            }
        }

        assert estacionMasCercana != null;
        return estacionMasCercana;
    }

    private double calcularDistancia(double latitudCliente, double longitudCliente, double latitudEstacion, double longitudEstacion) {
        // Implementa la lógica para calcular la distancia entre dos puntos geográficos.

        double radioTierra = 6371; // Radio de la Tierra en kilómetros

        double latitudDiferencia = Math.toRadians(latitudEstacion - latitudCliente);
        double longitudDiferencia = Math.toRadians(longitudEstacion - longitudCliente);

        double a = Math.sin(latitudDiferencia / 2) * Math.sin(latitudDiferencia / 2) +
                Math.cos(Math.toRadians(latitudCliente)) * Math.cos(Math.toRadians(latitudEstacion)) *
                        Math.sin(longitudDiferencia / 2) * Math.sin(longitudDiferencia / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return radioTierra * c;
    }

}
