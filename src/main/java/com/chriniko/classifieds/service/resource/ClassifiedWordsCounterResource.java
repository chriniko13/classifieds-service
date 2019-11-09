package com.chriniko.classifieds.service.resource;

import com.chriniko.classifieds.service.domain.ClassifiedWordsCounterResult;
import com.chriniko.classifieds.service.dto.ClassifiedWordsCounterReq;
import com.chriniko.classifieds.service.dto.ClassifiedWordsCounterResp;
import com.chriniko.classifieds.service.service.ClassifiedWordsCounter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/classified-words")
public class ClassifiedWordsCounterResource {

    private final ClassifiedWordsCounter classifiedWordsCounter;

    @Autowired
    public ClassifiedWordsCounterResource(ClassifiedWordsCounter classifiedWordsCounter) {
        this.classifiedWordsCounter = classifiedWordsCounter;
    }

    @PostMapping(
            path = "/count",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public @ResponseBody
    HttpEntity<ClassifiedWordsCounterResp> count(@RequestBody @Valid ClassifiedWordsCounterReq request) {

        @NotNull String text = request.getText();
        ClassifiedWordsCounterResult result = classifiedWordsCounter.process(text);

        return ResponseEntity.ok(new ClassifiedWordsCounterResp(text, result.getWords(), result.getEuroAmount()));
    }

    @PostMapping(
            path = "/countHtml",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public @ResponseBody
    HttpEntity<ClassifiedWordsCounterResp> countHtml(@RequestBody @Valid ClassifiedWordsCounterReq request) {

        @NotNull String text = request.getText();
        ClassifiedWordsCounterResult result = classifiedWordsCounter.processHtml(text);

        return ResponseEntity.ok(new ClassifiedWordsCounterResp(text, result.getWords(), result.getEuroAmount()));
    }

}
