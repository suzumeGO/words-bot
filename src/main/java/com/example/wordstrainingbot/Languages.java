package com.example.wordstrainingbot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum Languages {
    ENGLISH("Английский", "en"),
    KOREAN("Корейский", "ko"),
    JAPANESE("Японский", "ja"),
    CHINESE("Китайский", "zh-cn");
    private String name;
    private String code;
    Languages(String name, String code) {
        this.name = name;
        this.code = code;

    }

    public static List<String> getNames() {
        List<String> names = new ArrayList<>();
        Arrays.stream(Languages.values()).forEach(language -> names.add(language.getName()));
        return names;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }
}
