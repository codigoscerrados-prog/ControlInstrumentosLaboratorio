using BlazorApp1.Components.Models;

namespace BlazorApp1.Services;

public class TipoInstrumentoService
{
    private readonly BackendClient _backend;

    public TipoInstrumentoService(
        BackendClient backend)
    {
        _backend = backend;
    }

    public async Task<List<TipoInstrumentoUi>> ListarAsync()
    {
        var tipos =
            await _backend
                .GetAsync<List<TipoInstrumentoUi>>(
                    "api/tipo-instrumentos"
                );

        return tipos ?? new List<TipoInstrumentoUi>();
    }

    public async Task<TipoInstrumentoUi?> ObtenerAsync(
        long id)
    {
        return await _backend
            .GetAsync<TipoInstrumentoUi>(
                $"api/tipo-instrumentos/{id}"
            );
    }

    public async Task<TipoInstrumentoUi?> CrearAsync(
        TipoInstrumentoRequest tipo)
    {
        return await _backend
            .PostAsync<
                TipoInstrumentoRequest,
                TipoInstrumentoUi>(
                    "api/tipo-instrumentos",
                    tipo
                );
    }

    public async Task<TipoInstrumentoUi?> ActualizarAsync(
        long id,
        TipoInstrumentoRequest tipo)
    {
        tipo.Id = id;

        return await _backend
            .PutAsync<
                TipoInstrumentoRequest,
                TipoInstrumentoUi>(
                    $"api/tipo-instrumentos/{id}",
                    tipo
                );
    }
}