package com.classqr.sistema.estudiante.util.mapper;

import com.classqr.sistema.commons.dto.CursoEstudianteDTO;
import com.classqr.sistema.commons.dto.EstudianteDTO;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.List;

public interface CursoEstudianteExcelMapper {

    List<CursoEstudianteDTO> rowToListCursoEstudianteDTO(Sheet sheet, String codigoCurso);


}
