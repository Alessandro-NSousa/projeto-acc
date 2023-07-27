package br.com.acc.controle.web.rest;

import static br.com.acc.controle.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.acc.controle.IntegrationTest;
import br.com.acc.controle.domain.TipoAtividade;
import br.com.acc.controle.repository.TipoAtividadeRepository;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link TipoAtividadeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TipoAtividadeResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final Integer DEFAULT_NUMERO_PONTOS = 1;
    private static final Integer UPDATED_NUMERO_PONTOS = 2;

    private static final ZonedDateTime DEFAULT_DATA_CRIACAO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA_CRIACAO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/tipo-atividades";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TipoAtividadeRepository tipoAtividadeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTipoAtividadeMockMvc;

    private TipoAtividade tipoAtividade;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TipoAtividade createEntity(EntityManager em) {
        TipoAtividade tipoAtividade = new TipoAtividade()
            .nome(DEFAULT_NOME)
            .descricao(DEFAULT_DESCRICAO)
            .numeroPontos(DEFAULT_NUMERO_PONTOS)
            .dataCriacao(DEFAULT_DATA_CRIACAO);
        return tipoAtividade;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TipoAtividade createUpdatedEntity(EntityManager em) {
        TipoAtividade tipoAtividade = new TipoAtividade()
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO)
            .numeroPontos(UPDATED_NUMERO_PONTOS)
            .dataCriacao(UPDATED_DATA_CRIACAO);
        return tipoAtividade;
    }

    @BeforeEach
    public void initTest() {
        tipoAtividade = createEntity(em);
    }

    @Test
    @Transactional
    void createTipoAtividade() throws Exception {
        int databaseSizeBeforeCreate = tipoAtividadeRepository.findAll().size();
        // Create the TipoAtividade
        restTipoAtividadeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tipoAtividade)))
            .andExpect(status().isCreated());

        // Validate the TipoAtividade in the database
        List<TipoAtividade> tipoAtividadeList = tipoAtividadeRepository.findAll();
        assertThat(tipoAtividadeList).hasSize(databaseSizeBeforeCreate + 1);
        TipoAtividade testTipoAtividade = tipoAtividadeList.get(tipoAtividadeList.size() - 1);
        assertThat(testTipoAtividade.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testTipoAtividade.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testTipoAtividade.getNumeroPontos()).isEqualTo(DEFAULT_NUMERO_PONTOS);
        assertThat(testTipoAtividade.getDataCriacao()).isEqualTo(DEFAULT_DATA_CRIACAO);
    }

    @Test
    @Transactional
    void createTipoAtividadeWithExistingId() throws Exception {
        // Create the TipoAtividade with an existing ID
        tipoAtividade.setId(1L);

        int databaseSizeBeforeCreate = tipoAtividadeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTipoAtividadeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tipoAtividade)))
            .andExpect(status().isBadRequest());

        // Validate the TipoAtividade in the database
        List<TipoAtividade> tipoAtividadeList = tipoAtividadeRepository.findAll();
        assertThat(tipoAtividadeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipoAtividadeRepository.findAll().size();
        // set the field null
        tipoAtividade.setNome(null);

        // Create the TipoAtividade, which fails.

        restTipoAtividadeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tipoAtividade)))
            .andExpect(status().isBadRequest());

        List<TipoAtividade> tipoAtividadeList = tipoAtividadeRepository.findAll();
        assertThat(tipoAtividadeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTipoAtividades() throws Exception {
        // Initialize the database
        tipoAtividadeRepository.saveAndFlush(tipoAtividade);

        // Get all the tipoAtividadeList
        restTipoAtividadeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipoAtividade.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO.toString())))
            .andExpect(jsonPath("$.[*].numeroPontos").value(hasItem(DEFAULT_NUMERO_PONTOS)))
            .andExpect(jsonPath("$.[*].dataCriacao").value(hasItem(sameInstant(DEFAULT_DATA_CRIACAO))));
    }

    @Test
    @Transactional
    void getTipoAtividade() throws Exception {
        // Initialize the database
        tipoAtividadeRepository.saveAndFlush(tipoAtividade);

        // Get the tipoAtividade
        restTipoAtividadeMockMvc
            .perform(get(ENTITY_API_URL_ID, tipoAtividade.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tipoAtividade.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO.toString()))
            .andExpect(jsonPath("$.numeroPontos").value(DEFAULT_NUMERO_PONTOS))
            .andExpect(jsonPath("$.dataCriacao").value(sameInstant(DEFAULT_DATA_CRIACAO)));
    }

    @Test
    @Transactional
    void getNonExistingTipoAtividade() throws Exception {
        // Get the tipoAtividade
        restTipoAtividadeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTipoAtividade() throws Exception {
        // Initialize the database
        tipoAtividadeRepository.saveAndFlush(tipoAtividade);

        int databaseSizeBeforeUpdate = tipoAtividadeRepository.findAll().size();

        // Update the tipoAtividade
        TipoAtividade updatedTipoAtividade = tipoAtividadeRepository.findById(tipoAtividade.getId()).get();
        // Disconnect from session so that the updates on updatedTipoAtividade are not directly saved in db
        em.detach(updatedTipoAtividade);
        updatedTipoAtividade
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO)
            .numeroPontos(UPDATED_NUMERO_PONTOS)
            .dataCriacao(UPDATED_DATA_CRIACAO);

        restTipoAtividadeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTipoAtividade.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTipoAtividade))
            )
            .andExpect(status().isOk());

        // Validate the TipoAtividade in the database
        List<TipoAtividade> tipoAtividadeList = tipoAtividadeRepository.findAll();
        assertThat(tipoAtividadeList).hasSize(databaseSizeBeforeUpdate);
        TipoAtividade testTipoAtividade = tipoAtividadeList.get(tipoAtividadeList.size() - 1);
        assertThat(testTipoAtividade.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testTipoAtividade.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testTipoAtividade.getNumeroPontos()).isEqualTo(UPDATED_NUMERO_PONTOS);
        assertThat(testTipoAtividade.getDataCriacao()).isEqualTo(UPDATED_DATA_CRIACAO);
    }

    @Test
    @Transactional
    void putNonExistingTipoAtividade() throws Exception {
        int databaseSizeBeforeUpdate = tipoAtividadeRepository.findAll().size();
        tipoAtividade.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTipoAtividadeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tipoAtividade.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tipoAtividade))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoAtividade in the database
        List<TipoAtividade> tipoAtividadeList = tipoAtividadeRepository.findAll();
        assertThat(tipoAtividadeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTipoAtividade() throws Exception {
        int databaseSizeBeforeUpdate = tipoAtividadeRepository.findAll().size();
        tipoAtividade.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoAtividadeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tipoAtividade))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoAtividade in the database
        List<TipoAtividade> tipoAtividadeList = tipoAtividadeRepository.findAll();
        assertThat(tipoAtividadeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTipoAtividade() throws Exception {
        int databaseSizeBeforeUpdate = tipoAtividadeRepository.findAll().size();
        tipoAtividade.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoAtividadeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tipoAtividade)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TipoAtividade in the database
        List<TipoAtividade> tipoAtividadeList = tipoAtividadeRepository.findAll();
        assertThat(tipoAtividadeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTipoAtividadeWithPatch() throws Exception {
        // Initialize the database
        tipoAtividadeRepository.saveAndFlush(tipoAtividade);

        int databaseSizeBeforeUpdate = tipoAtividadeRepository.findAll().size();

        // Update the tipoAtividade using partial update
        TipoAtividade partialUpdatedTipoAtividade = new TipoAtividade();
        partialUpdatedTipoAtividade.setId(tipoAtividade.getId());

        partialUpdatedTipoAtividade.nome(UPDATED_NOME).descricao(UPDATED_DESCRICAO).numeroPontos(UPDATED_NUMERO_PONTOS);

        restTipoAtividadeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTipoAtividade.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTipoAtividade))
            )
            .andExpect(status().isOk());

        // Validate the TipoAtividade in the database
        List<TipoAtividade> tipoAtividadeList = tipoAtividadeRepository.findAll();
        assertThat(tipoAtividadeList).hasSize(databaseSizeBeforeUpdate);
        TipoAtividade testTipoAtividade = tipoAtividadeList.get(tipoAtividadeList.size() - 1);
        assertThat(testTipoAtividade.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testTipoAtividade.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testTipoAtividade.getNumeroPontos()).isEqualTo(UPDATED_NUMERO_PONTOS);
        assertThat(testTipoAtividade.getDataCriacao()).isEqualTo(DEFAULT_DATA_CRIACAO);
    }

    @Test
    @Transactional
    void fullUpdateTipoAtividadeWithPatch() throws Exception {
        // Initialize the database
        tipoAtividadeRepository.saveAndFlush(tipoAtividade);

        int databaseSizeBeforeUpdate = tipoAtividadeRepository.findAll().size();

        // Update the tipoAtividade using partial update
        TipoAtividade partialUpdatedTipoAtividade = new TipoAtividade();
        partialUpdatedTipoAtividade.setId(tipoAtividade.getId());

        partialUpdatedTipoAtividade
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO)
            .numeroPontos(UPDATED_NUMERO_PONTOS)
            .dataCriacao(UPDATED_DATA_CRIACAO);

        restTipoAtividadeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTipoAtividade.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTipoAtividade))
            )
            .andExpect(status().isOk());

        // Validate the TipoAtividade in the database
        List<TipoAtividade> tipoAtividadeList = tipoAtividadeRepository.findAll();
        assertThat(tipoAtividadeList).hasSize(databaseSizeBeforeUpdate);
        TipoAtividade testTipoAtividade = tipoAtividadeList.get(tipoAtividadeList.size() - 1);
        assertThat(testTipoAtividade.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testTipoAtividade.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testTipoAtividade.getNumeroPontos()).isEqualTo(UPDATED_NUMERO_PONTOS);
        assertThat(testTipoAtividade.getDataCriacao()).isEqualTo(UPDATED_DATA_CRIACAO);
    }

    @Test
    @Transactional
    void patchNonExistingTipoAtividade() throws Exception {
        int databaseSizeBeforeUpdate = tipoAtividadeRepository.findAll().size();
        tipoAtividade.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTipoAtividadeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tipoAtividade.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tipoAtividade))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoAtividade in the database
        List<TipoAtividade> tipoAtividadeList = tipoAtividadeRepository.findAll();
        assertThat(tipoAtividadeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTipoAtividade() throws Exception {
        int databaseSizeBeforeUpdate = tipoAtividadeRepository.findAll().size();
        tipoAtividade.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoAtividadeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tipoAtividade))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoAtividade in the database
        List<TipoAtividade> tipoAtividadeList = tipoAtividadeRepository.findAll();
        assertThat(tipoAtividadeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTipoAtividade() throws Exception {
        int databaseSizeBeforeUpdate = tipoAtividadeRepository.findAll().size();
        tipoAtividade.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoAtividadeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(tipoAtividade))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TipoAtividade in the database
        List<TipoAtividade> tipoAtividadeList = tipoAtividadeRepository.findAll();
        assertThat(tipoAtividadeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTipoAtividade() throws Exception {
        // Initialize the database
        tipoAtividadeRepository.saveAndFlush(tipoAtividade);

        int databaseSizeBeforeDelete = tipoAtividadeRepository.findAll().size();

        // Delete the tipoAtividade
        restTipoAtividadeMockMvc
            .perform(delete(ENTITY_API_URL_ID, tipoAtividade.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TipoAtividade> tipoAtividadeList = tipoAtividadeRepository.findAll();
        assertThat(tipoAtividadeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
