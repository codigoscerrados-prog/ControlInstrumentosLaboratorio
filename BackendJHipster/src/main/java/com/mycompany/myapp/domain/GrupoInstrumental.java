package com.mycompany.myapp.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

/**
 * A GrupoInstrumental.
 */
@Entity
@Table(name = "grupo_instrumental")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GrupoInstrumental implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 20)
    @Column(name = "codigo", length = 20, nullable = false)
    private String codigo;

    @NotNull
    @Size(max = 100)
    @Column(name = "nombre", length = 100, nullable = false)
    private String nombre;

    @Size(max = 250)
    @Column(name = "descripcion", length = 250)
    private String descripcion;

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

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public GrupoInstrumental id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return this.codigo;
    }

    public GrupoInstrumental codigo(String codigo) {
        this.setCodigo(codigo);
        return this;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return this.nombre;
    }

    public GrupoInstrumental nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public GrupoInstrumental descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Instant getFechaCreacion() {
        return this.fechaCreacion;
    }

    public GrupoInstrumental fechaCreacion(Instant fechaCreacion) {
        this.setFechaCreacion(fechaCreacion);
        return this;
    }

    public void setFechaCreacion(Instant fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getUsuarioCreacion() {
        return this.usuarioCreacion;
    }

    public GrupoInstrumental usuarioCreacion(String usuarioCreacion) {
        this.setUsuarioCreacion(usuarioCreacion);
        return this;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public Instant getFechaModificacion() {
        return this.fechaModificacion;
    }

    public GrupoInstrumental fechaModificacion(Instant fechaModificacion) {
        this.setFechaModificacion(fechaModificacion);
        return this;
    }

    public void setFechaModificacion(Instant fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public String getUsuarioModificacion() {
        return this.usuarioModificacion;
    }

    public GrupoInstrumental usuarioModificacion(String usuarioModificacion) {
        this.setUsuarioModificacion(usuarioModificacion);
        return this;
    }

    public void setUsuarioModificacion(String usuarioModificacion) {
        this.usuarioModificacion = usuarioModificacion;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GrupoInstrumental)) {
            return false;
        }
        return getId() != null && getId().equals(((GrupoInstrumental) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GrupoInstrumental{" +
            "id=" + getId() +
            ", codigo='" + getCodigo() + "'" +
            ", nombre='" + getNombre() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", fechaCreacion='" + getFechaCreacion() + "'" +
            ", usuarioCreacion='" + getUsuarioCreacion() + "'" +
            ", fechaModificacion='" + getFechaModificacion() + "'" +
            ", usuarioModificacion='" + getUsuarioModificacion() + "'" +
            "}";
    }
}
