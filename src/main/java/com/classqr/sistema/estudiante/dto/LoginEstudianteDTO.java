/**
 * DTO (Data Transfer Object) para el inicio de sesión de un estudiante.
 * Contiene los datos necesarios para autenticar a un estudiante en el sistema.
 */
package com.classqr.sistema.estudiante.dto;

import lombok.Data;

@Data
public class LoginEstudianteDTO {

    /**
     * Número de documento del estudiante.
     */
    private String numeroDocumento;

    /**
     * Código único del estudiante.
     */
    private String codigoEstudiante;

}
