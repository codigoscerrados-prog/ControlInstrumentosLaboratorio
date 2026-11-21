using BlazorApp1.Components.Models;

namespace BlazorApp1.Services;

public class LaboratorioCalibracionService
{
    private readonly BackendClient _backend;

    public LaboratorioCalibracionService(
        BackendClient backend)
    {
        _backend = backend;
    }

    public async Task<List<LaboratorioCalibracionUi>>
        ListarAsync()
    {
        var laboratorios =
            await _backend
                .GetAsync<List<LaboratorioCalibracionUi>>(
                    "api/laboratorio-calibracions"
                );

        return laboratorios
               ?? new List<LaboratorioCalibracionUi>();
    }

    public async Task<LaboratorioCalibracionUi?>
        ObtenerAsync(long id)
    {
        return await _backend
            .GetAsync<LaboratorioCalibracionUi>(
                $"api/laboratorio-calibracions/{id}"
            );
    }

    public async Task<LaboratorioCalibracionUi?>
        CrearAsync(
            LaboratorioCalibracionRequest laboratorio)
    {
        return await _backend
            .PostAsync<
                LaboratorioCalibracionRequest,
                LaboratorioCalibracionUi>(
                    "api/laboratorio-calibracions",
                    laboratorio
                );
    }

    public async Task<LaboratorioCalibracionUi?>
        ActualizarAsync(
            long id,
            LaboratorioCalibracionRequest laboratorio)
    {
        laboratorio.Id = id;

        return await _backend
            .PutAsync<
                LaboratorioCalibracionRequest,
                LaboratorioCalibracionUi>(
                    $"api/laboratorio-calibracions/{id}",
                    laboratorio
                );
    }
}