package com.classqr.sistema.estudiante.controller;

import com.classqr.sistema.commons.dto.RespuestaGeneralDTO;
import com.classqr.sistema.estudiante.dto.LoginEstudianteDTO;
import com.classqr.sistema.estudiante.service.IConsultaCursoEstudianteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/curso-estudiante")
@RequiredArgsConstructor
public class CursoEstudianteController {

    private final IConsultaCursoEstudianteService iConsultaCursoEstudianteService;

    @GetMapping("/all")
    public ResponseEntity<RespuestaGeneralDTO> consultaEstudiantesCurso(@RequestParam String codigoCurso){
        RespuestaGeneralDTO respuestaGeneralDTO = iConsultaCursoEstudianteService.buscarEstudiantesCurso(codigoCurso);
        return ResponseEntity.status(respuestaGeneralDTO.getStatus()).body(respuestaGeneralDTO);
    }

}
