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

    public async Task<GrupoInstrumentalUi?> ObtenerAsync(
        long id)
    {
        return await _backend
            .GetAsync<GrupoInstrumentalUi>(
                $"api/grupo-instrumentals/{id}"
            );
    }

    public async Task<GrupoInstrumentalUi?> CrearAsync(
        GrupoInstrumentalRequest grupo)
    {
        return await _backend
            .PostAsync<
                GrupoInstrumentalRequest,
                GrupoInstrumentalUi>(
                    "api/grupo-instrumentals",
                    grupo
                );
    }

    public async Task<GrupoInstrumentalUi?> ActualizarAsync(
        long id,
        GrupoInstrumentalRequest grupo)
    {
        grupo.Id = id;

        return await _backend
            .PutAsync<
                GrupoInstrumentalRequest,
                GrupoInstrumentalUi>(
                    $"api/grupo-instrumentals/{id}",
                    grupo
                );
    }
}