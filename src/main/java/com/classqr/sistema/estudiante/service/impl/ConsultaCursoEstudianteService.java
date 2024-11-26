package com.classqr.sistema.estudiante.service.impl;

import com.classqr.sistema.commons.dto.EstudianteDTO;
import com.classqr.sistema.commons.dto.RespuestaGeneralDTO;
import com.classqr.sistema.commons.util.mapper.EstudianteMapper;
import com.classqr.sistema.estudiante.repository.AsistenciaRepository;
import com.classqr.sistema.estudiante.repository.CursoEstudianteRepository;
import com.classqr.sistema.estudiante.service.IConsultaCursoEstudianteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class ConsultaCursoEstudianteService implements IConsultaCursoEstudianteService {

    private final CursoEstudianteRepository cursoEstudianteRepository;

    private final AsistenciaRepository asistenciaRepository;

    @Override
    public RespuestaGeneralDTO buscarEstudiantesCurso(String codigoCurso) {
        RespuestaGeneralDTO respuestaGeneralDTO = new RespuestaGeneralDTO();
        try{
            List<EstudianteDTO> estudiantes = cursoEstudianteRepository.findAllEstudianteCurso(codigoCurso);
            for(EstudianteDTO estudiante : estudiantes){
                estudiante.setAsistio(asistenciaRepository.existsByCodigoEstudianteFk_CodigoEstudiante(estudiante.getCodigoEstudiante()));
            }
            respuestaGeneralDTO.setData(estudiantes);
            respuestaGeneralDTO.setMessage("Se consultaron correctamente los estudiantes");
            respuestaGeneralDTO.setStatus(HttpStatus.OK);
        }catch (Exception e){
            log.error("Error ", e);
            respuestaGeneralDTO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            respuestaGeneralDTO.setMessage("Hubo un error en la consulta");
        }

        return respuestaGeneralDTO;
    }
}
