using System.Net.Http.Json;
using System.Text.Json.Serialization;

namespace BlazorApp1.Services;

public class AuthService
{
    private readonly IHttpClientFactory _httpClientFactory;
    private readonly SessionService _session;

    public AuthService(
        IHttpClientFactory httpClientFactory,
        SessionService session)
    {
        _httpClientFactory = httpClientFactory;
        _session = session;
    }

    public async Task<bool> LoginAsync(
        string username,
        string password)
    {
        var client =
            _httpClientFactory.CreateClient("Backend");

        var datos = new LoginRequest
        {
            Username = username,
            Password = password,
            RememberMe = false
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
            string.IsNullOrWhiteSpace(resultado.IdToken))
        {
            return false;
        }

        await _session.IniciarSesionAsync(
            resultado.IdToken,
            username
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