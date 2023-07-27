package br.com.acc.controle.repository;

import br.com.acc.controle.domain.TipoAtividade;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TipoAtividade entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TipoAtividadeRepository extends JpaRepository<TipoAtividade, Long> {}
