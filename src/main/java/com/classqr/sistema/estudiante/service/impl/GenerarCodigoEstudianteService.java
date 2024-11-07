package com.classqr.sistema.estudiante.service.impl;

import com.classqr.sistema.commons.dto.RespuestaGeneralDTO;
import com.classqr.sistema.commons.util.enums.CodigoUsuarioEnum;
import com.classqr.sistema.commons.util.helper.Utilidades;
import com.classqr.sistema.estudiante.repository.EstudianteReplicaRepository;
import com.classqr.sistema.estudiante.service.IGenerarCodigoEstudianteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class GenerarCodigoEstudianteService implements IGenerarCodigoEstudianteService {

    private final EstudianteReplicaRepository estudianteReplicaRepository;

    @Override
    public RespuestaGeneralDTO generarCodigoEstudiante() {
        RespuestaGeneralDTO respuestaGeneralDTO = new RespuestaGeneralDTO();
        try {
            while (true){
                String codigo = Utilidades.generarCodigo(CodigoUsuarioEnum.ESTUDIANTE);
                if(!estudianteReplicaRepository.existsByCodigoEstudiante(codigo)){
                    respuestaGeneralDTO.setStatus(HttpStatus.OK);
                    respuestaGeneralDTO.setMessage("Se creo el codigo");
                    respuestaGeneralDTO.setData(codigo);
                    break;
                }
            }
        }catch (Exception e) {
            log.error("Error en crear el codigo ", e);
            respuestaGeneralDTO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            respuestaGeneralDTO.setMessage("Error en crear el codigo");
        }
        return respuestaGeneralDTO;
    }
}
