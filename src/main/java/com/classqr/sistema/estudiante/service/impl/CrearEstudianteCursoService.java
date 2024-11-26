package com.classqr.sistema.estudiante.service.impl;

import com.classqr.sistema.commons.dto.CursoDTO;
import com.classqr.sistema.commons.dto.CursoEstudianteDTO;
import com.classqr.sistema.commons.dto.EstudianteDTO;
import com.classqr.sistema.commons.dto.RespuestaGeneralDTO;
import com.classqr.sistema.commons.dto.embeddable.CursoEstudianteIdDTO;
import com.classqr.sistema.commons.util.mapper.CursoEstudianteMapper;
import com.classqr.sistema.estudiante.dto.AdjuntarEstudianteCursoDTO;
import com.classqr.sistema.estudiante.repository.CursoEstudianteRepository;
import com.classqr.sistema.estudiante.service.ICrearEstudianteCursoService;
import com.classqr.sistema.estudiante.util.mapper.CursoEstudianteExcelMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
@Log4j2
public class CrearEstudianteCursoService implements ICrearEstudianteCursoService {

    private final CursoEstudianteRepository cursoEstudianteRepository;

    private final CursoEstudianteMapper cursoEstudianteMapper;

    private final CursoEstudianteExcelMapper cursoEstudianteExcelMapper;

    private static final List<String> EXPECTED_HEADERS = List.of(
            "codigo"
    );

    @Override
    @Transactional
    public RespuestaGeneralDTO crearEstudianteCursoExcel(MultipartFile file, String codigoCurso){
        RespuestaGeneralDTO respuestaGeneralDTO = new RespuestaGeneralDTO();
        if (file.isEmpty()) {
            respuestaGeneralDTO.setMessage("Por favor, sube un archivo válido.");
            respuestaGeneralDTO.setData(new ArrayList<>());
            respuestaGeneralDTO.setStatus(HttpStatus.BAD_REQUEST);
            return respuestaGeneralDTO;
        }
        try(Workbook workbook = new XSSFWorkbook(file.getInputStream())){
            Sheet sheet = workbook.getSheetAt(0); // Leer la primera hoja del archivo
            // Verificar las cabeceras
            Row headerRow = sheet.getRow(0);
            if (!areHeadersValid(headerRow)) {
                respuestaGeneralDTO.setMessage("Las cabeceras del archivo no son válidas.");
                respuestaGeneralDTO.setData(new ArrayList<>());
                respuestaGeneralDTO.setStatus(HttpStatus.BAD_REQUEST);
                return respuestaGeneralDTO;
            }

            // Procesar filas
            List<CursoEstudianteDTO> cursosEstudiante = cursoEstudianteExcelMapper.rowToListCursoEstudianteDTO(sheet, codigoCurso);

            for(CursoEstudianteDTO cursoEstudianteDTO : cursosEstudiante){
                cursoEstudianteRepository.save(cursoEstudianteMapper.dtoToEntity(cursoEstudianteDTO));
            }

            respuestaGeneralDTO.setMessage("Se adjuntaron correctamente los estudiantes con el curso");
            respuestaGeneralDTO.setData(new ArrayList<>());
            respuestaGeneralDTO.setStatus(HttpStatus.CREATED);
        }catch (Exception e){
            log.error("Error en adjuntar estudiante con el curso ", e);
            respuestaGeneralDTO.setMessage("Error en adjuntar estudiante con el curso");
            respuestaGeneralDTO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return respuestaGeneralDTO;
    }

    @Override
    @Transactional
    public RespuestaGeneralDTO crearEstudianteCurso(AdjuntarEstudianteCursoDTO estudiante) {
        RespuestaGeneralDTO respuestaGeneralDTO = new RespuestaGeneralDTO();
        try {
            CursoEstudianteIdDTO cursoEstudianteIdDTO = new CursoEstudianteIdDTO();
            cursoEstudianteIdDTO.setCodigoEstudianteFk(estudiante.getCodigoEstudiante());
            cursoEstudianteIdDTO.setCodigoCursoFk(estudiante.getCodigoCurso());
            CursoDTO cursoDTO = new CursoDTO();
            cursoDTO.setCodigoCurso(estudiante.getCodigoCurso());
            EstudianteDTO estudianteDTO = new EstudianteDTO();
            estudianteDTO.setCodigoEstudiante(estudiante.getCodigoEstudiante());
            CursoEstudianteDTO cursoEstudianteDTO = new CursoEstudianteDTO();
            cursoEstudianteDTO.setId(cursoEstudianteIdDTO);
            cursoEstudianteDTO.setCodigoEstudianteEntityFk(estudianteDTO);
            cursoEstudianteDTO.setCodigoCursoEntityFk(cursoDTO);
            cursoEstudianteRepository.save(cursoEstudianteMapper.dtoToEntity(cursoEstudianteDTO));
            respuestaGeneralDTO.setMessage("Se adjuntaron correctamente los estudiantes con el curso");
            respuestaGeneralDTO.setData(new ArrayList<>());
            respuestaGeneralDTO.setStatus(HttpStatus.CREATED);
        }catch (Exception e){
            log.error("Error en adjuntar estudiante con el curso ", e);
            respuestaGeneralDTO.setMessage("Error en adjuntar estudiante con el curso");
            respuestaGeneralDTO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return respuestaGeneralDTO;
    }

    private boolean areHeadersValid(Row headerRow) {
        if (headerRow == null) return false;

        for (int i = 0; i < EXPECTED_HEADERS.size(); i++) {
            String cellValue = headerRow.getCell(i).getStringCellValue().trim().toLowerCase();
            if (!cellValue.equals(EXPECTED_HEADERS.get(i))) {
                return false;
            }
        }
        return true;
    }

}
