package com.yuqiangdede.asr.dto.output;

import java.util.List;

public class HotwordConfigResponse {

    private List<String> baseTerms;

    public HotwordConfigResponse() {
    }

    public HotwordConfigResponse(List<String> baseTerms) {
        this.baseTerms = baseTerms;
    }

    public List<String> getBaseTerms() {
        return baseTerms;
    }

    public void setBaseTerms(List<String> baseTerms) {
        this.baseTerms = baseTerms;
    }
}
