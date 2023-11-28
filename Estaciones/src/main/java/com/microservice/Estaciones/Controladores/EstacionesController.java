package com.microservice.Estaciones.Controladores;

import com.microservice.Estaciones.Entidades.Estaciones;
import com.microservice.Estaciones.Servicios.EstacionesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RequestMapping("/api/estaciones")
@RestController
public class EstacionesController {

    private final EstacionesService estacionesService;

    public EstacionesController(EstacionesService estacionesService){
        this.estacionesService = estacionesService;
    }

    // Punto 1 Consultar el listado de todas las estaciones disponibles en la ciudad
    @GetMapping
    public ResponseEntity<List<Estaciones>> getAll(){
        List<Estaciones> values = estacionesService.getAll();
        return ResponseEntity.ok(values);
    }

    // Punto 2 Consultar los datos de la estación más cercana a una ubicación provista por el cliente.
    @GetMapping("/cercana")
    private ResponseEntity<Estaciones> buscarCercana(
            @RequestParam("lat") double lat,
            @RequestParam("lon") double lon) {
        Estaciones estacion = estacionesService.obtenerEstacionMasCercana(lat, lon);
        return ResponseEntity.ok(estacion);
    }

    // Punto 5 Agregar una nueva estación al sistema
    @PostMapping
    public ResponseEntity<Estaciones> add(@RequestBody Estaciones Estaciones){
        Estaciones estacion = this.estacionesService.add(Estaciones);
        return ResponseEntity.status(HttpStatus.CREATED).body(estacion);
    }

    // CRUD RESTANTES
    @GetMapping("/{id}")
    public ResponseEntity<Estaciones> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(this.estacionesService.getById(id));
    }

    @PutMapping()
    public ResponseEntity<Estaciones> update(@RequestBody Estaciones customer){
        return ResponseEntity.ok(this.estacionesService.update(customer));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Estaciones> deleteById(@PathVariable("id") Long id){
        return ResponseEntity.ok(this.estacionesService.delete(id));
    }

}
