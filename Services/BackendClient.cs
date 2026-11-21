using System.Net.Http.Headers;
using System.Net.Http.Json;

namespace BlazorApp1.Services;

public class BackendClient
{
    private readonly IHttpClientFactory _httpClientFactory;
    private readonly SessionService _session;

    public BackendClient(
        IHttpClientFactory httpClientFactory,
        SessionService session)
    {
        _httpClientFactory = httpClientFactory;
        _session = session;
    }

    private HttpClient CrearCliente()
    {
        var client =
            _httpClientFactory.CreateClient("Backend");

        if (!string.IsNullOrWhiteSpace(_session.Token))
        {
            client.DefaultRequestHeaders.Authorization =
                new AuthenticationHeaderValue(
                    "Bearer",
                    _session.Token
                );
        }

        return client;
    }

    public async Task<T?> GetAsync<T>(
        string url)
    {
        var client = CrearCliente();

        var response =
            await client.GetAsync(url);

        response.EnsureSuccessStatusCode();

        return await response.Content
            .ReadFromJsonAsync<T>();
    }

    public async Task<TResponse?> PostAsync<
        TRequest,
        TResponse>(
        string url,
        TRequest datos)
    {
        var client = CrearCliente();

        var response =
            await client.PostAsJsonAsync(
                url,
                datos
            );

        response.EnsureSuccessStatusCode();

        return await response.Content
            .ReadFromJsonAsync<TResponse>();
    }

    public async Task<TResponse?> PutAsync<
        TRequest,
        TResponse>(
        string url,
        TRequest datos)
    {
        var client = CrearCliente();

        var response =
            await client.PutAsJsonAsync(
                url,
                datos
            );

        response.EnsureSuccessStatusCode();

        return await response.Content
            .ReadFromJsonAsync<TResponse>();
    }

    public async Task DeleteAsync(
        string url)
    {
        var client = CrearCliente();

        var response =
            await client.DeleteAsync(url);

        response.EnsureSuccessStatusCode();
    }
}