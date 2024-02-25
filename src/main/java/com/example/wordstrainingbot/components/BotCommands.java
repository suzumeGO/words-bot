package com.example.wordstrainingbot.components;

import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.List;

public interface BotCommands {
    List<BotCommand> LIST_OF_COMMANDS = List.of(
            new BotCommand("/start", "start bot"),
            new BotCommand("/languages", "choose language"),
            new BotCommand("/words", "word list"),
            new BotCommand("/train", "train words"),
            new BotCommand("/reverse", "reverse training"),
            new BotCommand("/help", "bot info")

    );

    String HELP_TEXT = """
            Добро пожаловать в бот для повторения слов.
            Для добавления слова выберите язык и отправьте его в чат.
            Для просмотра списка ваших слов воспользуйтесь командой /words Для тренировки слов воспользуйтесь командой /train или /reverse (обратный режим)""";
    String LANGUAGES_TEXT = "Выберите язык. Для этого воспользуйтесь командой /languages";
}