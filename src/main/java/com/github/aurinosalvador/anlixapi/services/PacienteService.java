package com.github.aurinosalvador.anlixapi.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.aurinosalvador.anlixapi.controllers.FileStorageController;
import com.github.aurinosalvador.anlixapi.entities.Paciente;
import com.github.aurinosalvador.anlixapi.respositories.PacienteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/paciente")
public class PacienteService {
    private static final Logger logger = LoggerFactory.getLogger(PacienteService.class);

    @Autowired
    PacienteRepository pacienteRepository;

    @Autowired
    FileStorageController storageController;

    @GetMapping("/")
    ResponseEntity<List<Paciente>> getAll() {
        List<Paciente> pacientes = pacienteRepository
                .findAll(Sort.by(Sort.Direction.ASC, "nome"));

        return ResponseEntity.ok(pacientes);
    }

    @PostMapping("/import")
    ResponseEntity<String> importFromFile(@RequestParam("file") MultipartFile file) {
        try {
            storageController.init();
            storageController.save(file);
            Resource localFile = storageController.load(file.getOriginalFilename());

            ObjectMapper mapper = new ObjectMapper();

            Paciente[] arrayJson = mapper.readValue(localFile.getFile(), Paciente[].class);

            List<Paciente> pacientes = Arrays.asList(arrayJson);

            pacienteRepository.saveAll(pacientes);

//            logger.info("Nome: {}", pacientes.get(0).getNome());

            storageController.deleteAll();

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body("Uploaded the file successfully: " + file.getOriginalFilename());

        } catch (IOException e) {
            logger.error(e.getMessage());
            storageController.deleteAll();
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body("Could not upload the file: " + file.getOriginalFilename() + "!");
        }
    }

    @GetMapping("/filter/{nome}")
    ResponseEntity<List<Paciente>> getByNameContaining(@PathVariable String nome) {

        List<Paciente> pacientes = pacienteRepository.findByNomeContaining(nome);

        return pacientes.size() > 0
                ? ResponseEntity.ok(pacientes)
                : ResponseEntity.notFound().build();
    }


}
