/**
 * Interfaz para el mapeador de datos de Excel a objetos relacionados con estudiantes.
 * Define el contrato para convertir las filas de una hoja de Excel en una lista de objetos {@link EstudianteDTO}.
 */
package com.classqr.sistema.estudiante.util.mapper;

import com.classqr.sistema.commons.dto.EstudianteDTO;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.List;

public interface EstudianteExcelMapper {

    /**
     * Convierte las filas de una hoja de Excel en una lista de objetos {@link EstudianteDTO}.
     *
     * @param sheet la hoja de Excel que contiene los datos de los estudiantes.
     * @return una lista de objetos {@link EstudianteDTO} mapeados desde las filas del archivo Excel.
     */
    List<EstudianteDTO> rowToListEstudianteDTO(Sheet sheet);

}
