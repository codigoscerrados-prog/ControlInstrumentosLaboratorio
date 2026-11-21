namespace BlazorApp1.Components.Models;

public class LaboratorioCalibracionRequest
{
    public long? Id { get; set; }

    public string Nombre { get; set; } = "";

    public string Ruc { get; set; } = "";

    public string Direccion { get; set; } = "";

    public string Telefono { get; set; } = "";

    public string Correo { get; set; } = "";

    public string Estado { get; set; } = "ACTIVO";

    public DateTimeOffset? FechaCreacion { get; set; }

    public string UsuarioCreacion { get; set; } = "";

    public DateTimeOffset? FechaModificacion { get; set; }

    public string? UsuarioModificacion { get; set; }
}