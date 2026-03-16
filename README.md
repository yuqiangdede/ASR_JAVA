# ASR_JAVA

基于 Spring Boot 2.7、Java 11 和 sherpa-onnx 的本地离线语音识别服务。

仓库默认只保留源码、配置、页面和下载脚本，不再直接提交大模型与运行时二进制。首次使用时执行脚本下载依赖资源即可。

## 环境要求

- JDK 11
- Maven 3.8+
- Windows PowerShell 5+ 或 PowerShell 7+

## 项目结构

```text
src/main/java                    Java 源码
src/main/resources               Spring Boot 配置与前端页面
resource/asr/config              热词和短语规则配置
resource/asr/model/asr-model     ASR 模型目录
resource/asr/model/punctuation-model
resource/asr/runtime/hotwords    运行期热词缓存
resource/asr/uploads             上传目录
resource/lib/sherpa-onnx         sherpa-onnx Java/native 运行库
scripts/download-models.ps1      下载模型和运行库
scripts/download-models.bat      Windows 一键入口
```

## 首次准备

在项目根目录执行：

```powershell
.\scripts\download-models.bat
```

或：

```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\download-models.ps1
```

脚本会下载：

- 中文 ASR 模型：`encoder.fp16.onnx`、`decoder.fp16.onnx`、`joiner.fp16.onnx`、`tokens.txt`
- 中文标点模型：`model.int8.onnx`、`tokens.json`
- sherpa-onnx Java API jar
- Windows x64 / Linux x64 / Linux ARM64 native jar

如需强制重新下载：

```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\download-models.ps1 -Force
```

## 启动方式

开发启动：

```bash
mvn spring-boot:run
```

打包启动：

```bash
mvn clean package -DskipTests
java -jar target/asr-java.jar
```

## 默认地址

- 页面：`http://localhost:17008/asr/`
- 健康检查：`GET /asr/api/v1/asr/health`
- 转写接口：`POST /asr/api/v1/asr/transcribe`

## 配置说明

默认配置位于 `src/main/resources/application.properties`。

如果你的本地目录里已经存在旧的大模型或 jar 文件，脚本会默认跳过；需要覆盖时请加 `-Force`。

当前使用新的 `asr.*` 配置前缀，同时兼容旧的 `vision-mind.asr.*` 前缀，便于迁移。

如果要切换为非流式模型，可将：

```properties
asr.streaming-model=false
```

## 模型来源

- ASR 模型：`yuekai/icefall-asr-multi-zh-hans-zipformer-large`
- 标点模型：`csukuangfj/sherpa-onnx-punct-ct-transformer-zh-en-vocab272727-2024-04-12`
- 运行库：`k2-fsa/sherpa-onnx` Release `v1.12.29`
