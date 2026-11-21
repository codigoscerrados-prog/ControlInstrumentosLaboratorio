namespace BlazorApp1.Components.Models;

public class ActivoRequest
{
    public long? Id { get; set; }

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

    public ReferenciaEntidad Grupo { get; set; } = new();

    public ReferenciaEntidad TipoInstrumento { get; set; } = new();

    public DateTimeOffset? FechaCreacion { get; set; }

    public string UsuarioCreacion { get; set; } = "";

    public DateTimeOffset? FechaModificacion { get; set; }

    public string UsuarioModificacion { get; set; } = "";
}

public class ReferenciaEntidad
{
    public long Id { get; set; }
}