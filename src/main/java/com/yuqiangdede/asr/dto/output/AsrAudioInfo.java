package com.yuqiangdede.asr.dto.output;

public class AsrAudioInfo {

    private String sourceFormat;
    private int sampleRate;
    private int channels;
    private int sampleCount;
    private long durationMs;

    public AsrAudioInfo() {
    }

    public AsrAudioInfo(String sourceFormat, int sampleRate, int channels, int sampleCount, long durationMs) {
        this.sourceFormat = sourceFormat;
        this.sampleRate = sampleRate;
        this.channels = channels;
        this.sampleCount = sampleCount;
        this.durationMs = durationMs;
    }

    public String getSourceFormat() {
        return sourceFormat;
    }

    public void setSourceFormat(String sourceFormat) {
        this.sourceFormat = sourceFormat;
    }

    public int getSampleRate() {
        return sampleRate;
    }

    public void setSampleRate(int sampleRate) {
        this.sampleRate = sampleRate;
    }

    public int getChannels() {
        return channels;
    }

    public void setChannels(int channels) {
        this.channels = channels;
    }

    public int getSampleCount() {
        return sampleCount;
    }

    public void setSampleCount(int sampleCount) {
        this.sampleCount = sampleCount;
    }

    public long getDurationMs() {
        return durationMs;
    }

    public void setDurationMs(long durationMs) {
        this.durationMs = durationMs;
    }
}
