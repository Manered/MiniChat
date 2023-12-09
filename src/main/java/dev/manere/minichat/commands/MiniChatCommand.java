package dev.manere.minichat.commands;

import dev.manere.minichat.MiniChat;
import dev.manere.utils.command.builder.dispatcher.CommandContext;
import dev.manere.utils.command.builder.dispatcher.CommandDispatcher;
import dev.manere.utils.command.builder.dispatcher.SuggestionDispatcher;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MiniChatCommand implements CommandDispatcher, SuggestionDispatcher {
    @Override
    public boolean run(@NotNull CommandContext ctx) {
        if (!ctx.sender().hasPermission("minichat.command")) {
            ctx.sender().sendMessage(MiniChat.val("commands.minichat.no_permission"));
            return true;
        }

        if (!ctx.senderIsPlayer()) {
            ctx.sender().sendMessage(MiniChat.val("commands.minichat.sender_is_console"));
            return true;
        }

        Player player = ctx.player();

        if (ctx.rawArgs().isEmpty()) {
            player.sendMessage(MiniChat.vals("commands.minichat.info"));
            return true;
        }

        if (ctx.rawArgs().size() == 1 && ctx.rawArgs().get(0).equalsIgnoreCase("help")) {
            player.sendMessage(MiniChat.vals("commands.minichat.help"));
            return true;
        }

        if (ctx.rawArgs().get(0).equalsIgnoreCase("format")) {
            if (ctx.rawArgs().size() <= 1) {
                player.sendMessage(MiniChat.vals("commands.minichat.help"));
                return true;
            }

            List<String> formatArgs = new ArrayList<>(ctx.rawArgs());
            formatArgs.remove(0);

            MiniChat.set("format", StringUtils.join(formatArgs.toArray(), ' '));

            player.sendMessage(MiniChat.val("commands.minichat.format_changed_successfully"));
            return true;
        }

        if (ctx.rawArgs().get(0).equalsIgnoreCase("reload")) {
            MiniChat.reload();
            player.sendMessage(MiniChat.val("commands.minichat.reloaded_successfully"));
            return true;
        }

        player.sendMessage(MiniChat.vals("commands.minichat.help"));
        return true;
    }

    @Override
    public @Nullable List<String> suggest(@NotNull CommandContext ctx) {
        int size = ctx.rawArgs().size();

        if (size == 1) {
            return List.of("help", "format", "reload");
        } else if (size >= 2) {
            if (ctx.rawArgs().get(0).equalsIgnoreCase("format")) {
                return List.of("<format>");
            }
        }

        return null;
    }
}
