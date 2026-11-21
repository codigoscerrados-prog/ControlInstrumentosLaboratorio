namespace BlazorApp1.Components.Models;

public class TipoInstrumentoUi
{
    public long Id { get; set; }

    public string Nombre { get; set; } = "";

    public string Descripcion { get; set; } = "";

    public DateTimeOffset? FechaCreacion { get; set; }

    public string UsuarioCreacion { get; set; } = "";

    public DateTimeOffset? FechaModificacion { get; set; }

    public string? UsuarioModificacion { get; set; }
}