package p.zestianstaff.Command;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.ProxyServer;
import p.zestianstaff.ZestianStaff;
import p.zestianstaff.manager.LangManager;

import java.util.List;

public class StaffVelocity implements SimpleCommand {

    private final ZestianStaff plugin;
    private final ProxyServer proxy;

    public StaffVelocity(ZestianStaff plugin, ProxyServer proxy) {
        this.plugin = plugin;
        this.proxy = proxy;
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();

        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            ZestianStaff.setupConfig();
            LangManager.sendMessageKey(source, "reload_success", true);
        } else {

            LangManager.sendMessageKey(source, "reload_help", true);
        }
    }

    @Override
    public boolean hasPermission(final Invocation invocation) {
        return invocation.source().hasPermission("zestian.staff.reload");
    }

    @Override
    public List<String> suggest(final Invocation invocation) {
        String[] args = invocation.arguments();
        if (args.length == 0) {
            return List.of("reload");
        }
        return List.of();
    }
}