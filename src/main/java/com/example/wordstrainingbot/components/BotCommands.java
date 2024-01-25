package com.example.wordstrainingbot.components;

import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.List;

public interface BotCommands {
    List<BotCommand> LIST_OF_COMMANDS = List.of(
            new BotCommand("/start", "start bot"),
            new BotCommand("/help", "bot info"),
            new BotCommand("/languages", "available languages")
    );

    String HELP_TEXT = "Добро пожаловать в бот для повторения слов. " +
            "Для использования данного бота отправьте свою геолокацию в сообщении.";
    String LANGUAGES_TEXT = "Выберите язык. Для этого воспользуйтесь командой /languages";
}