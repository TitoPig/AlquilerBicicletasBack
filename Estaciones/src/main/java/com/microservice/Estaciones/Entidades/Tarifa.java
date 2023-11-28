package com.microservice.Estaciones.Entidades;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TARIFAS")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tarifa {

    @Id
    @GeneratedValue(generator = "Tarifas")
    @TableGenerator(name = "Tarifas", table = "sqlite_sequence",
            pkColumnName = "name", valueColumnName = "seq",
            pkColumnValue="TARIFAS",
            initialValue=1, allocationSize=1)
    @Column(name = "ID")
    private int id;

    @Column(name = "TIPO_TARIFA")
    private int tipoTarifa;

    @Column(name = "DEFINICION")
    private String definicion;

    @Column(name = "DIA_SEMANA")
    private Integer diaSemana;

    @Column(name = "DIA_MES")
    private Integer diaMes;

    @Column(name = "MES")
    private Integer mes;

    @Column(name = "ANIO", nullable = true)
    private Integer anio;

    @Column(name = "MONTO_FIJO_ALQUILER")
    private Long montoFijoAlquiler;

    @Column(name = "MONTO_MINUTO_FRACCION")
    private long montoMinutoFraccion;

    @Column(name = "MONTO_KM")
    private long montoKm;

    @Column(name = "MONTO_HORA")
    private long montoHora;
}
