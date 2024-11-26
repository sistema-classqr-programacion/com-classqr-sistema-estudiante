package com.classqr.sistema.estudiante.service;

import com.classqr.sistema.commons.dto.RespuestaGeneralDTO;
import com.classqr.sistema.estudiante.dto.AdjuntarEstudianteCursoDTO;
import org.springframework.web.multipart.MultipartFile;

public interface ICrearEstudianteCursoService {

    RespuestaGeneralDTO crearEstudianteCursoExcel(MultipartFile file, String codigoCurso) throws Exception;

    RespuestaGeneralDTO crearEstudianteCurso(AdjuntarEstudianteCursoDTO estudiante);

}
