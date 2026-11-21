using System.Text.Json.Serialization;

namespace BlazorApp1.Components.Models;

public class CalibracionUi
{
    public long Id { get; set; }

    public DateTime? FechaEnvio { get; set; }

    public DateTime? FechaCalibracion { get; set; }

    public DateTime? FechaRetorno { get; set; }

    public DateTime? FechaVencimiento { get; set; }

    public string NumeroCertificado { get; set; } = "";

    public string Resultado { get; set; } = "";

    public string Observaciones { get; set; } = "";

    // Relaciones reales del backend
    public ActivoUi? Activo { get; set; }

    // Dejamos ambas posibilidades porque necesitamos
    // comprobar el nombre exacto usado por JHipster.
    public LaboratorioCalibracionUi? Laboratorio { get; set; }

    public LaboratorioCalibracionUi? LaboratorioCalibracion { get; set; }


    // Propiedades auxiliares para los formularios

    [JsonIgnore]
    public long ActivoId { get; set; }

    [JsonIgnore]
    public string ActivoCodigo { get; set; } = "";

    [JsonIgnore]
    public string ActivoNombre { get; set; } = "";

    [JsonIgnore]
    public long LaboratorioId { get; set; }

    [JsonIgnore]
    public string LaboratorioNombre { get; set; } = "";


    // Auditoría

    public DateTimeOffset? FechaCreacion { get; set; }

    public string UsuarioCreacion { get; set; } = "";

    public DateTimeOffset? FechaModificacion { get; set; }

    public string? UsuarioModificacion { get; set; }
}