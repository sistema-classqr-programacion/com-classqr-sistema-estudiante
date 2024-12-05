/**
 * Servicio que implementa la validación de direcciones IP asociadas a estudiantes.
 * Proporciona la lógica para verificar si una dirección IP específica está registrada
 * en el sistema para una fecha dada.
 */
package com.classqr.sistema.estudiante.service.impl;

import com.classqr.sistema.commons.dto.RespuestaGeneralDTO;
import com.classqr.sistema.estudiante.repository.AsistenciaRepository;
import com.classqr.sistema.estudiante.service.IConsultarIpEstudianteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDate;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConsultarIpEstudianteService implements IConsultarIpEstudianteService {

    /**
     * Repositorio para manejar las operaciones relacionadas con la asistencia de estudiantes.
     */
    private final AsistenciaRepository asistenciaRepository;

    // Constantes para mensajes
    private static final String SUCCESS_MESSAGE = "Se validó correctamente la IP.";
    private static final String INVALID_IP_MESSAGE = "Formato de IP inválido.";
    private static final String UNEXPECTED_ERROR_MESSAGE = "Error inesperado al validar la IP.";
    private static final String ERROR_MESSAGE = "Error al validar la IP: ";

    /**
     * Valida si una dirección IP está registrada en el sistema para la fecha actual.
     *
     * @param ip la dirección IP a validar.
     * @return un objeto {@link RespuestaGeneralDTO} que contiene el resultado de la validación:
     *         - {@code true} si la IP existe en el sistema.
     *         - {@code false} si la IP no está registrada.
     *         Incluye también un mensaje y el estado HTTP de la operación.
     */
    @Override
    public RespuestaGeneralDTO consultarIpEstudiante(String ip) {
        final RespuestaGeneralDTO respuestaGeneralDTO = new RespuestaGeneralDTO();

        try {
            // Validar y convertir la IP
            final InetAddress inetAddress = validarIp(ip);

            // Consultar existencia en el repositorio
            final boolean exists = asistenciaRepository.existsByIpEstudianteAndFechaAsistencia(inetAddress, LocalDate.now());

            // Configurar respuesta exitosa
            respuestaGeneralDTO.setData(exists);
            respuestaGeneralDTO.setMessage(SUCCESS_MESSAGE);
            respuestaGeneralDTO.setStatus(HttpStatus.OK);

        } catch (UnknownHostException e) {
            log.error("{} Dirección IP inválida: {}", ERROR_MESSAGE, ip, e);
            respuestaGeneralDTO.setMessage(INVALID_IP_MESSAGE);
            respuestaGeneralDTO.setStatus(HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            log.error("{} Error inesperado al procesar la IP: {}", ERROR_MESSAGE, ip, e);
            respuestaGeneralDTO.setMessage(UNEXPECTED_ERROR_MESSAGE);
            respuestaGeneralDTO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return respuestaGeneralDTO;
    }

    /**
     * Valida y convierte una dirección IP desde su representación como cadena a {@link InetAddress}.
     *
     * @param ip la dirección IP en formato de cadena.
     * @return un objeto {@link InetAddress} representando la IP.
     * @throws UnknownHostException si la dirección IP es inválida o está vacía.
     */
    private InetAddress validarIp(String ip) throws UnknownHostException {
        if (ip == null || ip.trim().isEmpty()) {
            throw new UnknownHostException("La IP proporcionada está vacía o es nula.");
        }
        return InetAddress.getByName(ip);
    }
}
