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
}