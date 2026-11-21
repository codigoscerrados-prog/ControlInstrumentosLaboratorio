package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

/**
 * A RecuperacionPassword.
 */
@Entity
@Table(name = "recuperacion_password")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RecuperacionPassword implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 200)
    @Column(name = "token", length = 200, nullable = false)
    private String token;

    @NotNull
    @Column(name = "fecha_solicitud", nullable = false)
    private Instant fechaSolicitud;

    @NotNull
    @Column(name = "fecha_expiracion", nullable = false)
    private Instant fechaExpiracion;

    @NotNull
    @Column(name = "usado", nullable = false)
    private Boolean usado;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "rol" }, allowSetters = true)
    private Usuario usuario;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public RecuperacionPassword id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return this.token;
    }

    public RecuperacionPassword token(String token) {
        this.setToken(token);
        return this;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Instant getFechaSolicitud() {
        return this.fechaSolicitud;
    }

    public RecuperacionPassword fechaSolicitud(Instant fechaSolicitud) {
        this.setFechaSolicitud(fechaSolicitud);
        return this;
    }

    public void setFechaSolicitud(Instant fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public Instant getFechaExpiracion() {
        return this.fechaExpiracion;
    }

    public RecuperacionPassword fechaExpiracion(Instant fechaExpiracion) {
        this.setFechaExpiracion(fechaExpiracion);
        return this;
    }

    public void setFechaExpiracion(Instant fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }

    public Boolean getUsado() {
        return this.usado;
    }

    public RecuperacionPassword usado(Boolean usado) {
        this.setUsado(usado);
        return this;
    }

    public void setUsado(Boolean usado) {
        this.usado = usado;
    }

    public Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public RecuperacionPassword usuario(Usuario usuario) {
        this.setUsuario(usuario);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RecuperacionPassword)) {
            return false;
        }
        return getId() != null && getId().equals(((RecuperacionPassword) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RecuperacionPassword{" +
            "id=" + getId() +
            ", token='" + getToken() + "'" +
            ", fechaSolicitud='" + getFechaSolicitud() + "'" +
            ", fechaExpiracion='" + getFechaExpiracion() + "'" +
            ", usado='" + getUsado() + "'" +
            "}";
    }
}
