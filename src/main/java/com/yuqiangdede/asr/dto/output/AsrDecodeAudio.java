package com.yuqiangdede.asr.dto.output;

public class AsrDecodeAudio {

    private float[] samples;
    private AsrAudioInfo audioInfo;

    public AsrDecodeAudio() {
    }

    public AsrDecodeAudio(float[] samples, AsrAudioInfo audioInfo) {
        this.samples = samples;
        this.audioInfo = audioInfo;
    }

    public float[] getSamples() {
        return samples;
    }

    public void setSamples(float[] samples) {
        this.samples = samples;
    }

    public AsrAudioInfo getAudioInfo() {
        return audioInfo;
    }

    public void setAudioInfo(AsrAudioInfo audioInfo) {
        this.audioInfo = audioInfo;
    }
}
