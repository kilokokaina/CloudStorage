package com.work.cloudstorage.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@ApplicationScope
public class TranslitService {

    private final Map<String, String> alphabetMap = new HashMap<>();

    public TranslitService() {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/translit.conf"))) {
            String line;
            while((line = reader.readLine()) != null) {
                String[] keyValue = line.split(" ");
                alphabetMap.put(keyValue[0], keyValue[1]);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public String translitCyrillic(String inputString) {
        for (int i = 0; i < inputString.length(); i++) {
            char inputChar = inputString.charAt(i);
            if (this.alphabetMap.containsKey(String.valueOf(inputChar))) {
                String replaceChar = this.alphabetMap.get(String.valueOf(inputChar));
                inputString = inputString.replace(inputChar, replaceChar.charAt(0));
            }
        }

        return inputString;
    }

}
