package com.example.wordstrainingbot;

import com.example.wordstrainingbot.DTO.QuizDTO;
import com.example.wordstrainingbot.DTO.WordDTO;
import com.example.wordstrainingbot.DTO.WordsListDTO;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@SuppressWarnings("unused")
@Component
public class Bot extends TelegramLongPollingBot implements BotCommands {
    private static final String INCORRECT_MSG = "Некорректное сообщение";
    private static final String WORD_IS_ADDED_MSG = "Слово добавлено";
    private static final String WORD_ALREADY_EXISTS_MSG = "Слово уже существует";
    private static final String SELECT_LANG_MSG = "Выберите язык";
    private final BotConfig config;
    private final WordsProxy proxy;
    private final Map<Long, UserDetails> userDetails;
    public Bot(BotConfig config, WordsProxy proxy) {
        this.config = config;
        this.proxy = proxy;
        userDetails = new HashMap<>();
        try {
            this.execute(new SetMyCommands(LIST_OF_COMMANDS, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public void onUpdateReceived(@NotNull Update update) {
        if (update.hasCallbackQuery()) {
            handleCallbackQuery(update);
        } else if (update.hasMessage() && update.getMessage().hasText()) {
            handleTextMessage(update);
        } else {
            handleIncorrectMessage(update);
        }
    }
    private void createUserDetails(long chatId) {
        if (userDetails.get(chatId) == null) {
            userDetails.put(chatId, new UserDetails());
        }
    }
    private void handleTextMessage(@NotNull Update update) {
        long chatId;
        String receivedMessage;
        chatId = update.getMessage().getChatId();
        receivedMessage = update.getMessage().getText();
        createUserDetails(chatId);
        if (LIST_OF_COMMANDS.stream().anyMatch(x -> x.getCommand().equals(receivedMessage))) {
            answerUtils(receivedMessage, chatId);
        } else {
            handleWord(update, chatId);
        }
    }

    private void handleCallbackQuery(@NotNull Update update) {
        if (Buttons.getPages().contains(update.getCallbackQuery().getData())) {
            handlePages(update);
        }
        if (Languages.getNames().contains(update.getCallbackQuery().getData())) {
            setLanguage(update);
        }
    }

    private void handlePages(@NotNull Update update) {
        long chatId;
        chatId = update.getCallbackQuery().getMessage().getChatId();
        createUserDetails(chatId);
        switch (update.getCallbackQuery().getData()) {
            case "Следующая" -> {
                int nextPage = userDetails.get(chatId).getCurrentPage() + 1;
                if (nextPage <= userDetails.get(chatId).getTotalPages()) {
                    sendWordsListMessage(chatId, nextPage);
                }
            }
            case "Предыдущая" -> {
                if (userDetails.get(chatId).getCurrentPage() != 1) {
                    sendWordsListMessage(chatId, userDetails.get(chatId).getCurrentPage() - 1);
                }
            }
            case "Первая" -> sendWordsListMessage(chatId, 1);
            case "Последняя" -> sendWordsListMessage(chatId, userDetails.get(chatId).getTotalPages());
        }
    }

    private void handleIncorrectMessage(@NotNull Update update) {
        long chatId;
        chatId = update.getMessage().getChatId();
        createUserDetails(chatId);
        SendMessage message = new SendMessage();
        message.setText(INCORRECT_MSG);
        message.setChatId(chatId);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleWord(@NotNull Update update, long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        if (userDetails.get(chatId).getLanguage() != null) {
            try {
                proxy.addWord(update.getMessage().getText(), userDetails.get(chatId).getLanguage().getCode(), chatId);
                message.setText(WORD_IS_ADDED_MSG);
            } catch (Exception e) {
                message.setText(WORD_ALREADY_EXISTS_MSG);
            }
        } else {
            message.setText(SELECT_LANG_MSG);
        }
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void setLanguage(@NotNull Update update) {
        String receivedMessage;
        long chatId;
        chatId = update.getCallbackQuery().getMessage().getChatId();
        createUserDetails(chatId);
        receivedMessage = update.getCallbackQuery().getData();
        Languages lang = Arrays.stream(Languages.values()).filter(language -> language.getName().equals(receivedMessage)).findFirst().get();
        userDetails.get(chatId).setLanguage(lang);
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
        userDetails.get(chatId).setCurrentPage(1);
        sendWordsListMessage(chatId, 1);

    }

    private void sendWordsListMessage(long chatId, int page) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        WordsListDTO wordsListDTO = proxy.getWords(chatId, userDetails.get(chatId).getLanguage().getCode(), page);
        message.setText(formWordsListMessage(chatId, wordsListDTO));
        message.setReplyMarkup(Buttons.navigationButtons());
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private String formWordsListMessage(long chatId, WordsListDTO wordsListDTO) {
        StringBuilder stringBuilder = new StringBuilder();
        userDetails.get(chatId).setCurrentPage(wordsListDTO.getPagination().getCurrentPage());
        userDetails.get(chatId).setTotalPages(wordsListDTO.getPagination().getLastPage());
        int totalPages = wordsListDTO.getPagination().getLastPage();
        for (WordDTO word : wordsListDTO.getData()) {
            stringBuilder
                    .append(word.getWord())
                    .append(" - ")
                    .append(word.getTranslate())
                    .append(";")
                    .append("\n");
        }
        stringBuilder.append("\n");
        stringBuilder.append("Страница ").append(wordsListDTO.getPagination().getCurrentPage()).append("/").append(totalPages);
        return stringBuilder.toString();
    }

    @BotCommandHandler(value = "/train")
    private void studyCommandHandler(long chatId) {
        QuizDTO quizDTO = proxy.getWeakestQuiz(chatId, userDetails.get(chatId).getLanguage().getCode());
        
    }

    private void sendQuizMessage(long chatId, List<String> answers, WordDTO word) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(word.getWord());
        message.setReplyMarkup(Buttons.translationVariantButtons(answers));
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @BotCommandHandler(value = "/languages")
    private void languageCommandHandler(long chatId) {
        log.info("language command has been used");
        SendMessage message = new SendMessage();
        if (userDetails.get(chatId).getLanguage() == null) {
            message.setText(SELECT_LANG_MSG);
            message.setReplyMarkup(Buttons.languageButtons());
        } else {
            message.setText("Ваш язык - " + userDetails.get(chatId).getLanguage().getName());
            message.setReplyMarkup(Buttons.languageButtons());
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
        log.info("help command has been used");
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
        log.info("start command has been used");
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
