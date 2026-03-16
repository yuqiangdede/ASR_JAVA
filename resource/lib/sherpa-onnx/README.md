将 sherpa-onnx 官方 Java 运行时放到此目录。

基础 Java API jar 使用官方带 Java 版本后缀的命名，例如：

- `sherpa-onnx-v1.12.29-java21.jar`
- `sherpa-onnx-v1.12.29-java24.jar`

应用会自动选择目录中 `java` 版本不高于当前运行 JDK 的最高版本 jar。

native jar 按运行平台自动解析，文件名遵循官方 release 命名：

- Windows x64: `sherpa-onnx-native-lib-win-x64-v1.12.10.jar`
- Linux x64: `sherpa-onnx-native-lib-linux-x64-v1.12.10.jar`
- Linux ARM64: `sherpa-onnx-native-lib-linux-aarch64-v1.12.10.jar`
- macOS x64: `sherpa-onnx-native-lib-osx-x64-v1.12.10.jar`
- macOS ARM64: `sherpa-onnx-native-lib-osx-aarch64-v1.12.10.jar`

应用会根据 `os.name` 和 `os.arch` 自动选择对应 native jar。
