package events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.junit.experimental.categories.Categories;

public class Joinevent extends JavaPlugin implements Listener{

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        String playerName = event.getPlayer().getName();
        getLogger().info("El jugador " + playerName + " se ha unido a la comunidad #01");
    }
}
