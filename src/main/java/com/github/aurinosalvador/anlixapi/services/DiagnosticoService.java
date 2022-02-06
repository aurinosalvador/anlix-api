package com.github.aurinosalvador.anlixapi.services;

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
import java.util.Date;
import java.util.List;

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

            Diagnostico diagnostico = new Diagnostico();
            Date date=new SimpleDateFormat("ddMMyyyy").parse(file.getOriginalFilename());
            logger.info("data: {}", date);
            diagnostico.setData(date);

            while (line != null) {
                String[] list = line.split(" ");

                if(firstLine){
                    diagnostico.setTipo(list[list.length - 1]);
                    logger.info("type: {}", list[list.length - 1]);

                    firstLine = false;
                    line = br.readLine();
                    continue;
                }

                List<Paciente> pacientes = pacienteRepository.findByCpf(list[0]);

                diagnostico.setPaciente(pacientes.get(0));
                diagnostico.setEpoc(Long.parseLong(list[1]));
                diagnostico.setValor(Double.parseDouble(list[2]));

//                logger.info("Diagnostico: {} {} - {}: {}",
//                        diagnostico.getPaciente().getNome(),
//                        diagnostico.getData(),
//                        diagnostico.getTipo(),
//                        diagnostico.getValor()
//                );

                line = br.readLine();
            }


            br.close();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok().build();
    }

}
