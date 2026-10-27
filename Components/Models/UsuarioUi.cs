namespace BlazorApp1.Components.Models;

public class UsuarioUi
{
    public long Id { get; set; }

    public string Username { get; set; } = "";

    public string Password { get; set; } = "";

    public string Estado { get; set; } = "ACTIVO";

    public long RolId { get; set; }

    public string RolNombre { get; set; } = "";
}