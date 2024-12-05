/**
 * Interfaz para el servicio de consulta de estudiantes.
 * Define el contrato para la operación de inicio de sesión de un estudiante en el sistema.
 */
package com.classqr.sistema.estudiante.service;

import com.classqr.sistema.commons.dto.RespuestaGeneralDTO;
import com.classqr.sistema.estudiante.dto.LoginEstudianteDTO;

public interface IQueryEstudianteService {

    /**
     * Realiza el inicio de sesión de un estudiante.
     *
     * @param loginEstudianteDTO un objeto {@link LoginEstudianteDTO} que contiene
     *                           las credenciales del estudiante, como el número de documento
     *                           y el código del estudiante.
     * @return un objeto {@link RespuestaGeneralDTO} con el resultado de la operación,
     *         incluyendo el estado del inicio de sesión y cualquier mensaje adicional.
     */
    RespuestaGeneralDTO loginEstudiante(LoginEstudianteDTO loginEstudianteDTO);

}
