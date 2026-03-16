package com.yuqiangdede.asr.dto.output;

import java.util.ArrayList;
import java.util.List;

public class PhraseRuleStore {

    private List<PhraseRuleItem> rules = new ArrayList<>();

    public List<PhraseRuleItem> getRules() {
        return rules;
    }

    public void setRules(List<PhraseRuleItem> rules) {
        this.rules = rules;
    }
}
