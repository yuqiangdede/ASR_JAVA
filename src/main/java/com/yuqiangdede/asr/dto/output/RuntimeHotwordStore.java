package com.yuqiangdede.asr.dto.output;

import java.util.ArrayList;
import java.util.List;

public class RuntimeHotwordStore {

    private List<String> baseTerms = new ArrayList<>();

    public List<String> getBaseTerms() {
        return baseTerms;
    }

    public void setBaseTerms(List<String> baseTerms) {
        this.baseTerms = baseTerms;
    }
}
