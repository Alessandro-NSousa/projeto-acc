package br.com.acc.controle.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A TipoAtividade.
 */
@Entity
@Table(name = "tipo_atividade")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TipoAtividade implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nome", nullable = false)
    private String nome;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "descricao")
    private String descricao;

    @Column(name = "numero_pontos")
    private Integer numeroPontos;

    @Column(name = "data_criacao")
    private ZonedDateTime dataCriacao;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TipoAtividade id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public TipoAtividade nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public TipoAtividade descricao(String descricao) {
        this.setDescricao(descricao);
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getNumeroPontos() {
        return this.numeroPontos;
    }

    public TipoAtividade numeroPontos(Integer numeroPontos) {
        this.setNumeroPontos(numeroPontos);
        return this;
    }

    public void setNumeroPontos(Integer numeroPontos) {
        this.numeroPontos = numeroPontos;
    }

    public ZonedDateTime getDataCriacao() {
        return this.dataCriacao;
    }

    public TipoAtividade dataCriacao(ZonedDateTime dataCriacao) {
        this.setDataCriacao(dataCriacao);
        return this;
    }

    public void setDataCriacao(ZonedDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TipoAtividade)) {
            return false;
        }
        return id != null && id.equals(((TipoAtividade) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TipoAtividade{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", descricao='" + getDescricao() + "'" +
            ", numeroPontos=" + getNumeroPontos() +
            ", dataCriacao='" + getDataCriacao() + "'" +
            "}";
    }
}
