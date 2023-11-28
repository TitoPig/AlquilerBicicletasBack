package com.microservice.Alquileres.Entidades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "Alquileres")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Alquileres {


    @Id
    @GeneratedValue(generator = "Alquileres")
    @TableGenerator(name = "Alquileres", table = "sqlite_sequence",
            pkColumnName = "name", valueColumnName = "seq",
            pkColumnValue="id",
            initialValue=1, allocationSize=1)
    @Column(name = "ID")
    private int id;

    @Column(name = "ID_CLIENTE")
    private String idCliente;

    @Column(name = "ESTADO")
    private int estado;

    @Column(name = "FECHA_HORA_RETIRO")
    private LocalDateTime fechaHoraRetiro;


    @Column(name = "FECHA_HORA_DEVOLUCION", nullable = true)
    private LocalDateTime fechaHoraDevolucion;

    @Column(name = "MONTO")
    private double monto;

    @OneToOne
    @JoinColumn(name = "ESTACION_RETIRO")
    private Estaciones estacionRetiro;

    @OneToOne
    @JoinColumn(name = "ESTACION_DEVOLUCION", nullable = true)
    private Estaciones estacionDevolucion;

    @ManyToOne
    @JoinColumn(name = "ID_TARIFA")
    private Tarifa tarifa;
}
