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
    public long ActivoId { get; set; }
    public string ActivoCodigo { get; set; } = "";
    public string ActivoNombre { get; set; } = "";
    public long LaboratorioId { get; set; }
    public string LaboratorioNombre { get; set; } = "";
}
