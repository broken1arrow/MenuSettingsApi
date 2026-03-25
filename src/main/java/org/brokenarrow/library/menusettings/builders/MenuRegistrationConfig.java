package org.brokenarrow.library.menusettings.builders;

import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.brokenarrow.library.menusettings.utillity.MenuActionHandler;

public class MenuRegistrationConfig {
    private  BukkitAudiences audiences;
    private  boolean oneFile;
    private  boolean generateDefaultFiles;
    private  MenuActionHandler actionHandler;

    public boolean isOneFile() {
        return oneFile;
    }

    public void setOneFile(boolean oneFile) {
        this.oneFile = oneFile;
    }

    public boolean isGenerateDefaultFiles() {
        return generateDefaultFiles;
    }

    public void setGenerateDefaultFiles(boolean generateDefaultFiles) {
        this.generateDefaultFiles = generateDefaultFiles;
    }

    public MenuActionHandler getActionHandler() {
        return actionHandler;
    }

    public void setActionHandler(MenuActionHandler actionHandler) {
        this.actionHandler = actionHandler;
    }

    public BukkitAudiences getAudiences() {
        return audiences;
    }

    public void setAudiences(BukkitAudiences audiences) {
        this.audiences = audiences;
    }
}