package com.example.wordstrainingbot.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WordDTO {
    private String word;
    private String sourceLang;

}
