$ErrorActionPreference = 'Stop'
$projectRoot = Split-Path $PSScriptRoot -Parent
& (Join-Path $PSScriptRoot 'New-LocalConfig.ps1')
Push-Location $projectRoot
try { & .\mvnw.cmd spring-boot:run; exit $LASTEXITCODE } finally { Pop-Location }
