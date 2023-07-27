package br.com.acc.controle.domain;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.acc.controle.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CertificadoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Certificado.class);
        Certificado certificado1 = new Certificado();
        certificado1.setId(1L);
        Certificado certificado2 = new Certificado();
        certificado2.setId(certificado1.getId());
        assertThat(certificado1).isEqualTo(certificado2);
        certificado2.setId(2L);
        assertThat(certificado1).isNotEqualTo(certificado2);
        certificado1.setId(null);
        assertThat(certificado1).isNotEqualTo(certificado2);
    }
}
