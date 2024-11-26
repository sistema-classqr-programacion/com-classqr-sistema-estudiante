package com.classqr.sistema.estudiante.repository;

import com.classqr.sistema.commons.entity.AsistenciaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("estudiante")
public interface AsistenciaRepository extends JpaRepository<AsistenciaEntity, String> {

    Boolean existsByCodigoEstudianteFk_CodigoEstudiante(String codigoEstudiante);

}
