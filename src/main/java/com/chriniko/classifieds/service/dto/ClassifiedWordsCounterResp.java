package com.chriniko.classifieds.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassifiedWordsCounterResp {

    @JsonProperty("Text")
    private String text;

    @JsonProperty("Words")
    private int words;

    @JsonProperty("Euro")
    private BigDecimal euroAmount;
}
