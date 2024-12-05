/**
 * Interfaz para el servicio de creación de estudiantes en un curso.
 * Define el contrato para las operaciones relacionadas con la creación de estudiantes,
 * tanto a partir de datos individuales como de un archivo Excel.
 */
package com.classqr.sistema.estudiante.service;

import com.classqr.sistema.commons.dto.RespuestaGeneralDTO;
import com.classqr.sistema.estudiante.dto.AdjuntarEstudianteCursoDTO;
import org.springframework.web.multipart.MultipartFile;

public interface ICrearEstudianteCursoService {

    /**
     * Crea estudiantes en un curso a partir de un archivo Excel.
     *
     * @param file        el archivo Excel que contiene los datos de los estudiantes.
     * @param codigoCurso el código único del curso al que se asociarán los estudiantes.
     * @return un objeto {@link RespuestaGeneralDTO} con el resultado de la operación
     *         y cualquier mensaje adicional.
     * @throws Exception si ocurre un error durante la carga o el procesamiento del archivo.
     */
    RespuestaGeneralDTO crearEstudianteCursoExcel(MultipartFile file, String codigoCurso) throws Exception;

    /**
     * Crea un estudiante en un curso específico utilizando datos individuales.
     *
     * @param estudiante un objeto {@link AdjuntarEstudianteCursoDTO} que contiene
     *                   la información del estudiante y el curso.
     * @return un objeto {@link RespuestaGeneralDTO} con el resultado de la operación
     *         y cualquier mensaje adicional.
     */
    RespuestaGeneralDTO crearEstudianteCurso(AdjuntarEstudianteCursoDTO estudiante);

}
