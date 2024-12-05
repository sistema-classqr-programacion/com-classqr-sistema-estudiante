/**
 * Implementación del mapeador para convertir las filas de una hoja de Excel en objetos {@link EstudianteDTO}.
 * Esta clase procesa las filas de un archivo Excel, extrayendo datos de estudiantes y creando una lista de objetos DTO para su manejo.
 */
package com.classqr.sistema.estudiante.util.mapper;

import com.classqr.sistema.commons.dto.EstudianteDTO;
import com.classqr.sistema.commons.dto.TipoDocumentoDTO;
import com.classqr.sistema.commons.util.helper.Utilidades;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class EstudianteExcelMapperImpl implements EstudianteExcelMapper {

    /**
     * Convierte las filas de una hoja de Excel en una lista de objetos {@link EstudianteDTO}.
     * Procesa los datos de los estudiantes, como nombres, apellidos, número de documento y tipo de documento,
     * y los mapea en objetos DTO.
     *
     * @param sheet la hoja de Excel que contiene los datos de los estudiantes.
     * @return una lista de objetos {@link EstudianteDTO} creados a partir de las filas del archivo Excel.
     * @throws RuntimeException si ocurre un error durante el procesamiento de las filas.
     */
    @Override
    public List<EstudianteDTO> rowToListEstudianteDTO(Sheet sheet) {
        boolean fallo = false; // Indicador de errores durante el procesamiento
        List<EstudianteDTO> estudiantes = new ArrayList<>();
        DecimalFormat decimalFormat = new DecimalFormat("#"); // Formato para eliminar decimales

        // Iterar sobre las filas de la hoja
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;

            try {
                // Procesar tipo de documento
                Double idTipoDocumento = Utilidades.checkType(row.getCell(3).getNumericCellValue(), Double.class).orElse(null);
                TipoDocumentoDTO tipoDocumentoDTO = new TipoDocumentoDTO();
                tipoDocumentoDTO.setId(idTipoDocumento != null ? idTipoDocumento.intValue() : null);

                // Procesar número de documento y construir el DTO del estudiante
                String formatNumber = decimalFormat.format(row.getCell(2).getNumericCellValue());
                EstudianteDTO estudiante = EstudianteDTO.builder()
                        .nombresEstudiante(Utilidades.checkType(row.getCell(0).getStringCellValue(), String.class).orElse(""))
                        .apellidosEstudiante(Utilidades.checkType(row.getCell(1).getStringCellValue(), String.class).orElse(""))
                        .numeroDocumento(formatNumber)
                        .idTipoDocumentoEntityFk(tipoDocumentoDTO)
                        .build();

                // Agregar estudiante a la lista
                estudiantes.add(estudiante);
            } catch (Exception e) {
                log.error("Error procesando la fila {}: {}", i, e.getMessage());
                fallo = true; // Marcar el fallo
            }
        }

        // Lanzar excepción si hubo errores
        if (fallo) {
            throw new RuntimeException("Error en el casteo de estudiantes");
        }

        return estudiantes;
    }
}
