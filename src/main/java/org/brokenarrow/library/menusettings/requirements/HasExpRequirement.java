package org.brokenarrow.library.menusettings.requirements;

import org.brokenarrow.library.menusettings.MenuSettingsAddon;
import org.brokenarrow.library.menusettings.command.MenuPlaceholderContext;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static org.brokenarrow.library.menusettings.utillity.ExperienceUtillity.getPlayerExp;

public class HasExpRequirement extends Requirement {

    private final String experiencesAmount;
    private final boolean inverted;
    private final boolean checkLevel;

    public HasExpRequirement(String experiencesAmount, boolean checkLevel, boolean inverted) {
        this.experiencesAmount = experiencesAmount;
        this.inverted = inverted;
        this.checkLevel = checkLevel;
    }

    @Override
    boolean estimate(@NotNull final Player wiver, @Nullable final MenuPlaceholderContext menuPlaceholderContext) {
        int playerExperiens = this.checkLevel ? wiver.getLevel() : getPlayerExp(wiver);
        int exp = 0;
        try {
            exp = Integer.parseInt(MenuSettingsAddon.setPlaceholders(wiver, this.experiencesAmount, menuPlaceholderContext));
        } catch (NumberFormatException exception) {
            exception.printStackTrace();
        }
        return inverted == (playerExperiens < exp);
    }
}
