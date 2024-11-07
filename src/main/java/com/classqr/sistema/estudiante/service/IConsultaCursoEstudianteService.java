package com.classqr.sistema.estudiante.service;

import com.classqr.sistema.commons.dto.RespuestaGeneralDTO;

public interface IConsultaCursoEstudianteService {

    RespuestaGeneralDTO buscarEstudiantesCurso(String codigoCurso);

}
