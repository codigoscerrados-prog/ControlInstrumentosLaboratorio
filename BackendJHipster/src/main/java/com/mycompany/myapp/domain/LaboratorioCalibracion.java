package com.mycompany.myapp.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

/**
 * A LaboratorioCalibracion.
 */
@Entity
@Table(name = "laboratorio_calibracion")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LaboratorioCalibracion implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 150)
    @Column(name = "nombre", length = 150, nullable = false)
    private String nombre;

    @Size(max = 20)
    @Column(name = "ruc", length = 20)
    private String ruc;

    @Size(max = 250)
    @Column(name = "direccion", length = 250)
    private String direccion;

    @Size(max = 30)
    @Column(name = "telefono", length = 30)
    private String telefono;

    @Size(max = 100)
    @Column(name = "correo", length = 100)
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

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public LaboratorioCalibracion id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public LaboratorioCalibracion nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRuc() {
        return this.ruc;
    }

    public LaboratorioCalibracion ruc(String ruc) {
        this.setRuc(ruc);
        return this;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getDireccion() {
        return this.direccion;
    }

    public LaboratorioCalibracion direccion(String direccion) {
        this.setDireccion(direccion);
        return this;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return this.telefono;
    }

    public LaboratorioCalibracion telefono(String telefono) {
        this.setTelefono(telefono);
        return this;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return this.correo;
    }

    public LaboratorioCalibracion correo(String correo) {
        this.setCorreo(correo);
        return this;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getEstado() {
        return this.estado;
    }

    public LaboratorioCalibracion estado(String estado) {
        this.setEstado(estado);
        return this;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Instant getFechaCreacion() {
        return this.fechaCreacion;
    }

    public LaboratorioCalibracion fechaCreacion(Instant fechaCreacion) {
        this.setFechaCreacion(fechaCreacion);
        return this;
    }

    public void setFechaCreacion(Instant fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getUsuarioCreacion() {
        return this.usuarioCreacion;
    }

    public LaboratorioCalibracion usuarioCreacion(String usuarioCreacion) {
        this.setUsuarioCreacion(usuarioCreacion);
        return this;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public Instant getFechaModificacion() {
        return this.fechaModificacion;
    }

    public LaboratorioCalibracion fechaModificacion(Instant fechaModificacion) {
        this.setFechaModificacion(fechaModificacion);
        return this;
    }

    public void setFechaModificacion(Instant fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public String getUsuarioModificacion() {
        return this.usuarioModificacion;
    }

    public LaboratorioCalibracion usuarioModificacion(String usuarioModificacion) {
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
        if (!(o instanceof LaboratorioCalibracion)) {
            return false;
        }
        return getId() != null && getId().equals(((LaboratorioCalibracion) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LaboratorioCalibracion{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", ruc='" + getRuc() + "'" +
            ", direccion='" + getDireccion() + "'" +
            ", telefono='" + getTelefono() + "'" +
            ", correo='" + getCorreo() + "'" +
            ", estado='" + getEstado() + "'" +
            ", fechaCreacion='" + getFechaCreacion() + "'" +
            ", usuarioCreacion='" + getUsuarioCreacion() + "'" +
            ", fechaModificacion='" + getFechaModificacion() + "'" +
            ", usuarioModificacion='" + getUsuarioModificacion() + "'" +
            "}";
    }
}
