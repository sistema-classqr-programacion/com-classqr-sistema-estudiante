/**
 * Servicio para gestionar la asociación de estudiantes a cursos.
 * Proporciona métodos para adjuntar estudiantes a cursos, ya sea de forma individual
 * o mediante la carga de un archivo Excel.
 */
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

@Service
@RequiredArgsConstructor
@Log4j2
public class CrearEstudianteCursoService implements ICrearEstudianteCursoService {

    /**
     * Repositorio para gestionar la asociación entre estudiantes y cursos.
     */
    private final CursoEstudianteRepository cursoEstudianteRepository;

    /**
     * Mapper para convertir entre objetos DTO y entidades relacionadas con la asociación entre estudiantes y cursos.
     */
    private final CursoEstudianteMapper cursoEstudianteMapper;

    /**
     * Mapper para procesar datos de archivos Excel y convertirlos en objetos relacionados con estudiantes y cursos.
     */
    private final CursoEstudianteExcelMapper cursoEstudianteExcelMapper;

    private static final List<String> EXPECTED_HEADERS = List.of("codigo");

    private static final String SUCCESS_MESSAGE = "Se adjuntaron correctamente los estudiantes con el curso";
    private static final String DUPLICATE_MESSAGE = "El estudiante ya está registrado en el curso";
    private static final String INVALID_FILE_MESSAGE = "Por favor, sube un archivo válido.";
    private static final String INVALID_HEADERS_MESSAGE = "Las cabeceras del archivo no son válidas.";
    private static final String ERROR_MESSAGE = "Error en adjuntar estudiante con el curso";

