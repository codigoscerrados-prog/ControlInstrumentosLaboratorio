namespace BlazorApp1.Components.Models;

public class RegistroUsuarioRequest
{
    public string Username { get; set; } = "";

    public string Password { get; set; } = "";

    public string Correo { get; set; } = "";

    public string Rol { get; set; } = "TECNICO";
}