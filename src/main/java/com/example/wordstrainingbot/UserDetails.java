package com.example.wordstrainingbot;

import com.example.wordstrainingbot.DTO.QuizDTO;
import lombok.Data;

@Data
public class UserDetails {
    private Languages language;
    private int currentPage;
    private int totalPages;
    private QuizDTO quiz;
}
