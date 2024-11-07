package com.classqr.sistema.estudiante.service;

import com.classqr.sistema.commons.dto.EstudianteDTO;
import com.classqr.sistema.commons.dto.RespuestaGeneralDTO;

import java.util.List;

public interface ICrearEstudianteService {

    RespuestaGeneralDTO crearEstudiante(List<EstudianteDTO> estudiantes);

}
