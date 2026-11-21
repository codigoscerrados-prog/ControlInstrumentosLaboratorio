using BlazorApp1.Components.Models;

namespace BlazorApp1.Services;

public class CalibracionService
{
    private readonly BackendClient _backend;

    public CalibracionService(
        BackendClient backend)
    {
        _backend = backend;
    }

    public async Task<List<CalibracionUi>> ListarAsync()
    {
        var calibraciones =
            await _backend
                .GetAsync<List<CalibracionUi>>(
                    "api/calibracions"
                );

        return calibraciones
               ?? new List<CalibracionUi>();
    }

    public async Task<CalibracionUi?> ObtenerAsync(
        long id)
    {
        return await _backend
            .GetAsync<CalibracionUi>(
                $"api/calibracions/{id}"
            );
    }
    public async Task<CalibracionUi?> CrearAsync(
    CalibracionRequest calibracion)
    {
        return await _backend
            .PostAsync<CalibracionRequest, CalibracionUi>(
                "api/calibracions",
                calibracion
            );
    }
    public async Task<CalibracionUi?> ActualizarAsync(
    long id,
    CalibracionRequest calibracion)
    {
        calibracion.Id = id;

        return await _backend
            .PutAsync<CalibracionRequest, CalibracionUi>(
                $"api/calibracions/{id}",
                calibracion
            );
    }
    public async Task<List<CalibracionUi>> ListarPorActivoAsync(
    long activoId)
    {
        var calibraciones =
            await ListarAsync();

        return calibraciones
            .Where(x =>
                x.Activo?.Id == activoId
            )
            .OrderByDescending(x =>
                x.FechaCalibracion
                ?? x.FechaEnvio
            )
            .ToList();
    }
}