package br.com.acc.controle.domain;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.acc.controle.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TurmaACCTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TurmaACC.class);
        TurmaACC turmaACC1 = new TurmaACC();
        turmaACC1.setId(1L);
        TurmaACC turmaACC2 = new TurmaACC();
        turmaACC2.setId(turmaACC1.getId());
        assertThat(turmaACC1).isEqualTo(turmaACC2);
        turmaACC2.setId(2L);
        assertThat(turmaACC1).isNotEqualTo(turmaACC2);
        turmaACC1.setId(null);
        assertThat(turmaACC1).isNotEqualTo(turmaACC2);
    }
}
