package com.example.wordstrainingbot;

import com.example.wordstrainingbot.DTO.WordDTO;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
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
	@Test
	public void testStringLength() {
		String s1 = "изысканный;воображать;предполагать".substring(0,21);
		String s2 = "изысканный;воображать;предполагать";
		String s3 = "изысканный;воображать;предполагать";
		String s4 = "123";
		Assertions.assertTrue(s2.contains(s3));
		Assertions.assertEquals("изысканный;воображать", s1);

	}
	@Test
	public void testStringSeparate() {
		String text = "regardless";
		text = text.replace(" ", "");
		text = text.substring(0, text.lastIndexOf("-"));
		Assertions.assertEquals("adasdada", text);
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
