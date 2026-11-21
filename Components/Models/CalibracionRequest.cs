namespace BlazorApp1.Components.Models;

public class CalibracionRequest
{
    public long? Id { get; set; }

    public DateTime? FechaEnvio { get; set; }

    public DateTime? FechaCalibracion { get; set; }

    public DateTime? FechaRetorno { get; set; }

    public DateTime? FechaVencimiento { get; set; }

    public string NumeroCertificado { get; set; } = "";

    public string Resultado { get; set; } = "";

    public string Observaciones { get; set; } = "";

    public ReferenciaEntidad Activo { get; set; } = new();

    public ReferenciaEntidad Laboratorio { get; set; } = new();

    public DateTimeOffset? FechaCreacion { get; set; }

    public string UsuarioCreacion { get; set; } = "";

    public DateTimeOffset? FechaModificacion { get; set; }

    public string? UsuarioModificacion { get; set; }
}