/**
 * Servicio para la consulta de estudiantes asociados a un curso.
 * Implementa la interfaz {@link IConsultaCursoEstudianteService} y proporciona
 * la lógica para buscar estudiantes en un curso específico, incluyendo información
 * sobre su asistencia.
 */
package com.classqr.sistema.estudiante.service.impl;

import com.classqr.sistema.commons.dto.EstudianteDTO;
import com.classqr.sistema.commons.dto.RespuestaGeneralDTO;
import com.classqr.sistema.estudiante.repository.AsistenciaRepository;
import com.classqr.sistema.estudiante.repository.CursoEstudianteRepository;
import com.classqr.sistema.estudiante.service.IConsultaCursoEstudianteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class ConsultaCursoEstudianteService implements IConsultaCursoEstudianteService {

    /**
     * Repositorio para manejar operaciones relacionadas con estudiantes y cursos.
     */
    private final CursoEstudianteRepository cursoEstudianteRepository;

    /**
     * Repositorio para manejar operaciones relacionadas con la asistencia de estudiantes.
     */
    private final AsistenciaRepository asistenciaRepository;

    /**
     * Mensaje de éxito para la consulta de estudiantes.
     */
    private static final String SUCCESS_MESSAGE = "Se consultaron correctamente los estudiantes";

    /**
     * Mensaje de error para la consulta de estudiantes.
     */
    private static final String ERROR_MESSAGE = "Hubo un error en la consulta";

    /**
     * Busca los estudiantes asociados a un curso específico, incluyendo su estado de asistencia.
     *
     * @param codigoCurso el código único del curso.
     * @return un objeto {@link RespuestaGeneralDTO} que contiene la lista de estudiantes,
     *         su información de asistencia y el estado de la operación.
     */
    @Override
    public RespuestaGeneralDTO buscarEstudiantesCurso(String codigoCurso) {
        RespuestaGeneralDTO respuestaGeneralDTO = new RespuestaGeneralDTO();

        try {
            // Obtener la lista de estudiantes del curso
            List<EstudianteDTO> estudiantes = cursoEstudianteRepository.findAllEstudianteCurso(codigoCurso);

            // Completar información de asistencia para cada estudiante
            List<EstudianteDTO> estudiantesConAsistencia = completarAsistenciaEstudiantes(estudiantes, codigoCurso);

            // Configurar la respuesta exitosa
            respuestaGeneralDTO.setData(estudiantesConAsistencia);
            respuestaGeneralDTO.setMessage(SUCCESS_MESSAGE);
            respuestaGeneralDTO.setStatus(HttpStatus.OK);

        } catch (Exception e) {
            log.error("Error al consultar estudiantes del curso {}: {}", codigoCurso, e.getMessage(), e);
            respuestaGeneralDTO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            respuestaGeneralDTO.setMessage(ERROR_MESSAGE);
        }

        return respuestaGeneralDTO;
    }

    /**
     * Completa la información de asistencia para una lista de estudiantes.
     *
     * @param estudiantes la lista de estudiantes obtenida del repositorio.
     * @return una lista de objetos {@link EstudianteDTO} con la información de asistencia actualizada.
     */
    private List<EstudianteDTO> completarAsistenciaEstudiantes(List<EstudianteDTO> estudiantes, String codigoCurso) {
        return estudiantes.stream()
                .peek(estudiante -> estudiante.setAsistio(
                        asistenciaRepository.existsByCodigoEstudianteFk_CodigoEstudianteAndCodigoCursoFk_CodigoCurso(estudiante.getCodigoEstudiante(),codigoCurso)
                ))
                .collect(Collectors.toList());
    }
}
