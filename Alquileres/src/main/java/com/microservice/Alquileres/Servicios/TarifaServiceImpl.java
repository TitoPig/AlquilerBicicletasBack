package com.microservice.Alquileres.Servicios;

import com.microservice.Alquileres.Entidades.Tarifa;
import com.microservice.Alquileres.Repositorios.TarifaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TarifaServiceImpl implements TarifaService {

    private final TarifaRepository tarifaRepository;

    public TarifaServiceImpl(TarifaRepository tarifaRepository){this.tarifaRepository = tarifaRepository;}

    @Override
    public Tarifa add(Tarifa entity) {
        return tarifaRepository.save(entity);
    }

    @Override
    public Tarifa update(Tarifa entity) {
        return tarifaRepository.save(entity);
    }

    @Override
    public Tarifa delete(Long id) {
        Tarifa tarifa = this.getById(id);
        if (tarifa != null)
            this.tarifaRepository.delete(tarifa);
        return tarifa;
    }

    @Override
    public Tarifa getById(Long id) {
        return this.tarifaRepository.findById(id).orElseThrow();
    }

    @Override
    public List<Tarifa> getAll() {
        return this.tarifaRepository.findAll();
    }

    @Override
    public List<Tarifa> findAllByDiaSemanaIsNotNull() {
        return this.tarifaRepository.findAllByDiaSemanaIsNotNull();
    }

    @Override
    public List<Tarifa> findAllByDiaMesIsNotNull() {
        return this.tarifaRepository.findAllByDiaMesIsNotNull();
    }
}
