/**
 * Interfaz para el servicio de consulta de IP de un estudiante.
 * Define el contrato para las operaciones relacionadas con la validación o búsqueda de información
 * basada en la dirección IP de un estudiante.
 */
package com.classqr.sistema.estudiante.service;

import com.classqr.sistema.commons.dto.RespuestaGeneralDTO;

public interface IConsultarIpEstudianteService {

    /**
     * Consulta y valida la información de un estudiante basada en su dirección IP.
     *
     * @param ip la dirección IP del estudiante.
     * @return un objeto {@link RespuestaGeneralDTO} que contiene los resultados de la consulta
     *         y el estado de la operación.
     */
    RespuestaGeneralDTO consultarIpEstudiante(String ip);

}
