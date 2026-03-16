package com.yuqiangdede.asr.controller;

import com.yuqiangdede.asr.dto.input.HotwordConfigUpdateRequest;
import com.yuqiangdede.asr.dto.input.PhraseRuleConfigUpdateRequest;
import com.yuqiangdede.asr.dto.output.AsrHealthResponse;
import com.yuqiangdede.asr.dto.output.AsrTranscribeResponse;
import com.yuqiangdede.asr.dto.output.HotwordConfigResponse;
import com.yuqiangdede.asr.dto.output.PhraseRuleConfigResponse;
import com.yuqiangdede.asr.service.AsrConfigService;
import com.yuqiangdede.asr.service.AsrService;
import com.yuqiangdede.common.dto.output.HttpResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/asr")
public class AsrController {

    private static final Logger log = LoggerFactory.getLogger(AsrController.class);

    private final AsrService asrService;
    private final AsrConfigService asrConfigService;

    public AsrController(AsrService asrService, AsrConfigService asrConfigService) {
        this.asrService = asrService;
        this.asrConfigService = asrConfigService;
    }

    @GetMapping(value = "/health", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<AsrHealthResponse> health() {
        try {
            return new HttpResult<>(true, asrService.health());
        } catch (RuntimeException e) {
            log.error("ASR health failed", e);
            return new HttpResult<>(false, e.getMessage());
        }
    }

    @GetMapping(value = "/hotwords", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<HotwordConfigResponse> hotwords() {
        try {
            return new HttpResult<>(true, new HotwordConfigResponse(asrConfigService.getBaseHotwords()));
        } catch (RuntimeException e) {
            log.error("Load hotwords failed", e);
            return new HttpResult<>(false, e.getMessage());
        }
    }

    @PostMapping(value = "/hotwords", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<HotwordConfigResponse> saveHotwords(@RequestBody HotwordConfigUpdateRequest request) {
        try {
            return new HttpResult<>(true, new HotwordConfigResponse(asrConfigService.saveBaseHotwords(request.getBaseTerms())));
        } catch (RuntimeException e) {
            log.error("Save hotwords failed", e);
            return new HttpResult<>(false, e.getMessage());
        }
    }

    @GetMapping(value = "/phrase-rules", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<PhraseRuleConfigResponse> phraseRules() {
        try {
            return new HttpResult<>(true, new PhraseRuleConfigResponse(asrConfigService.getPhraseRules()));
        } catch (RuntimeException e) {
            log.error("Load phrase rules failed", e);
            return new HttpResult<>(false, e.getMessage());
        }
    }

    @PostMapping(value = "/phrase-rules", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<PhraseRuleConfigResponse> savePhraseRules(@RequestBody PhraseRuleConfigUpdateRequest request) {
        try {
            return new HttpResult<>(true, new PhraseRuleConfigResponse(asrConfigService.savePhraseRules(request.getLines())));
        } catch (IllegalArgumentException e) {
            log.warn("Save phrase rules validation failed: {}", e.getMessage());
            return new HttpResult<>(false, e.getMessage());
        } catch (RuntimeException e) {
            log.error("Save phrase rules failed", e);
            return new HttpResult<>(false, e.getMessage());
        }
    }

    @PostMapping(value = "/transcribe", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<AsrTranscribeResponse> transcribe(@RequestParam("file") MultipartFile file,
                                                        @RequestParam(value = "enablePunctuation", defaultValue = "false") boolean enablePunctuation) {
        long start = System.currentTimeMillis();
        try {
            AsrTranscribeResponse response = asrService.transcribe(file, enablePunctuation);
            log.info("ASR transcribe success: file={} cost={}ms", file.getOriginalFilename(), System.currentTimeMillis() - start);
            return new HttpResult<>(true, response);
        } catch (IllegalArgumentException e) {
            log.warn("ASR transcribe validation failed: {}", e.getMessage());
            return new HttpResult<>(false, e.getMessage());
        } catch (RuntimeException e) {
            log.error("ASR transcribe failed", e);
            return new HttpResult<>(false, e.getMessage());
        }
    }
}
