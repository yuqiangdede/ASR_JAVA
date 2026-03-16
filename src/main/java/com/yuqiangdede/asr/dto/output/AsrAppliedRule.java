package com.yuqiangdede.asr.dto.output;

public class AsrAppliedRule {

    private String stage;
    private String rule;
    private String pattern;
    private String replacement;
    private String before;
    private String after;

    public AsrAppliedRule() {
    }

    public AsrAppliedRule(String stage, String rule, String pattern, String replacement, String before, String after) {
        this.stage = stage;
        this.rule = rule;
        this.pattern = pattern;
        this.replacement = replacement;
        this.before = before;
        this.after = after;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getReplacement() {
        return replacement;
    }

    public void setReplacement(String replacement) {
        this.replacement = replacement;
    }

    public String getBefore() {
        return before;
    }

    public void setBefore(String before) {
        this.before = before;
    }

    public String getAfter() {
        return after;
    }

    public void setAfter(String after) {
        this.after = after;
    }
}
