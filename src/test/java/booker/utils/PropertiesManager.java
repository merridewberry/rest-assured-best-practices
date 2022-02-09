package booker.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

public class PropertiesManager {

    private static final Logger log = (Logger) LogManager.getLogger(MethodHandles.lookup().lookupClass());

    static final String pathToProperties = "config.properties";

    public static String getProperty(String property) {
        Properties properties;
        try {
            InputStream is = new FileInputStream(pathToProperties);
            properties = new Properties();
            properties.load(is);
            is.close();
            return properties.getProperty(property);
        } catch (IOException e) {
            log.error("File " + pathToProperties + " not found");
        }
        return property;
    }

    public static void setProperty(String key, String value) {
        Properties properties;
        try {
            InputStream is = new FileInputStream(pathToProperties);
            properties = new Properties();
            properties.load(is);
            is.close();
            FileOutputStream os = new FileOutputStream(pathToProperties);
            properties.setProperty(key, value);
            properties.store(os, null);
            os.close();
        } catch (IOException e) {
            log.error("File " + pathToProperties + " not found");
        }
    }

}
