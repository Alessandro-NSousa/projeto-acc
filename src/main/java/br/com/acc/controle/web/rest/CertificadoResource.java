package br.com.acc.controle.web.rest;

import br.com.acc.controle.domain.Certificado;
import br.com.acc.controle.domain.enumeration.StatusCertificado;
import br.com.acc.controle.repository.CertificadoRepository;
import br.com.acc.controle.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link br.com.acc.controle.domain.Certificado}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CertificadoResource {

    private final Logger log = LoggerFactory.getLogger(CertificadoResource.class);

    private static final String ENTITY_NAME = "certificado";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CertificadoRepository certificadoRepository;

    public CertificadoResource(CertificadoRepository certificadoRepository) {
        this.certificadoRepository = certificadoRepository;
    }

    /**
     * {@code POST  /certificados} : Create a new certificado.
     *
     * @param certificado the certificado to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new certificado, or with status {@code 400 (Bad Request)} if the certificado has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/certificados")
    public ResponseEntity<Certificado> createCertificado(@RequestBody Certificado certificado) throws URISyntaxException {
        log.debug("REST request to save Certificado : {}", certificado);
        if (certificado.getId() != null) {
            throw new BadRequestAlertException("A new certificado cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Certificado result = certificadoRepository.save(certificado);
        return ResponseEntity
            .created(new URI("/api/certificados/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /certificados/:id} : Updates an existing certificado.
     *
     * @param id the id of the certificado to save.
     * @param certificado the certificado to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated certificado,
     * or with status {@code 400 (Bad Request)} if the certificado is not valid,
     * or with status {@code 500 (Internal Server Error)} if the certificado couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/certificados/{id}")
    public ResponseEntity<Certificado> updateCertificado(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Certificado certificado
    ) throws URISyntaxException {
        log.debug("REST request to update Certificado : {}, {}", id, certificado);
        if (certificado.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, certificado.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!certificadoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Certificado result = certificadoRepository.save(certificado);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, certificado.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /certificados/:id} : Partial updates given fields of an existing certificado, field will ignore if it is null
     *
     * @param id the id of the certificado to save.
     * @param certificado the certificado to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated certificado,
     * or with status {@code 400 (Bad Request)} if the certificado is not valid,
     * or with status {@code 404 (Not Found)} if the certificado is not found,
     * or with status {@code 500 (Internal Server Error)} if the certificado couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/certificados/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Certificado> partialUpdateCertificado(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Certificado certificado
    ) throws URISyntaxException {
        log.debug("REST request to partial update Certificado partially : {}, {}", id, certificado);
        if (certificado.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, certificado.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!certificadoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Certificado> result = certificadoRepository
            .findById(certificado.getId())
            .map(existingCertificado -> {
                if (certificado.getTitulo() != null) {
                    existingCertificado.setTitulo(certificado.getTitulo());
                }
                if (certificado.getDescricao() != null) {
                    existingCertificado.setDescricao(certificado.getDescricao());
                }
                if (certificado.getDataEnvio() != null) {
                    existingCertificado.setDataEnvio(certificado.getDataEnvio());
                }
                if (certificado.getObservacao() != null) {
                    existingCertificado.setObservacao(certificado.getObservacao());
                }
                if (certificado.getModalidade() != null) {
                    existingCertificado.setModalidade(certificado.getModalidade());
                }
                if (certificado.getChCuprida() != null) {
                    existingCertificado.setChCuprida(certificado.getChCuprida());
                }
                if (certificado.getPontuacao() != null) {
                    existingCertificado.setPontuacao(certificado.getPontuacao());
                }
                if (certificado.getStatus() != null) {
                    existingCertificado.setStatus(certificado.getStatus());
                }
                if (certificado.getCaminhoArquivo() != null) {
                    existingCertificado.setCaminhoArquivo(certificado.getCaminhoArquivo());
                }

                return existingCertificado;
            })
            .map(certificadoRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, certificado.getId().toString())
        );
    }

    /**
     * {@code GET  /certificados} : get all the certificados.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of certificados in body.
     */
    @GetMapping("/certificados")
    public List<Certificado> getAllCertificados(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Certificados");
        return certificadoRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /certificados/:id} : get the "id" certificado.
     *
     * @param id the id of the certificado to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the certificado, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/certificados/{id}")
    public ResponseEntity<Certificado> getCertificado(@PathVariable Long id) {
        log.debug("REST request to get Certificado : {}", id);
        Optional<Certificado> certificado = certificadoRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(certificado);
    }

    @GetMapping("/certificados/total/{status}")
    public ResponseEntity<Long> getTotalCertificadoPorStatus(@PathVariable StatusCertificado status) {
        Optional<Long> total = Optional.ofNullable(certificadoRepository.selectTotalPorStatus(status));
        return ResponseUtil.wrapOrNotFound(total);
    }

    /**
     * {@code DELETE  /certificados/:id} : delete the "id" certificado.
     *
     * @param id the id of the certificado to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/certificados/{id}")
    public ResponseEntity<Void> deleteCertificado(@PathVariable Long id) {
        log.debug("REST request to delete Certificado : {}", id);
        certificadoRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
