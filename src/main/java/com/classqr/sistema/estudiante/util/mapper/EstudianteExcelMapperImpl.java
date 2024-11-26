package com.classqr.sistema.estudiante.util.mapper;

import com.classqr.sistema.commons.dto.EstudianteDTO;
import com.classqr.sistema.commons.dto.TipoDocumentoDTO;
import com.classqr.sistema.commons.util.helper.Utilidades;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class EstudianteExcelMapperImpl implements EstudianteExcelMapper {

    public List<EstudianteDTO> rowToListEstudianteDTO(Sheet sheet) {
        // Procesar filas
        boolean fallo = false;
        List<EstudianteDTO> estudiantes = new ArrayList<>();
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;

            try {
                Double idTipoDocumento = Utilidades.checkType(row.getCell(3).getNumericCellValue(), Double.class).orElse(null);
                TipoDocumentoDTO tipoDocumentoDTO = new TipoDocumentoDTO();
                tipoDocumentoDTO.setId(idTipoDocumento != null ? idTipoDocumento.intValue() : null);
                EstudianteDTO estudiante = EstudianteDTO.builder()
                        .nombresEstudiante(Utilidades.checkType(row.getCell(0).getStringCellValue(), String.class).orElse(""))
                        .apellidosEstudiante(Utilidades.checkType(row.getCell(1).getStringCellValue(), String.class).orElse(""))
                        .numeroDocumento(Utilidades.checkType(row.getCell(2).getNumericCellValue(), String.class).orElse(""))
                        .idTipoDocumentoEntityFk(tipoDocumentoDTO)
                        .build();

                estudiantes.add(estudiante);
            } catch (Exception e) {
                log.error("Error procesando la fila {}: {}", i, e.getMessage());
                fallo = true;
            }
        }
        if(fallo){
            throw new RuntimeException("Error en el casteo de estudiantes");
        }
        return estudiantes;
    }

}
