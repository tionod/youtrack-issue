package tz.jetbrains.helper;

import lombok.extern.slf4j.Slf4j;
import tz.jetbrains.pojo.YouTrackProperties;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

@Slf4j
public class ConfigHelper {

    private static final String CONFIG_FILE_NAME = "config.properties";
    private static final String RESOURCES_PATH = "src/test/resources/";

    public static YouTrackProperties getYouTrackProperties() {
        Properties configProperties = getProperties();
        YouTrackProperties youTrackProperties = new YouTrackProperties();
        youTrackProperties.setUrl(configProperties.getProperty("youtrack.url"));
        youTrackProperties.setLogin(configProperties.getProperty("youtrack.reporter.login"));
        youTrackProperties.setPassword(configProperties.getProperty("youtrack.reporter.password"));
        return youTrackProperties;
    }

    private static Properties getProperties() {
        Properties properties = new Properties();
        try (FileInputStream inputStream = new FileInputStream(RESOURCES_PATH + CONFIG_FILE_NAME);
             InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
            properties.load(inputStreamReader);
        } catch (IOException e) {
            log.error("Файл {} отсутствует в {}", CONFIG_FILE_NAME, RESOURCES_PATH);
        }
        return properties;
    }
}
