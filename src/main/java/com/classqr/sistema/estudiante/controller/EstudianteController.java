package com.classqr.sistema.estudiante.controller;

import com.classqr.sistema.commons.dto.RespuestaGeneralDTO;
import com.classqr.sistema.estudiante.dto.LoginEstudianteDTO;
import com.classqr.sistema.estudiante.service.ICrearEstudianteExcelService;
import com.classqr.sistema.estudiante.service.IGenerarCodigoEstudianteService;
import com.classqr.sistema.estudiante.service.IQueryEstudianteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/estudiante")
@RequiredArgsConstructor
public class EstudianteController {

    private final IQueryEstudianteService iQueryEstudianteService;

    private final ICrearEstudianteExcelService iCrearEstudianteExcelService;

    private final IGenerarCodigoEstudianteService iGenerarCodigoEstudianteService;

    @PostMapping("/login")
    public ResponseEntity<RespuestaGeneralDTO> loginEstudiante(@RequestBody LoginEstudianteDTO loginEstudianteDTO){
        RespuestaGeneralDTO respuestaGeneralDTO = iQueryEstudianteService.loginEstudiante(loginEstudianteDTO);
        return ResponseEntity.status(respuestaGeneralDTO.getStatus()).body(respuestaGeneralDTO);
    }

    @PostMapping(value = "/crear-estudiantes", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RespuestaGeneralDTO> crearEstudiante(@RequestParam("estudiantes") MultipartFile estudiantes
    ){
        RespuestaGeneralDTO respuestaGeneralDTO = iCrearEstudianteExcelService.crearEstudiantesExcel(estudiantes);
        return ResponseEntity.status(respuestaGeneralDTO.getStatus()).body(respuestaGeneralDTO);
    }
}
