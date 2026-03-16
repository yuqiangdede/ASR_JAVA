package com.yuqiangdede.asr.dto.output;

import java.util.List;

public class AsrTranscribeResponse {

    private String rawText;
    private String textAfterPhrase;
    private List<AsrAppliedRule> appliedRules;
    private AsrAudioInfo audioInfo;
    private List<String> hotwords;
    private boolean punctuationEnabled;

    public AsrTranscribeResponse() {
    }

    public AsrTranscribeResponse(String rawText, String textAfterPhrase, List<AsrAppliedRule> appliedRules,
                                 AsrAudioInfo audioInfo, List<String> hotwords, boolean punctuationEnabled) {
        this.rawText = rawText;
        this.textAfterPhrase = textAfterPhrase;
        this.appliedRules = appliedRules;
        this.audioInfo = audioInfo;
        this.hotwords = hotwords;
        this.punctuationEnabled = punctuationEnabled;
    }

    public String getRawText() {
        return rawText;
    }

    public void setRawText(String rawText) {
        this.rawText = rawText;
    }

    public String getTextAfterPhrase() {
        return textAfterPhrase;
    }

    public void setTextAfterPhrase(String textAfterPhrase) {
        this.textAfterPhrase = textAfterPhrase;
    }

    public List<AsrAppliedRule> getAppliedRules() {
        return appliedRules;
    }

    public void setAppliedRules(List<AsrAppliedRule> appliedRules) {
        this.appliedRules = appliedRules;
    }

    public AsrAudioInfo getAudioInfo() {
        return audioInfo;
    }

    public void setAudioInfo(AsrAudioInfo audioInfo) {
        this.audioInfo = audioInfo;
    }

    public List<String> getHotwords() {
        return hotwords;
    }

    public void setHotwords(List<String> hotwords) {
        this.hotwords = hotwords;
    }

    public boolean isPunctuationEnabled() {
        return punctuationEnabled;
    }

    public void setPunctuationEnabled(boolean punctuationEnabled) {
        this.punctuationEnabled = punctuationEnabled;
    }
}
