package dev.manere.minichat;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.Set;

public class ChatFormatHandler {
    public static Component format(Player player, Component msg) {
        return MiniChat.papiEnabled()
                /* PAPI found */
                ? MiniMessage.miniMessage()
                .deserialize(Objects.requireNonNull(
                        MiniChat.getPlugin(MiniChat.class).getConfig().getString("format")
                ), papi(player), name(player), displayName(player), message(msg))

                /* PAPI not found */
                : MiniMessage.miniMessage()
                .deserialize(Objects.requireNonNull(
                        MiniChat.getPlugin(MiniChat.class).getConfig().getString("format")
                ), name(player), displayName(player), message(msg));
    }

    public static TagResolver papi(Player player) {
        return TagResolver.resolver(
                Set.of("papi", "placeholder", "placeholderapi", "placeholder-api", "placeholder_api"),
                ((queue, ctx) -> {
                    String papiPlaceholder = queue.popOr("papi tag requires an argument").value();
                    String parsedPlaceholder = PlaceholderAPI.setPlaceholders(player, '%' + papiPlaceholder + '%');

                    Component componentPlaceholder = LegacyComponentSerializer.legacySection()
                            .deserialize(parsedPlaceholder);

                    return Tag.selfClosingInserting(componentPlaceholder);
                })
        );
    }

    public static TagResolver name(Player player) {
        return TagResolver.resolver(
                Set.of("player", "name", "player_name", "player-name"),
                ((queue, ctx) -> Tag.selfClosingInserting(player.name()))
        );
    }

    public static TagResolver displayName(Player player) {
        return TagResolver.resolver(
                Set.of("display", "displayname", "display-name", "display_name"),
                ((queue, ctx) -> Tag.selfClosingInserting(player.displayName()))
        );
    }

    public static TagResolver message(Component msg) {
        return TagResolver.resolver(
                Set.of("message", "text"),
                ((queue, ctx) -> Tag.selfClosingInserting(msg))
        );
    }
}
