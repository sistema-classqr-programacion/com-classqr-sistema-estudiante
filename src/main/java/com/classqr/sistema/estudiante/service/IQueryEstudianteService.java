package com.classqr.sistema.estudiante.service;

import com.classqr.sistema.commons.dto.RespuestaGeneralDTO;
import com.classqr.sistema.estudiante.dto.LoginEstudianteDTO;

public interface IQueryEstudianteService {

    RespuestaGeneralDTO loginEstudiante(LoginEstudianteDTO loginEstudianteDTO);

}
