using BlazorApp1.Components.Models;

namespace BlazorApp1.Services;

public class ActivoService
{
    private readonly BackendClient _backend;
    private readonly SessionService _session;

    public ActivoService(
        BackendClient backend,
        SessionService session)
    {
        _backend = backend;
        _session = session;
    }

    public async Task<List<ActivoUi>> ListarAsync()
    {
        var activos =
            await _backend.GetAsync<List<ActivoUi>>(
                "api/activos"
            );

        return activos ?? new List<ActivoUi>();
    }

    public async Task<ActivoUi?> ObtenerAsync(
        long id)
    {
        return await _backend.GetAsync<ActivoUi>(
            $"api/activos/{id}"
        );
    }

    public async Task<ActivoUi?> CrearAsync(
        ActivoRequest activo)
    {
        return await _backend
            .PostAsync<ActivoRequest, ActivoUi>(
                "api/activos",
                activo
            );
    }

    public async Task<ActivoUi?> ActualizarAsync(
        long id,
        ActivoRequest activo)
    {
        activo.Id = id;

        return await _backend
            .PutAsync<ActivoRequest, ActivoUi>(
                $"api/activos/{id}",
                activo
            );
    }

    public async Task<ActivoUi?> CambiarEstadoAsync(
        ActivoUi activo,
        string nuevoEstado)
    {
        var request = new ActivoRequest
        {
            Id = activo.Id,

            Codigo = activo.Codigo,
            Nombre = activo.Nombre,
            Marca = activo.Marca,
            Modelo = activo.Modelo,
            NumeroSerie = activo.NumeroSerie,
            Rango = activo.Rango,
            Resolucion = activo.Resolucion,
            Ubicacion = activo.Ubicacion,

            Estado = nuevoEstado,

            Observaciones =
                activo.Observaciones,

            Grupo =
                new ReferenciaEntidad
                {
                    Id =
                        activo.Grupo?.Id
                        ?? activo.GrupoId
                },

            TipoInstrumento =
                new ReferenciaEntidad
                {
                    Id =
                        activo.TipoInstrumento?.Id
                        ?? activo.TipoInstrumentoId
                },

            FechaCreacion =
                activo.FechaCreacion,

            UsuarioCreacion =
                activo.UsuarioCreacion,

            FechaModificacion =
                DateTimeOffset.UtcNow,

            UsuarioModificacion =
                _session.Username
        };

        return await ActualizarAsync(
            activo.Id,
            request
        );
    }

        return await ActualizarAsync(
            activo.Id,
            request
        );
    }
}