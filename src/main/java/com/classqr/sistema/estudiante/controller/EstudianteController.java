package com.classqr.sistema.estudiante.controller;

import com.classqr.sistema.commons.dto.RespuestaGeneralDTO;
import com.classqr.sistema.estudiante.dto.LoginEstudianteDTO;
import com.classqr.sistema.estudiante.service.IQueryEstudianteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/estudiante")
@RequiredArgsConstructor
public class EstudianteController {

    private final IQueryEstudianteService iQueryEstudianteService;

    @PostMapping("/login")
    public ResponseEntity<RespuestaGeneralDTO> loginEstudiante(@RequestBody LoginEstudianteDTO loginEstudianteDTO){
        RespuestaGeneralDTO respuestaGeneralDTO = iQueryEstudianteService.loginEstudiante(loginEstudianteDTO);
        return ResponseEntity.status(respuestaGeneralDTO.getStatus()).body(respuestaGeneralDTO);
    }

    @PostMapping("/crear")
    public ResponseEntity<RespuestaGeneralDTO> crearEstudiante(@RequestBody LoginEstudianteDTO loginEstudianteDTO){
        RespuestaGeneralDTO respuestaGeneralDTO = iQueryEstudianteService.loginEstudiante(loginEstudianteDTO);
        return ResponseEntity.status(respuestaGeneralDTO.getStatus()).body(respuestaGeneralDTO);
    }
}
