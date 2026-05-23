package org.brokenarrow.library.menusettings.requirements;

import org.brokenarrow.library.menusettings.MenuDataRegister;
import org.brokenarrow.library.menusettings.MenuSettingsAddon;
import org.brokenarrow.library.menusettings.command.MenuPlaceholderContext;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class HasMoneyRequirement extends Requirement {
    private final MenuDataRegister menuDataRegister = MenuDataRegister.getInstance();
    private final boolean invert;
    private final String amount;

    public HasMoneyRequirement(boolean invert, String amount) {
        this.invert = invert;
        this.amount = amount;
    }

    @Override
    boolean estimate(@NotNull final Player wiver, @Nullable final MenuPlaceholderContext menuPlaceholderContext) {
        try {
            double parsed = Double.parseDouble(MenuSettingsAddon.setPlaceholders(wiver, this.amount, menuPlaceholderContext));
            return !invert == menuDataRegister.getEconomyProvider().hasAmount(wiver.getUniqueId(), parsed);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return false;
    }
}
