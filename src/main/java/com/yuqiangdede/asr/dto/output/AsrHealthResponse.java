package com.yuqiangdede.asr.dto.output;

public class AsrHealthResponse {

    private boolean ready;
    private String modelDir;
    private String configDir;
    private String uploadDir;
    private String runtimeJavaJar;
    private String runtimeNativeJar;
    private String message;

    public AsrHealthResponse() {
    }

    public AsrHealthResponse(boolean ready, String modelDir, String configDir, String uploadDir,
                             String runtimeJavaJar, String runtimeNativeJar, String message) {
        this.ready = ready;
        this.modelDir = modelDir;
        this.configDir = configDir;
        this.uploadDir = uploadDir;
        this.runtimeJavaJar = runtimeJavaJar;
        this.runtimeNativeJar = runtimeNativeJar;
        this.message = message;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public String getModelDir() {
        return modelDir;
    }

    public void setModelDir(String modelDir) {
        this.modelDir = modelDir;
    }

    public String getConfigDir() {
        return configDir;
    }

    public void setConfigDir(String configDir) {
        this.configDir = configDir;
    }

    public String getUploadDir() {
        return uploadDir;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }

    public String getRuntimeJavaJar() {
        return runtimeJavaJar;
    }

    public void setRuntimeJavaJar(String runtimeJavaJar) {
        this.runtimeJavaJar = runtimeJavaJar;
    }

    public String getRuntimeNativeJar() {
        return runtimeNativeJar;
    }

    public void setRuntimeNativeJar(String runtimeNativeJar) {
        this.runtimeNativeJar = runtimeNativeJar;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
