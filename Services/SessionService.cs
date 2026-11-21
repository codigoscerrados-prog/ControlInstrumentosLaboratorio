using Microsoft.AspNetCore.Components.Server.ProtectedBrowserStorage;

namespace BlazorApp1.Services;

public class SessionService
{
    private const string SessionKey = "ci_sesion";

    private readonly ProtectedSessionStorage _sessionStorage;

    public string Token { get; private set; } = "";

    public string Username { get; private set; } = "";

    public string Rol { get; private set; } = "";

    public bool IsAuthenticated =>
        !string.IsNullOrWhiteSpace(Token)
        &&
        !string.IsNullOrWhiteSpace(Rol);

    public bool EsAdmin =>
        Rol.Equals(
            "ADMIN",
            StringComparison.OrdinalIgnoreCase
        );

    public bool EsTecnico =>
        Rol.Equals(
            "TECNICO",
            StringComparison.OrdinalIgnoreCase
        );

    public bool EsConsulta =>
        Rol.Equals(
            "CONSULTA",
            StringComparison.OrdinalIgnoreCase
        );

    public bool PuedeEditar =>
        EsAdmin || EsTecnico;

    public bool IsInitialized { get; private set; }


    public SessionService(
        ProtectedSessionStorage sessionStorage)
    {
        _sessionStorage = sessionStorage;
    }


    public async Task IniciarSesionAsync(
        string token,
        string username,
        string rol)
    {
        Token = token;
        Username = username;
        Rol = rol;
        IsInitialized = true;

        var datos = new DatosSesion
        {
            Token = token,
            Username = username,
            Rol = rol
        };

        await _sessionStorage.SetAsync(
            SessionKey,
            datos
        );
    }


    public async Task RestaurarSesionAsync()
    {
        if (IsInitialized)
        {
            return;
        }

        try
        {
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
                Token =
                    session.Value.Token;

                Username =
                    session.Value.Username;

                Rol =
                    session.Value.Rol;
            }
        }
        catch
        {
            Token = "";
            Username = "";
            Rol = "";
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
        Rol = "";
        IsInitialized = true;

        await _sessionStorage.DeleteAsync(
            SessionKey
        );
    }
}


public class DatosSesion
{
    public string Token { get; set; } = "";

    public string Username { get; set; } = "";

    public string Rol { get; set; } = "";
}