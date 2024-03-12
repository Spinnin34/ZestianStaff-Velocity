package p.zestianstaff.manager;

import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import p.zestianstaff.database.DatabaseCredentials;
import p.zestianstaff.LangConfigUtils.ConfigUtils;

import java.nio.file.Path;


public class ConfigManager {

    public static ConfigurationNode config;

    public static void setupConfig(Path dataDirectory, String filename) {
        config = ConfigUtils.getConfig(dataDirectory, filename);
    }

    public static DatabaseCredentials getDBHostname() {
        try {
            return config.node("mysql").get(DatabaseCredentials.class);
        } catch (ConfigurateException e) {
            return null;
        }
    }


}
