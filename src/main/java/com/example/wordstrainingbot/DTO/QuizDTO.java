package com.example.wordstrainingbot.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizDTO {
    @JsonProperty("words")
    private List<QuizVariant> words;
    @Data
    public static class QuizVariant {
        @JsonProperty("word")
        WordDTO word;
        @JsonProperty("translations")
        List<String> translations;
    }

}