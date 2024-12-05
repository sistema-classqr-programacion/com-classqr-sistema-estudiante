/**
 * Repositorio JPA para gestionar las operaciones relacionadas con la entidad {@link AsistenciaEntity}.
 * Proporciona métodos para verificar la existencia de registros de asistencia basados en
 * el código del estudiante y la combinación de IP con fecha de asistencia.
 */
package com.classqr.sistema.estudiante.repository;

import com.classqr.sistema.commons.entity.AsistenciaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.net.InetAddress;
import java.time.LocalDate;

@Repository("estudiante")
public interface AsistenciaRepository extends JpaRepository<AsistenciaEntity, String> {

    /**
     * Verifica si existe un registro de asistencia para un estudiante específico basado en su código.
     *
     * @param codigoEstudiante el código único del estudiante.
     * @return {@code true} si existe un registro de asistencia para el código de estudiante proporcionado, {@code false} en caso contrario.
     */
    Boolean existsByCodigoEstudianteFk_CodigoEstudiante(String codigoEstudiante);

    /**
     * Verifica si existe un registro de asistencia basado en la dirección IP del estudiante y la fecha de asistencia.
     *
     * @param ipEstudiante    la dirección IP del estudiante.
     * @param fechaAsistencia la fecha de la asistencia.
     * @return {@code true} si existe un registro de asistencia para la combinación de IP y fecha proporcionadas, {@code false} en caso contrario.
     */
    Boolean existsByIpEstudianteAndFechaAsistencia(InetAddress ipEstudiante, LocalDate fechaAsistencia);

}
