package br.com.acc.controle.domain;

import br.com.acc.controle.domain.enumeration.Perfil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Usuario.
 */
@Entity
@Table(name = "usuario")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nome", nullable = false)
    private String nome;

    @NotNull
    @Column(name = "login", nullable = false)
    private String login;

    @NotNull
    @Column(name = "senha", nullable = false)
    private String senha;

    @Column(name = "data_cadastro")
    private ZonedDateTime dataCadastro;

    @Column(name = "ultimo_acesso")
    private ZonedDateTime ultimoAcesso;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "perfil", nullable = false)
    private Perfil perfil;

    @ManyToMany
    @JoinTable(
        name = "rel_usuario__turmas",
        joinColumns = @JoinColumn(name = "usuario_id"),
        inverseJoinColumns = @JoinColumn(name = "turmas_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "usuarios", "cursos" }, allowSetters = true)
    private Set<TurmaACC> turmas = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_usuario__cursos",
        joinColumns = @JoinColumn(name = "usuario_id"),
        inverseJoinColumns = @JoinColumn(name = "cursos_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "turmas", "usuarios" }, allowSetters = true)
    private Set<Curso> cursos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Usuario id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public Usuario nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLogin() {
        return this.login;
    }

    public Usuario login(String login) {
        this.setLogin(login);
        return this;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return this.senha;
    }

    public Usuario senha(String senha) {
        this.setSenha(senha);
        return this;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public ZonedDateTime getDataCadastro() {
        return this.dataCadastro;
    }

    public Usuario dataCadastro(ZonedDateTime dataCadastro) {
        this.setDataCadastro(dataCadastro);
        return this;
    }

    public void setDataCadastro(ZonedDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public ZonedDateTime getUltimoAcesso() {
        return this.ultimoAcesso;
    }

    public Usuario ultimoAcesso(ZonedDateTime ultimoAcesso) {
        this.setUltimoAcesso(ultimoAcesso);
        return this;
    }

    public void setUltimoAcesso(ZonedDateTime ultimoAcesso) {
        this.ultimoAcesso = ultimoAcesso;
    }

    public Perfil getPerfil() {
        return this.perfil;
    }

    public Usuario perfil(Perfil perfil) {
        this.setPerfil(perfil);
        return this;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    public Set<TurmaACC> getTurmas() {
        return this.turmas;
    }

    public void setTurmas(Set<TurmaACC> turmaACCS) {
        this.turmas = turmaACCS;
    }

    public Usuario turmas(Set<TurmaACC> turmaACCS) {
        this.setTurmas(turmaACCS);
        return this;
    }

    public Usuario addTurmas(TurmaACC turmaACC) {
        this.turmas.add(turmaACC);
        turmaACC.getUsuarios().add(this);
        return this;
    }

    public Usuario removeTurmas(TurmaACC turmaACC) {
        this.turmas.remove(turmaACC);
        turmaACC.getUsuarios().remove(this);
        return this;
    }

    public Set<Curso> getCursos() {
        return this.cursos;
    }

    public void setCursos(Set<Curso> cursos) {
        this.cursos = cursos;
    }

    public Usuario cursos(Set<Curso> cursos) {
        this.setCursos(cursos);
        return this;
    }

    public Usuario addCursos(Curso curso) {
        this.cursos.add(curso);
        curso.getUsuarios().add(this);
        return this;
    }

    public Usuario removeCursos(Curso curso) {
        this.cursos.remove(curso);
        curso.getUsuarios().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Usuario)) {
            return false;
        }
        return id != null && id.equals(((Usuario) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Usuario{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", login='" + getLogin() + "'" +
            ", senha='" + getSenha() + "'" +
            ", dataCadastro='" + getDataCadastro() + "'" +
            ", ultimoAcesso='" + getUltimoAcesso() + "'" +
            ", perfil='" + getPerfil() + "'" +
            "}";
    }
}
