using System.Text.Json.Serialization;

namespace BlazorApp1.Components.Models;

public class ActivoUi
{
    public long Id { get; set; }

    public string Codigo { get; set; } = "";

    public string Nombre { get; set; } = "";

    public string Marca { get; set; } = "";

    public string Modelo { get; set; } = "";

    public string NumeroSerie { get; set; } = "";

    public string Rango { get; set; } = "";

    public string Resolucion { get; set; } = "";

    public string Ubicacion { get; set; } = "";

    public string Estado { get; set; } = "OPERATIVO";

    public string Observaciones { get; set; } = "";

    // Relaciones reales provenientes de JHipster
    public GrupoInstrumentalUi? Grupo { get; set; }

    public TipoInstrumentoUi? TipoInstrumento { get; set; }

    // Los conservamos temporalmente para los formularios actuales.
    // Más adelante los reemplazaremos por las relaciones reales.

    [JsonIgnore]
    public long GrupoId { get; set; }

    [JsonIgnore]
    public string GrupoNombre { get; set; } = "";

    [JsonIgnore]
    public long TipoInstrumentoId { get; set; }

    [JsonIgnore]
    public string TipoInstrumentoNombre { get; set; } = "";

    public DateTimeOffset? FechaCreacion { get; set; }

    public string UsuarioCreacion { get; set; } = "";

    public DateTimeOffset? FechaModificacion { get; set; }

    public string UsuarioModificacion { get; set; } = "";
}