using BlazorApp1.Components.Models;

namespace BlazorApp1.Services;

public class GrupoInstrumentalService
{
    private readonly BackendClient _backend;

    public GrupoInstrumentalService(
        BackendClient backend)
    {
        _backend = backend;
    }

    public async Task<List<GrupoInstrumentalUi>> ListarAsync()
    {
        var grupos =
            await _backend
                .GetAsync<List<GrupoInstrumentalUi>>(
                    "api/grupo-instrumentals"
                );

        return grupos ?? new List<GrupoInstrumentalUi>();
    }
}