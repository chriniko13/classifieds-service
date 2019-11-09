package com.chriniko.classifieds.service.domain;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Getter
public class ClassifiedWordsCounterResult {

    private final int words;
    private final BigDecimal euroAmount;
}
