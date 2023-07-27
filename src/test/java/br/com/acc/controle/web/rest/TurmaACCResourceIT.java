package br.com.acc.controle.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.acc.controle.IntegrationTest;
import br.com.acc.controle.domain.TurmaACC;
import br.com.acc.controle.repository.TurmaACCRepository;
import java.time.LocalDate;
import java.time.ZoneId;
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

/**
 * Integration tests for the {@link TurmaACCResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TurmaACCResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_INICIO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_INICIO = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_TERMINO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TERMINO = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/turma-accs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TurmaACCRepository turmaACCRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTurmaACCMockMvc;

    private TurmaACC turmaACC;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TurmaACC createEntity(EntityManager em) {
        TurmaACC turmaACC = new TurmaACC().nome(DEFAULT_NOME).inicio(DEFAULT_INICIO).termino(DEFAULT_TERMINO);
        return turmaACC;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TurmaACC createUpdatedEntity(EntityManager em) {
        TurmaACC turmaACC = new TurmaACC().nome(UPDATED_NOME).inicio(UPDATED_INICIO).termino(UPDATED_TERMINO);
        return turmaACC;
    }

    @BeforeEach
    public void initTest() {
        turmaACC = createEntity(em);
    }

    @Test
    @Transactional
    void createTurmaACC() throws Exception {
        int databaseSizeBeforeCreate = turmaACCRepository.findAll().size();
        // Create the TurmaACC
        restTurmaACCMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(turmaACC)))
            .andExpect(status().isCreated());

        // Validate the TurmaACC in the database
        List<TurmaACC> turmaACCList = turmaACCRepository.findAll();
        assertThat(turmaACCList).hasSize(databaseSizeBeforeCreate + 1);
        TurmaACC testTurmaACC = turmaACCList.get(turmaACCList.size() - 1);
        assertThat(testTurmaACC.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testTurmaACC.getInicio()).isEqualTo(DEFAULT_INICIO);
        assertThat(testTurmaACC.getTermino()).isEqualTo(DEFAULT_TERMINO);
    }

    @Test
    @Transactional
    void createTurmaACCWithExistingId() throws Exception {
        // Create the TurmaACC with an existing ID
        turmaACC.setId(1L);

        int databaseSizeBeforeCreate = turmaACCRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTurmaACCMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(turmaACC)))
            .andExpect(status().isBadRequest());

        // Validate the TurmaACC in the database
        List<TurmaACC> turmaACCList = turmaACCRepository.findAll();
        assertThat(turmaACCList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = turmaACCRepository.findAll().size();
        // set the field null
        turmaACC.setNome(null);

        // Create the TurmaACC, which fails.

        restTurmaACCMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(turmaACC)))
            .andExpect(status().isBadRequest());

        List<TurmaACC> turmaACCList = turmaACCRepository.findAll();
        assertThat(turmaACCList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTurmaACCS() throws Exception {
        // Initialize the database
        turmaACCRepository.saveAndFlush(turmaACC);

        // Get all the turmaACCList
        restTurmaACCMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(turmaACC.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].inicio").value(hasItem(DEFAULT_INICIO.toString())))
            .andExpect(jsonPath("$.[*].termino").value(hasItem(DEFAULT_TERMINO.toString())));
    }

    @Test
    @Transactional
    void getTurmaACC() throws Exception {
        // Initialize the database
        turmaACCRepository.saveAndFlush(turmaACC);

        // Get the turmaACC
        restTurmaACCMockMvc
            .perform(get(ENTITY_API_URL_ID, turmaACC.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(turmaACC.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.inicio").value(DEFAULT_INICIO.toString()))
            .andExpect(jsonPath("$.termino").value(DEFAULT_TERMINO.toString()));
    }

    @Test
    @Transactional
    void getNonExistingTurmaACC() throws Exception {
        // Get the turmaACC
        restTurmaACCMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTurmaACC() throws Exception {
        // Initialize the database
        turmaACCRepository.saveAndFlush(turmaACC);

        int databaseSizeBeforeUpdate = turmaACCRepository.findAll().size();

        // Update the turmaACC
        TurmaACC updatedTurmaACC = turmaACCRepository.findById(turmaACC.getId()).get();
        // Disconnect from session so that the updates on updatedTurmaACC are not directly saved in db
        em.detach(updatedTurmaACC);
        updatedTurmaACC.nome(UPDATED_NOME).inicio(UPDATED_INICIO).termino(UPDATED_TERMINO);

        restTurmaACCMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTurmaACC.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTurmaACC))
            )
            .andExpect(status().isOk());

        // Validate the TurmaACC in the database
        List<TurmaACC> turmaACCList = turmaACCRepository.findAll();
        assertThat(turmaACCList).hasSize(databaseSizeBeforeUpdate);
        TurmaACC testTurmaACC = turmaACCList.get(turmaACCList.size() - 1);
        assertThat(testTurmaACC.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testTurmaACC.getInicio()).isEqualTo(UPDATED_INICIO);
        assertThat(testTurmaACC.getTermino()).isEqualTo(UPDATED_TERMINO);
    }

    @Test
    @Transactional
    void putNonExistingTurmaACC() throws Exception {
        int databaseSizeBeforeUpdate = turmaACCRepository.findAll().size();
        turmaACC.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTurmaACCMockMvc
            .perform(
                put(ENTITY_API_URL_ID, turmaACC.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(turmaACC))
            )
            .andExpect(status().isBadRequest());

        // Validate the TurmaACC in the database
        List<TurmaACC> turmaACCList = turmaACCRepository.findAll();
        assertThat(turmaACCList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTurmaACC() throws Exception {
        int databaseSizeBeforeUpdate = turmaACCRepository.findAll().size();
        turmaACC.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTurmaACCMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(turmaACC))
            )
            .andExpect(status().isBadRequest());

        // Validate the TurmaACC in the database
        List<TurmaACC> turmaACCList = turmaACCRepository.findAll();
        assertThat(turmaACCList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTurmaACC() throws Exception {
        int databaseSizeBeforeUpdate = turmaACCRepository.findAll().size();
        turmaACC.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTurmaACCMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(turmaACC)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TurmaACC in the database
        List<TurmaACC> turmaACCList = turmaACCRepository.findAll();
        assertThat(turmaACCList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTurmaACCWithPatch() throws Exception {
        // Initialize the database
        turmaACCRepository.saveAndFlush(turmaACC);

        int databaseSizeBeforeUpdate = turmaACCRepository.findAll().size();

        // Update the turmaACC using partial update
        TurmaACC partialUpdatedTurmaACC = new TurmaACC();
        partialUpdatedTurmaACC.setId(turmaACC.getId());

        partialUpdatedTurmaACC.nome(UPDATED_NOME).termino(UPDATED_TERMINO);

        restTurmaACCMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTurmaACC.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTurmaACC))
            )
            .andExpect(status().isOk());

        // Validate the TurmaACC in the database
        List<TurmaACC> turmaACCList = turmaACCRepository.findAll();
        assertThat(turmaACCList).hasSize(databaseSizeBeforeUpdate);
        TurmaACC testTurmaACC = turmaACCList.get(turmaACCList.size() - 1);
        assertThat(testTurmaACC.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testTurmaACC.getInicio()).isEqualTo(DEFAULT_INICIO);
        assertThat(testTurmaACC.getTermino()).isEqualTo(UPDATED_TERMINO);
    }

    @Test
    @Transactional
    void fullUpdateTurmaACCWithPatch() throws Exception {
        // Initialize the database
        turmaACCRepository.saveAndFlush(turmaACC);

        int databaseSizeBeforeUpdate = turmaACCRepository.findAll().size();

        // Update the turmaACC using partial update
        TurmaACC partialUpdatedTurmaACC = new TurmaACC();
        partialUpdatedTurmaACC.setId(turmaACC.getId());

        partialUpdatedTurmaACC.nome(UPDATED_NOME).inicio(UPDATED_INICIO).termino(UPDATED_TERMINO);

        restTurmaACCMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTurmaACC.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTurmaACC))
            )
            .andExpect(status().isOk());

        // Validate the TurmaACC in the database
        List<TurmaACC> turmaACCList = turmaACCRepository.findAll();
        assertThat(turmaACCList).hasSize(databaseSizeBeforeUpdate);
        TurmaACC testTurmaACC = turmaACCList.get(turmaACCList.size() - 1);
        assertThat(testTurmaACC.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testTurmaACC.getInicio()).isEqualTo(UPDATED_INICIO);
        assertThat(testTurmaACC.getTermino()).isEqualTo(UPDATED_TERMINO);
    }

    @Test
    @Transactional
    void patchNonExistingTurmaACC() throws Exception {
        int databaseSizeBeforeUpdate = turmaACCRepository.findAll().size();
        turmaACC.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTurmaACCMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, turmaACC.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(turmaACC))
            )
            .andExpect(status().isBadRequest());

        // Validate the TurmaACC in the database
        List<TurmaACC> turmaACCList = turmaACCRepository.findAll();
        assertThat(turmaACCList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTurmaACC() throws Exception {
        int databaseSizeBeforeUpdate = turmaACCRepository.findAll().size();
        turmaACC.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTurmaACCMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(turmaACC))
            )
            .andExpect(status().isBadRequest());

        // Validate the TurmaACC in the database
        List<TurmaACC> turmaACCList = turmaACCRepository.findAll();
        assertThat(turmaACCList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTurmaACC() throws Exception {
        int databaseSizeBeforeUpdate = turmaACCRepository.findAll().size();
        turmaACC.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTurmaACCMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(turmaACC)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TurmaACC in the database
        List<TurmaACC> turmaACCList = turmaACCRepository.findAll();
        assertThat(turmaACCList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTurmaACC() throws Exception {
        // Initialize the database
        turmaACCRepository.saveAndFlush(turmaACC);

        int databaseSizeBeforeDelete = turmaACCRepository.findAll().size();

        // Delete the turmaACC
        restTurmaACCMockMvc
            .perform(delete(ENTITY_API_URL_ID, turmaACC.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TurmaACC> turmaACCList = turmaACCRepository.findAll();
        assertThat(turmaACCList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
