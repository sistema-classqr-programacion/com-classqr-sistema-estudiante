package com.classqr.sistema.estudiante.service.impl;

import com.classqr.sistema.commons.dto.EstudianteDTO;
import com.classqr.sistema.commons.dto.RespuestaGeneralDTO;
import com.classqr.sistema.commons.util.mapper.EstudianteMapper;
import com.classqr.sistema.estudiante.repository.EstudianteReplicaRepository;
import com.classqr.sistema.estudiante.service.ICrearEstudianteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class CrearEstudianteService implements ICrearEstudianteService {

    private final EstudianteReplicaRepository estudianteReplicaRepository;
    private final EstudianteMapper estudianteMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public RespuestaGeneralDTO crearEstudiante(List<EstudianteDTO> estudiantes) {
        RespuestaGeneralDTO respuestaGeneralDTO = new RespuestaGeneralDTO();
        try{
            for(EstudianteDTO estudianteDTO : estudiantes){
                estudianteDTO.setNumeroDocumento(passwordEncoder.encode(estudianteDTO.getNumeroDocumento()));
                estudianteReplicaRepository.save(estudianteMapper.dtoToEntity(estudianteDTO));
            }
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
}
