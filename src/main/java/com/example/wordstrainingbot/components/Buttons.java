package com.example.wordstrainingbot.components;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class Buttons {
    private static final String ENGLISH = "Английский";
    private static final InlineKeyboardButton ENGLISH_BUTTON = new InlineKeyboardButton(ENGLISH);
    private static final String JAPANESE = "Японский";
    private static final InlineKeyboardButton JAPANESE_BUTTON = new InlineKeyboardButton(JAPANESE);
    private static final String KOREAN = "Корейский";
    private static final InlineKeyboardButton KOREAN_BUTTON = new InlineKeyboardButton(KOREAN);
    private static final String CHINESE = "Китайский";
    private static final InlineKeyboardButton CHINESE_BUTTON = new InlineKeyboardButton(CHINESE);
    private static final String NEXT_PAGE = "Следующая";
    private static final String PREVIOUS_PAGE = "Предыдущая";
    private static final String FIRST_PAGE = "Первая";
    private static final String LAST_PAGE = "Последняя";
    private static final InlineKeyboardButton NEXT_PAGE_BTN = new InlineKeyboardButton(NEXT_PAGE);
    private static final InlineKeyboardButton PREVIOUS_PAGE_BTN = new InlineKeyboardButton(PREVIOUS_PAGE);
    private static final InlineKeyboardButton FIRST_PAGE_BTN = new InlineKeyboardButton(FIRST_PAGE);
    private static final InlineKeyboardButton LAST_PAGE_BTN = new InlineKeyboardButton(LAST_PAGE);
    private static InlineKeyboardButton FIRST_ANSWER;
    private static InlineKeyboardButton SECOND_ANSWER;
    private static InlineKeyboardButton THIRD_ANSWER;
    private static InlineKeyboardButton FOURTH_ANSWER;

    public static List<String> getPages() {
        ArrayList<String> pages = new ArrayList<>();
        pages.add(NEXT_PAGE);
        pages.add(PREVIOUS_PAGE);
        pages.add(FIRST_PAGE);
        pages.add(LAST_PAGE);
        return pages;
    }

    public static InlineKeyboardMarkup languageButtons() {
        ENGLISH_BUTTON.setCallbackData(ENGLISH);
        JAPANESE_BUTTON.setCallbackData(JAPANESE);
        KOREAN_BUTTON.setCallbackData(KOREAN);
        CHINESE_BUTTON.setCallbackData(CHINESE);

        List<InlineKeyboardButton> rowIn1line = List.of(ENGLISH_BUTTON, KOREAN_BUTTON);
        List<InlineKeyboardButton> rowIn2line = List.of(JAPANESE_BUTTON, CHINESE_BUTTON);
        List<List<InlineKeyboardButton>> rowsInLine = List.of(rowIn1line, rowIn2line);

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.setKeyboard(rowsInLine);

        return markupInline;
    }

    public static InlineKeyboardMarkup translationVariantButtons(List<String> answers) {
        Collections.shuffle(answers);

        FIRST_ANSWER = new InlineKeyboardButton(answers.get(0));
        FIRST_ANSWER.setCallbackData(answers.get(0));

        SECOND_ANSWER = new InlineKeyboardButton(answers.get(1));
        SECOND_ANSWER.setCallbackData(answers.get(1));

        THIRD_ANSWER = new InlineKeyboardButton(answers.get(2));
        THIRD_ANSWER.setCallbackData(answers.get(2));

        FOURTH_ANSWER = new InlineKeyboardButton(answers.get(3));
        FOURTH_ANSWER.setCallbackData(answers.get(3));

        List<InlineKeyboardButton> rowIn1line = List.of(FIRST_ANSWER, SECOND_ANSWER);
        List<InlineKeyboardButton> rowIn2line = List.of(THIRD_ANSWER, FOURTH_ANSWER);
        List<List<InlineKeyboardButton>> rowsInLine = List.of(rowIn1line, rowIn2line);

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.setKeyboard(rowsInLine);

        return markupInline;
    }

    public static InlineKeyboardMarkup navigationButtons() {
        NEXT_PAGE_BTN.setCallbackData(NEXT_PAGE);
        PREVIOUS_PAGE_BTN.setCallbackData(PREVIOUS_PAGE);
        FIRST_PAGE_BTN.setCallbackData(FIRST_PAGE);
        LAST_PAGE_BTN.setCallbackData(LAST_PAGE);

        List<InlineKeyboardButton> rowIn1line = List.of(PREVIOUS_PAGE_BTN, NEXT_PAGE_BTN);
        List<InlineKeyboardButton> rowIn2line = List.of(FIRST_PAGE_BTN, LAST_PAGE_BTN);
        List<List<InlineKeyboardButton>> rowsInLine = List.of(rowIn1line, rowIn2line);

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.setKeyboard(rowsInLine);

        return markupInline;
    }

}

