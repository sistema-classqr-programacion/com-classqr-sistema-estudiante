/**
 * Implementación del mapeador para convertir las filas de una hoja de Excel en objetos {@link CursoEstudianteDTO}.
 * Esta clase procesa las filas de un archivo Excel, extrayendo datos de estudiantes y cursos,
 * y creando una lista de objetos DTO para su posterior manejo.
 */
package com.classqr.sistema.estudiante.util.mapper;

import com.classqr.sistema.commons.dto.CursoDTO;
import com.classqr.sistema.commons.dto.CursoEstudianteDTO;
import com.classqr.sistema.commons.dto.EstudianteDTO;
import com.classqr.sistema.commons.dto.TipoDocumentoDTO;
import com.classqr.sistema.commons.dto.embeddable.CursoEstudianteIdDTO;
import com.classqr.sistema.commons.util.helper.Utilidades;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class CursoEstudianteExcelMapperImpl implements CursoEstudianteExcelMapper {

    /**
     * Procesa las filas de una hoja de Excel y las convierte en una lista de objetos {@link CursoEstudianteDTO}.
     *
     * @param sheet       la hoja de Excel que contiene los datos de los estudiantes y cursos.
     * @param codigoCurso el código único del curso al que se asociarán los estudiantes.
     * @return una lista de objetos {@link CursoEstudianteDTO} creados a partir de las filas del archivo Excel.
     * @throws RuntimeException si ocurre un error durante el procesamiento de las filas.
     */
    @Override
    public List<CursoEstudianteDTO> rowToListCursoEstudianteDTO(Sheet sheet, String codigoCurso) {
        boolean fallo = false; // Indicador para errores durante el procesamiento
        List<CursoEstudianteDTO> cursoEstudiante = new ArrayList<>();

        // Iterar sobre las filas de la hoja
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null || row.getCell(0) == null) continue;

            try {
                // Extraer y procesar el código del estudiante
                String codigo = Utilidades.checkType(row.getCell(0).getStringCellValue(), String.class).orElse("");
                if (codigo.isEmpty()) {
                    continue;
                }

                // Crear los objetos necesarios para el mapeo
                CursoEstudianteIdDTO cursoEstudianteIdDTO = new CursoEstudianteIdDTO();
                CursoEstudianteDTO cursoEstudianteDTO = new CursoEstudianteDTO();
                CursoDTO cursoDTO = new CursoDTO();

                cursoDTO.setCodigoCurso(codigoCurso);
                cursoEstudianteIdDTO.setCodigoCursoFk(codigoCurso);
                cursoEstudianteIdDTO.setCodigoEstudianteFk(codigo);
                cursoEstudianteDTO.setId(cursoEstudianteIdDTO);
                cursoEstudianteDTO.setCodigoEstudianteEntityFk(EstudianteDTO.builder().codigoEstudiante(codigo).build());
                cursoEstudianteDTO.setCodigoCursoEntityFk(cursoDTO);

                // Agregar a la lista de resultados
                cursoEstudiante.add(cursoEstudianteDTO);
            } catch (Exception e) {
                log.error("Error procesando la fila {}: {}", i, e.getMessage());
                fallo = true; // Marcar el fallo
            }
        }

        // Lanzar excepción si hubo fallos
        if (fallo) {
            throw new RuntimeException("Error en el casteo de curso estudiante");
        }

        return cursoEstudiante;
    }
}
