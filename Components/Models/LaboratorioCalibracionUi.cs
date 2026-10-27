namespace BlazorApp1.Components.Models;

public class LaboratorioCalibracionUi
{
    public long Id { get; set; }

    public string Nombre { get; set; } = "";

    public string Ruc { get; set; } = "";

    public string Direccion { get; set; } = "";

    public string Telefono { get; set; } = "";

    public string Correo { get; set; } = "";

    public string Estado { get; set; } = "ACTIVO";
}