package com.example.imageannotationapi.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Utils {
    private static final Logger logger = LoggerFactory.getLogger(Utils.class);

    private Utils() {}

    public static void timeoutToSimulateLongProcess() {
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            logger.error("Error occurred while simulating long process", e);
        }
    }
}
