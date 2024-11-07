package com.classqr.sistema.estudiante.repository;

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

    @Query("""
        SELECT ce.codigoEstudianteEntityFk FROM CursoEstudianteEntity ce WHERE ce.codigoCursoEntityFk.codigoCurso = :codigoCurso    
    """)
    List<EstudianteEntity> findAllEstudianteCurso(@Param("codigoCurso") String codigoCurso);

}
