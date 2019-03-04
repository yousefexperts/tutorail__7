package blockchains.iaas.experts.core.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class Configuration {

    private static Configuration instance;
    private static final String PROPERTIES_FILE = "config.properties";
    public Properties properties = null;

    private Configuration(){

    }

    private void readProperties() {
        properties = new Properties();
        final InputStream inputStream = getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE);
        if (inputStream != null) {
            try {
                properties.load(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Configuration getInstance(){
        if(instance == null) {
            instance = new Configuration();
            instance.readProperties();
        }

        return instance;
    }
}
