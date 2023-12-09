package dev.manere.minichat;

import dev.manere.minichat.commands.MiniChatCommand;
import dev.manere.utils.command.CommandType;
import dev.manere.utils.command.builder.CommandBuilder;
import dev.manere.utils.library.Utils;
import dev.manere.utils.server.ServerUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class MiniChat extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        Utils.init(this);
        initConfig();
        initCommands();
    }

    void initCommands() {
        if (booleanVal("commands.minichat.enabled")) {
            CommandBuilder.command("minichat", CommandType.COMMAND_MAP)
                    .description("Manage the MiniChat plugin via commands.")
                    .executes(new MiniChatCommand())
                    .completes(new MiniChatCommand())
                    .build()
                    .register();
        }
    }

    void initConfig() {
        getConfig().options()
                .setHeader(header())
                .parseComments(true)
                .copyDefaults(true);

        saveDefaultConfig();
        saveConfig();
    }

    public static List<String> header() {
        return List.of(
                "╔═ HEADER ═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=══╗",
                "║                                                                       ║",
                "║    ███╗   ███╗██╗███╗   ██╗██╗     ██████╗██╗  ██╗ █████╗ ████████╗   ║",
                "║    ████╗ ████║██║████╗  ██║██║    ██╔═=═=╝██║  ██║██╔═=██╗╚═=██╔═=╝   ║",
                "║    ██╔████╔██║██║██╔██╗ ██║██║    ██║     ███████║███████║   ██║      ║",
                "║    ██║╚██╔╝██║██║██║╚██╗██║██║    ██║     ██╔═=██║██╔═=██║   ██║      ║",
                "║    ██║ ╚═╝ ██║██║██║ ╚████║██║    ╚██████╗██║  ██║██║  ██║   ██║      ║",
                "║    ╚═╝     ╚═╝╚═╝╚═╝  ╚═=═╝╚═╝     ╚═=═=═╝╚═╝  ╚═╝╚═╝  ╚═╝   ╚═╝      ║",
                "║                                                                       ║",
                "║   ╔═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═╗   ║",
                "║   ║ A chat format editor plugin made specifically for MiniMessage ║   ║",
                "║   ║         Full support for MiniMessage, Placeholder API         ║   ║",
                "║   ╚═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═╝   ║",
                "╚═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═╝",
                "╔═ GITHUB ═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=══╗",
                "║                      github.com/Manered/MiniChat                      ║",
                "╚═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═╝",
                "=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=",
                "Placeholders:",
                "1. <[papi | placeholderapi | placeholder | placeholder-api | placeholder_api]:papi_tag>",
                "   Requirement: Placeholder API plugin installed on your server.",
                "   Example: <papi:luckperms_prefix>",
                "2. <[player | name | player-name | player_name]>",
                "   Requirement: None",
                "   Example: <player>",
                "3. <[display | displayname | display-name | display_name]>",
                "   Requirement: None",
                "   Example: <displayname>",
                "4. <[message | text]>",
                "   Requirement: None",
                "   Example: <message>",
                "=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═=═="
        );
    }

    public static boolean booleanVal(String key) {
        MiniChat plugin = JavaPlugin.getPlugin(MiniChat.class);
        return plugin.getConfig().getBoolean(key);
    }

    public static Component val(String key) {
        MiniChat plugin = JavaPlugin.getPlugin(MiniChat.class);
        String raw = plugin.getConfig().getString(key);

        if (raw == null) {
            return Component.text(key);
        }

        return MiniMessage.miniMessage().deserialize(raw);
    }

    public static Component vals(String key) {
        MiniChat plugin = JavaPlugin.getPlugin(MiniChat.class);
        List<String> rawList = plugin.getConfig().getStringList(key);

        List<Component> components = new ArrayList<>();
        for (String raw : rawList) {
            components.add(MiniMessage.miniMessage().deserialize(raw));
        }

        return Component.join(JoinConfiguration.newlines(), components);
    }

    public static void set(String key, Object val) {
        MiniChat plugin = JavaPlugin.getPlugin(MiniChat.class);
        plugin.getConfig().set(key, val);
        plugin.saveConfig();
    }

    public static void reload() {
        MiniChat plugin = JavaPlugin.getPlugin(MiniChat.class);
        plugin.reloadConfig();
    }

    public static boolean papiEnabled() {
        return ServerUtils.plugin("PlaceholderAPI") != null;
    }
}
