package com.yuqiangdede.asr.dto.input;

import java.util.ArrayList;
import java.util.List;

public class PhraseRuleConfigUpdateRequest {

    private List<String> lines = new ArrayList<>();

    public List<String> getLines() {
        return lines;
    }

    public void setLines(List<String> lines) {
        this.lines = lines;
    }
}