    /**
     * Carga un archivo Excel y asocia a los estudiantes indicados en el archivo con un curso específico.
     *
     * @param file        el archivo Excel que contiene los datos de los estudiantes.
     * @param codigoCurso el código único del curso.
     * @return un objeto {@link RespuestaGeneralDTO} que contiene el resultado de la operación.
     */
    @Override
    @Transactional
    public RespuestaGeneralDTO crearEstudianteCursoExcel(MultipartFile file, String codigoCurso) {
        RespuestaGeneralDTO respuestaGeneralDTO = new RespuestaGeneralDTO();

        if (file.isEmpty()) {
            return crearRespuesta(HttpStatus.BAD_REQUEST, INVALID_FILE_MESSAGE, new ArrayList<>());
        }

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            // Validar cabeceras del archivo
            if (!areHeadersValid(sheet.getRow(0))) {
                return crearRespuesta(HttpStatus.BAD_REQUEST, INVALID_HEADERS_MESSAGE, new ArrayList<>());
            }

            // Procesar estudiantes desde el archivo
            List<CursoEstudianteDTO> cursosEstudiante = cursoEstudianteExcelMapper.rowToListCursoEstudianteDTO(sheet, codigoCurso);
            procesarEstudiantes(cursosEstudiante);

            return crearRespuesta(HttpStatus.CREATED, SUCCESS_MESSAGE, new ArrayList<>());
        } catch (Exception e) {
            log.error("{}: {}", ERROR_MESSAGE, e.getMessage(), e);
            return crearRespuesta(HttpStatus.INTERNAL_SERVER_ERROR, ERROR_MESSAGE, new ArrayList<>());
        }
    }

    /**
     * Asocia un estudiante a un curso específico de forma individual.
     *
     * @param estudiante el objeto DTO con los datos del estudiante y del curso.
     * @return un objeto {@link RespuestaGeneralDTO} que contiene el resultado de la operación.
     */
    @Override
    @Transactional
    public RespuestaGeneralDTO crearEstudianteCurso(AdjuntarEstudianteCursoDTO estudiante) {
        try {
            if (cursoEstudianteRepository.existsByCodigoEstudianteEntityFk_CodigoEstudianteAndCodigoCursoEntityFk_CodigoCurso(
                    estudiante.getCodigoEstudiante(), estudiante.getCodigoCurso())) {
                return crearRespuesta(HttpStatus.BAD_REQUEST, DUPLICATE_MESSAGE, new ArrayList<>());
            }

            // Mapear y guardar estudiante
            CursoEstudianteDTO cursoEstudianteDTO = mapearEstudianteCurso(estudiante);
            cursoEstudianteRepository.save(cursoEstudianteMapper.dtoToEntity(cursoEstudianteDTO));

            return crearRespuesta(HttpStatus.CREATED, SUCCESS_MESSAGE, new ArrayList<>());
        } catch (Exception e) {
            log.error("{}: {}", ERROR_MESSAGE, e.getMessage(), e);
            return crearRespuesta(HttpStatus.INTERNAL_SERVER_ERROR, ERROR_MESSAGE, new ArrayList<>());
        }
    }

    /**
     * Valida si las cabeceras del archivo son correctas.
     *
     * @param headerRow la fila que contiene las cabeceras.
     * @return true si las cabeceras son válidas, false en caso contrario.
     */
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

    /**
     * Procesa y guarda una lista de estudiantes asociados a un curso.
     *
     * @param cursosEstudiante lista de estudiantes a procesar.
     */
    private void procesarEstudiantes(List<CursoEstudianteDTO> cursosEstudiante) {
        for (CursoEstudianteDTO cursoEstudianteDTO : cursosEstudiante) {
            boolean exists = cursoEstudianteRepository.existsByCodigoEstudianteEntityFk_CodigoEstudianteAndCodigoCursoEntityFk_CodigoCurso(
                    cursoEstudianteDTO.getCodigoEstudianteEntityFk().getCodigoEstudiante(),
                    cursoEstudianteDTO.getCodigoCursoEntityFk().getCodigoCurso());

            if (!exists) {
                cursoEstudianteRepository.save(cursoEstudianteMapper.dtoToEntity(cursoEstudianteDTO));
            }
        }
    }

    /**
     * Mapea los datos de un estudiante y curso a un objeto {@link CursoEstudianteDTO}.
     *
     * @param estudiante objeto DTO con los datos del estudiante y curso.
     * @return un objeto {@link CursoEstudianteDTO}.
     */
    private CursoEstudianteDTO mapearEstudianteCurso(AdjuntarEstudianteCursoDTO estudiante) {
        CursoEstudianteIdDTO cursoEstudianteIdDTO = new CursoEstudianteIdDTO();
        cursoEstudianteIdDTO.setCodigoEstudianteFk(estudiante.getCodigoEstudiante());
        cursoEstudianteIdDTO.setCodigoCursoFk(estudiante.getCodigoCurso());
        CursoDTO cursoDTO = new CursoDTO();
        cursoDTO.setCodigoCurso(estudiante.getCodigoCurso());
        EstudianteDTO estudianteDTO = new EstudianteDTO();
        estudianteDTO.setCodigoEstudiante(estudiante.getCodigoEstudiante());
        CursoEstudianteDTO cursoEstudianteDTO = new CursoEstudianteDTO();
        cursoEstudianteDTO.setId(cursoEstudianteIdDTO);
        cursoEstudianteDTO.setCodigoCursoEntityFk(cursoDTO);
        cursoEstudianteDTO.setCodigoEstudianteEntityFk(estudianteDTO);
        return cursoEstudianteDTO;
    }

    /**
     * Crea un objeto {@link RespuestaGeneralDTO}.
     *
     * @param status  el estado HTTP de la respuesta.
     * @param message el mensaje de la respuesta.
     * @param data    los datos adicionales de la respuesta.
     * @return un objeto {@link RespuestaGeneralDTO}.
     */
    private RespuestaGeneralDTO crearRespuesta(HttpStatus status, String message, Object data) {
        RespuestaGeneralDTO respuesta = new RespuestaGeneralDTO();
        respuesta.setStatus(status);
        respuesta.setMessage(message);
        respuesta.setData(data);
        return respuesta;
    }
}
