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
            List<EstudianteDTO> estudiantes = estudianteExcelMapper.rowToListEstudianteDTO(sheet);

            // Crear un ExecutorService con 3 hilos
            ExecutorService executorService = Executors.newFixedThreadPool(3);

            // Dividir la lista de estudiantes en 3 sublistas
            int batchSize = (int) Math.ceil(estudiantes.size() / 3.0);
            List<List<EstudianteDTO>> subLists = List.of(
                    estudiantes.subList(0, Math.min(batchSize, estudiantes.size())),
                    estudiantes.subList(Math.min(batchSize, estudiantes.size()), Math.min(2 * batchSize, estudiantes.size())),
                    estudiantes.subList(Math.min(2 * batchSize, estudiantes.size()), estudiantes.size())
            );

            // Procesar cada sublista en paralelo
            for (List<EstudianteDTO> subList : subLists) {
                executorService.submit(() -> processEstudiantes(subList));
            }

            // Apagar el ExecutorService
            executorService.shutdown();

            respuestaGeneralDTO.setMessage("Se creo correctamente el usuario");
            respuestaGeneralDTO.setData(new ArrayList<>());
            respuestaGeneralDTO.setStatus(HttpStatus.CREATED);
        }catch (Exception e){
            log.error("Error en crear el estudiante ", e);
            respuestaGeneralDTO.setMessage("Error en crear el estudiante");
            respuestaGeneralDTO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return respuestaGeneralDTO;
    }

    private void processEstudiantes(List<EstudianteDTO> estudiantes) {
        for (EstudianteDTO estudianteDTO : estudiantes) {
            try {
                String codigo = Utilidades.generarCodigo(CodigoUsuarioEnum.ESTUDIANTE);
                while (estudianteReplicaRepository.existsByCodigoEstudiante(codigo)) {
                    codigo = Utilidades.generarCodigo(CodigoUsuarioEnum.ESTUDIANTE);
                }
                estudianteDTO.setCodigoEstudiante(codigo);
                estudianteDTO.setNumeroDocumento(passwordEncoder.encode(estudianteDTO.getNumeroDocumento()));
                estudianteReplicaRepository.save(estudianteMapper.dtoToEntity(estudianteDTO));
            } catch (Exception e) {
                log.error("Error al procesar estudiante: {}", estudianteDTO, e);
            }
        }
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
