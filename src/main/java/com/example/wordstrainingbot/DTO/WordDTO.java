package com.example.wordstrainingbot.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WordDTO {
    @JsonProperty("word")
    private String word;
    @JsonProperty("translate")
    private String translate;
    @JsonProperty("language")
    private String language;
    @JsonProperty("occurrences")
    private int occurrences;
    @JsonProperty("correct_replies")
    private int correctReplies;
    @JsonProperty("correct_rate")
    private double correctRate;
    @JsonProperty("addition_date")
    private LocalDate additionDate;

}
