package dev.manere.minichat.listeners;

import dev.manere.minichat.ChatFormatHandler;
import dev.manere.utils.command.annotation.AutoRegister;
import dev.manere.utils.event.EventListener;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.event.EventPriority;
import org.jetbrains.annotations.NotNull;

@AutoRegister
public class MiniChatListener extends EventListener<AsyncChatEvent> {
    public MiniChatListener() {
        super(AsyncChatEvent.class, EventPriority.HIGHEST);
    }

    @Override
    protected void execute(@NotNull AsyncChatEvent event) {
        event.renderer((src, srcName, msg, viewer) -> ChatFormatHandler.format(src, msg));
    }
}
