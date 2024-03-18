package p.zestianstaff;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import p.zestianstaff.Command.*;
import p.zestianstaff.database.SQLManager;
import p.zestianstaff.events.PlayerEvents;
import p.zestianstaff.manager.ConfigManager;
import p.zestianstaff.manager.LangManager;
import p.zestianstaff.manager.StaffModeManager;

import java.nio.file.Path;
import java.util.logging.Logger;


@Plugin(
        id = "zestianstaff",
        name = "ZestianStaff",
        version = "1.0"
)

public class ZestianStaff {
    private SQLManager sql;

    private static Path dataDirectory;
    @Inject
    private final Logger logger;

    private final CommandManager commandManager;

    private final ProxyServer proxy;

    @Inject
    public ZestianStaff(ProxyServer proxy, CommandManager commandManager, Logger logger, @DataDirectory Path dataDirectory_) {
        this.proxy = proxy;
        this.commandManager = commandManager;
        this.logger = logger;

        dataDirectory = dataDirectory_;
        setupConfig();
        initDatabase();
    }

    public static void setupConfig() {
        LangManager.setupConfig(dataDirectory, "lang.yml");
        ConfigManager.setupConfig(dataDirectory, "config.yml");
    }
    private void initDatabase() {
        sql = new SQLManager(this);
    }


    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        //base de datos
        StaffModeManager staffModeManager = new StaffModeManager("https://discord.com/api/webhooks/1194290240895062076/gBa8pTc-rqGjavwsDj2lldp1qXg8wOtSXZkcBKmI0zMSyt4C_2rqC5cPzoOsXuJKx6xj", sql);

        new StaffModeCommand(commandManager, staffModeManager, sql);
        proxy.getCommandManager().register("vstaff", new StaffVelocity(this, proxy));
        new PlayerEvents(proxy, this, staffModeManager);

        new StaffListCommand(this, proxy, staffModeManager);
        proxy.getCommandManager().register("/soporte", new SoporteCommand(this, proxy));
        new StaffTopCommand(sql, commandManager);

        logger.info("ZestianStaff ha sido habilitado");
    }




}



