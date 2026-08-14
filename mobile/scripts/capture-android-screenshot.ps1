param(
    [Parameter(Mandatory = $true)]
    [ValidateSet('home', 'search', 'product', 'history', 'comparison', 'alerts')]
    [string]$Screen,
    [string]$Device = ''
)

$ErrorActionPreference = 'Stop'
$root = Resolve-Path (Join-Path $PSScriptRoot '..\..')
$output = Join-Path $root "artifacts\screenshots\android\$Screen.png"
New-Item -ItemType Directory -Force -Path (Split-Path $output) | Out-Null
$adbArgs = @()
if ($Device) { $adbArgs += @('-s', $Device) }

& adb @adbArgs get-state | Out-Null
if ($LASTEXITCODE -ne 0) { throw 'Nenhum dispositivo Android acessível pelo adb.' }

$remote = "/sdcard/hardware-deals-$Screen.png"
& adb @adbArgs shell screencap -p $remote
if ($LASTEXITCODE -ne 0) { throw 'Falha ao capturar a tela.' }
& adb @adbArgs pull $remote $output | Out-Null
& adb @adbArgs shell rm $remote
if ($LASTEXITCODE -ne 0 -or !(Test-Path $output)) { throw 'Falha ao copiar a captura.' }

Write-Host "Captura salva em $output"
