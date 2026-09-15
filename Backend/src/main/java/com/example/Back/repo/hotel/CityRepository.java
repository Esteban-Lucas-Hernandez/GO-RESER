package com.example.back.repo.hotel;

import com.example.back.models.hotel.City;
import com.example.back.models.hotel.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<City, Integer> {
    Optional<City> findByNombre(String nombre);
    List<City> findByDepartamento(State departamento);
    List<City> findByDepartamentoId(Integer departamentoId);
}
