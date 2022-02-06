package com.github.aurinosalvador.anlixapi.services;

import com.github.aurinosalvador.anlixapi.entities.Paciente;
import com.github.aurinosalvador.anlixapi.respositories.PacienteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/client")
public class PacienteService {
    private static final Logger logger = LoggerFactory.getLogger(PacienteService.class);

    @Autowired
    PacienteRepository pacienteRepository;

    @GetMapping("/")
    ResponseEntity<List<Paciente>> getAll() {
        List<Paciente> pacientes = pacienteRepository
                .findAll(Sort.by(Sort.Direction.ASC, "nome"));

        return ResponseEntity.ok(pacientes);
    }


}
