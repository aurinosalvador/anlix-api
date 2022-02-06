package com.github.aurinosalvador.anlixapi.services;

import com.github.aurinosalvador.anlixapi.entities.Paciente;
import com.github.aurinosalvador.anlixapi.respositories.PacienteRepository;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
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

    @PostMapping("/import")
    ResponseEntity<List<Paciente>> importFromFile(@RequestParam("file") MultipartFile file) {
        try {
            Gson gson = new Gson();

            File localFile = ResourceUtils.getFile("classpath:targetFile.tmp");
            file.transferTo(localFile);

            Paciente[] arrayJson = gson.fromJson(new FileReader(localFile), Paciente[].class);

            List<Paciente> pacientes = Arrays.asList(arrayJson);

            List<Paciente> ret = pacienteRepository.saveAll(pacientes);

//            logger.info("Nome: {}", pacientes.get(0).getNome());

            return ResponseEntity.ok(ret);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.internalServerError().build();
    }


}
