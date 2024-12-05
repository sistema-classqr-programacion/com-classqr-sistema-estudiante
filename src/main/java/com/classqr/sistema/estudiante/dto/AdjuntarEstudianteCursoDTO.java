/**
 * DTO (Data Transfer Object) para adjuntar un estudiante a un curso.
 * Contiene la información necesaria para asociar un estudiante con un curso específico.
 */
package com.classqr.sistema.estudiante.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdjuntarEstudianteCursoDTO {

    /**
     * Código único del estudiante.
     */
    private String codigoEstudiante;

    /**
     * Código único del curso.
     */
    private String codigoCurso;

}
