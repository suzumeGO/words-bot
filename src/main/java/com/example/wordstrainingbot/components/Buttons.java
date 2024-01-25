package com.example.wordstrainingbot.components;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

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

    public static InlineKeyboardMarkup inlineMarkup() {
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

}

