package com.classqr.sistema.estudiante.controller;

import com.classqr.sistema.commons.dto.RespuestaGeneralDTO;
import com.classqr.sistema.estudiante.dto.AdjuntarEstudianteCursoDTO;
import com.classqr.sistema.estudiante.dto.LoginEstudianteDTO;
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

    private final IConsultaCursoEstudianteService iConsultaCursoEstudianteService;

    private final ICrearEstudianteCursoService iCrearEstudianteCursoService;

    @GetMapping("/estudiante-curso")
    public ResponseEntity<RespuestaGeneralDTO> consultarEstudiantesCurso(
            @RequestParam String codigoCurso
    ){
        RespuestaGeneralDTO respuestaGeneralDTO = iConsultaCursoEstudianteService.buscarEstudiantesCurso(codigoCurso);
        return ResponseEntity.status(respuestaGeneralDTO.getStatus()).body(respuestaGeneralDTO);
    }

    @PostMapping("/cargar-estudiante-curso")
    public ResponseEntity<RespuestaGeneralDTO> cargarEstudianteCurso(AdjuntarEstudianteCursoDTO estudiante){
        RespuestaGeneralDTO respuestaGeneralDTO = iCrearEstudianteCursoService.crearEstudianteCurso(estudiante);
        return ResponseEntity.status(respuestaGeneralDTO.getStatus()).body(respuestaGeneralDTO);
    }

    @PostMapping(value = "/cargar-estudiante-curso-excel", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RespuestaGeneralDTO> cargarEstudiantesCursoExcel(
            @RequestParam("file") MultipartFile file,
            @RequestParam("codigoCurso") String  codigoCurso
    ) throws Exception {
        RespuestaGeneralDTO respuestaGeneralDTO = iCrearEstudianteCursoService.crearEstudianteCursoExcel(file, codigoCurso);
        return ResponseEntity.status(respuestaGeneralDTO.getStatus()).body(respuestaGeneralDTO);
    }
}
