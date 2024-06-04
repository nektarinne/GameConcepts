package com.nektarinne.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.Map;

public class Utils {

    private static final Logger logger = LogManager.getLogger(Utils.class);

    public <T extends Comparable<T>> void printMap(Map<T, Integer> map, String description) {
        logger.info(description);
        printMap(map);
    }

    public <T extends Comparable<T>> void printMap(Map<T, Integer> map) {
        if (map.isEmpty()) {
            return;
        }
        int numberOfChar = Collections.max(map.values()).toString().length();
        printMap(map, numberOfChar);
    }

    public <T extends Comparable<T>> void printMap(Map<T, Integer> map, int numberOfChar) {
        if (map.isEmpty()) {
            return;
        }
        String format = "%" + numberOfChar + "d x %s";
        map.entrySet()
                .stream()
                .sorted(Map.Entry.<T, Integer>comparingByValue().thenComparing(Map.Entry.comparingByKey()))
                .forEach(entry -> logger.info(format.formatted(entry.getValue(), entry.getKey())));
    }
}
