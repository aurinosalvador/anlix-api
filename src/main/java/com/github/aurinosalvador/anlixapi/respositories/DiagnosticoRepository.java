package com.github.aurinosalvador.anlixapi.respositories;

import com.github.aurinosalvador.anlixapi.DTO.DiagnosticoDTO;
import com.github.aurinosalvador.anlixapi.entities.Diagnostico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface DiagnosticoRepository extends JpaRepository<Diagnostico, Long> {

    @Query(value = "(SELECT * FROM diagnostico d  WHERE paciente_id = ?1 and tipo = 'ind_card' order by \"data\" desc limit 1) \n" +
            "union all \n" +
            "(SELECT * FROM diagnostico d  WHERE paciente_id = ?1 and tipo = 'ind_pulm' order by \"data\" desc limit 1)", nativeQuery = true)
    List<Diagnostico> findLastByPacienteId(Long id);

    List<Diagnostico> findByPacienteIdAndTipoOrderByDataDesc(Long id, String tipo);

    List<Diagnostico> findByData(Date data);

    List<Diagnostico> findByPacienteIdAndDataBetween(Long id, Date initDate, Date endDate);

    @Query(value = "SELECT * FROM diagnostico d  WHERE valor = (SELECT max(valor)  FROM diagnostico d  " +
            "WHERE paciente_id = ?1 and tipo = ?2 and valor between ?3 and ?4 " +
            "group by \"data\"  order by \"data\" desc limit 1 )", nativeQuery = true)
    Diagnostico findByPacienteIdAndTipoAndValorBetween(Long id, String tipo, double minValor, double maxValor);

    List<Diagnostico> findByPacienteId(Long id);
}
