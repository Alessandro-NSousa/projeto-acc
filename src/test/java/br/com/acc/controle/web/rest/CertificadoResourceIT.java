package br.com.acc.controle.web.rest;

import static br.com.acc.controle.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.acc.controle.IntegrationTest;
import br.com.acc.controle.domain.Certificado;
import br.com.acc.controle.domain.enumeration.Modalidade;
import br.com.acc.controle.domain.enumeration.StatusCertificado;
import br.com.acc.controle.repository.CertificadoRepository;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link CertificadoResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CertificadoResourceIT {

    private static final String DEFAULT_TITULO = "AAAAAAAAAA";
    private static final String UPDATED_TITULO = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATA_ENVIO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA_ENVIO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_OBSERVACAO = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVACAO = "BBBBBBBBBB";

    private static final Modalidade DEFAULT_MODALIDADE = Modalidade.LOCAL;
    private static final Modalidade UPDATED_MODALIDADE = Modalidade.REGIONAL;

    private static final Integer DEFAULT_CH_CUPRIDA = 1;
    private static final Integer UPDATED_CH_CUPRIDA = 2;

    private static final Integer DEFAULT_PONTUACAO = 1;
    private static final Integer UPDATED_PONTUACAO = 2;

    private static final StatusCertificado DEFAULT_STATUS = StatusCertificado.EM_ESPERA;
    private static final StatusCertificado UPDATED_STATUS = StatusCertificado.EM_AVALIACAO;

    private static final String DEFAULT_CAMINHO_ARQUIVO = "AAAAAAAAAA";
    private static final String UPDATED_CAMINHO_ARQUIVO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/certificados";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CertificadoRepository certificadoRepository;

    @Mock
    private CertificadoRepository certificadoRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCertificadoMockMvc;

    private Certificado certificado;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Certificado createEntity(EntityManager em) {
        Certificado certificado = new Certificado()
            .titulo(DEFAULT_TITULO)
            .descricao(DEFAULT_DESCRICAO)
            .dataEnvio(DEFAULT_DATA_ENVIO)
            .observacao(DEFAULT_OBSERVACAO)
            .modalidade(DEFAULT_MODALIDADE)
            .chCuprida(DEFAULT_CH_CUPRIDA)
            .pontuacao(DEFAULT_PONTUACAO)
            .status(DEFAULT_STATUS)
            .caminhoArquivo(DEFAULT_CAMINHO_ARQUIVO);
        return certificado;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Certificado createUpdatedEntity(EntityManager em) {
        Certificado certificado = new Certificado()
            .titulo(UPDATED_TITULO)
            .descricao(UPDATED_DESCRICAO)
            .dataEnvio(UPDATED_DATA_ENVIO)
            .observacao(UPDATED_OBSERVACAO)
            .modalidade(UPDATED_MODALIDADE)
            .chCuprida(UPDATED_CH_CUPRIDA)
            .pontuacao(UPDATED_PONTUACAO)
            .status(UPDATED_STATUS)
            .caminhoArquivo(UPDATED_CAMINHO_ARQUIVO);
        return certificado;
    }

    @BeforeEach
    public void initTest() {
        certificado = createEntity(em);
    }

    @Test
    @Transactional
    void createCertificado() throws Exception {
        int databaseSizeBeforeCreate = certificadoRepository.findAll().size();
        // Create the Certificado
        restCertificadoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(certificado)))
            .andExpect(status().isCreated());

        // Validate the Certificado in the database
        List<Certificado> certificadoList = certificadoRepository.findAll();
        assertThat(certificadoList).hasSize(databaseSizeBeforeCreate + 1);
        Certificado testCertificado = certificadoList.get(certificadoList.size() - 1);
        assertThat(testCertificado.getTitulo()).isEqualTo(DEFAULT_TITULO);
        assertThat(testCertificado.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testCertificado.getDataEnvio()).isEqualTo(DEFAULT_DATA_ENVIO);
        assertThat(testCertificado.getObservacao()).isEqualTo(DEFAULT_OBSERVACAO);
        assertThat(testCertificado.getModalidade()).isEqualTo(DEFAULT_MODALIDADE);
        assertThat(testCertificado.getChCuprida()).isEqualTo(DEFAULT_CH_CUPRIDA);
        assertThat(testCertificado.getPontuacao()).isEqualTo(DEFAULT_PONTUACAO);
        assertThat(testCertificado.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testCertificado.getCaminhoArquivo()).isEqualTo(DEFAULT_CAMINHO_ARQUIVO);
    }

    @Test
    @Transactional
    void createCertificadoWithExistingId() throws Exception {
        // Create the Certificado with an existing ID
        certificado.setId(1L);

        int databaseSizeBeforeCreate = certificadoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCertificadoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(certificado)))
            .andExpect(status().isBadRequest());

        // Validate the Certificado in the database
        List<Certificado> certificadoList = certificadoRepository.findAll();
        assertThat(certificadoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCertificados() throws Exception {
        // Initialize the database
        certificadoRepository.saveAndFlush(certificado);

        // Get all the certificadoList
        restCertificadoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(certificado.getId().intValue())))
            .andExpect(jsonPath("$.[*].titulo").value(hasItem(DEFAULT_TITULO)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO.toString())))
            .andExpect(jsonPath("$.[*].dataEnvio").value(hasItem(sameInstant(DEFAULT_DATA_ENVIO))))
            .andExpect(jsonPath("$.[*].observacao").value(hasItem(DEFAULT_OBSERVACAO.toString())))
            .andExpect(jsonPath("$.[*].modalidade").value(hasItem(DEFAULT_MODALIDADE.toString())))
            .andExpect(jsonPath("$.[*].chCuprida").value(hasItem(DEFAULT_CH_CUPRIDA)))
            .andExpect(jsonPath("$.[*].pontuacao").value(hasItem(DEFAULT_PONTUACAO)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].caminhoArquivo").value(hasItem(DEFAULT_CAMINHO_ARQUIVO)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCertificadosWithEagerRelationshipsIsEnabled() throws Exception {
        when(certificadoRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCertificadoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(certificadoRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCertificadosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(certificadoRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCertificadoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(certificadoRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getCertificado() throws Exception {
        // Initialize the database
        certificadoRepository.saveAndFlush(certificado);

        // Get the certificado
        restCertificadoMockMvc
            .perform(get(ENTITY_API_URL_ID, certificado.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(certificado.getId().intValue()))
            .andExpect(jsonPath("$.titulo").value(DEFAULT_TITULO))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO.toString()))
            .andExpect(jsonPath("$.dataEnvio").value(sameInstant(DEFAULT_DATA_ENVIO)))
            .andExpect(jsonPath("$.observacao").value(DEFAULT_OBSERVACAO.toString()))
            .andExpect(jsonPath("$.modalidade").value(DEFAULT_MODALIDADE.toString()))
            .andExpect(jsonPath("$.chCuprida").value(DEFAULT_CH_CUPRIDA))
            .andExpect(jsonPath("$.pontuacao").value(DEFAULT_PONTUACAO))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.caminhoArquivo").value(DEFAULT_CAMINHO_ARQUIVO));
    }

    @Test
    @Transactional
    void getNonExistingCertificado() throws Exception {
        // Get the certificado
        restCertificadoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCertificado() throws Exception {
        // Initialize the database
        certificadoRepository.saveAndFlush(certificado);

        int databaseSizeBeforeUpdate = certificadoRepository.findAll().size();

        // Update the certificado
        Certificado updatedCertificado = certificadoRepository.findById(certificado.getId()).get();
        // Disconnect from session so that the updates on updatedCertificado are not directly saved in db
        em.detach(updatedCertificado);
        updatedCertificado
            .titulo(UPDATED_TITULO)
            .descricao(UPDATED_DESCRICAO)
            .dataEnvio(UPDATED_DATA_ENVIO)
            .observacao(UPDATED_OBSERVACAO)
            .modalidade(UPDATED_MODALIDADE)
            .chCuprida(UPDATED_CH_CUPRIDA)
            .pontuacao(UPDATED_PONTUACAO)
            .status(UPDATED_STATUS)
            .caminhoArquivo(UPDATED_CAMINHO_ARQUIVO);

        restCertificadoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCertificado.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCertificado))
            )
            .andExpect(status().isOk());

        // Validate the Certificado in the database
        List<Certificado> certificadoList = certificadoRepository.findAll();
        assertThat(certificadoList).hasSize(databaseSizeBeforeUpdate);
        Certificado testCertificado = certificadoList.get(certificadoList.size() - 1);
        assertThat(testCertificado.getTitulo()).isEqualTo(UPDATED_TITULO);
        assertThat(testCertificado.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testCertificado.getDataEnvio()).isEqualTo(UPDATED_DATA_ENVIO);
        assertThat(testCertificado.getObservacao()).isEqualTo(UPDATED_OBSERVACAO);
        assertThat(testCertificado.getModalidade()).isEqualTo(UPDATED_MODALIDADE);
        assertThat(testCertificado.getChCuprida()).isEqualTo(UPDATED_CH_CUPRIDA);
        assertThat(testCertificado.getPontuacao()).isEqualTo(UPDATED_PONTUACAO);
        assertThat(testCertificado.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testCertificado.getCaminhoArquivo()).isEqualTo(UPDATED_CAMINHO_ARQUIVO);
    }

    @Test
    @Transactional
    void putNonExistingCertificado() throws Exception {
        int databaseSizeBeforeUpdate = certificadoRepository.findAll().size();
        certificado.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCertificadoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, certificado.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(certificado))
            )
            .andExpect(status().isBadRequest());

        // Validate the Certificado in the database
        List<Certificado> certificadoList = certificadoRepository.findAll();
        assertThat(certificadoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCertificado() throws Exception {
        int databaseSizeBeforeUpdate = certificadoRepository.findAll().size();
        certificado.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCertificadoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(certificado))
            )
            .andExpect(status().isBadRequest());

        // Validate the Certificado in the database
        List<Certificado> certificadoList = certificadoRepository.findAll();
        assertThat(certificadoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCertificado() throws Exception {
        int databaseSizeBeforeUpdate = certificadoRepository.findAll().size();
        certificado.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCertificadoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(certificado)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Certificado in the database
        List<Certificado> certificadoList = certificadoRepository.findAll();
        assertThat(certificadoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCertificadoWithPatch() throws Exception {
        // Initialize the database
        certificadoRepository.saveAndFlush(certificado);

        int databaseSizeBeforeUpdate = certificadoRepository.findAll().size();

        // Update the certificado using partial update
        Certificado partialUpdatedCertificado = new Certificado();
        partialUpdatedCertificado.setId(certificado.getId());

        partialUpdatedCertificado
            .descricao(UPDATED_DESCRICAO)
            .chCuprida(UPDATED_CH_CUPRIDA)
            .pontuacao(UPDATED_PONTUACAO)
            .status(UPDATED_STATUS)
            .caminhoArquivo(UPDATED_CAMINHO_ARQUIVO);

        restCertificadoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCertificado.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCertificado))
            )
            .andExpect(status().isOk());

        // Validate the Certificado in the database
        List<Certificado> certificadoList = certificadoRepository.findAll();
        assertThat(certificadoList).hasSize(databaseSizeBeforeUpdate);
        Certificado testCertificado = certificadoList.get(certificadoList.size() - 1);
        assertThat(testCertificado.getTitulo()).isEqualTo(DEFAULT_TITULO);
        assertThat(testCertificado.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testCertificado.getDataEnvio()).isEqualTo(DEFAULT_DATA_ENVIO);
        assertThat(testCertificado.getObservacao()).isEqualTo(DEFAULT_OBSERVACAO);
        assertThat(testCertificado.getModalidade()).isEqualTo(DEFAULT_MODALIDADE);
        assertThat(testCertificado.getChCuprida()).isEqualTo(UPDATED_CH_CUPRIDA);
        assertThat(testCertificado.getPontuacao()).isEqualTo(UPDATED_PONTUACAO);
        assertThat(testCertificado.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testCertificado.getCaminhoArquivo()).isEqualTo(UPDATED_CAMINHO_ARQUIVO);
    }

    @Test
    @Transactional
    void fullUpdateCertificadoWithPatch() throws Exception {
        // Initialize the database
        certificadoRepository.saveAndFlush(certificado);

        int databaseSizeBeforeUpdate = certificadoRepository.findAll().size();

        // Update the certificado using partial update
        Certificado partialUpdatedCertificado = new Certificado();
        partialUpdatedCertificado.setId(certificado.getId());

        partialUpdatedCertificado
            .titulo(UPDATED_TITULO)
            .descricao(UPDATED_DESCRICAO)
            .dataEnvio(UPDATED_DATA_ENVIO)
            .observacao(UPDATED_OBSERVACAO)
            .modalidade(UPDATED_MODALIDADE)
            .chCuprida(UPDATED_CH_CUPRIDA)
            .pontuacao(UPDATED_PONTUACAO)
            .status(UPDATED_STATUS)
            .caminhoArquivo(UPDATED_CAMINHO_ARQUIVO);

        restCertificadoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCertificado.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCertificado))
            )
            .andExpect(status().isOk());

        // Validate the Certificado in the database
        List<Certificado> certificadoList = certificadoRepository.findAll();
        assertThat(certificadoList).hasSize(databaseSizeBeforeUpdate);
        Certificado testCertificado = certificadoList.get(certificadoList.size() - 1);
        assertThat(testCertificado.getTitulo()).isEqualTo(UPDATED_TITULO);
        assertThat(testCertificado.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testCertificado.getDataEnvio()).isEqualTo(UPDATED_DATA_ENVIO);
        assertThat(testCertificado.getObservacao()).isEqualTo(UPDATED_OBSERVACAO);
        assertThat(testCertificado.getModalidade()).isEqualTo(UPDATED_MODALIDADE);
        assertThat(testCertificado.getChCuprida()).isEqualTo(UPDATED_CH_CUPRIDA);
        assertThat(testCertificado.getPontuacao()).isEqualTo(UPDATED_PONTUACAO);
        assertThat(testCertificado.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testCertificado.getCaminhoArquivo()).isEqualTo(UPDATED_CAMINHO_ARQUIVO);
    }

    @Test
    @Transactional
    void patchNonExistingCertificado() throws Exception {
        int databaseSizeBeforeUpdate = certificadoRepository.findAll().size();
        certificado.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCertificadoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, certificado.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(certificado))
            )
            .andExpect(status().isBadRequest());

        // Validate the Certificado in the database
        List<Certificado> certificadoList = certificadoRepository.findAll();
        assertThat(certificadoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCertificado() throws Exception {
        int databaseSizeBeforeUpdate = certificadoRepository.findAll().size();
        certificado.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCertificadoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(certificado))
            )
            .andExpect(status().isBadRequest());

        // Validate the Certificado in the database
        List<Certificado> certificadoList = certificadoRepository.findAll();
        assertThat(certificadoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCertificado() throws Exception {
        int databaseSizeBeforeUpdate = certificadoRepository.findAll().size();
        certificado.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCertificadoMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(certificado))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Certificado in the database
        List<Certificado> certificadoList = certificadoRepository.findAll();
        assertThat(certificadoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCertificado() throws Exception {
        // Initialize the database
        certificadoRepository.saveAndFlush(certificado);

        int databaseSizeBeforeDelete = certificadoRepository.findAll().size();

        // Delete the certificado
        restCertificadoMockMvc
            .perform(delete(ENTITY_API_URL_ID, certificado.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Certificado> certificadoList = certificadoRepository.findAll();
        assertThat(certificadoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
