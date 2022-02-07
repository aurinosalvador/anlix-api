package com.github.aurinosalvador.anlixapi.DTO;

import com.github.aurinosalvador.anlixapi.entities.Diagnostico;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiagnosticoDTO {
    private Long id;

    private String epoc;

    private String tipo;

    private double valor;

    private Date data;

    public static DiagnosticoDTO parserDTO(Diagnostico diagnostico) {
        return new DiagnosticoDTO(diagnostico.getId(),
                diagnostico.getEpoc(),
                diagnostico.getTipo(),
                diagnostico.getValor(),
                diagnostico.getData()
        );
    }
}
