package br.com.acc.controle.web.rest;

import br.com.acc.controle.domain.TipoAtividade;
import br.com.acc.controle.repository.TipoAtividadeRepository;
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
 * REST controller for managing {@link br.com.acc.controle.domain.TipoAtividade}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class TipoAtividadeResource {

    private final Logger log = LoggerFactory.getLogger(TipoAtividadeResource.class);

    private static final String ENTITY_NAME = "tipoAtividade";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TipoAtividadeRepository tipoAtividadeRepository;

    public TipoAtividadeResource(TipoAtividadeRepository tipoAtividadeRepository) {
        this.tipoAtividadeRepository = tipoAtividadeRepository;
    }

    /**
     * {@code POST  /tipo-atividades} : Create a new tipoAtividade.
     *
     * @param tipoAtividade the tipoAtividade to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tipoAtividade, or with status {@code 400 (Bad Request)} if the tipoAtividade has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tipo-atividades")
    public ResponseEntity<TipoAtividade> createTipoAtividade(@Valid @RequestBody TipoAtividade tipoAtividade) throws URISyntaxException {
        log.debug("REST request to save TipoAtividade : {}", tipoAtividade);
        if (tipoAtividade.getId() != null) {
            throw new BadRequestAlertException("A new tipoAtividade cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TipoAtividade result = tipoAtividadeRepository.save(tipoAtividade);
        return ResponseEntity
            .created(new URI("/api/tipo-atividades/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tipo-atividades/:id} : Updates an existing tipoAtividade.
     *
     * @param id the id of the tipoAtividade to save.
     * @param tipoAtividade the tipoAtividade to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tipoAtividade,
     * or with status {@code 400 (Bad Request)} if the tipoAtividade is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tipoAtividade couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tipo-atividades/{id}")
    public ResponseEntity<TipoAtividade> updateTipoAtividade(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TipoAtividade tipoAtividade
    ) throws URISyntaxException {
        log.debug("REST request to update TipoAtividade : {}, {}", id, tipoAtividade);
        if (tipoAtividade.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tipoAtividade.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tipoAtividadeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TipoAtividade result = tipoAtividadeRepository.save(tipoAtividade);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tipoAtividade.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tipo-atividades/:id} : Partial updates given fields of an existing tipoAtividade, field will ignore if it is null
     *
     * @param id the id of the tipoAtividade to save.
     * @param tipoAtividade the tipoAtividade to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tipoAtividade,
     * or with status {@code 400 (Bad Request)} if the tipoAtividade is not valid,
     * or with status {@code 404 (Not Found)} if the tipoAtividade is not found,
     * or with status {@code 500 (Internal Server Error)} if the tipoAtividade couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tipo-atividades/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TipoAtividade> partialUpdateTipoAtividade(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TipoAtividade tipoAtividade
    ) throws URISyntaxException {
        log.debug("REST request to partial update TipoAtividade partially : {}, {}", id, tipoAtividade);
        if (tipoAtividade.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tipoAtividade.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tipoAtividadeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TipoAtividade> result = tipoAtividadeRepository
            .findById(tipoAtividade.getId())
            .map(existingTipoAtividade -> {
                if (tipoAtividade.getNome() != null) {
                    existingTipoAtividade.setNome(tipoAtividade.getNome());
                }
                if (tipoAtividade.getDescricao() != null) {
                    existingTipoAtividade.setDescricao(tipoAtividade.getDescricao());
                }
                if (tipoAtividade.getNumeroPontos() != null) {
                    existingTipoAtividade.setNumeroPontos(tipoAtividade.getNumeroPontos());
                }
                if (tipoAtividade.getDataCriacao() != null) {
                    existingTipoAtividade.setDataCriacao(tipoAtividade.getDataCriacao());
                }

                return existingTipoAtividade;
            })
            .map(tipoAtividadeRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tipoAtividade.getId().toString())
        );
    }

    /**
     * {@code GET  /tipo-atividades} : get all the tipoAtividades.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tipoAtividades in body.
     */
    @GetMapping("/tipo-atividades")
    public List<TipoAtividade> getAllTipoAtividades() {
        log.debug("REST request to get all TipoAtividades");
        return tipoAtividadeRepository.findAll();
    }

    /**
     * {@code GET  /tipo-atividades/:id} : get the "id" tipoAtividade.
     *
     * @param id the id of the tipoAtividade to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tipoAtividade, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tipo-atividades/{id}")
    public ResponseEntity<TipoAtividade> getTipoAtividade(@PathVariable Long id) {
        log.debug("REST request to get TipoAtividade : {}", id);
        Optional<TipoAtividade> tipoAtividade = tipoAtividadeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(tipoAtividade);
    }

    /**
     * {@code DELETE  /tipo-atividades/:id} : delete the "id" tipoAtividade.
     *
     * @param id the id of the tipoAtividade to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tipo-atividades/{id}")
    public ResponseEntity<Void> deleteTipoAtividade(@PathVariable Long id) {
        log.debug("REST request to delete TipoAtividade : {}", id);
        tipoAtividadeRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
