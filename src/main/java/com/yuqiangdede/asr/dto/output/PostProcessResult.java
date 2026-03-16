package com.yuqiangdede.asr.dto.output;

import java.util.List;

public class PostProcessResult {

    private String rawText;
    private String textAfterPhrase;
    private List<AsrAppliedRule> appliedRules;

    public PostProcessResult() {
    }

    public PostProcessResult(String rawText, String textAfterPhrase, List<AsrAppliedRule> appliedRules) {
        this.rawText = rawText;
        this.textAfterPhrase = textAfterPhrase;
        this.appliedRules = appliedRules;
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
}
