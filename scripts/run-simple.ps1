$ErrorActionPreference = 'Stop'
Set-StrictMode -Version Latest

Push-Location $PSScriptRoot\..
try {
  $projectRoot = (Get-Location).Path
  $jfxSdkDir = Get-ChildItem -Directory "$projectRoot\tools\javafx" | Where-Object { $_.Name -like 'javafx-sdk-*' } | Select-Object -First 1
  if (-not $jfxSdkDir) { throw "JavaFX SDK not found in tools\javafx. Run the downloader step or place the SDK there." }
  $JFX_LIB = Join-Path $jfxSdkDir.FullName 'lib'

  $H2_JAR = Join-Path $projectRoot 'tools\h2\h2-2.2.224.jar'
  if (-not (Test-Path $H2_JAR)) { throw "H2 jar missing at $H2_JAR" }

  $outDir = Join-Path $projectRoot 'out'
  if (-not (Test-Path $outDir)) { New-Item -ItemType Directory -Path $outDir | Out-Null }

  Write-Host "Compiling..."
  & javac --module-path "$JFX_LIB" --add-modules javafx.controls,javafx.fxml `
      -cp "$H2_JAR" `
      -d "$outDir" `
      src\main\java\farmersapp\simple\Produce.java `
      src\main\java\farmersapp\simple\SimpleProduceApp.java

  Write-Host "Running..."
  & java --module-path "$JFX_LIB" --add-modules javafx.controls,javafx.fxml `
      -cp "$outDir;$H2_JAR" `
      farmersapp.simple.SimpleProduceApp
}
finally {
  Pop-Location
}
