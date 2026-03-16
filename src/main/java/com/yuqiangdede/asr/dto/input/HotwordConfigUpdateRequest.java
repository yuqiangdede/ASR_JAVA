package com.yuqiangdede.asr.dto.input;

import java.util.ArrayList;
import java.util.List;

public class HotwordConfigUpdateRequest {

    private List<String> baseTerms = new ArrayList<>();

    public List<String> getBaseTerms() {
        return baseTerms;
    }

    public void setBaseTerms(List<String> baseTerms) {
        this.baseTerms = baseTerms;
    }
}
