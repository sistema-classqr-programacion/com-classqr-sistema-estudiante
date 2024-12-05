package com.classqr.sistema.estudiante.service.impl;

import com.classqr.sistema.commons.dto.EstudianteDTO;
import com.classqr.sistema.commons.dto.RespuestaGeneralDTO;
import com.classqr.sistema.commons.util.enums.CodigoUsuarioEnum;
import com.classqr.sistema.commons.util.helper.Utilidades;
import com.classqr.sistema.commons.util.mapper.EstudianteMapper;
import com.classqr.sistema.estudiante.repository.EstudianteReplicaRepository;
import com.classqr.sistema.estudiante.service.ICrearEstudianteExcelService;
import com.classqr.sistema.estudiante.util.mapper.EstudianteExcelMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
@Log4j2
public class CrearEstudianteExcelService implements ICrearEstudianteExcelService {

    private final EstudianteReplicaRepository estudianteReplicaRepository;
    private final EstudianteMapper estudianteMapper;
    private final PasswordEncoder passwordEncoder;
    private final EstudianteExcelMapper estudianteExcelMapper;

    private static final List<String> EXPECTED_HEADERS = List.of(
            "nombre", "apellido", "numero_documento", "tipo_documento"
    );

    @Override
    @Transactional
    public RespuestaGeneralDTO crearEstudiantesExcel(MultipartFile file) {
        RespuestaGeneralDTO respuestaGeneralDTO = new RespuestaGeneralDTO();

        if (file.isEmpty()) {
            return crearRespuesta(HttpStatus.BAD_REQUEST, "Por favor, sube un archivo válido.", new ArrayList<>());
        }

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            // Validar cabeceras
            if (!areHeadersValid(sheet.getRow(0))) {
                return crearRespuesta(HttpStatus.BAD_REQUEST, "Las cabeceras del archivo no son válidas.", new ArrayList<>());
            }

            // Procesar filas del archivo
            List<EstudianteDTO> estudiantes = estudianteExcelMapper.rowToListEstudianteDTO(sheet);

            // Procesar estudiantes en paralelo
            procesarEnParalelo(estudiantes);

            return crearRespuesta(HttpStatus.CREATED, "Se creó correctamente el usuario.", new ArrayList<>());

        } catch (Exception e) {
            log.error("Error en crear estudiantes: {}", e.getMessage(), e);
            return crearRespuesta(HttpStatus.INTERNAL_SERVER_ERROR, "Error en crear estudiantes.", new ArrayList<>());
        }
    }

    /**
     * Procesa la lista de estudiantes en paralelo, dividiendo la carga en sublistas.
     *
     * @param estudiantes lista de estudiantes a procesar.
     */
    private void procesarEnParalelo(List<EstudianteDTO> estudiantes) {
        int numThreads = Math.min(3, estudiantes.size());
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

        // Dividir la lista en sublistas
        int batchSize = (int) Math.ceil(estudiantes.size() / (double) numThreads);
        for (int i = 0; i < estudiantes.size(); i += batchSize) {
            List<EstudianteDTO> subList = estudiantes.subList(i, Math.min(i + batchSize, estudiantes.size()));
            executorService.submit(() -> processEstudiantes(subList));
        }

        // Apagar el ExecutorService
        executorService.shutdown();
    }

    /**
     * Procesa y guarda una lista de estudiantes en la base de datos.
     *
     * @param estudiantes sublista de estudiantes a procesar.
     */
    private void processEstudiantes(List<EstudianteDTO> estudiantes) {
        for (EstudianteDTO estudianteDTO : estudiantes) {
            try {
                // Generar código único para el estudiante
                String codigo = generarCodigoUnico();

                // Configurar datos del estudiante
                estudianteDTO.setCodigoEstudiante(codigo);
                log.info(estudianteDTO.getNumeroDocumento().trim());
                estudianteDTO.setNumeroDocumento(passwordEncoder.encode(estudianteDTO.getNumeroDocumento().trim()));

                // Guardar en la base de datos
                estudianteReplicaRepository.save(estudianteMapper.dtoToEntity(estudianteDTO));
            } catch (Exception e) {
                log.error("Error al procesar estudiante: {}", estudianteDTO, e);
            }
        }
    }

    /**
     * Genera un código único para un estudiante.
     *
     * @return el código único generado.
     */
    private String generarCodigoUnico() {
        String codigo;
        do {
            codigo = Utilidades.generarCodigo(CodigoUsuarioEnum.ESTUDIANTE);
        } while (estudianteReplicaRepository.existsByCodigoEstudiante(codigo));
        return codigo;
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
