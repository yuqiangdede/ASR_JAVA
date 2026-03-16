package com.yuqiangdede.asr.dto.output;

import java.util.ArrayList;
import java.util.List;

public class PhraseRuleItem {

    private String name;
    private List<String> patterns = new ArrayList<>();
    private String replacement;

    public PhraseRuleItem() {
    }

    public PhraseRuleItem(String name, List<String> patterns, String replacement) {
        this.name = name;
        this.patterns = patterns;
        this.replacement = replacement;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getPatterns() {
        return patterns;
    }

    public void setPatterns(List<String> patterns) {
        this.patterns = patterns;
    }

    public String getReplacement() {
        return replacement;
    }

    public void setReplacement(String replacement) {
        this.replacement = replacement;
    }
}
