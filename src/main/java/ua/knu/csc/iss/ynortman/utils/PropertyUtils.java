package ua.knu.csc.iss.ynortman.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Slf4j
public class PropertyUtils {
    public static String getProperty(String property) {
        String prop = null;
        try (InputStream inputStream = PropertyUtils.class.getResourceAsStream("/application.properties");
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(property)) {
                    prop = line.split("=")[1].trim();
                    break;
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
        return prop;
    }
}
