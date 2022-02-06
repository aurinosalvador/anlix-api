package com.github.aurinosalvador.anlixapi.respositories;

import com.github.aurinosalvador.anlixapi.entities.Diagnostico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiagnosticoRepository extends JpaRepository<Diagnostico, Long> {
}
