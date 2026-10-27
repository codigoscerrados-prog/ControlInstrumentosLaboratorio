param(
    [string]$ProjectRoot = "."
)

$ErrorActionPreference = "Stop"

function FullPath([string]$relative) {
    return Join-Path (Resolve-Path $ProjectRoot) $relative
}

$root = (Resolve-Path $ProjectRoot).Path
$timestamp = Get-Date -Format "yyyyMMdd_HHmmss"
$backup = Join-Path $root "_backup_limpieza_$timestamp"
New-Item -ItemType Directory -Path $backup -Force | Out-Null

Write-Host ""
Write-Host "============================================" -ForegroundColor Cyan
Write-Host " LIMPIEZA FRONTEND BLAZOR - INSTRUMENTOS" -ForegroundColor Cyan
Write-Host "============================================" -ForegroundColor Cyan
Write-Host "Proyecto: $root"
Write-Host "Backup:   $backup"
Write-Host ""

# 1) Mover páginas antiguas que chocan con las nuevas en subcarpetas
$legacy = @(
    "Components\Pages\Instrumentos.razor",
    "Components\Pages\NuevoInstrumento.razor",
    "Components\Pages\EditarInstrumento.razor",
    "Components\Pages\VerInstrumento.razor"
)

foreach ($rel in $legacy) {
    $src = Join-Path $root $rel
    if (Test-Path $src) {
        $dest = Join-Path $backup ([IO.Path]::GetFileName($src))
        Move-Item $src $dest -Force
        Write-Host "Movido antiguo: $rel" -ForegroundColor Yellow
    }
}

# 2) Mover el modelo antiguo InstrumentoUi si ya existe ActivoUi
$oldModel = Join-Path $root "Components\Models\InstrumentoUi.cs"
$newModel = Join-Path $root "Components\Models\ActivoUi.cs"

if ((Test-Path $oldModel) -and (Test-Path $newModel)) {
    Move-Item $oldModel (Join-Path $backup "InstrumentoUi.cs") -Force
    Write-Host "Movido modelo antiguo: Components\Models\InstrumentoUi.cs" -ForegroundColor Yellow
}

# 3) Eliminar @rendermode InteractiveServer de páginas/componentes.
# Si tu proyecto tiene interactividad global, no hace falta repetirlo en cada página.
$razorFiles = Get-ChildItem (Join-Path $root "Components") -Recurse -Filter *.razor

foreach ($file in $razorFiles) {
    $text = Get-Content $file.FullName -Raw
    $original = $text

    # Quitar directivas @rendermode individuales
    $text = [regex]::Replace(
        $text,
        '(?m)^[ \t]*@rendermode[ \t]+InteractiveServer[ \t]*\r?\n?',
        ''
    )

    # Asegurar que @code comience en una línea nueva
    $text = [regex]::Replace(
        $text,
        '[ \t]+@code[ \t]*\{',
        "`r`n@code {"
    )

    # También corrige "@code" pegado al final de una etiqueta o bloque
    $text = [regex]::Replace(
        $text,
        '(?<!^)(?<!\r)(?<!\n)@code[ \t]*\{',
        "`r`n@code {"
    )

    if ($text -ne $original) {
        Copy-Item $file.FullName (Join-Path $backup ($file.Name + ".bak")) -Force
        Set-Content $file.FullName $text -Encoding UTF8
        Write-Host "Normalizado Razor: $($file.FullName.Substring($root.Length + 1))" -ForegroundColor Green
    }
}

# 4) Mostrar referencias antiguas a InstrumentoUi que todavía queden
Write-Host ""
Write-Host "Buscando referencias antiguas a InstrumentoUi..." -ForegroundColor Cyan

$remaining = Get-ChildItem (Join-Path $root "Components") -Recurse -File |
    Where-Object { $_.Extension -in ".razor", ".cs" } |
    Select-String -Pattern "\bInstrumentoUi\b"

if ($remaining) {
    Write-Host "ATENCION: todavía existen referencias a InstrumentoUi:" -ForegroundColor Red
    $remaining | ForEach-Object {
        Write-Host ("  " + $_.Path.Substring($root.Length + 1) + ":" + $_.LineNumber + "  " + $_.Line.Trim())
    }
    Write-Host ""
    Write-Host "Esas referencias deben cambiarse a ActivoUi o pertenecer a archivos antiguos." -ForegroundColor Yellow
} else {
    Write-Host "OK: no quedan referencias a InstrumentoUi." -ForegroundColor Green
}

# 5) Limpiar compilaciones anteriores
$bin = Join-Path $root "bin"
$obj = Join-Path $root "obj"

if (Test-Path $bin) {
    Remove-Item $bin -Recurse -Force
    Write-Host "Eliminado: bin" -ForegroundColor DarkGray
}

if (Test-Path $obj) {
    Remove-Item $obj -Recurse -Force
    Write-Host "Eliminado: obj" -ForegroundColor DarkGray
}

Write-Host ""
Write-Host "Limpieza terminada." -ForegroundColor Green
Write-Host ""
Write-Host "Ahora ejecuta:" -ForegroundColor Cyan
Write-Host "  dotnet clean"
Write-Host "  dotnet build"
Write-Host ""
Write-Host "Si quedan errores, copia solo los PRIMEROS 10 errores de dotnet build." -ForegroundColor Cyan
