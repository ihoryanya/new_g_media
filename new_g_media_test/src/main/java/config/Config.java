package config;

import java.io.IOException;
import java.util.Properties;

public class Config {
    private static final String configPath = "/config.properties";

    public static String login;
    public static String password;
    public static String url;
    public static String globalURL;
    public static void loadConfig() throws IOException {
        Properties properties = new Properties();
        properties.load(Config.class.getResourceAsStream(configPath));
        login = properties.getProperty("login");
        password = properties.getProperty("password");
        url = properties.getProperty("url");
        globalURL = "https://" + Config.login  + ":" + Config.password + "@"
                + url.replaceAll("https://", "");
    }
}
