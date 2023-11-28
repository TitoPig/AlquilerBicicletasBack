package com.microservice.Alquileres.Controladores;

import com.microservice.Alquileres.Entidades.Alquileres;
import com.microservice.Alquileres.Servicios.AlquileresService;
import com.microservice.Alquileres.Servicios.TarifaService;
import com.microservice.Alquileres.dto.AlquileresResponseDTO;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController()
@RequestMapping("/api/alquileres")
public class AlquileresController {
    
    private final AlquileresService alquileresService;

    public AlquileresController(AlquileresService alquileresService, TarifaService tarifaService) {
        this.alquileresService = alquileresService;
    }

    // PUNTO 3 Iniciar el alquiler de una bicicleta desde una estación dada
    @PostMapping("/add")
    public ResponseEntity<Alquileres> add(
            @RequestParam("idCliente") String idCliente,
            @RequestParam("estacionRetiro") Long idEstacion) {
        try {
            Alquileres resultado = this.alquileresService.iniciar(idCliente, idEstacion);
            return ResponseEntity.ok(resultado);
        }catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    // Punto 4
    @PutMapping("/devolucion")
    public ResponseEntity<AlquileresResponseDTO> update(
            @RequestParam("idAlquiler") Long idAlquiler,
            @RequestParam(value = "moneda", required = false) String moneda) {
        Random random = new Random();
        Long estacionAleatoria = random.nextLong(18) + 1;
        try {
            AlquileresResponseDTO alquiler = alquileresService.finalizar(estacionAleatoria, idAlquiler, moneda);
            return ResponseEntity.ok(alquiler);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Alquileres>> getAll(){
        List<Alquileres> values = this.alquileresService.getAll();
        return ResponseEntity.ok(values);
    }

    //Punto 6 Obtener un listado de los alquileres realizados mayores a un monto ingresado
    @GetMapping("/filtrar-por-monto-mayor")
    public ResponseEntity<List<Alquileres>> filtrarAlquileresPorMontoMayor(@RequestParam(name = "montoMinimo") Long montoMinimo) {
        List<Alquileres> alquileresFiltrados = alquileresService.findByMontoGreaterThan(montoMinimo);
        return ResponseEntity.ok(alquileresFiltrados);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Alquileres> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(this.alquileresService.getById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Alquileres> deleteById(@PathVariable("id") Long id){
        return ResponseEntity.ok(this.alquileresService.delete(id));
    }
}
