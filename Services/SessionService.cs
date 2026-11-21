using Microsoft.AspNetCore.Components.Server.ProtectedBrowserStorage;

namespace BlazorApp1.Services;

public class SessionService
{
    private const string SessionKey = "ci_sesion";

    private readonly ProtectedSessionStorage _sessionStorage;
    private readonly ProtectedLocalStorage _localStorage;

    public string Token { get; private set; } = "";

    public string Username { get; private set; } = "";

    public bool IsAuthenticated =>
        !string.IsNullOrWhiteSpace(Token);

    public bool IsInitialized { get; private set; }

    public SessionService(
        ProtectedSessionStorage sessionStorage,
        ProtectedLocalStorage localStorage)
    {
        _sessionStorage = sessionStorage;
        _localStorage = localStorage;
    }

    public async Task IniciarSesionAsync(
        string token,
        string username,
        bool recordarme)
    {
        Token = token;
        Username = username;
        IsInitialized = true;

        var datos = new DatosSesion
        {
            Token = token,
            Username = username
        };

        if (recordarme)
        {
            await _localStorage.SetAsync(
                SessionKey,
                datos
            );

            await _sessionStorage.DeleteAsync(
                SessionKey
            );
        }
        else
        {
            await _sessionStorage.SetAsync(
                SessionKey,
                datos
            );

            await _localStorage.DeleteAsync(
                SessionKey
            );
        }
    }

    public async Task RestaurarSesionAsync()
    {
        if (IsInitialized)
        {
            return;
        }

        try
        {
            // Primero buscamos una sesión recordada.
            var local =
                await _localStorage
                    .GetAsync<DatosSesion>(
                        SessionKey
                    );

            if (local.Success &&
                local.Value is not null &&
                !string.IsNullOrWhiteSpace(
                    local.Value.Token))
            {
                Token = local.Value.Token;
                Username = local.Value.Username;
                return;
            }

            // Si no existe en localStorage,
            // revisamos sessionStorage.
            var session =
                await _sessionStorage
                    .GetAsync<DatosSesion>(
                        SessionKey
                    );

            if (session.Success &&
                session.Value is not null &&
                !string.IsNullOrWhiteSpace(
                    session.Value.Token))
            {
                Token = session.Value.Token;
                Username = session.Value.Username;
            }
        }
        catch
        {
            Token = "";
            Username = "";
        }
        finally
        {
            IsInitialized = true;
        }
    }

    public async Task CerrarSesionAsync()
    {
        Token = "";
        Username = "";
        IsInitialized = true;

        await _sessionStorage.DeleteAsync(
            SessionKey
        );

        await _localStorage.DeleteAsync(
            SessionKey
        );
    }
}

public class DatosSesion
{
    public string Token { get; set; } = "";

    public string Username { get; set; } = "";
}