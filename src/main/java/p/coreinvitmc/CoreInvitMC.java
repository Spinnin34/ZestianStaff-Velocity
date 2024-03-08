package p.coreinvitmc;

import org.bukkit.plugin.java.JavaPlugin;

public final class CoreInvitMC extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("El plugin a sido inciado correctamente");

        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        getLogger().info("El plugin a sido apagado");
        // Plugin shutdown logic
    }
}
