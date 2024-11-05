package com.classqr.sistema.estudiante.service.impl;

import com.classqr.sistema.commons.dto.AuthResponseDTO;
import com.classqr.sistema.commons.dto.EstudianteSeguridadDTO;
import com.classqr.sistema.commons.dto.RespuestaGeneralDTO;
import com.classqr.sistema.commons.entity.EstudianteEntity;
import com.classqr.sistema.commons.service.impl.JwtService;
import com.classqr.sistema.commons.util.mapper.EstudianteMapper;
import com.classqr.sistema.estudiante.dto.LoginEstudianteDTO;
import com.classqr.sistema.estudiante.repository.EstudianteReplicaRepository;
import com.classqr.sistema.estudiante.service.IQueryEstudianteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class QueryEstudianteService implements IQueryEstudianteService {

    private final EstudianteMapper estudianteMapper;

    private final EstudianteReplicaRepository estudianteReplicaRepository;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    @Override
    public RespuestaGeneralDTO loginEstudiante(LoginEstudianteDTO loginEstudianteDTO) {
        RespuestaGeneralDTO respuestaGeneralDto = new RespuestaGeneralDTO();
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginEstudianteDTO.getCodigoEstudiante(), loginEstudianteDTO.getNumeroDocumento()));
            EstudianteEntity estudiante = estudianteReplicaRepository.findByCodigoEstudiante(loginEstudianteDTO.getCodigoEstudiante()).orElseThrow();
            UserDetails user = new EstudianteSeguridadDTO(estudianteMapper.entityToDto(estudiante));
            String token = jwtService.getToken(user);
            respuestaGeneralDto.setData(AuthResponseDTO.builder().token(token).build());
            respuestaGeneralDto.setMessage("Se inicio correctamente");
            respuestaGeneralDto.setStatus(HttpStatus.OK);
        }catch (Exception e){
            log.error("Error ", e);
            respuestaGeneralDto.setMessage("Hubo un error en el login del estudiante");
            respuestaGeneralDto.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return respuestaGeneralDto;
    }
}
