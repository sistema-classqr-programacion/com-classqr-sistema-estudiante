package com.classqr.sistema.estudiante.repository;

import com.classqr.sistema.commons.entity.EstudianteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EstudianteReplicaRepository extends JpaRepository<EstudianteEntity, String> {

    Optional<EstudianteEntity> findByCodigoEstudiante(String codigoEstudiante);

}
