package br.com.acc.controle.domain;

import br.com.acc.controle.domain.enumeration.Modalidade;
import br.com.acc.controle.domain.enumeration.StatusCertificado;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A Certificado.
 */
@Entity
@Table(name = "certificado")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Certificado implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "titulo")
    private String titulo;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "descricao")
    private String descricao;

    @Column(name = "data_envio")
    private ZonedDateTime dataEnvio;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "observacao")
    private String observacao;

    @Enumerated(EnumType.STRING)
    @Column(name = "modalidade")
    private Modalidade modalidade;

    @Column(name = "ch_cuprida")
    private Integer chCuprida;

    @Column(name = "pontuacao")
    private Integer pontuacao;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusCertificado status;

    @Column(name = "caminho_arquivo")
    private String caminhoArquivo;

    @ManyToOne
    @JsonIgnoreProperties(value = { "turmas", "cursos" }, allowSetters = true)
    private Usuario usuario;

    @ManyToOne
    @JsonIgnoreProperties(value = { "usuarios", "cursos" }, allowSetters = true)
    private TurmaACC turmaAcc;

    @ManyToOne
    private TipoAtividade tipoAtividade;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Certificado id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return this.titulo;
    }

    public Certificado titulo(String titulo) {
        this.setTitulo(titulo);
        return this;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public Certificado descricao(String descricao) {
        this.setDescricao(descricao);
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public ZonedDateTime getDataEnvio() {
        return this.dataEnvio;
    }

    public Certificado dataEnvio(ZonedDateTime dataEnvio) {
        this.setDataEnvio(dataEnvio);
        return this;
    }

    public void setDataEnvio(ZonedDateTime dataEnvio) {
        this.dataEnvio = dataEnvio;
    }

    public String getObservacao() {
        return this.observacao;
    }

    public Certificado observacao(String observacao) {
        this.setObservacao(observacao);
        return this;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Modalidade getModalidade() {
        return this.modalidade;
    }

    public Certificado modalidade(Modalidade modalidade) {
        this.setModalidade(modalidade);
        return this;
    }

    public void setModalidade(Modalidade modalidade) {
        this.modalidade = modalidade;
    }

    public Integer getChCuprida() {
        return this.chCuprida;
    }

    public Certificado chCuprida(Integer chCuprida) {
        this.setChCuprida(chCuprida);
        return this;
    }

    public void setChCuprida(Integer chCuprida) {
        this.chCuprida = chCuprida;
    }

    public Integer getPontuacao() {
        return this.pontuacao;
    }

    public Certificado pontuacao(Integer pontuacao) {
        this.setPontuacao(pontuacao);
        return this;
    }

    public void setPontuacao(Integer pontuacao) {
        this.pontuacao = pontuacao;
    }

    public StatusCertificado getStatus() {
        return this.status;
    }

    public Certificado status(StatusCertificado status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(StatusCertificado status) {
        this.status = status;
    }

    public String getCaminhoArquivo() {
        return this.caminhoArquivo;
    }

    public Certificado caminhoArquivo(String caminhoArquivo) {
        this.setCaminhoArquivo(caminhoArquivo);
        return this;
    }

    public void setCaminhoArquivo(String caminhoArquivo) {
        this.caminhoArquivo = caminhoArquivo;
    }

    public Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Certificado usuario(Usuario usuario) {
        this.setUsuario(usuario);
        return this;
    }

    public TurmaACC getTurmaAcc() {
        return this.turmaAcc;
    }

    public void setTurmaAcc(TurmaACC turmaACC) {
        this.turmaAcc = turmaACC;
    }

    public Certificado turmaAcc(TurmaACC turmaACC) {
        this.setTurmaAcc(turmaACC);
        return this;
    }

    public TipoAtividade getTipoAtividade() {
        return this.tipoAtividade;
    }

    public void setTipoAtividade(TipoAtividade tipoAtividade) {
        this.tipoAtividade = tipoAtividade;
    }

    public Certificado tipoAtividade(TipoAtividade tipoAtividade) {
        this.setTipoAtividade(tipoAtividade);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Certificado)) {
            return false;
        }
        return id != null && id.equals(((Certificado) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Certificado{" +
            "id=" + getId() +
            ", titulo='" + getTitulo() + "'" +
            ", descricao='" + getDescricao() + "'" +
            ", dataEnvio='" + getDataEnvio() + "'" +
            ", observacao='" + getObservacao() + "'" +
            ", modalidade='" + getModalidade() + "'" +
            ", chCuprida=" + getChCuprida() +
            ", pontuacao=" + getPontuacao() +
            ", status='" + getStatus() + "'" +
            ", caminhoArquivo='" + getCaminhoArquivo() + "'" +
            "}";
    }
}
