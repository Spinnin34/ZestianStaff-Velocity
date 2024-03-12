package p.zestianstaff.LangConfigUtils;

import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;
import p.zestianstaff.LangConfigUtils.ResourceUtils;

import java.io.File;
import java.nio.file.Path;

public class ConfigUtils {


    public static YamlConfigurationLoader getLoader(Path dataDirectory, String filename) {
        File copyFile = new File(dataDirectory.toFile(), filename);
        if (!copyFile.exists()) {
            copyFile.getParentFile().mkdirs();
            ResourceUtils.copyResource(filename, copyFile);
        }

        return YamlConfigurationLoader.builder().file(copyFile).build();
    }

    public static ConfigurationNode getConfig(Path dataDirectory, String filename) {
        YamlConfigurationLoader loader = getLoader(dataDirectory, filename);
        try {
            return loader.load();
        } catch (ConfigurateException e) {
            e.printStackTrace();
        }
        return null;
    }
}
