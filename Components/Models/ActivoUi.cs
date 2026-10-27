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
    public long GrupoId { get; set; }
    public string GrupoNombre { get; set; } = "";
    public long TipoInstrumentoId { get; set; }
    public string TipoInstrumentoNombre { get; set; } = "";
}
