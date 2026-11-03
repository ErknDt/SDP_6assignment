$ErrorActionPreference = "Stop"
$projectRoot = Split-Path -Parent $MyInvocation.MyCommand.Path
Set-Location $projectRoot
New-Item -ItemType Directory -Force -Path out | Out-Null
$javaFiles = @(Get-ChildItem -Path "src/main/java", "src/test/java" -Recurse -Filter "*.java" | ForEach-Object { $_.FullName })
javac --release 17 -d out $javaFiles
if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }
java -cp out kz.aitu.visitor.VisitorPatternTest
if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }
