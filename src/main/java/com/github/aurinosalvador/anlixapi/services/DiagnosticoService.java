package com.github.aurinosalvador.anlixapi.services;

import com.github.aurinosalvador.anlixapi.DTO.DiagnosticoDTO;
import com.github.aurinosalvador.anlixapi.entities.Diagnostico;
import com.github.aurinosalvador.anlixapi.entities.Paciente;
import com.github.aurinosalvador.anlixapi.respositories.DiagnosticoRepository;
import com.github.aurinosalvador.anlixapi.respositories.PacienteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/diagnostico")
public class DiagnosticoService {

    private static final Logger logger = LoggerFactory.getLogger(DiagnosticoService.class);

    @Autowired
    DiagnosticoRepository diagnosticoRepository;

    @Autowired
    PacienteRepository pacienteRepository;

    @PostMapping("/import")
    ResponseEntity<List<Diagnostico>> importFromFile(@RequestParam("file") MultipartFile file) {

        try {
            boolean firstLine = true;
            File localFile = ResourceUtils.getFile("classpath:targetFile.tmp");
            file.transferTo(localFile);

            BufferedReader br = new BufferedReader(new FileReader(localFile));
            String line = br.readLine();


            Date date = new SimpleDateFormat("ddMMyyyy").parse(file.getOriginalFilename());
            logger.info("data: {}", date);


            List<Diagnostico> diagnosticos = new ArrayList<>();
            String type = "";

            while (line != null) {
                String[] list = line.split(" ");

                Diagnostico diagnostico = new Diagnostico();
                diagnostico.setData(date);

                if (firstLine) {
                    type = list[list.length - 1];
                    logger.info("type: {}", list[list.length - 1]);

                    firstLine = false;
                    line = br.readLine();
                    continue;
                }

                List<Paciente> pacientes = pacienteRepository.findByCpf(list[0]);
                diagnostico.setTipo(type);
                diagnostico.setPaciente(pacientes.get(0));
                diagnostico.setEpoc(list[1]);
                diagnostico.setValor(Double.parseDouble(list[2]));

                logger.info("Diagnostico: {} {} - {}: {}",
                        diagnostico.getPaciente().getNome(),
                        diagnostico.getData(),
                        diagnostico.getTipo(),
                        diagnostico.getValor()
                );
                diagnosticos.add(diagnostico);
                line = br.readLine();
            }
            br.close();

//            logger.info("Diagnosticos: {}", diagnosticos.size());

            List<Diagnostico> ret = diagnosticoRepository.saveAll(diagnosticos);

            return ResponseEntity.ok(ret);

        } catch (IOException | ParseException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/paciente/{id}/tipo/{tipo}/last")
    ResponseEntity<DiagnosticoDTO> getByPacienteIdAndType(@PathVariable Long id, @PathVariable String tipo) {
        List<DiagnosticoDTO> diagnosticos = diagnosticoRepository.findByPacienteIdAndTipoOrderByDataDesc(id, tipo)
                .stream()
                .map(DiagnosticoDTO::parserDTO)
                .collect(Collectors.toList());

        return diagnosticos.size() > 0
                ? ResponseEntity.ok(diagnosticos.get(0))
                : ResponseEntity.notFound().build();
    }

    @GetMapping("/paciente/{id}")
    ResponseEntity<List<DiagnosticoDTO>> getByPacienteId(@PathVariable Long id) {
        List<DiagnosticoDTO> diagnosticos = diagnosticoRepository.findLastByPacienteId(id)
                .stream()
                .map(DiagnosticoDTO::parserDTO)
                .collect(Collectors.toList());


        return ResponseEntity.ok(diagnosticos);
    }
}
