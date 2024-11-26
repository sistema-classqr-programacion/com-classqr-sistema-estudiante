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

}
