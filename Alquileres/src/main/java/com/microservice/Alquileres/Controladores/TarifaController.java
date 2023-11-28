package com.microservice.Alquileres.Controladores;

import com.microservice.Alquileres.Entidades.Tarifa;
import com.microservice.Alquileres.Servicios.TarifaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tarifa")
public class TarifaController {

    private final TarifaService tarifaService;

    public TarifaController(TarifaService tarifaService){this.tarifaService = tarifaService;}

    @GetMapping
    public ResponseEntity<List<Tarifa>> getAll(){
        List<Tarifa> values = this.tarifaService.getAll();
        return ResponseEntity.ok(values);
    }

    @PostMapping
    public ResponseEntity<Tarifa> add(@RequestBody Tarifa Tarifa){
        Tarifa tarifa = this.tarifaService.add(Tarifa);
        return ResponseEntity.status(HttpStatus.CREATED).body(tarifa);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tarifa> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(this.tarifaService.getById(id));
    }

    @PutMapping()
    public ResponseEntity<Tarifa> update(@RequestBody Tarifa tarifa){
        return ResponseEntity.ok(this.tarifaService.update(tarifa));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Tarifa> deleteById(@PathVariable("id") Long id){
        return ResponseEntity.ok(this.tarifaService.delete(id));
    }


}
