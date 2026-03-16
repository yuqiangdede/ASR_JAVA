param(
    [switch]$Force
)

$ErrorActionPreference = "Stop"
[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12

$projectRoot = Split-Path -Parent $PSScriptRoot

$downloads = @(
    @{
        Path = "resource/asr/model/asr-model/encoder.fp16.onnx"
        Url = "https://huggingface.co/yuekai/icefall-asr-multi-zh-hans-zipformer-large/resolve/main/encoder.fp16.onnx?download=true"
    },
    @{
        Path = "resource/asr/model/asr-model/decoder.fp16.onnx"
        Url = "https://huggingface.co/yuekai/icefall-asr-multi-zh-hans-zipformer-large/resolve/main/decoder.fp16.onnx?download=true"
    },
    @{
        Path = "resource/asr/model/asr-model/joiner.fp16.onnx"
        Url = "https://huggingface.co/yuekai/icefall-asr-multi-zh-hans-zipformer-large/resolve/main/joiner.fp16.onnx?download=true"
    },
    @{
        Path = "resource/asr/model/asr-model/tokens.txt"
        Url = "https://huggingface.co/yuekai/icefall-asr-multi-zh-hans-zipformer-large/resolve/main/tokens.txt?download=true"
    },
    @{
        Path = "resource/asr/model/punctuation-model/model.int8.onnx"
        Url = "https://huggingface.co/csukuangfj/sherpa-onnx-punct-ct-transformer-zh-en-vocab272727-2024-04-12/resolve/main/model.int8.onnx?download=true"
    },
    @{
        Path = "resource/asr/model/punctuation-model/tokens.json"
        Url = "https://huggingface.co/csukuangfj/sherpa-onnx-punct-ct-transformer-zh-en-vocab272727-2024-04-12/resolve/main/tokens.json?download=true"
    },
    @{
        Path = "resource/lib/sherpa-onnx/sherpa-onnx-v1.12.29.jar"
        Url = "https://github.com/k2-fsa/sherpa-onnx/releases/download/v1.12.29/sherpa-onnx-v1.12.29.jar"
    },
    @{
        Path = "resource/lib/sherpa-onnx/sherpa-onnx-native-lib-win-x64-v1.12.29.jar"
        Url = "https://github.com/k2-fsa/sherpa-onnx/releases/download/v1.12.29/sherpa-onnx-native-lib-win-x64-v1.12.29.jar"
    },
    @{
        Path = "resource/lib/sherpa-onnx/sherpa-onnx-native-lib-linux-x64-v1.12.29.jar"
        Url = "https://github.com/k2-fsa/sherpa-onnx/releases/download/v1.12.29/sherpa-onnx-native-lib-linux-x64-v1.12.29.jar"
    },
    @{
        Path = "resource/lib/sherpa-onnx/sherpa-onnx-native-lib-linux-aarch64-v1.12.29.jar"
        Url = "https://github.com/k2-fsa/sherpa-onnx/releases/download/v1.12.29/sherpa-onnx-native-lib-linux-aarch64-v1.12.29.jar"
    }
)

function Save-RemoteFile {
    param(
        [Parameter(Mandatory = $true)][string]$Url,
        [Parameter(Mandatory = $true)][string]$Destination,
        [switch]$ForceDownload
    )

    if ((Test-Path $Destination) -and -not $ForceDownload) {
        Write-Host "[skip] $Destination"
        return
    }

    $directory = Split-Path -Parent $Destination
    if ($directory) {
        New-Item -ItemType Directory -Path $directory -Force | Out-Null
    }

    Write-Host "[download] $Url"
    Invoke-WebRequest -Uri $Url -OutFile $Destination
    Write-Host "[saved] $Destination"
}

foreach ($item in $downloads) {
    $destination = Join-Path $projectRoot $item.Path
    Save-RemoteFile -Url $item.Url -Destination $destination -ForceDownload:$Force
}

Write-Host ""
Write-Host "资源下载完成。"
Write-Host "启动命令: mvn spring-boot:run"
