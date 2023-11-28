package com.microservice.Estaciones.Servicios;

import com.microservice.Estaciones.Entidades.Estaciones;

public interface EstacionesService extends Service<Estaciones,Long> {
    Estaciones obtenerEstacionMasCercana(Double latitud, Double longitud);

}
