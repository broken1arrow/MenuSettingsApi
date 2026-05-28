package org.brokenarrow.library.menusettings.command.modal;

import org.bukkit.entity.Player;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
/**
 * Represents the execution context for a menu interaction.
 *
 * <p>This context holds a mapping of placeholder keys to values, typically
 * derived from command arguments at the time the menu is opened or interacted with.</p>
 *
 * <p>Placeholders (e.g. {@code {player}}) are resolved using this context and
 * applied to all menu-related content, including:</p>
 * <ul>
 *   <li>Requirement evaluations</li>
 *   <li>Command execution</li>
 *   <li>Display names and lore</li>
 * </ul>
 *
 * <p>This ensures that all parts of the menu pipeline operate on a consistent
 * set of resolved values during a single execution.</p>
 *
 * <p>Note that this context only affects internal placeholder replacement.
 * External systems or plugins performing asynchronous updates may not reflect
 * these values immediately.</p>
 *
 * <p>The context is immutable after creation.</p>
 */
public class MenuPlaceholderContext {
    private final Map<String, String> placeholders = new LinkedHashMap<>();
    private final Player player;

    public MenuPlaceholderContext(final Player player, final List<Argument> placeholders, final String[] inputArgs) {
        this.player = player;
        int i = 0;
        for (Argument entry : placeholders) {
            String value = (i < inputArgs.length) ? inputArgs[i] : "";
            this.placeholders.put(entry.getArgument(), value);
            i++;
        }
    }

    public String apply(final String text) {
        String result = text;

        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            result = result.replace("{" + entry.getKey() + "}", entry.getValue());
        }

        return result;
    }
}