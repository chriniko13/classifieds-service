package com.chriniko.classifieds.service.test.infra;

import com.chriniko.classifieds.service.dto.ClassifiedWordsCounterReq;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

public abstract class Specification {

    protected String getBaseUrl(int port) {
        return "http://localhost:" + port + "/classified-words";
    }

    protected HttpEntity<String> createHttpEntity(String payload) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json");

        return new HttpEntity<>(payload, httpHeaders);
    }

    protected HttpEntity<ClassifiedWordsCounterReq> createHttpEntity(ClassifiedWordsCounterReq req) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json");

        return new HttpEntity<>(req, httpHeaders);
    }


}
