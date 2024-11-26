package com.classqr.sistema.estudiante.util.mapper;

import com.classqr.sistema.commons.dto.EstudianteDTO;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.List;

public interface EstudianteExcelMapper {

    List<EstudianteDTO> rowToListEstudianteDTO(Sheet sheet);

}
