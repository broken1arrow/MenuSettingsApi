package org.brokenarrow.library.menusettings.command;

import org.brokenarrow.library.menusettings.requirements.RequirementsContext;

import java.util.List;

public class CommandHandlerSettings {
    List<String> openCommands;
    List<?> openArguments;
    RequirementsContext openArgsRequirement;


    public List<String> getOpenCommands() {
        return openCommands;
    }

    public void setOpenCommands(List<String> openCommands) {
        this.openCommands = openCommands;
    }

    public List<?> getOpenArguments() {
        return openArguments;
    }

    public void setOpenArguments(List<?> openArguments) {
        this.openArguments = openArguments;
    }

    public RequirementsContext getOpenArgsRequirement() {
        return openArgsRequirement;
    }

    public void setOpenArgsRequirement(RequirementsContext openArgsRequirement) {
        this.openArgsRequirement = openArgsRequirement;
    }
}