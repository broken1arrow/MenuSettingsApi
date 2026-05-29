package org.brokenarrow.library.menusettings.command.modal;

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
    private final Map<String, String> placeholdersMap = new LinkedHashMap<>();


    public MenuPlaceholderContext(final List<Argument> placeholders, final String[] inputArgs) {
        placeholders.forEach(argument -> {
            this.placeholdersMap.put(argument.getArgument(), "");
        });
        for (String arg : inputArgs) {
            int lastIndex = arg.indexOf(":");
            if (lastIndex < 0) continue;

            for (Argument argument : placeholders) {
                String argKey = argument.getArgument();
                if (arg.startsWith(argKey + ":")) {
                    this.placeholdersMap.put(argKey, arg.substring(lastIndex + 1));
                }
            }
        }
    }

    public String apply(final String text) {
        String result = text;

        for (Map.Entry<String, String> entry : placeholdersMap.entrySet()) {
            result = result.replace("{" + entry.getKey() + "}", entry.getValue());
        }

        return result;
    }
}