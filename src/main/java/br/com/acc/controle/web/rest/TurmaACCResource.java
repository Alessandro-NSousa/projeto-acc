package br.com.acc.controle.web.rest;

import br.com.acc.controle.domain.TurmaACC;
import br.com.acc.controle.repository.TurmaACCRepository;
import br.com.acc.controle.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link br.com.acc.controle.domain.TurmaACC}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class TurmaACCResource {

    private final Logger log = LoggerFactory.getLogger(TurmaACCResource.class);

    private static final String ENTITY_NAME = "turmaACC";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TurmaACCRepository turmaACCRepository;

    public TurmaACCResource(TurmaACCRepository turmaACCRepository) {
        this.turmaACCRepository = turmaACCRepository;
    }

    /**
     * {@code POST  /turma-accs} : Create a new turmaACC.
     *
     * @param turmaACC the turmaACC to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new turmaACC, or with status {@code 400 (Bad Request)} if the turmaACC has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/turma-accs")
    public ResponseEntity<TurmaACC> createTurmaACC(@Valid @RequestBody TurmaACC turmaACC) throws URISyntaxException {
        log.debug("REST request to save TurmaACC : {}", turmaACC);
        if (turmaACC.getId() != null) {
            throw new BadRequestAlertException("A new turmaACC cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TurmaACC result = turmaACCRepository.save(turmaACC);
        return ResponseEntity
            .created(new URI("/api/turma-accs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /turma-accs/:id} : Updates an existing turmaACC.
     *
     * @param id the id of the turmaACC to save.
     * @param turmaACC the turmaACC to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated turmaACC,
     * or with status {@code 400 (Bad Request)} if the turmaACC is not valid,
     * or with status {@code 500 (Internal Server Error)} if the turmaACC couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/turma-accs/{id}")
    public ResponseEntity<TurmaACC> updateTurmaACC(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TurmaACC turmaACC
    ) throws URISyntaxException {
        log.debug("REST request to update TurmaACC : {}, {}", id, turmaACC);
        if (turmaACC.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, turmaACC.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!turmaACCRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TurmaACC result = turmaACCRepository.save(turmaACC);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, turmaACC.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /turma-accs/:id} : Partial updates given fields of an existing turmaACC, field will ignore if it is null
     *
     * @param id the id of the turmaACC to save.
     * @param turmaACC the turmaACC to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated turmaACC,
     * or with status {@code 400 (Bad Request)} if the turmaACC is not valid,
     * or with status {@code 404 (Not Found)} if the turmaACC is not found,
     * or with status {@code 500 (Internal Server Error)} if the turmaACC couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/turma-accs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TurmaACC> partialUpdateTurmaACC(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TurmaACC turmaACC
    ) throws URISyntaxException {
        log.debug("REST request to partial update TurmaACC partially : {}, {}", id, turmaACC);
        if (turmaACC.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, turmaACC.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!turmaACCRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TurmaACC> result = turmaACCRepository
            .findById(turmaACC.getId())
            .map(existingTurmaACC -> {
                if (turmaACC.getNome() != null) {
                    existingTurmaACC.setNome(turmaACC.getNome());
                }
                if (turmaACC.getInicio() != null) {
                    existingTurmaACC.setInicio(turmaACC.getInicio());
                }
                if (turmaACC.getTermino() != null) {
                    existingTurmaACC.setTermino(turmaACC.getTermino());
                }

                return existingTurmaACC;
            })
            .map(turmaACCRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, turmaACC.getId().toString())
        );
    }

    /**
     * {@code GET  /turma-accs} : get all the turmaACCS.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of turmaACCS in body.
     */
    @GetMapping("/turma-accs")
    public List<TurmaACC> getAllTurmaACCS() {
        log.debug("REST request to get all TurmaACCS");
        return turmaACCRepository.findAll();
    }

    /**
     * {@code GET  /turma-accs/:id} : get the "id" turmaACC.
     *
     * @param id the id of the turmaACC to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the turmaACC, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/turma-accs/{id}")
    public ResponseEntity<TurmaACC> getTurmaACC(@PathVariable Long id) {
        log.debug("REST request to get TurmaACC : {}", id);
        Optional<TurmaACC> turmaACC = turmaACCRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(turmaACC);
    }

    /**
     * {@code DELETE  /turma-accs/:id} : delete the "id" turmaACC.
     *
     * @param id the id of the turmaACC to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/turma-accs/{id}")
    public ResponseEntity<Void> deleteTurmaACC(@PathVariable Long id) {
        log.debug("REST request to delete TurmaACC : {}", id);
        turmaACCRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
