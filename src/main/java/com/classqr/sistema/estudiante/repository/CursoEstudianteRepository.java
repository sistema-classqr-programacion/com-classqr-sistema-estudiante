/**
 * Repositorio JPA para gestionar las operaciones relacionadas con la entidad {@link CursoEstudianteEntity}.
 * Proporciona métodos personalizados para consultar estudiantes en cursos y verificar la existencia de registros.
 */
package com.classqr.sistema.estudiante.repository;

import com.classqr.sistema.commons.dto.EstudianteDTO;
import com.classqr.sistema.commons.entity.CursoEstudianteEntity;
import com.classqr.sistema.commons.entity.EstudianteEntity;
import com.classqr.sistema.commons.entity.embeddable.CursoEstudianteIdEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CursoEstudianteRepository extends JpaRepository<CursoEstudianteEntity, CursoEstudianteIdEntity> {

    /**
     * Obtiene una lista de estudiantes asociados a un curso específico.
     *
     * @param codigoCurso el código único del curso.
     * @return una lista de objetos {@link EstudianteDTO} que representan a los estudiantes asociados al curso.
     */
    @Query(value = """
        SELECT new com.classqr.sistema.commons.dto.EstudianteDTO(
            ce.codigoEstudianteEntityFk.codigoEstudiante,
            ce.codigoEstudianteEntityFk.nombresEstudiante,
            ce.codigoEstudianteEntityFk.apellidosEstudiante,
            null,
            null,
            null
        )
        FROM CursoEstudianteEntity ce WHERE ce.codigoCursoEntityFk.codigoCurso = :codigoCurso 
    """)
    List<EstudianteDTO> findAllEstudianteCurso(@Param("codigoCurso") String codigoCurso);

    /**
     * Verifica si existe un registro que asocia un estudiante a un curso específico.
     *
     * @param codigoEstudiante el código único del estudiante.
     * @param codigoCurso      el código único del curso.
     * @return {@code true} si existe un registro que asocia al estudiante con el curso, {@code false} en caso contrario.
     */
    Boolean existsByCodigoEstudianteEntityFk_CodigoEstudianteAndCodigoCursoEntityFk_CodigoCurso(String codigoEstudiante, String codigoCurso);

}
