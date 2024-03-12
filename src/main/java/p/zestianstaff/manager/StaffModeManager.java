package p.zestianstaff.manager;

import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import p.zestianstaff.database.SQLManager;
import p.zestianstaff.utils.Date;
import p.zestianstaff.utils.DiscordWebhookMessage;
import p.zestianstaff.utils.WebhookUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class StaffModeManager {

    private final Map<UUID, Long> staffModePlayers;
    private final String webHookUrl;
    private final SQLManager sqlManager;


    public StaffModeManager(String webHookUrl, SQLManager sqlManager) {
        this.sqlManager = sqlManager;
        this.webHookUrl = webHookUrl;
        staffModePlayers = new HashMap<>();
    };

    public void activateStaffMode(Player player) {
        UUID playerUUID = player.getUniqueId();

        if (staffModePlayers.containsKey(playerUUID)) {
            LangManager.sendStringListKey(player, "2x_activate_staffmode");
            return;
        }

        staffModePlayers.put(playerUUID, System.currentTimeMillis());
        LangManager.sendStringListKey(player, "activate_staffmode");
    }

    public void deactivateStaffMode(Player player) {
        UUID playerUUID = player.getUniqueId();

        if (!staffModePlayers.containsKey(playerUUID)) {
            return;
        }

        long timeInStaffModeMillis = System.currentTimeMillis() - staffModePlayers.get(playerUUID);


        // Calcula horas, minutos y segundos
        int hours = (int) (timeInStaffModeMillis / 3600000);
        int minutes = (int) ((timeInStaffModeMillis % 3600000) / 60000);
        int seconds = (int) ((timeInStaffModeMillis % 60000) / 1000);

        // Actualiza la base de datos
        sqlManager.agregarTiempoJugado(player.getUsername(), hours, minutes, seconds);


        staffModePlayers.remove(playerUUID);


        if (timeInStaffModeMillis < 600000) {
            LangManager.sendStringListKey(player, "cooldown");
            return;
        }



        // Obtener datos del jugador
        String playerName = player.getUsername();
        long startTime = System.currentTimeMillis() - timeInStaffModeMillis;
        long endTime = System.currentTimeMillis();

        LangManager.sendStringListKey(player, "final_staffmode");
        player.sendMessage(Component.text("§fTiempo en proporción: §c" + Date.formatTime(timeInStaffModeMillis)));
        LangManager.sendStringListKey(player, "developer");

        // Envía el mensaje al webhook al desconectarse
        sendDiscordWebhook(playerName, getPlayerGroup(player.getUniqueId()), startTime, endTime, timeInStaffModeMillis);
    }

    public Boolean isStaffModeActive(UUID uuid) {
        return  staffModePlayers.containsKey(uuid);
    }

    private void sendDiscordWebhook(String playerName, String playerGroup, long startTime, long endTime, long timeInStaffModeMillis) {
        WebhookUtil.sendDiscordWebhook(webHookUrl, createWebhookMessage(playerName, playerGroup, startTime, endTime, timeInStaffModeMillis));
    }

    private DiscordWebhookMessage createWebhookMessage(String playerName, String playerGroup, long startTime, long endTime, long timeInStaffModeMillis) {
        // Obtén el tiempo formateado
        String formattedTime = Date.formatTime(timeInStaffModeMillis);
        String formattedStartTime = Date.formatDate(startTime);
        String formattedEndTime = Date.formatDate(endTime);

        // Construye el mensaje del webhook con los datos proporcionados
        return new DiscordWebhookMessage(
                null, // You might want to add text content here if needed
                List.of(new DiscordWebhookMessage.Embed(
                        "📢 Ha finalizado una sesión de moderación",
                        5814783,
                        List.of(
                                new DiscordWebhookMessage.Embed.Field("🧰 Rango", "> " + playerGroup, false),
                                new DiscordWebhookMessage.Embed.Field("⏰ Tiempo", "> " + formattedTime, false),
                                new DiscordWebhookMessage.Embed.Field("📆 Fecha Inicio", "> " + formattedStartTime, true),
                                new DiscordWebhookMessage.Embed.Field("📆 Fecha fin", "> " + formattedEndTime, true)
                        ),
                        new DiscordWebhookMessage.Embed.Author(playerName, "https://cdn.discordapp.com/attachments/1076769716103811132/1217132638662557716/3fb1362dc39995e21583a4547f45c77c.png?ex=6602ea1a&is=65f0751a&hm=49d5affbf0f6477c31dc298b8c34ce3c621aa300ac7ac2363c513a9eacd56e26&"),
                        new DiscordWebhookMessage.Embed.Footer("Developer By the Spinnin", "https://cdn.discordapp.com/attachments/1076769716103811132/1217132442083655681/d88a8aa9df7b0a680f334b37a2332624.png?ex=6602e9eb&is=65f074eb&hm=5c297bfc80353465c882033bb4c44653acf78519a1dd0112fc0d3dec1c2dc199&"),
                        new DiscordWebhookMessage.Embed.Thumbnail("https://visage.surgeplay.com/bust/" + playerName + ".png")
                ))
        );
    }

    public String getPlayerGroup(UUID uuid ){
        LuckPerms api = LuckPermsProvider.get();
        User user = api.getUserManager().getUser(uuid);
        if (user == null) return "";
        return user.getCachedData().getMetaData().getPrimaryGroup();
    }

    public String getPlayerPrefix(UUID uuid ){
        LuckPerms api = LuckPermsProvider.get();
        User user = api.getUserManager().getUser(uuid);
        if (user == null) return "";
        return user.getCachedData().getMetaData().getPrefix();
    }
}
