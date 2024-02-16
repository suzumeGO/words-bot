package com.example.wordstrainingbot;

import com.example.wordstrainingbot.DTO.WordDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;


class WordsTrainingBotApplicationTests {

	@Test
	void contextLoads() {
	}
	@Test
	public void testChangeCorrectRate() {
		Assertions.assertEquals(1, getWord().getCorrectRate());
	}

	private WordDTO getWord() {
		WordDTO word = new WordDTO();
		word.setWord("assess");
		word.setLanguage(Languages.ENGLISH.getCode());
		word.setTranslate("Оценивать");
		word.setOccurrences(1);
		word.setCorrectReplies(1);
		word.setCorrectRate((double) word.getCorrectReplies() / (double) word.getOccurrences());
		word.setAdditionDate(LocalDate.now());
		return word;
	}

}
