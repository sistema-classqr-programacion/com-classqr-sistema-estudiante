/**
 * Controlador REST para la gestión de estudiantes.
 * Proporciona endpoints para funcionalidades relacionadas con estudiantes,
 * como el inicio de sesión, la creación de estudiantes desde un archivo Excel,
 * y la validación de IP de un estudiante.
 */
package com.classqr.sistema.estudiante.controller;

import com.classqr.sistema.commons.dto.RespuestaGeneralDTO;
import com.classqr.sistema.estudiante.dto.LoginEstudianteDTO;
import com.classqr.sistema.estudiante.service.IConsultarIpEstudianteService;
import com.classqr.sistema.estudiante.service.ICrearEstudianteExcelService;
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

    /**
     * Servicio para manejar consultas relacionadas con estudiantes.
     */
    private final IQueryEstudianteService iQueryEstudianteService;

    /**
     * Servicio para crear estudiantes desde un archivo Excel.
     */
    private final ICrearEstudianteExcelService iCrearEstudianteExcelService;

    /**
     * Servicio para consultar y validar la IP de un estudiante.
     */
    private final IConsultarIpEstudianteService iConsultarIpEstudianteService;

    /**
     * Endpoint para realizar el inicio de sesión de un estudiante.
     *
     * @param loginEstudianteDTO el objeto {@link LoginEstudianteDTO} que contiene
     *                           las credenciales del estudiante.
     * @return un {@link ResponseEntity} con el estado de la operación y los datos
     *         del estudiante, si el inicio de sesión es exitoso.
     */
    @PostMapping("/login")
    public ResponseEntity<RespuestaGeneralDTO> loginEstudiante(@RequestBody LoginEstudianteDTO loginEstudianteDTO) {
        RespuestaGeneralDTO respuestaGeneralDTO = iQueryEstudianteService.loginEstudiante(loginEstudianteDTO);
        return ResponseEntity.status(respuestaGeneralDTO.getStatus()).body(respuestaGeneralDTO);
    }

    /**
     * Endpoint para crear múltiples estudiantes desde un archivo Excel.
     *
     * @param estudiantes el archivo Excel que contiene la información de los estudiantes.
     * @return un {@link ResponseEntity} con el estado de la operación y cualquier mensaje adicional.
     */
    @PostMapping(value = "/crear-estudiantes", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RespuestaGeneralDTO> crearEstudiante(@RequestParam("estudiantes") MultipartFile estudiantes) {
        RespuestaGeneralDTO respuestaGeneralDTO = iCrearEstudianteExcelService.crearEstudiantesExcel(estudiantes);
        return ResponseEntity.status(respuestaGeneralDTO.getStatus()).body(respuestaGeneralDTO);
    }

    /**
     * Endpoint para validar la IP de un estudiante.
     *
     * @param ipEstudiante la IP del estudiante que se desea validar.
     * @return un {@link ResponseEntity} con el resultado de la validación y el estado de la operación.
     */
    @GetMapping("/validar-ip")
    public ResponseEntity<RespuestaGeneralDTO> validarIpEstudiante(@RequestParam("ip") String ipEstudiante) {
        RespuestaGeneralDTO respuestaGeneralDTO = iConsultarIpEstudianteService.consultarIpEstudiante(ipEstudiante);
        return ResponseEntity.status(respuestaGeneralDTO.getStatus()).body(respuestaGeneralDTO);
    }
}
