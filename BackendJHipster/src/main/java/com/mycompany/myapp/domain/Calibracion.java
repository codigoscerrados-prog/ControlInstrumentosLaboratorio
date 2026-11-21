package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;

/**
 * A Calibracion.
 */
@Entity
@Table(name = "calibracion")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Calibracion implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "fecha_envio")
    private LocalDate fechaEnvio;

    @Column(name = "fecha_calibracion")
    private LocalDate fechaCalibracion;

    @Column(name = "fecha_retorno")
    private LocalDate fechaRetorno;

    @Column(name = "fecha_vencimiento")
    private LocalDate fechaVencimiento;

    @Size(max = 100)
    @Column(name = "numero_certificado", length = 100)
    private String numeroCertificado;

    @Size(max = 50)
    @Column(name = "resultado", length = 50)
    private String resultado;

    @Size(max = 500)
    @Column(name = "observaciones", length = 500)
    private String observaciones;

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
    @JsonIgnoreProperties(value = { "grupo", "tipoInstrumento" }, allowSetters = true)
    private Activo activo;

    @ManyToOne(optional = false)
    @NotNull
    private LaboratorioCalibracion laboratorio;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Calibracion id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFechaEnvio() {
        return this.fechaEnvio;
    }

    public Calibracion fechaEnvio(LocalDate fechaEnvio) {
        this.setFechaEnvio(fechaEnvio);
        return this;
    }

    public void setFechaEnvio(LocalDate fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public LocalDate getFechaCalibracion() {
        return this.fechaCalibracion;
    }

    public Calibracion fechaCalibracion(LocalDate fechaCalibracion) {
        this.setFechaCalibracion(fechaCalibracion);
        return this;
    }

    public void setFechaCalibracion(LocalDate fechaCalibracion) {
        this.fechaCalibracion = fechaCalibracion;
    }

    public LocalDate getFechaRetorno() {
        return this.fechaRetorno;
    }

    public Calibracion fechaRetorno(LocalDate fechaRetorno) {
        this.setFechaRetorno(fechaRetorno);
        return this;
    }

    public void setFechaRetorno(LocalDate fechaRetorno) {
        this.fechaRetorno = fechaRetorno;
    }

    public LocalDate getFechaVencimiento() {
        return this.fechaVencimiento;
    }

    public Calibracion fechaVencimiento(LocalDate fechaVencimiento) {
        this.setFechaVencimiento(fechaVencimiento);
        return this;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public String getNumeroCertificado() {
        return this.numeroCertificado;
    }

    public Calibracion numeroCertificado(String numeroCertificado) {
        this.setNumeroCertificado(numeroCertificado);
        return this;
    }

    public void setNumeroCertificado(String numeroCertificado) {
        this.numeroCertificado = numeroCertificado;
    }

    public String getResultado() {
        return this.resultado;
    }

    public Calibracion resultado(String resultado) {
        this.setResultado(resultado);
        return this;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public String getObservaciones() {
        return this.observaciones;
    }

    public Calibracion observaciones(String observaciones) {
        this.setObservaciones(observaciones);
        return this;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Instant getFechaCreacion() {
        return this.fechaCreacion;
    }

    public Calibracion fechaCreacion(Instant fechaCreacion) {
        this.setFechaCreacion(fechaCreacion);
        return this;
    }

    public void setFechaCreacion(Instant fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getUsuarioCreacion() {
        return this.usuarioCreacion;
    }

    public Calibracion usuarioCreacion(String usuarioCreacion) {
        this.setUsuarioCreacion(usuarioCreacion);
        return this;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public Instant getFechaModificacion() {
        return this.fechaModificacion;
    }

    public Calibracion fechaModificacion(Instant fechaModificacion) {
        this.setFechaModificacion(fechaModificacion);
        return this;
    }

    public void setFechaModificacion(Instant fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public String getUsuarioModificacion() {
        return this.usuarioModificacion;
    }

    public Calibracion usuarioModificacion(String usuarioModificacion) {
        this.setUsuarioModificacion(usuarioModificacion);
        return this;
    }

    public void setUsuarioModificacion(String usuarioModificacion) {
        this.usuarioModificacion = usuarioModificacion;
    }

    public Activo getActivo() {
        return this.activo;
    }

    public void setActivo(Activo activo) {
        this.activo = activo;
    }

    public Calibracion activo(Activo activo) {
        this.setActivo(activo);
        return this;
    }

    public LaboratorioCalibracion getLaboratorio() {
        return this.laboratorio;
    }

    public void setLaboratorio(LaboratorioCalibracion laboratorioCalibracion) {
        this.laboratorio = laboratorioCalibracion;
    }

    public Calibracion laboratorio(LaboratorioCalibracion laboratorioCalibracion) {
        this.setLaboratorio(laboratorioCalibracion);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Calibracion)) {
            return false;
        }
        return getId() != null && getId().equals(((Calibracion) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Calibracion{" +
            "id=" + getId() +
            ", fechaEnvio='" + getFechaEnvio() + "'" +
            ", fechaCalibracion='" + getFechaCalibracion() + "'" +
            ", fechaRetorno='" + getFechaRetorno() + "'" +
            ", fechaVencimiento='" + getFechaVencimiento() + "'" +
            ", numeroCertificado='" + getNumeroCertificado() + "'" +
            ", resultado='" + getResultado() + "'" +
            ", observaciones='" + getObservaciones() + "'" +
            ", fechaCreacion='" + getFechaCreacion() + "'" +
            ", usuarioCreacion='" + getUsuarioCreacion() + "'" +
            ", fechaModificacion='" + getFechaModificacion() + "'" +
            ", usuarioModificacion='" + getUsuarioModificacion() + "'" +
            "}";
    }
}
