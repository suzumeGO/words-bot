package com.example.wordstrainingbot.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class WordsListDTO {
    @JsonProperty("pagination")
    Pagination pagination;
    @JsonProperty("data")
    List<WordDTO> data;

    @Data
    public static class Pagination {
        @JsonProperty("last_page")
        private int lastPage;
        @JsonProperty("has_next_page")
        private boolean hasNextPage;
        @JsonProperty("current_page")
        private int currentPage;
        @JsonProperty("items")
        private Items items;

        @Data
        public static class Items {
            @JsonProperty("count")
            private int count;
            @JsonProperty("total")
            private int total;
            @JsonProperty("per_page")
            private int perPage;
        }

    }

}

