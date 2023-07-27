package br.com.acc.controle.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Curso.
 */
@Entity
@Table(name = "curso")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Curso implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nome_curso", nullable = false)
    private String nomeCurso;

    @Column(name = "sigla")
    private String sigla;

    @ManyToMany
    @JoinTable(
        name = "rel_curso__turmas",
        joinColumns = @JoinColumn(name = "curso_id"),
        inverseJoinColumns = @JoinColumn(name = "turmas_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "usuarios", "cursos" }, allowSetters = true)
    private Set<TurmaACC> turmas = new HashSet<>();

    @ManyToMany(mappedBy = "cursos")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "turmas", "cursos" }, allowSetters = true)
    private Set<Usuario> usuarios = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Curso id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeCurso() {
        return this.nomeCurso;
    }

    public Curso nomeCurso(String nomeCurso) {
        this.setNomeCurso(nomeCurso);
        return this;
    }

    public void setNomeCurso(String nomeCurso) {
        this.nomeCurso = nomeCurso;
    }

    public String getSigla() {
        return this.sigla;
    }

    public Curso sigla(String sigla) {
        this.setSigla(sigla);
        return this;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public Set<TurmaACC> getTurmas() {
        return this.turmas;
    }

    public void setTurmas(Set<TurmaACC> turmaACCS) {
        this.turmas = turmaACCS;
    }

    public Curso turmas(Set<TurmaACC> turmaACCS) {
        this.setTurmas(turmaACCS);
        return this;
    }

    public Curso addTurmas(TurmaACC turmaACC) {
        this.turmas.add(turmaACC);
        turmaACC.getCursos().add(this);
        return this;
    }

    public Curso removeTurmas(TurmaACC turmaACC) {
        this.turmas.remove(turmaACC);
        turmaACC.getCursos().remove(this);
        return this;
    }

    public Set<Usuario> getUsuarios() {
        return this.usuarios;
    }

    public void setUsuarios(Set<Usuario> usuarios) {
        if (this.usuarios != null) {
            this.usuarios.forEach(i -> i.removeCursos(this));
        }
        if (usuarios != null) {
            usuarios.forEach(i -> i.addCursos(this));
        }
        this.usuarios = usuarios;
    }

    public Curso usuarios(Set<Usuario> usuarios) {
        this.setUsuarios(usuarios);
        return this;
    }

    public Curso addUsuarios(Usuario usuario) {
        this.usuarios.add(usuario);
        usuario.getCursos().add(this);
        return this;
    }

    public Curso removeUsuarios(Usuario usuario) {
        this.usuarios.remove(usuario);
        usuario.getCursos().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Curso)) {
            return false;
        }
        return id != null && id.equals(((Curso) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Curso{" +
            "id=" + getId() +
            ", nomeCurso='" + getNomeCurso() + "'" +
            ", sigla='" + getSigla() + "'" +
            "}";
    }
}
