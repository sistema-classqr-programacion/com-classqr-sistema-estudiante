/**
 * Repositorio JPA para gestionar las operaciones relacionadas con la entidad {@link EstudianteEntity}.
 * Proporciona métodos personalizados para buscar y verificar la existencia de estudiantes por su código.
 */
package com.classqr.sistema.estudiante.repository;

import com.classqr.sistema.commons.entity.EstudianteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EstudianteReplicaRepository extends JpaRepository<EstudianteEntity, String> {

    /**
     * Busca un estudiante por su código único.
     *
     * @param codigoEstudiante el código único del estudiante.
     * @return un {@link Optional} que contiene el {@link EstudianteEntity} encontrado,
     *         o vacío si no se encuentra.
     */
    Optional<EstudianteEntity> findByCodigoEstudiante(String codigoEstudiante);

    /**
     * Verifica si existe un estudiante con el código especificado.
     *
     * @param codigoEstudiante el código único del estudiante.
     * @return {@code true} si existe un estudiante con el código proporcionado, {@code false} en caso contrario.
     */
    Boolean existsByCodigoEstudiante(String codigoEstudiante);

}
