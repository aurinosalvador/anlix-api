package com.github.aurinosalvador.anlixapi.respositories;

import com.github.aurinosalvador.anlixapi.entities.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    List<Paciente> findByCpf(String cpf);

    List<Paciente> findByNomeContaining(String nome);

}
