/**
 * Interfaz para el mapeador de datos de Excel a objetos relacionados con estudiantes y cursos.
 * Define el contrato para convertir las filas de un archivo Excel en una lista de objetos {@link CursoEstudianteDTO}.
 */
package com.classqr.sistema.estudiante.util.mapper;

import com.classqr.sistema.commons.dto.CursoEstudianteDTO;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.List;

public interface CursoEstudianteExcelMapper {

    /**
     * Convierte las filas de una hoja de Excel en una lista de objetos {@link CursoEstudianteDTO}.
     *
     * @param sheet       la hoja de Excel que contiene los datos de los estudiantes y cursos.
     * @param codigoCurso el código único del curso al que se asociarán los estudiantes.
     * @return una lista de objetos {@link CursoEstudianteDTO} mapeados desde el archivo Excel.
     */
    List<CursoEstudianteDTO> rowToListCursoEstudianteDTO(Sheet sheet, String codigoCurso);

}
