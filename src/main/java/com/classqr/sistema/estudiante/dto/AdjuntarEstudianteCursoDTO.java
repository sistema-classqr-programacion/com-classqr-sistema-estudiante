package com.classqr.sistema.estudiante.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdjuntarEstudianteCursoDTO {

    private String codigoEstudiante;

    private String codigoCurso;

}
