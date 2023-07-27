package br.com.acc.controle.repository;

import br.com.acc.controle.domain.Usuario;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class UsuarioRepositoryWithBagRelationshipsImpl implements UsuarioRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Usuario> fetchBagRelationships(Optional<Usuario> usuario) {
        return usuario.map(this::fetchTurmas).map(this::fetchCursos);
    }

    @Override
    public Page<Usuario> fetchBagRelationships(Page<Usuario> usuarios) {
        return new PageImpl<>(fetchBagRelationships(usuarios.getContent()), usuarios.getPageable(), usuarios.getTotalElements());
    }

    @Override
    public List<Usuario> fetchBagRelationships(List<Usuario> usuarios) {
        return Optional.of(usuarios).map(this::fetchTurmas).map(this::fetchCursos).orElse(Collections.emptyList());
    }

    Usuario fetchTurmas(Usuario result) {
        return entityManager
            .createQuery("select usuario from Usuario usuario left join fetch usuario.turmas where usuario is :usuario", Usuario.class)
            .setParameter("usuario", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Usuario> fetchTurmas(List<Usuario> usuarios) {
        return entityManager
            .createQuery(
                "select distinct usuario from Usuario usuario left join fetch usuario.turmas where usuario in :usuarios",
                Usuario.class
            )
            .setParameter("usuarios", usuarios)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }

    Usuario fetchCursos(Usuario result) {
        return entityManager
            .createQuery("select usuario from Usuario usuario left join fetch usuario.cursos where usuario is :usuario", Usuario.class)
            .setParameter("usuario", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Usuario> fetchCursos(List<Usuario> usuarios) {
        return entityManager
            .createQuery(
                "select distinct usuario from Usuario usuario left join fetch usuario.cursos where usuario in :usuarios",
                Usuario.class
            )
            .setParameter("usuarios", usuarios)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
