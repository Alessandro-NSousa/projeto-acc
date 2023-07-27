package br.com.acc.controle.repository;

import br.com.acc.controle.domain.TurmaACC;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TurmaACC entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TurmaACCRepository extends JpaRepository<TurmaACC, Long> {}
