# Regenera el keystore PKCS12 de desarrollo (autofirmado para localhost).
# Uso: .\scripts\generate-dev-keystore.ps1

$ErrorActionPreference = "Stop"
$keystoreDir = Join-Path $PSScriptRoot "..\src\main\resources\keystore"
$keystore = Join-Path $keystoreDir "plactica-dev.p12"

New-Item -ItemType Directory -Force -Path $keystoreDir | Out-Null
if (Test-Path $keystore) {
    Remove-Item $keystore -Force
}

keytool -genkeypair `
    -alias practicaserver `
    -keyalg RSA `
    -keysize 2048 `
    -storetype PKCS12 `
    -keystore $keystore `
    -storepass changeit `
    -keypass changeit `
    -validity 3650 `
    -dname "CN=localhost, OU=Plactica, O=Plactica, L=Local, ST=Dev, C=MX" `
    -ext "SAN=dns:localhost,ip:127.0.0.1"

Write-Host "Keystore generado: $keystore"
