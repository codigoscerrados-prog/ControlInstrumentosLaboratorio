package com.mycompany.myapp.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

/**
 * A Activo.
 */
@Entity
@Table(name = "activo")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Activo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 40)
    @Column(name = "codigo", length = 40, nullable = false)
    private String codigo;

    @NotNull
    @Size(max = 150)
    @Column(name = "nombre", length = 150, nullable = false)
    private String nombre;

    @Size(max = 100)
    @Column(name = "marca", length = 100)
    private String marca;

    @Size(max = 100)
    @Column(name = "modelo", length = 100)
    private String modelo;

    @Size(max = 100)
    @Column(name = "numero_serie", length = 100)
    private String numeroSerie;

    @Size(max = 100)
    @Column(name = "rango", length = 100)
    private String rango;

    @Size(max = 100)
    @Column(name = "resolucion", length = 100)
    private String resolucion;

    @Size(max = 150)
    @Column(name = "ubicacion", length = 150)
    private String ubicacion;

    @NotNull
    @Size(max = 50)
    @Column(name = "estado", length = 50, nullable = false)
    private String estado;

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
    private GrupoInstrumental grupo;

    @ManyToOne(optional = false)
    @NotNull
    private TipoInstrumento tipoInstrumento;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Activo id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return this.codigo;
    }

    public Activo codigo(String codigo) {
        this.setCodigo(codigo);
        return this;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Activo nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMarca() {
        return this.marca;
    }

    public Activo marca(String marca) {
        this.setMarca(marca);
        return this;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return this.modelo;
    }

    public Activo modelo(String modelo) {
        this.setModelo(modelo);
        return this;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getNumeroSerie() {
        return this.numeroSerie;
    }

    public Activo numeroSerie(String numeroSerie) {
        this.setNumeroSerie(numeroSerie);
        return this;
    }

    public void setNumeroSerie(String numeroSerie) {
        this.numeroSerie = numeroSerie;
    }

    public String getRango() {
        return this.rango;
    }

    public Activo rango(String rango) {
        this.setRango(rango);
        return this;
    }

    public void setRango(String rango) {
        this.rango = rango;
    }

    public String getResolucion() {
        return this.resolucion;
    }

    public Activo resolucion(String resolucion) {
        this.setResolucion(resolucion);
        return this;
    }

    public void setResolucion(String resolucion) {
        this.resolucion = resolucion;
    }

    public String getUbicacion() {
        return this.ubicacion;
    }

    public Activo ubicacion(String ubicacion) {
        this.setUbicacion(ubicacion);
        return this;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getEstado() {
        return this.estado;
    }

    public Activo estado(String estado) {
        this.setEstado(estado);
        return this;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getObservaciones() {
        return this.observaciones;
    }

    public Activo observaciones(String observaciones) {
        this.setObservaciones(observaciones);
        return this;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Instant getFechaCreacion() {
        return this.fechaCreacion;
    }

    public Activo fechaCreacion(Instant fechaCreacion) {
        this.setFechaCreacion(fechaCreacion);
        return this;
    }

    public void setFechaCreacion(Instant fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getUsuarioCreacion() {
        return this.usuarioCreacion;
    }

    public Activo usuarioCreacion(String usuarioCreacion) {
        this.setUsuarioCreacion(usuarioCreacion);
        return this;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public Instant getFechaModificacion() {
        return this.fechaModificacion;
    }

    public Activo fechaModificacion(Instant fechaModificacion) {
        this.setFechaModificacion(fechaModificacion);
        return this;
    }

    public void setFechaModificacion(Instant fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public String getUsuarioModificacion() {
        return this.usuarioModificacion;
    }

    public Activo usuarioModificacion(String usuarioModificacion) {
        this.setUsuarioModificacion(usuarioModificacion);
        return this;
    }

    public void setUsuarioModificacion(String usuarioModificacion) {
        this.usuarioModificacion = usuarioModificacion;
    }

    public GrupoInstrumental getGrupo() {
        return this.grupo;
    }

    public void setGrupo(GrupoInstrumental grupoInstrumental) {
        this.grupo = grupoInstrumental;
    }

    public Activo grupo(GrupoInstrumental grupoInstrumental) {
        this.setGrupo(grupoInstrumental);
        return this;
    }

    public TipoInstrumento getTipoInstrumento() {
        return this.tipoInstrumento;
    }

    public void setTipoInstrumento(TipoInstrumento tipoInstrumento) {
        this.tipoInstrumento = tipoInstrumento;
    }

    public Activo tipoInstrumento(TipoInstrumento tipoInstrumento) {
        this.setTipoInstrumento(tipoInstrumento);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Activo)) {
            return false;
        }
        return getId() != null && getId().equals(((Activo) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Activo{" +
            "id=" + getId() +
            ", codigo='" + getCodigo() + "'" +
            ", nombre='" + getNombre() + "'" +
            ", marca='" + getMarca() + "'" +
            ", modelo='" + getModelo() + "'" +
            ", numeroSerie='" + getNumeroSerie() + "'" +
            ", rango='" + getRango() + "'" +
            ", resolucion='" + getResolucion() + "'" +
            ", ubicacion='" + getUbicacion() + "'" +
            ", estado='" + getEstado() + "'" +
            ", observaciones='" + getObservaciones() + "'" +
            ", fechaCreacion='" + getFechaCreacion() + "'" +
            ", usuarioCreacion='" + getUsuarioCreacion() + "'" +
            ", fechaModificacion='" + getFechaModificacion() + "'" +
            ", usuarioModificacion='" + getUsuarioModificacion() + "'" +
            "}";
    }
}
