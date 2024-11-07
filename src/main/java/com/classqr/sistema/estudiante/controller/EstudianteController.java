package com.classqr.sistema.estudiante.controller;

import com.classqr.sistema.commons.dto.EstudianteDTO;
import com.classqr.sistema.commons.dto.RespuestaGeneralDTO;
import com.classqr.sistema.estudiante.dto.LoginEstudianteDTO;
import com.classqr.sistema.estudiante.service.ICrearEstudianteService;
import com.classqr.sistema.estudiante.service.IGenerarCodigoEstudianteService;
import com.classqr.sistema.estudiante.service.IQueryEstudianteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/estudiante")
@RequiredArgsConstructor
public class EstudianteController {

    private final IQueryEstudianteService iQueryEstudianteService;

    private final ICrearEstudianteService iCrearEstudianteService;

    private final IGenerarCodigoEstudianteService iGenerarCodigoEstudianteService;

    @PostMapping("/login")
    public ResponseEntity<RespuestaGeneralDTO> loginEstudiante(@RequestBody LoginEstudianteDTO loginEstudianteDTO){
        RespuestaGeneralDTO respuestaGeneralDTO = iQueryEstudianteService.loginEstudiante(loginEstudianteDTO);
        return ResponseEntity.status(respuestaGeneralDTO.getStatus()).body(respuestaGeneralDTO);
    }

    @PostMapping("/crear")
    public ResponseEntity<RespuestaGeneralDTO> crearEstudiante(@RequestBody List<EstudianteDTO> estudiantes){
        RespuestaGeneralDTO respuestaGeneralDTO = iCrearEstudianteService.crearEstudiante(estudiantes);
        return ResponseEntity.status(respuestaGeneralDTO.getStatus()).body(respuestaGeneralDTO);
    }

    @GetMapping("/generar-codigo")
    public ResponseEntity<RespuestaGeneralDTO> generarCodigoEstudiante(){
        RespuestaGeneralDTO respuestaGeneralDTO = iGenerarCodigoEstudianteService.generarCodigoEstudiante();
        return ResponseEntity.status(respuestaGeneralDTO.getStatus()).body(respuestaGeneralDTO);
    }

}
