package br.com.acc.controle.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TurmaACC.
 */
@Entity
@Table(name = "turma_acc")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TurmaACC implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nome", nullable = false, unique = true)
    private String nome;

    @Column(name = "inicio")
    private LocalDate inicio;

    @Column(name = "termino")
    private LocalDate termino;

    @ManyToMany(mappedBy = "turmas")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "turmas", "cursos" }, allowSetters = true)
    private Set<Usuario> usuarios = new HashSet<>();

    @ManyToMany(mappedBy = "turmas")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "turmas", "usuarios" }, allowSetters = true)
    private Set<Curso> cursos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TurmaACC id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public TurmaACC nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getInicio() {
        return this.inicio;
    }

    public TurmaACC inicio(LocalDate inicio) {
        this.setInicio(inicio);
        return this;
    }

    public void setInicio(LocalDate inicio) {
        this.inicio = inicio;
    }

    public LocalDate getTermino() {
        return this.termino;
    }

    public TurmaACC termino(LocalDate termino) {
        this.setTermino(termino);
        return this;
    }

    public void setTermino(LocalDate termino) {
        this.termino = termino;
    }

    public Set<Usuario> getUsuarios() {
        return this.usuarios;
    }

    public void setUsuarios(Set<Usuario> usuarios) {
        if (this.usuarios != null) {
            this.usuarios.forEach(i -> i.removeTurmas(this));
        }
        if (usuarios != null) {
            usuarios.forEach(i -> i.addTurmas(this));
        }
        this.usuarios = usuarios;
    }

    public TurmaACC usuarios(Set<Usuario> usuarios) {
        this.setUsuarios(usuarios);
        return this;
    }

    public TurmaACC addUsuarios(Usuario usuario) {
        this.usuarios.add(usuario);
        usuario.getTurmas().add(this);
        return this;
    }

    public TurmaACC removeUsuarios(Usuario usuario) {
        this.usuarios.remove(usuario);
        usuario.getTurmas().remove(this);
        return this;
    }

    public Set<Curso> getCursos() {
        return this.cursos;
    }

    public void setCursos(Set<Curso> cursos) {
        if (this.cursos != null) {
            this.cursos.forEach(i -> i.removeTurmas(this));
        }
        if (cursos != null) {
            cursos.forEach(i -> i.addTurmas(this));
        }
        this.cursos = cursos;
    }

    public TurmaACC cursos(Set<Curso> cursos) {
        this.setCursos(cursos);
        return this;
    }

    public TurmaACC addCursos(Curso curso) {
        this.cursos.add(curso);
        curso.getTurmas().add(this);
        return this;
    }

    public TurmaACC removeCursos(Curso curso) {
        this.cursos.remove(curso);
        curso.getTurmas().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TurmaACC)) {
            return false;
        }
        return id != null && id.equals(((TurmaACC) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TurmaACC{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", inicio='" + getInicio() + "'" +
            ", termino='" + getTermino() + "'" +
            "}";
    }
}
