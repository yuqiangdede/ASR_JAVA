package com.yuqiangdede.asr.service;

import com.yuqiangdede.asr.dto.output.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
public class AsrService {

    private static final Logger log = LoggerFactory.getLogger(AsrService.class);

    private final AudioDecodeService audioDecodeService;
    private final SherpaOnnxAsrService sherpaOnnxAsrService;
    private final SherpaOnnxPunctuationService sherpaOnnxPunctuationService;
    private final AsrConfigService asrConfigService;
    private final AsrPostProcessService asrPostProcessService;
    private final AsrPathResolver pathResolver;

    public AsrService(AudioDecodeService audioDecodeService,
                      SherpaOnnxAsrService sherpaOnnxAsrService,
                      SherpaOnnxPunctuationService sherpaOnnxPunctuationService,
                      AsrConfigService asrConfigService,
                      AsrPostProcessService asrPostProcessService,
                      AsrPathResolver pathResolver) {
        this.audioDecodeService = audioDecodeService;
        this.sherpaOnnxAsrService = sherpaOnnxAsrService;
        this.sherpaOnnxPunctuationService = sherpaOnnxPunctuationService;
        this.asrConfigService = asrConfigService;
        this.asrPostProcessService = asrPostProcessService;
        this.pathResolver = pathResolver;
    }

    public AsrHealthResponse health() {
        return new AsrHealthResponse(
                sherpaOnnxAsrService.isReady(),
                pathResolver.getModelDir().toString(),
                pathResolver.getConfigDir().toString(),
                pathResolver.getUploadDir().toString(),
                pathResolver.getRuntimeJavaJar().toString(),
                pathResolver.getRuntimeNativeJar().toString(),
                sherpaOnnxAsrService.runtimeMessage()
        );
    }

    public AsrTranscribeResponse transcribe(MultipartFile file, boolean enablePunctuation) {
        long totalStart = System.currentTimeMillis();
        List<String> mergedHotwords = mergeHotwords(asrConfigService.getBaseHotwords());

        long decodeStart = System.currentTimeMillis();
        AsrDecodeAudio decodedAudio = audioDecodeService.decode(file);
        long decodeCost = System.currentTimeMillis() - decodeStart;

        long asrStart = System.currentTimeMillis();
        String rawText = sherpaOnnxAsrService.transcribe(decodedAudio.getSamples(), mergedHotwords);
        long asrCost = System.currentTimeMillis() - asrStart;

        List<PhraseRuleItem> rules = asrConfigService.getPhraseRules();

        long postProcessStart = System.currentTimeMillis();
        PostProcessResult postProcess = asrPostProcessService.process(rawText, rules);
        long postProcessCost = System.currentTimeMillis() - postProcessStart;

        String correctedText = postProcess.getTextAfterPhrase();
        long punctuationCost = 0L;
        if (enablePunctuation) {
            long punctuationStart = System.currentTimeMillis();
            correctedText = sherpaOnnxPunctuationService.addPunctuation(correctedText);
            punctuationCost = System.currentTimeMillis() - punctuationStart;
        }

        long totalCost = System.currentTimeMillis() - totalStart;
        AsrAudioInfo audioInfo = decodedAudio.getAudioInfo();
        log.info(
                "ASR stages: file={} durationMs={} sampleCount={} hotwords={} decode={}ms asr={}ms post={}ms punctuation={}ms total={}ms",
                file == null ? null : file.getOriginalFilename(),
                audioInfo == null ? null : audioInfo.getDurationMs(),
                audioInfo == null ? null : audioInfo.getSampleCount(),
                mergedHotwords.size(),
                decodeCost,
                asrCost,
                postProcessCost,
                punctuationCost,
                totalCost
        );

        return new AsrTranscribeResponse(
                postProcess.getRawText(),
                correctedText,
                postProcess.getAppliedRules(),
                audioInfo,
                mergedHotwords,
                enablePunctuation
        );
    }

    private List<String> mergeHotwords(List<String> baseTerms) {
        Set<String> values = new LinkedHashSet<>();
        append(values, baseTerms);
        return new ArrayList<>(values);
    }

    private void append(Set<String> values, List<String> items) {
        if (items == null) {
            return;
        }
        for (String item : items) {
            String normalized = item == null ? "" : item.trim();
            if (!normalized.isEmpty()) {
                values.add(normalized);
            }
        }
    }
}
