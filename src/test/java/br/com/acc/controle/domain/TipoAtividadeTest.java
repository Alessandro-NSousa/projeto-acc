package br.com.acc.controle.domain;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.acc.controle.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TipoAtividadeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TipoAtividade.class);
        TipoAtividade tipoAtividade1 = new TipoAtividade();
        tipoAtividade1.setId(1L);
        TipoAtividade tipoAtividade2 = new TipoAtividade();
        tipoAtividade2.setId(tipoAtividade1.getId());
        assertThat(tipoAtividade1).isEqualTo(tipoAtividade2);
        tipoAtividade2.setId(2L);
        assertThat(tipoAtividade1).isNotEqualTo(tipoAtividade2);
        tipoAtividade1.setId(null);
        assertThat(tipoAtividade1).isNotEqualTo(tipoAtividade2);
    }
}
