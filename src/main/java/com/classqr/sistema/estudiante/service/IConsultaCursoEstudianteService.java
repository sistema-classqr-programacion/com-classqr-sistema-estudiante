/**
 * Interfaz para el servicio de consulta de estudiantes asociados a un curso.
 * Define el contrato para las operaciones relacionadas con la búsqueda de estudiantes en un curso específico.
 */
package com.classqr.sistema.estudiante.service;

import com.classqr.sistema.commons.dto.RespuestaGeneralDTO;

public interface IConsultaCursoEstudianteService {

    /**
     * Busca los estudiantes asociados a un curso específico.
     *
     * @param codigoCurso el código único del curso.
     * @return un objeto {@link RespuestaGeneralDTO} que contiene la información de los estudiantes encontrados
     *         y el estado de la operación.
     */
    RespuestaGeneralDTO buscarEstudiantesCurso(String codigoCurso);

}
