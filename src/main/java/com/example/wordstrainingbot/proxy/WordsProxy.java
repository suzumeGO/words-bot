package com.example.wordstrainingbot.proxy;

import com.example.wordstrainingbot.DTO.WordDTO;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "words",
        url = "${api.url}")
public interface WordsProxy {

    // addWord(WordDTO word);

}
