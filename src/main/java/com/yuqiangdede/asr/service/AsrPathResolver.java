package com.yuqiangdede.asr.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class AsrPathResolver {

    private static final Pattern SHERPA_JAVA_VERSIONED_JAR = Pattern.compile("sherpa-onnx-v([0-9.]+)-java(\\d+)\\.jar");
    private static final Pattern SHERPA_JAVA_JAR_VERSION = Pattern.compile("sherpa-onnx-v([0-9.]+)\\.jar");
    private static final Pattern SHERPA_JAVA_ANY_JAR = Pattern.compile("sherpa-onnx-v([0-9.]+)(?:-java(\\d+))?\\.jar");
    private static final String RESOURCE_PREFIX = "resource/";

    @Value("${asr.model-dir:${vision-mind.asr.model-dir:resource/asr/model/asr-model}}")
    private String modelDirValue;

    @Value("${asr.punctuation-model-dir:${vision-mind.asr.punctuation-model-dir:resource/asr/model/punctuation-model}}")
    private String punctuationModelDirValue;

    @Value("${asr.config-dir:${vision-mind.asr.config-dir:resource/asr/config}}")
    private String configDirValue;

    @Value("${asr.upload-dir:${vision-mind.asr.upload-dir:resource/asr/uploads}}")
    private String uploadDirValue;

    @Value("${asr.hotwords-dir:${vision-mind.asr.hotwords-dir:resource/asr/runtime/hotwords}}")
    private String hotwordsDirValue;

    @Value("${asr.runtime-java-jar:${vision-mind.asr.runtime-java-jar:auto}}")
    private String runtimeJavaJarValue;

    @Value("${asr.runtime-native-jar:${vision-mind.asr.runtime-native-jar:auto}}")
    private String runtimeNativeJarValue;

    @Value("${asr.runtime-provider:${vision-mind.asr.runtime-provider:cpu}}")
    private String provider;

    @Value("${asr.num-threads:${vision-mind.asr.num-threads:2}}")
    private int numThreads;

    @Value("${asr.hotwords-score:${vision-mind.asr.hotwords-score:1.5}}")
    private float hotwordsScore;

    @Value("${asr.decoding-method:${vision-mind.asr.decoding-method:modified_beam_search}}")
    private String decodingMethod;

    @Value("${asr.streaming-model:${vision-mind.asr.streaming-model:true}}")
    private boolean streamingModel;

    private Path projectRoot;
    private Path modelDir;
    private Path punctuationModelDir;
    private Path configDir;
    private Path uploadDir;
    private Path hotwordsDir;
    private Path runtimeJavaJar;
    private Path runtimeNativeJar;
    private Path extractionRoot;
    private final PathMatchingResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();

    @PostConstruct
    public void init() {
        projectRoot = Paths.get("").toAbsolutePath().normalize();
        extractionRoot = resolveExtractionRoot();
        modelDir = resolveReadOnly(modelDirValue);
        punctuationModelDir = resolveReadOnly(punctuationModelDirValue);
        configDir = resolveWritable(configDirValue);
        uploadDir = resolveWritable(uploadDirValue);
        hotwordsDir = resolveWritable(hotwordsDirValue);
        runtimeJavaJar = resolveRuntimeJavaJar();
        runtimeNativeJar = resolveRuntimeNativeJar();
    }

    public Path resolve(String pathValue) {
        return resolveWritable(pathValue);
    }

    public String getProvider() {
        return provider;
    }

    public int getNumThreads() {
        return numThreads;
    }

    public float getHotwordsScore() {
        return hotwordsScore;
    }

    public String getDecodingMethod() {
        return decodingMethod;
    }

    public boolean isStreamingModel() {
        return streamingModel;
    }

    public Path getProjectRoot() {
        return projectRoot;
    }

    public Path getModelDir() {
        return modelDir;
    }

    public Path getPunctuationModelDir() {
        return punctuationModelDir;
    }

    public Path getConfigDir() {
        return configDir;
    }

    public Path getUploadDir() {
        return uploadDir;
    }

    public Path getHotwordsDir() {
        return hotwordsDir;
    }

    public Path getRuntimeJavaJar() {
        return runtimeJavaJar;
    }

    public Path getRuntimeNativeJar() {
        return runtimeNativeJar;
    }

    public Path getExtractionRoot() {
        return extractionRoot;
    }

    private Path resolveWritable(String pathValue) {
        Path path = Paths.get(pathValue);
        if (path.isAbsolute()) {
            return path.normalize();
        }
        String normalizedValue = normalizeResourcePath(pathValue);
        Path primary = projectRoot.resolve(path).normalize();
        if (!Files.exists(primary)
                && normalizedValue.startsWith(RESOURCE_PREFIX)
                && classpathDirectoryExists(normalizedValue)) {
            extractClasspathDirectory(normalizedValue, primary, false);
        }
        if (Files.exists(primary)) {
            return primary;
        }
        Path fallback = resolveProjectFallback(path, normalizedValue);
        if (fallback != null) {
            return fallback;
        }
        return primary;
    }

    private Path resolveReadOnly(String pathValue) {
        Path externalPath = resolveWritable(pathValue);
        if (Files.exists(externalPath)) {
            return externalPath;
        }
        String normalizedValue = normalizeResourcePath(pathValue);
        if (normalizedValue.startsWith(RESOURCE_PREFIX) && classpathDirectoryExists(normalizedValue)) {
            return extractClasspathDirectory(normalizedValue, extractionRoot.resolve(normalizedValue).normalize(), false);
        }
        return externalPath;
    }

    private Path resolveExtractionRoot() {
        try {
            URI location = AsrPathResolver.class.getProtectionDomain().getCodeSource().getLocation().toURI();
            Path codeSourcePath = Paths.get(location).toAbsolutePath().normalize();
            Path baseDir = Files.isRegularFile(codeSourcePath) ? codeSourcePath.getParent() : codeSourcePath;
            if (baseDir != null) {
                return baseDir.resolve("asr-java-bundle").normalize();
            }
        } catch (Exception ignored) {
        }
        return projectRoot.resolve("asr-java-bundle").normalize();
    }

    public Path hotwordsConfigFile() {
        return configDir.resolve("hotwords.yaml");
    }

    public Path phraseRulesConfigFile() {
        return configDir.resolve("phrase-rules.yaml");
    }

    public void ensureMutableDirectories() {
        createDirectory(configDir);
        createDirectory(uploadDir);
        createDirectory(hotwordsDir);
    }

    private Path resolveRuntimeNativeJar() {
        String configured = runtimeNativeJarValue == null ? "" : runtimeNativeJarValue.trim();
        if (!configured.isEmpty() && !"auto".equalsIgnoreCase(configured)) {
            return resolve(configured);
        }

        String version = extractSherpaVersion(runtimeJavaJar.getFileName().toString());
        String platformTag = resolveSherpaPlatformTag();
        String nativeJarName = String.format("sherpa-onnx-native-lib-%s-v%s.jar", platformTag, version);
        return runtimeJavaJar.getParent().resolve(nativeJarName).normalize();
    }

    private Path resolveRuntimeJavaJar() {
        String configured = runtimeJavaJarValue == null ? "" : runtimeJavaJarValue.trim();
        if (!configured.isEmpty() && !"auto".equalsIgnoreCase(configured)) {
            return resolve(configured);
        }

        Path sherpaDir = resolveReadOnly("resource/lib/sherpa-onnx");
        int currentJavaFeature = Runtime.version().feature();
        Path best = null;
        int bestFeature = Integer.MIN_VALUE;
        boolean bestIsGeneric = true;

        try (java.util.stream.Stream<Path> stream = Files.list(sherpaDir)) {
            for (Path candidate : stream.filter(Files::isRegularFile).collect(Collectors.toList())) {
                Matcher matcher = SHERPA_JAVA_ANY_JAR.matcher(candidate.getFileName().toString());
                if (!matcher.matches()) {
                    continue;
                }
                String featureGroup = matcher.group(2);
                boolean genericJar = featureGroup == null || featureGroup.isBlank();
                int jarFeature = genericJar ? Integer.MIN_VALUE : Integer.parseInt(featureGroup);
                if (!genericJar && jarFeature > currentJavaFeature) {
                    continue;
                }

                if (best == null) {
                    best = candidate;
                    bestFeature = jarFeature;
                    bestIsGeneric = genericJar;
                    continue;
                }

                if (bestIsGeneric && !genericJar) {
                    best = candidate;
                    bestFeature = jarFeature;
                    bestIsGeneric = false;
                    continue;
                }

                if (bestIsGeneric == genericJar && jarFeature > bestFeature) {
                    best = candidate;
                    bestFeature = jarFeature;
                    bestIsGeneric = genericJar;
                }
            }
        } catch (Exception e) {
            throw new IllegalStateException("扫描 sherpa Java API jar 失败: " + sherpaDir, e);
        }

        if (best != null) {
            return best.normalize();
        }

        throw new IllegalStateException("未找到可用的 sherpa Java API jar。请放入 sherpa-onnx-v<版本>.jar 或 sherpa-onnx-v<版本>-java<版本>.jar");
    }

    private String extractSherpaVersion(String javaJarName) {
        Matcher versionedMatcher = SHERPA_JAVA_VERSIONED_JAR.matcher(javaJarName);
        if (versionedMatcher.matches()) {
            return versionedMatcher.group(1);
        }
        Matcher matcher = SHERPA_JAVA_JAR_VERSION.matcher(javaJarName);
        if (!matcher.matches()) {
            throw new IllegalStateException("无法从 sherpa Java jar 文件名解析版本: " + javaJarName);
        }
        return matcher.group(1);
    }

    private String resolveSherpaPlatformTag() {
        String osName = System.getProperty("os.name", "").toLowerCase(Locale.ROOT);
        String archName = System.getProperty("os.arch", "").toLowerCase(Locale.ROOT);

        if (osName.contains("win")) {
            return "win-x64";
        }
        if (osName.contains("linux")) {
            if (archName.contains("aarch64") || archName.contains("arm64")) {
                return "linux-aarch64";
            }
            if (archName.contains("amd64") || archName.contains("x86_64")) {
                return "linux-x64";
            }
            throw new IllegalStateException("暂不支持的 Linux 架构: " + archName);
        }
        if (osName.contains("mac") || osName.contains("darwin")) {
            if (archName.contains("aarch64") || archName.contains("arm64")) {
                return "osx-aarch64";
            }
            if (archName.contains("amd64") || archName.contains("x86_64")) {
                return "osx-x64";
            }
            throw new IllegalStateException("暂不支持的 macOS 架构: " + archName);
        }
        throw new IllegalStateException("暂不支持的操作系统: " + osName);
    }

    private void createDirectory(Path path) {
        try {
            Files.createDirectories(path);
        } catch (Exception e) {
            throw new IllegalStateException("创建目录失败: " + path, e);
        }
    }

    private String normalizeResourcePath(String pathValue) {
        String normalizedValue = pathValue.replace("\\", "/");
        if (projectRoot.getFileName() != null
                && "resource".equalsIgnoreCase(projectRoot.getFileName().toString())
                && normalizedValue.startsWith(RESOURCE_PREFIX)) {
            return normalizedValue.substring(RESOURCE_PREFIX.length());
        }
        return normalizedValue;
    }

    private Path resolveProjectFallback(Path path, String normalizedValue) {
        if (!normalizedValue.startsWith(RESOURCE_PREFIX)) {
            return null;
        }
        Path projectParent = projectRoot.getParent();
        if (projectParent == null) {
            return null;
        }
        Path fallback = projectParent.resolve(path).normalize();
        return Files.exists(fallback) ? fallback : null;
    }

    private boolean classpathDirectoryExists(String normalizedValue) {
        try {
            Resource[] resources = resourceResolver.getResources("classpath*:" + normalizedValue + "/**");
            for (Resource resource : resources) {
                if (resource.exists() && resource.isReadable()) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    private Path extractClasspathDirectory(String normalizedValue, Path targetDir, boolean overwriteExisting) {
        try {
            Files.createDirectories(targetDir);
            Resource[] resources = resourceResolver.getResources("classpath*:" + normalizedValue + "/**");
            String marker = normalizedValue + "/";
            for (Resource resource : resources) {
                if (!resource.exists() || !resource.isReadable()) {
                    continue;
                }
                String relativePath = resolveRelativePath(resource, marker);
                if (relativePath.isEmpty()) {
                    continue;
                }
                Path targetFile = targetDir.resolve(relativePath).normalize();
                Files.createDirectories(targetFile.getParent());
                if (Files.exists(targetFile) && !overwriteExisting) {
                    continue;
                }
                try (InputStream inputStream = resource.getInputStream()) {
                    if (overwriteExisting) {
                        Files.copy(inputStream, targetFile, StandardCopyOption.REPLACE_EXISTING);
                    } else {
                        Files.copy(inputStream, targetFile);
                    }
                }
            }
            cleanupEmptyDirectories(targetDir);
            return targetDir;
        } catch (Exception e) {
            throw new IllegalStateException("提取内置资源失败: " + normalizedValue, e);
        }
    }

    private String resolveRelativePath(Resource resource, String marker) throws Exception {
        String url = resource.getURL().toString().replace("\\", "/");
        int index = url.indexOf(marker);
        if (index < 0) {
            return "";
        }
        String relativePath = url.substring(index + marker.length());
        int nestedJarIndex = relativePath.indexOf("!/");
        if (nestedJarIndex >= 0) {
            relativePath = relativePath.substring(nestedJarIndex + 2);
        }
        if (relativePath.endsWith("/")) {
            return "";
        }
        return relativePath;
    }

    private void cleanupEmptyDirectories(Path targetDir) {
        try (java.util.stream.Stream<Path> stream = Files.walk(targetDir)) {
            stream.sorted(Comparator.reverseOrder())
                    .filter(Files::isDirectory)
                    .forEach(path -> {
                        try (java.util.stream.Stream<Path> children = Files.list(path)) {
                            if (!children.findFirst().isPresent() && !path.equals(targetDir)) {
                                Files.deleteIfExists(path);
                            }
                        } catch (Exception ignored) {
                        }
                    });
        } catch (Exception ignored) {
        }
    }
}
