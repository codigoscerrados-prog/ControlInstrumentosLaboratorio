using BlazorApp1.Components.Models;

namespace BlazorApp1.Services;

public class UsuarioService
{
    private readonly BackendClient _backend;

    public UsuarioService(
        BackendClient backend)
    {
        _backend = backend;
    }

    public async Task<List<UsuarioUi>> ListarAsync()
    {
        var usuarios =
            await _backend
                .GetAsync<List<UsuarioUi>>(
                    "api/usuarios"
                );

        return usuarios
            ?? new List<UsuarioUi>();
    }

    public async Task<UsuarioUi?> RegistrarAsync(
        RegistroUsuarioRequest usuario)
    {
        return await _backend
            .PostAsync<
                RegistroUsuarioRequest,
                UsuarioUi>(
                    "api/usuarios/registrar",
                    usuario
                );
    }
}