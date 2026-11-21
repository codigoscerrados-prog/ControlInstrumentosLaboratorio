package com.mycompany.myapp.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

/**
 * A Usuario.
 */
@Entity
@Table(name = "usuario")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Usuario implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "username", length = 50, nullable = false)
    private String username;

    @NotNull
    @Size(max = 100)
    @Column(name = "jhi_password", length = 100, nullable = false)
    private String password;

    @NotNull
    @Size(max = 100)
    @Column(name = "correo", length = 100, nullable = false)
    private String correo;

    @NotNull
    @Size(max = 20)
    @Column(name = "estado", length = 20, nullable = false)
    private String estado;

    @Column(name = "fecha_creacion")
    private Instant fechaCreacion;

    @Size(max = 50)
    @Column(name = "usuario_creacion", length = 50)
    private String usuarioCreacion;

    @Column(name = "fecha_modificacion")
    private Instant fechaModificacion;

    @Size(max = 50)
    @Column(name = "usuario_modificacion", length = 50)
    private String usuarioModificacion;

    @ManyToOne(optional = false)
    @NotNull
    private Rol rol;

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

    public String getUsername() {
        return this.username;
    }

    public Usuario username(String username) {
        this.setUsername(username);
        return this;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public Usuario password(String password) {
        this.setPassword(password);
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCorreo() {
        return this.correo;
    }

    public Usuario correo(String correo) {
        this.setCorreo(correo);
        return this;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getEstado() {
        return this.estado;
    }

    public Usuario estado(String estado) {
        this.setEstado(estado);
        return this;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Instant getFechaCreacion() {
        return this.fechaCreacion;
    }

    public Usuario fechaCreacion(Instant fechaCreacion) {
        this.setFechaCreacion(fechaCreacion);
        return this;
    }

    public void setFechaCreacion(Instant fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getUsuarioCreacion() {
        return this.usuarioCreacion;
    }

    public Usuario usuarioCreacion(String usuarioCreacion) {
        this.setUsuarioCreacion(usuarioCreacion);
        return this;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public Instant getFechaModificacion() {
        return this.fechaModificacion;
    }

    public Usuario fechaModificacion(Instant fechaModificacion) {
        this.setFechaModificacion(fechaModificacion);
        return this;
    }

    public void setFechaModificacion(Instant fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public String getUsuarioModificacion() {
        return this.usuarioModificacion;
    }

    public Usuario usuarioModificacion(String usuarioModificacion) {
        this.setUsuarioModificacion(usuarioModificacion);
        return this;
    }

    public void setUsuarioModificacion(String usuarioModificacion) {
        this.usuarioModificacion = usuarioModificacion;
    }

    public Rol getRol() {
        return this.rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public Usuario rol(Rol rol) {
        this.setRol(rol);
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
        return getId() != null && getId().equals(((Usuario) o).getId());
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
            ", username='" + getUsername() + "'" +
            ", password='" + getPassword() + "'" +
            ", correo='" + getCorreo() + "'" +
            ", estado='" + getEstado() + "'" +
            ", fechaCreacion='" + getFechaCreacion() + "'" +
            ", usuarioCreacion='" + getUsuarioCreacion() + "'" +
            ", fechaModificacion='" + getFechaModificacion() + "'" +
            ", usuarioModificacion='" + getUsuarioModificacion() + "'" +
            "}";
    }
}
