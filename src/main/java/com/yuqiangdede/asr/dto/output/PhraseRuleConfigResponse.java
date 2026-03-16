package com.yuqiangdede.asr.dto.output;

import java.util.List;

public class PhraseRuleConfigResponse {

    private List<PhraseRuleItem> rules;

    public PhraseRuleConfigResponse() {
    }

    public PhraseRuleConfigResponse(List<PhraseRuleItem> rules) {
        this.rules = rules;
    }

    public List<PhraseRuleItem> getRules() {
        return rules;
    }

    public void setRules(List<PhraseRuleItem> rules) {
        this.rules = rules;
    }
}
