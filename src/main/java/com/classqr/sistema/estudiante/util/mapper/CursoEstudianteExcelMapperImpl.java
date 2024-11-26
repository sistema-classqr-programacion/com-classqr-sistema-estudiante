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
public class CursoEstudianteExcelMapperImpl implements CursoEstudianteExcelMapper{
    @Override
    public List<CursoEstudianteDTO> rowToListCursoEstudianteDTO(Sheet sheet, String codigoCurso) {
        // Procesar filas
        boolean fallo = false;
        List<CursoEstudianteDTO> cursoEstudiante = new ArrayList<>();
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null || row.getCell(0) == null) continue;
            try {
                String codigo = Utilidades.checkType(row.getCell(0).getStringCellValue(), String.class).orElse("");
                if(codigo.isEmpty()){
                    continue;
                }
                CursoEstudianteIdDTO cursoEstudianteIdDTO = new CursoEstudianteIdDTO();
                CursoEstudianteDTO cursoEstudianteDTO = new CursoEstudianteDTO();
                CursoDTO cursoDTO = new CursoDTO();
                cursoDTO.setCodigoCurso(codigoCurso);
                cursoEstudianteIdDTO.setCodigoCursoFk(codigoCurso);
                cursoEstudianteIdDTO.setCodigoEstudianteFk(codigo);
                cursoEstudianteDTO.setId(cursoEstudianteIdDTO);
                cursoEstudianteDTO.setCodigoEstudianteEntityFk(EstudianteDTO.builder().codigoEstudiante(codigo).build());
                cursoEstudianteDTO.setCodigoCursoEntityFk(cursoDTO);
                cursoEstudiante.add(cursoEstudianteDTO);
            } catch (Exception e) {
                log.error("Error procesando la fila {}: {}", i, e.getMessage());
                fallo = true;
            }
        }
        if(fallo){
            throw new RuntimeException("Error en el casteo de curso estudiante");
        }
        return cursoEstudiante;
    }
}
