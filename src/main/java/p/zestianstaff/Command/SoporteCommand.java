package p.zestianstaff.Command;

import com.google.inject.Inject;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import p.zestianstaff.ZestianStaff;
import p.zestianstaff.manager.LangManager;
import p.zestianstaff.message.MessageUtils;

import java.util.UUID;

public class SoporteCommand implements SimpleCommand {

    private final ZestianStaff plugin;
    private final ProxyServer proxy; // Inyectado desde Main

    @Inject
    public SoporteCommand(ZestianStaff plugin, ProxyServer proxy) {
        this.plugin = plugin;
        this.proxy = proxy;
    }

    @Override
    public void execute(Invocation invocation) {
        if (!(invocation.source() instanceof Player)) {
            LangManager.sendMessageKey(invocation.source(), "only_player", true);
            return;
        }

        Player jugador = (Player) invocation.source();
        UUID jugadorUUID = jugador.getUniqueId();

        // Verificar permiso
        if (!jugador.hasPermission("zestianstaff.soporte.use")) {
            LangManager.sendMessageKey(jugador, "no_perms", true);
            return;
        }

        // Obtener la información del servidor actual
        ServerInfo servidorActual = jugador.getCurrentServer().get().getServerInfo();

        // Obtener la dirección del servidor actual
        String direccionServidor = servidorActual.getAddress().getHostString();

        // Construir el mensaje con la dirección del servidor actual
        String[] lineasMensaje = {
                " ",
                "§c§lZestian Soporte",
                " ",
                "§7Se neceista ayuda §c" + direccionServidor,
                "Haz clic para unirte al servidor",
                " "
        };

        // Enviar mensaje solo a jugadores con el mismo permiso
        for (Player onlinePlayer : proxy.getAllPlayers()) {
            for (String linea : lineasMensaje) {
                onlinePlayer.sendMessage(Component.text(MessageUtils.sendCenteredMessage(linea))
                        .clickEvent(ClickEvent.runCommand("/server " + direccionServidor)));
            }
        }
    }

    @Override
    public boolean hasPermission(final Invocation invocation) {
        return invocation.source().hasPermission("zestianstaff.soporte.use");
    }
}
