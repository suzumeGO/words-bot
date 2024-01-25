package com.example.wordstrainingbot;

import com.example.wordstrainingbot.annotations.BotCommandHandler;
import com.example.wordstrainingbot.components.BotCommands;
import com.example.wordstrainingbot.components.Buttons;
import com.example.wordstrainingbot.config.BotConfig;
import com.example.wordstrainingbot.proxy.WordsProxy;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class Bot extends TelegramLongPollingBot implements BotCommands {

    private static final String ENGLISH = "Английский";
    private static final String CHINESE = "Китайский";
    private static final String KOREAN = "Корейский";
    private static final String JAPANESE = "Японский";
    private final BotConfig config;
    private final WordsProxy proxy;
    private Map<Long, String> userLanguage;
    private final List<String> languages;

    public Bot(BotConfig config, WordsProxy proxy) {
        this.config = config;
        this.proxy = proxy;
        userLanguage = new HashMap<>();
        languages = List.of(ENGLISH, KOREAN, CHINESE, JAPANESE);
        try {
            this.execute(new SetMyCommands(LIST_OF_COMMANDS, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }
    @Override
    public void onUpdateReceived(@NotNull Update update) {
        long chatId;
        String receivedMessage;
        if (update.hasCallbackQuery() && languages.contains(update.getCallbackQuery().getData())) {
            setLanguage(update);
        } else if (update.hasMessage() && update.getMessage().hasText()) {
            chatId = update.getMessage().getChatId();
            receivedMessage = update.getMessage().getText();
            if (LIST_OF_COMMANDS.stream().anyMatch(x -> x.getCommand().equals(receivedMessage))) {
                answerUtils(receivedMessage, chatId);
            } else {
                SendMessage message = new SendMessage();
                message.setChatId(chatId);


            }
        } else {
            chatId = update.getMessage().getChatId();
            SendMessage message = new SendMessage();
            message.setText("Некорректное сообщение");
            message.setChatId(chatId);
            try {
                execute(message);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void setLanguage(@NotNull Update update) {
        String receivedMessage;
        long chatId;
        chatId = update.getCallbackQuery().getMessage().getChatId();
        receivedMessage = update.getCallbackQuery().getData();
        userLanguage.put(chatId, receivedMessage);
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Выбран " + receivedMessage.toLowerCase() + " язык");
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void answerUtils(String messageText, long chatId) {
        for (Method method : this.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(BotCommandHandler.class)) {
                BotCommandHandler commandHandler = method.getAnnotation(BotCommandHandler.class);
                if (commandHandler.value().equals(messageText)) {
                    try {
                        method.invoke(this, chatId);
                        return;
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        log.error(e.getMessage());
                    }
                }
            }
        }
    }
    @BotCommandHandler(value = "/words")
    private void wordsCommandHandler(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);

    }

    @BotCommandHandler(value = "/study")
    private void studyCommandHandler(long chatId) {

    }

    @BotCommandHandler(value = "/languages")
    private void languageCommandHandler(long chatId) {
        log.info("language command was used");
        SendMessage message = new SendMessage();
        if (userLanguage.get(chatId) == null) {
            message.setText("Выберите язык");
            message.setReplyMarkup(Buttons.inlineMarkup());
        } else {
            message.setText("Ваш язык - " + userLanguage.get(chatId));
            message.setReplyMarkup(Buttons.inlineMarkup());
        }
        message.setChatId(chatId);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    @BotCommandHandler(value = "/help")
    private void helpCommandHandler(long chatId) {
        log.info("help command was used");
        SendMessage message = new SendMessage();
        message.setText(HELP_TEXT);
        message.setChatId(chatId);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    @BotCommandHandler(value = "/start")
    private void startCommandHandler(long chatId) {
        log.info("start command was used");
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(LANGUAGES_TEXT);
        try {
            execute(message);
            log.info("Reply sent");
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public String getBotToken() {
        return config.getBotToken();
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }
}
