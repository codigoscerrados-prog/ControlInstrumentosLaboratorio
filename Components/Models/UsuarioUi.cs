using System.Text.Json.Serialization;

namespace BlazorApp1.Components.Models;

public class UsuarioUi
{
    public long Id { get; set; }

    public string Username { get; set; } = "";

    public string Correo { get; set; } = "";

    public string Estado { get; set; } = "ACTIVO";

    // Campo real de la entidad JHipster.
    // No se utilizará para autenticar desde Blazor.
    [JsonPropertyName("jhiPassword")]
    public string JhiPassword { get; set; } = "";

    // Relación real con ROL
    public RolUi? Rol { get; set; }

    // Campos auxiliares para formularios
    [JsonIgnore]
    public string Password { get; set; } = "";

    [JsonIgnore]
    public long RolId { get; set; }

    [JsonIgnore]
    public string RolNombre { get; set; } = "";

    // Auditoría
    public DateTimeOffset? FechaCreacion { get; set; }

    public string UsuarioCreacion { get; set; } = "";

    public DateTimeOffset? FechaModificacion { get; set; }

    public string? UsuarioModificacion { get; set; }
}