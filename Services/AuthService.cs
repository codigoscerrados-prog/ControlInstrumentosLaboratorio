using System.Net.Http.Headers;
using System.Net.Http.Json;
using System.Text.Json.Serialization;
using BlazorApp1.Components.Models;

namespace BlazorApp1.Services;

public class AuthService
{
    private readonly IHttpClientFactory _httpClientFactory;

    private readonly SessionService _session;


    public AuthService(
        IHttpClientFactory httpClientFactory,
        SessionService session)
    {
        _httpClientFactory =
            httpClientFactory;

        _session =
            session;
    }


    public async Task<bool> LoginAsync(
        string username,
        string password)
    {
        var client =
            _httpClientFactory
                .CreateClient("Backend");


        // 1. Autenticación real de JHipster
        var datos =
            new LoginRequest
            {
                Username =
                    username,

                Password =
                    password,

                RememberMe =
                    false
            };


        var response =
            await client.PostAsJsonAsync(
                "api/authenticate",
                datos
            );


        if (!response.IsSuccessStatusCode)
        {
            return false;
        }


        var resultado =
            await response.Content
                .ReadFromJsonAsync<LoginResponse>();


        if (resultado is null ||
            string.IsNullOrWhiteSpace(
                resultado.IdToken))
        {
            return false;
        }


        // 2. Utilizamos el JWT para consultar
        // nuestra tabla USUARIO.
        client.DefaultRequestHeaders.Authorization =
            new AuthenticationHeaderValue(
                "Bearer",
                resultado.IdToken
            );


        var usuarios =
            await client
                .GetFromJsonAsync<List<UsuarioUi>>(
                    "api/usuarios?size=100"
                );


        var usuarioSistema =
            usuarios?
                .FirstOrDefault(
                    x =>
                        x.Username.Equals(
                            username,
                            StringComparison.OrdinalIgnoreCase
                        )
                );


        // El usuario puede existir en JHI_USER,
        // pero también debe existir en nuestra
        // tabla USUARIO.
        if (usuarioSistema is null)
        {
            return false;
        }


        // Usuario inactivo no puede ingresar.
        if (!usuarioSistema.Estado.Equals(
                "ACTIVO",
                StringComparison.OrdinalIgnoreCase))
        {
            return false;
        }


        var rol =
            usuarioSistema.Rol?.Nombre
            ?? usuarioSistema.RolNombre;


        // Solo permitimos nuestros tres roles.
        if (rol != "ADMIN" &&
            rol != "TECNICO" &&
            rol != "CONSULTA")
        {
            return false;
        }


        // 3. Guardamos JWT + usuario + rol
        // en la sesión Blazor.
        await _session.IniciarSesionAsync(
            resultado.IdToken,
            username,
            rol
        );


        return true;
    }


    public async Task LogoutAsync()
    {
        await _session.CerrarSesionAsync();
    }


    private class LoginRequest
    {
        [JsonPropertyName("username")]
        public string Username { get; set; } = "";

        [JsonPropertyName("password")]
        public string Password { get; set; } = "";

        [JsonPropertyName("rememberMe")]
        public bool RememberMe { get; set; }
    }


    private class LoginResponse
    {
        [JsonPropertyName("id_token")]
        public string IdToken { get; set; } = "";
    }
}