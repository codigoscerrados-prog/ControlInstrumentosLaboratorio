using BlazorApp1.Components;
using BlazorApp1.Services;
using Microsoft.AspNetCore.Components.Server.ProtectedBrowserStorage;


var builder =
    WebApplication.CreateBuilder(args);

builder.Services
    .AddRazorComponents()
    .AddInteractiveServerComponents();

builder.Services.AddHttpClient(
    "Backend",
    client =>
    {
        client.BaseAddress =
            new Uri(
                "http://localhost:9999/"
            );
    }
);

builder.Services.AddScoped<ProtectedSessionStorage>();


builder.Services.AddScoped<SessionService>();
builder.Services.AddScoped<AuthService>();
builder.Services.AddScoped<BackendClient>();


builder.Services.AddScoped<ActivoService>();
builder.Services.AddScoped<GrupoInstrumentalService>();
builder.Services.AddScoped<TipoInstrumentoService>();
builder.Services.AddScoped<CalibracionService>();
builder.Services.AddScoped<LaboratorioCalibracionService>();
builder.Services.AddScoped<UsuarioService>();


var app = builder.Build();

if (!app.Environment.IsDevelopment())
{
    app.UseExceptionHandler(
        "/Error",
        createScopeForErrors: true
    );

    app.UseHsts();
}

app.UseStatusCodePagesWithReExecute(
    "/not-found",
    createScopeForStatusCodePages: true
);

app.UseHttpsRedirection();

app.UseAntiforgery();

app.MapStaticAssets();

app.MapRazorComponents<App>()
    .AddInteractiveServerRenderMode();

app.Run();