/**
 * Controlador REST para la gestión de estudiantes en cursos.
 * Proporciona endpoints para consultar y cargar estudiantes en un curso,
 * incluyendo la carga mediante un archivo Excel.
 */
package com.classqr.sistema.estudiante.controller;

import com.classqr.sistema.commons.dto.RespuestaGeneralDTO;
import com.classqr.sistema.estudiante.dto.AdjuntarEstudianteCursoDTO;
import com.classqr.sistema.estudiante.service.IConsultaCursoEstudianteService;
import com.classqr.sistema.estudiante.service.ICrearEstudianteCursoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/curso-estudiante")
@RequiredArgsConstructor
public class CursoEstudianteController {

    /**
     * Servicio para consultar los estudiantes asociados a un curso.
     */
    private final IConsultaCursoEstudianteService iConsultaCursoEstudianteService;

    /**
     * Servicio para gestionar la creación de estudiantes en cursos.
     */
    private final ICrearEstudianteCursoService iCrearEstudianteCursoService;

    /**
     * Endpoint para consultar los estudiantes asociados a un curso específico.
     *
     * @param codigoCurso el código del curso cuyos estudiantes se quieren consultar.
     * @return un {@link ResponseEntity} con la información de los estudiantes
     * y el estado de la operación.
     */
    @GetMapping("/estudiante-curso")
    public ResponseEntity<RespuestaGeneralDTO> consultarEstudiantesCurso(
            @RequestParam String codigoCurso
    ) {
        RespuestaGeneralDTO respuestaGeneralDTO = iConsultaCursoEstudianteService.buscarEstudiantesCurso(codigoCurso);
        return ResponseEntity.status(respuestaGeneralDTO.getStatus()).body(respuestaGeneralDTO);
    }

    /**
     * Endpoint para agregar un estudiante a un curso.
     *
     * @param estudiante un objeto {@link AdjuntarEstudianteCursoDTO} con los datos del estudiante a agregar.
     * @return un {@link ResponseEntity} con el estado de la operación y cualquier mensaje adicional.
     */
    @PostMapping("/cargar-estudiante-curso")
    public ResponseEntity<RespuestaGeneralDTO> cargarEstudianteCurso(@RequestBody AdjuntarEstudianteCursoDTO estudiante) {
        RespuestaGeneralDTO respuestaGeneralDTO = iCrearEstudianteCursoService.crearEstudianteCurso(estudiante);
        return ResponseEntity.status(respuestaGeneralDTO.getStatus()).body(respuestaGeneralDTO);
    }

    /**
     * Endpoint para cargar estudiantes en un curso a través de un archivo Excel.
     *
     * @param file       el archivo Excel que contiene los datos de los estudiantes.
     * @param codigoCurso el código del curso al que se asociarán los estudiantes.
     * @return un {@link ResponseEntity} con el estado de la operación y cualquier mensaje adicional.
     * @throws Exception si ocurre un error durante la carga del archivo o el procesamiento de los datos.
     */
    @PostMapping(value = "/cargar-estudiante-curso-excel", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RespuestaGeneralDTO> cargarEstudiantesCursoExcel(
            @RequestParam("file") MultipartFile file,
            @RequestParam("codigoCurso") String codigoCurso
    ) throws Exception {
        RespuestaGeneralDTO respuestaGeneralDTO = iCrearEstudianteCursoService.crearEstudianteCursoExcel(file, codigoCurso);
        return ResponseEntity.status(respuestaGeneralDTO.getStatus()).body(respuestaGeneralDTO);
    }
}
