package com.classqr.sistema.estudiante.service;

import com.classqr.sistema.commons.dto.RespuestaGeneralDTO;
import org.springframework.web.multipart.MultipartFile;

public interface ICrearEstudianteExcelService {

    RespuestaGeneralDTO crearEstudiantesExcel(MultipartFile estudiantes);

}
