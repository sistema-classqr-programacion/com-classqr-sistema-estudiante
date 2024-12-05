/**
 * Interfaz para el servicio de creación de estudiantes a partir de un archivo Excel.
 * Define el contrato para las operaciones relacionadas con la carga y procesamiento
 * de archivos Excel para registrar estudiantes en el sistema.
 */
package com.classqr.sistema.estudiante.service;

import com.classqr.sistema.commons.dto.RespuestaGeneralDTO;
import org.springframework.web.multipart.MultipartFile;

public interface ICrearEstudianteExcelService {

    /**
     * Crea estudiantes en el sistema a partir de los datos contenidos en un archivo Excel.
     *
     * @param estudiantes el archivo Excel que contiene la información de los estudiantes a registrar.
     * @return un objeto {@link RespuestaGeneralDTO} con el resultado de la operación
     *         y cualquier mensaje adicional.
     */
    RespuestaGeneralDTO crearEstudiantesExcel(MultipartFile estudiantes);

}
