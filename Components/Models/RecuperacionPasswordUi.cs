namespace BlazorApp1.Components.Models;

public class RecuperacionPasswordUi
{
    public long Id { get; set; }

    public string Token { get; set; } = "";

    public DateTime? FechaSolicitud { get; set; }

    public DateTime? FechaExpiracion { get; set; }

    public bool Usado { get; set; }

    public long UsuarioId { get; set; }

    public string UsuarioNombre { get; set; } = "";
}