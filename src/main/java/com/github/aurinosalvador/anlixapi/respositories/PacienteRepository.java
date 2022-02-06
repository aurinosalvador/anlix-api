package com.github.aurinosalvador.anlixapi.respositories;

import com.github.aurinosalvador.anlixapi.entities.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
}
