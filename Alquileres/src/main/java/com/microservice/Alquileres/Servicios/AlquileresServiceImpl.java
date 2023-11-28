package com.microservice.Alquileres.Servicios;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.Alquileres.Entidades.Alquileres;
import com.microservice.Alquileres.Entidades.Estaciones;
import com.microservice.Alquileres.Entidades.Tarifa;
import com.microservice.Alquileres.Repositorios.AlquileresRepository;
import com.microservice.Alquileres.dto.AlquileresResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class AlquileresServiceImpl implements AlquileresService{

    private final AlquileresRepository alquileresRepository;
    @Autowired
    private TarifaService tarifaService;

    public AlquileresServiceImpl(AlquileresRepository alquileresRepository) {
        this.alquileresRepository = alquileresRepository;

    }

    @Override
    public Alquileres add(Alquileres entity) {
        return alquileresRepository.save(entity);
    }

    @Override
    public Alquileres update(Alquileres entity) {
        return alquileresRepository.save(entity);
    }

    @Override
    public Alquileres delete(Long id) {
        Alquileres alquiler = this.getById(id);
        if (alquiler !=null){
            alquileresRepository.deleteById(id);
        }
        return alquiler;
    }

    @Override
    public Alquileres getById(Long id) {
        return this.alquileresRepository.findById(id).orElseThrow();
    }

    @Override
    public List<Alquileres> getAll() {
        return this.alquileresRepository.findAll();
    }

    @Override
    public List<Alquileres> findByMontoGreaterThan(Long monto) {
        return this.alquileresRepository.findByMontoGreaterThan(monto);
    }


    //Punto 3
    @Override
    public Alquileres iniciar(String idCliente, Long idEstacion) {

        Estaciones estacionRetiro;

        //Consultar si existe una estacion de idIngresada
        try {
            RestTemplate template = new RestTemplate();
            ResponseEntity<Estaciones> res = template.getForEntity(
                    "http://localhost:8081/api/estaciones/{id}", Estaciones.class, idEstacion
            );
            if (res.getStatusCode().is2xxSuccessful()) {
                estacionRetiro = res.getBody();
            } else {
                throw new NoSuchElementException("No se encontró esa estación");
            }
        } catch (HttpClientErrorException e) {
            throw new NoSuchElementException("Error al buscar la estación");
        }

        Optional<Alquileres> optionalAlquiler = alquileresRepository.findByIdClienteAndEstado(idCliente, 1);

        if (optionalAlquiler.isPresent()) {
            throw new RuntimeException("El cliente " + idCliente + " ya tiene un alquiler activo");
        }

        // Crear un alquiler
        Alquileres nuevo = new Alquileres();
        nuevo.setEstado(1);
        nuevo.setIdCliente(idCliente);
        nuevo.setEstacionRetiro(estacionRetiro);
        nuevo.setFechaHoraRetiro(LocalDateTime.now());
        return add(nuevo);
    }

    //PUNTO 4
    @Override
    public AlquileresResponseDTO finalizar(Long estacionAleatoria, Long idAlquiler, String moneda) {
        Estaciones estacion;
        try {
            RestTemplate template = new RestTemplate();
            ResponseEntity<Estaciones> res = template.getForEntity(
                    "http://localhost:8081/api/estaciones/{id}", Estaciones.class, estacionAleatoria);
            if (res.getStatusCode().is2xxSuccessful()) {
                estacion = res.getBody();
            } else {
                throw new NoSuchElementException("No se encontró esa estación");
            }
        } catch (RuntimeException e) {
            throw new NoSuchElementException("Error al buscar la estación");
        }

        // Validar que exista un alquiler para el cliente ingresado
        Alquileres alquiler = alquileresRepository.findByIdAndEstado(idAlquiler, 1);

        //excepcion no se encontro el alquiler
        if (alquiler == null) {
            throw new NoSuchElementException("No se encontro el alquiler iniciado del cliente");
        }

        //setea estado finalizado
        alquiler.setEstado(2);
        //setea cuando se finalizo el alquiler
        alquiler.setFechaHoraDevolucion(LocalDateTime.now());
        //en donde se devolvio
        alquiler.setEstacionDevolucion(estacion);
        //calcula cuanto duro el alquiler, aplicando lo pedido si duro mas de 31 minutos
        List<Long> cantidadHoras = calcularTiempoAlquiler(alquiler);
        //le asigna la tarifa al alquiler dependiendo el dia
        asignarTarifa(alquiler);
        //calcula cuanto se recorrio durante el alquiler
        double distancia = calcularDistancia(alquiler);
        //calcula cuanto costara al final el alquiler
        double monto = calcularCostoFinal(alquiler, cantidadHoras, distancia);
        //setea el monto en el alquiler
        alquiler.setMonto(monto);

        update(alquiler);
        AlquileresResponseDTO alquilerResponseDTO = new AlquileresResponseDTO();

        //consumo del api de la moneda
        try {
            if (moneda != null) {
                // Crear el cuerpo de la solicitud para la API externa
                Map<String, Object> requestMap = new HashMap<>();
                requestMap.put("moneda_destino", moneda);
                requestMap.put("importe", alquiler.getMonto());

                // Convertir a JSON
                ObjectMapper objectMapper = new ObjectMapper();
                String requestBody = objectMapper.writeValueAsString(requestMap);

                // Configurar los encabezados de la solicitud HTTP
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

                // URL de la API externa
                String url = "http://34.82.105.125:8080/convertir";

                // Realizar la solicitud POST a la API externa
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

                // Obtener la respuesta de la API externa
                String response = responseEntity.getBody();
                System.out.println("Respuesta de la API externa: " + response);

                // Deserializar la respuesta JSON de la API externa
                Map<String, Object> responseBody = objectMapper.readValue(response, new TypeReference<>() {
                });
                alquilerResponseDTO.setImporteConvertido((Double) responseBody.get("importe"));
                alquilerResponseDTO.setMonedaDestino((String) responseBody.get("moneda"));
            } else {
                alquilerResponseDTO.setImporteConvertido((Double) alquiler.getMonto());
                alquilerResponseDTO.setMonedaDestino((String) "ARS");
            }
            alquilerResponseDTO.setAlquileres(alquiler);
        } catch (Exception e) {
            // Manejar la excepción adecuadamente
            e.printStackTrace();
        }

        return alquilerResponseDTO;
    }

    private List<Long> calcularTiempoAlquiler(Alquileres alquiler) {
        Duration duration = Duration.between(alquiler.getFechaHoraRetiro(), alquiler.getFechaHoraDevolucion());
        List<Long> tiempo = new ArrayList<>();
        long horas = duration.toHours();
        long minutos = duration.minusHours(horas).toMinutes();
        if (minutos >= 31) {
            horas += 1;
            minutos = 0;
        }
        tiempo.add(horas);
        tiempo.add(minutos);
        return tiempo;
    }

    private void asignarTarifa(Alquileres alquiler) {
        List<Tarifa> tarifasMes = this.tarifaService.findAllByDiaMesIsNotNull();
        List<Tarifa> tarifasDias = this.tarifaService.findAllByDiaSemanaIsNotNull();
        Tarifa tarifaAplicar = null;
        /// 10 12 2005 == alguna fecha de la lista tarifa
        for (Tarifa tarifa : tarifasMes) {
            if (Objects.equals(alquiler.getFechaHoraRetiro().toLocalDate(), LocalDate.of(tarifa.getAnio(), tarifa.getMes(), tarifa.getDiaMes()))) {
                tarifaAplicar = tarifa;
            }
        }
        if (tarifaAplicar == null) {
            for (Tarifa tarifa : tarifasDias) {
                if (alquiler.getFechaHoraRetiro().getDayOfWeek().getValue() == tarifa.getDiaSemana()) {
                    tarifaAplicar = tarifa;
                }
            }
        }
        alquiler.setTarifa(tarifaAplicar);
    }


    private Long calcularCostoFinal(Alquileres alquiler, List<Long> cantidadHoras, double distancia) {
        Long costoFinal = 0L;
        costoFinal += alquiler.getTarifa().getMontoFijoAlquiler();
        costoFinal += (alquiler.getTarifa().getMontoHora() * cantidadHoras.get(0));
        costoFinal += (alquiler.getTarifa().getMontoMinutoFraccion() * cantidadHoras.get(1));
        costoFinal += Math.round(alquiler.getTarifa().getMontoKm() * distancia);
        return costoFinal;
    }

    private Double calcularDistancia(Alquileres alquiler) {
        Double latitud1 = alquiler.getEstacionRetiro().getLatitud();
        Double longitud1 = alquiler.getEstacionRetiro().getLongitud();
        Double latitud2 = alquiler.getEstacionDevolucion().getLatitud();
        Double longitud2 = alquiler.getEstacionDevolucion().getLongitud();
        double distanciaLatitud = (latitud2 - latitud1) * 110000;
        double distanciaLongitud = (longitud2 - longitud1) * 110000;

        // Calcular la distancia euclidiana
        return (Math.sqrt(distanciaLatitud * distanciaLatitud + distanciaLongitud * distanciaLongitud) / 1000);
    }
}

