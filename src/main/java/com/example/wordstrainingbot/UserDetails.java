package com.example.wordstrainingbot;

import lombok.Data;

@Data
public class UserDetails {
    private Languages language;
    private int currentPage;
    private int totalPages;

}
