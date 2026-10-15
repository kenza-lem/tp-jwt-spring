$ErrorActionPreference = 'Stop'
$projectRoot = Split-Path $PSScriptRoot -Parent
$configPath = Join-Path $projectRoot '.env'
if (Test-Path -LiteralPath $configPath) {
    Write-Host 'Le fichier .env existe deja ; il est conserve.'
    return
}
$keyBytes = New-Object byte[] 48
$rng = [System.Security.Cryptography.RandomNumberGenerator]::Create()
try { $rng.GetBytes($keyBytes) } finally { $rng.Dispose() }
$localSecret = [Convert]::ToBase64String($keyBytes)
@("JWT_SECRET=$localSecret", 'JWT_EXPIRATION=3600000', 'SERVER_PORT=8080',
  'H2_CONSOLE_ENABLED=false', 'SHOW_SQL=false') | Set-Content -LiteralPath $configPath -Encoding ascii
Write-Host 'Configuration locale creee. La cle reste dans .env et ne doit pas etre publiee.'
