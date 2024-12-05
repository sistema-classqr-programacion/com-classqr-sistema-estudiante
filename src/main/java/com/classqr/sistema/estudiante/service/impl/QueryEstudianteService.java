/**
 * Servicio para la autenticación y manejo de inicio de sesión de estudiantes.
 * Este servicio realiza la validación de credenciales, busca los datos del estudiante en la base de datos
 * y genera un token JWT para permitir el acceso a recursos protegidos.
 */
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

    /**
     * Mapper para convertir entre entidades y DTOs de estudiantes.
     */
    private final EstudianteMapper estudianteMapper;

    /**
     * Repositorio para realizar operaciones de consulta relacionadas con estudiantes.
     */
    private final EstudianteReplicaRepository estudianteReplicaRepository;

    /**
     * Manejador de autenticación de Spring Security.
     */
    private final AuthenticationManager authenticationManager;

    /**
     * Servicio para la generación y manejo de tokens JWT.
     */
    private final JwtService jwtService;

    private static final String LOGIN_SUCCESS_MESSAGE = "Inicio de sesión exitoso.";
    private static final String LOGIN_ERROR_MESSAGE = "Hubo un error en el inicio de sesión del estudiante.";
    private static final String AUTHENTICATION_ERROR_MESSAGE = "Error de autenticación: credenciales inválidas.";
    private static final String STUDENT_NOT_FOUND_MESSAGE = "El estudiante no fue encontrado en la base de datos.";

    /**
     * Realiza el proceso de inicio de sesión de un estudiante, autenticando sus credenciales
     * y generando un token JWT en caso de éxito.
     *
     * @param loginEstudianteDTO objeto que contiene las credenciales del estudiante.
     * @return un objeto {@link RespuestaGeneralDTO} con el token JWT o información del error.
     */
    @Override
    public RespuestaGeneralDTO loginEstudiante(LoginEstudianteDTO loginEstudianteDTO) {
        RespuestaGeneralDTO respuestaGeneralDto = new RespuestaGeneralDTO();
        try {
            // Autenticar credenciales
            autenticarEstudiante(loginEstudianteDTO);

            // Obtener datos del estudiante
            EstudianteEntity estudiante = obtenerEstudiantePorCodigo(loginEstudianteDTO.getCodigoEstudiante());

            // Generar token JWT
            String token = generarToken(estudiante);

            // Configurar respuesta exitosa
            respuestaGeneralDto.setData(AuthResponseDTO.builder().token(token).build());
            respuestaGeneralDto.setMessage(LOGIN_SUCCESS_MESSAGE);
            respuestaGeneralDto.setStatus(HttpStatus.OK);

        } catch (IllegalArgumentException e) {
            log.error("{}: {}", AUTHENTICATION_ERROR_MESSAGE, e.getMessage());
            respuestaGeneralDto.setMessage(AUTHENTICATION_ERROR_MESSAGE);
            respuestaGeneralDto.setStatus(HttpStatus.UNAUTHORIZED);
        } catch (IllegalStateException e) {
            log.error("{}: {}", STUDENT_NOT_FOUND_MESSAGE, e.getMessage());
            respuestaGeneralDto.setMessage(STUDENT_NOT_FOUND_MESSAGE);
            respuestaGeneralDto.setStatus(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("{}: {}", LOGIN_ERROR_MESSAGE, e.getMessage(), e);
            respuestaGeneralDto.setMessage(LOGIN_ERROR_MESSAGE);
            respuestaGeneralDto.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return respuestaGeneralDto;
    }

    /**
     * Autentica las credenciales del estudiante utilizando el {@link AuthenticationManager}.
     *
     * @param loginEstudianteDTO objeto que contiene las credenciales del estudiante.
     * @throws IllegalArgumentException si las credenciales son inválidas.
     */
    private void autenticarEstudiante(LoginEstudianteDTO loginEstudianteDTO) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginEstudianteDTO.getCodigoEstudiante(),
                            loginEstudianteDTO.getNumeroDocumento()
                    )
            );
        } catch (Exception e) {
            throw new IllegalArgumentException(AUTHENTICATION_ERROR_MESSAGE, e);
        }
    }

    /**
     * Obtiene los datos de un estudiante por su código.
     *
     * @param codigoEstudiante el código único del estudiante.
     * @return la entidad {@link EstudianteEntity} del estudiante.
     * @throws IllegalStateException si no se encuentra el estudiante en la base de datos.
     */
    private EstudianteEntity obtenerEstudiantePorCodigo(String codigoEstudiante) {
        return estudianteReplicaRepository.findByCodigoEstudiante(codigoEstudiante)
                .orElseThrow(() -> new IllegalStateException(STUDENT_NOT_FOUND_MESSAGE));
    }

    /**
     * Genera un token JWT para el estudiante autenticado.
     *
     * @param estudiante la entidad {@link EstudianteEntity} del estudiante.
     * @return el token JWT generado.
     */
    private String generarToken(EstudianteEntity estudiante) {
        UserDetails user = new EstudianteSeguridadDTO(estudianteMapper.entityToDto(estudiante));
        return jwtService.getToken(user);
    }
}
