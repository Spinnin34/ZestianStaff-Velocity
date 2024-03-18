package p.zestianstaff.LangConfigUtils;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import p.zestianstaff.utils.ConfigUtils;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class LangManager {

    public static ConfigurationNode config;

    public static void setupConfig(Path dataDirectory, String filename) {
        config = ConfigUtils.getConfig(dataDirectory, filename);
    }

    public static String getPrefix() {
        return config.node("prefix").getString();
    }

    public static void sendMessage(Player player, String message, boolean withPrefix) {
        sendMessage((CommandSource) player, message, withPrefix);
    }

    public static void sendMessage(CommandSource player, String message, boolean withPrefix) {
        String prefix = withPrefix ? getPrefix() : "";
        String messageToSend = prefix + message;

        player.sendMessage(MiniMessage.miniMessage().deserialize((messageToSend)));
    }

    public static void sendMessageKey(Player player, String key, boolean withPrefix) {
        sendMessageKey((CommandSource) player, key, withPrefix);
    }

    public static void sendMessageKey(CommandSource player, String key, boolean withPrefix) {
        String prefix = withPrefix ? getPrefix() : "";
        String messageToSend = prefix + config.node(key).getString();

        player.sendMessage(MiniMessage.miniMessage().deserialize((messageToSend)));
    }

    public static void sendStringListKey(CommandSource player, String key) {
        try {
            List<String> messages = config.node(key).getList(String.class);
            if (messages != null) {
                for (String message : messages) {
                    sendMessage(player, message, false);
                }
            }
            ;
        } catch (ConfigurateException e) {
            return;
        }

    }

    public static List<String> getStringListPlaceholder(String key, String placeholder, String replace, String placeholder2, String replace2) {
        List<String> messages = new ArrayList<>();

        try {
            messages = config.node(key).getList(String.class);
            if (messages != null) {
                return messages.stream().map(message -> (message.replaceAll(placeholder, replace).replaceAll(placeholder2, replace2))).collect(Collectors.toList());
            }
        } catch (ConfigurateException e) {
        }
        return messages;
    }
}
