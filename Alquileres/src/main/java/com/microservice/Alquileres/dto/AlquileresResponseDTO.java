package com.microservice.Alquileres.dto;
import com.microservice.Alquileres.Entidades.Alquileres;
import com.microservice.Alquileres.Entidades.Estaciones;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlquileresResponseDTO {

    private Alquileres alquileres;
    private Double importeConvertido;
    private String monedaDestino;

    public Alquileres getAlquileres() {
        return alquileres;
    }

    public void setAlquileres(Alquileres alquileres) {
        this.alquileres = alquileres;
    }

    public Double getImporteConvertido() {
        return importeConvertido;
    }

    public void setImporteConvertido(Double importeConvertido) {
        this.importeConvertido = importeConvertido;
    }

    public String getMonedaDestino() {
        return monedaDestino;
    }

    public void setMonedaDestino(String monedaDestino) {
        this.monedaDestino = monedaDestino;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AlquileresResponseDTO that = (AlquileresResponseDTO) o;
        return Objects.equals(alquileres, that.alquileres) &&
                Objects.equals(importeConvertido, that.importeConvertido) &&
                Objects.equals(monedaDestino, that.monedaDestino);
    }

    @Override
    public int hashCode() {
        return Objects.hash(alquileres, importeConvertido, monedaDestino);
    }

    @Override
    public String toString() {
        return "AlquileresResponseDTO{" +
                "alquileres=" + alquileres +
                ", importeConvertido=" + importeConvertido +
                ", monedaDestino='" + monedaDestino + '\'' +
                '}';
    }

}