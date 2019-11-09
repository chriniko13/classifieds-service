package com.chriniko.classifieds.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassifiedWordsCounterReq {

    @NotNull
    @JsonProperty("Text")
    private String text;
}
