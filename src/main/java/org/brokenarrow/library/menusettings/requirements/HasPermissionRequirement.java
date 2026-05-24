package org.brokenarrow.library.menusettings.requirements;

import org.brokenarrow.library.menusettings.MenuSettingsAddon;
import org.brokenarrow.library.menusettings.command.MenuPlaceholderContext;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class HasPermissionRequirement extends Requirement {

    private final String permission;
    private final boolean inverted;

    public HasPermissionRequirement(String permission, boolean inverted) {
        this.permission = permission;
        this.inverted = inverted;
    }

    @Override
    boolean estimate(@NotNull final Player wiver, @Nullable final MenuPlaceholderContext menuPlaceholderContext) {
        return this.inverted != wiver.hasPermission(MenuSettingsAddon.setPlaceholders(wiver, permission, menuPlaceholderContext));
    }

    @Override
    public String toString() {
        return "HasPermissionRequirement{" +
                "permission='" + permission + '\'' +
                ", inverted=" + inverted +
                "} " + super.toString();
    }
}
