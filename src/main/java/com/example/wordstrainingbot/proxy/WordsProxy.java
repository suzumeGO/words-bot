package com.example.wordstrainingbot.proxy;

import com.example.wordstrainingbot.DTO.QuizDTO;
import com.example.wordstrainingbot.DTO.WordDTO;
import com.example.wordstrainingbot.DTO.WordsListDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "words",
        url = "${api.url}")
public interface WordsProxy {
    @PostMapping("/words/add")
    void addWord(@RequestParam String word, @RequestParam String lang, @RequestParam long chatId);

    @PostMapping("/words/updateWord")
    void updateWord(@RequestBody WordDTO word, @RequestParam long chatId);

    @GetMapping("/words/list")
    WordsListDTO getWords(@RequestParam long chatId, @RequestParam String lang, @RequestParam int page);

    @GetMapping("/words/weakestQuiz")
    QuizDTO getWeakestQuiz(@RequestParam long chatId, @RequestParam String lang);
    @GetMapping("/words/reverseQuiz")
    QuizDTO getReverseQuiz(@RequestParam long chatId, @RequestParam String lang);

}
