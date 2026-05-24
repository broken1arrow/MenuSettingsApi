package org.brokenarrow.library.menusettings.command;

import org.brokenarrow.library.menusettings.clickactions.ClickActionHandler;
import org.brokenarrow.library.menusettings.requirements.RequirementsContext;

import java.util.List;

public class CommandHandlerSettings {
    private List<String> openCommands;
    private List<?> openArguments;
    private RequirementsContext openArgsRequirement;
    private ClickActionHandler openCommandsAction;
    private List<String> message;
    private RequirementsContext openRequirement;

    public RequirementsContext getOpenRequirement() {
        return openRequirement;
    }

    public List<String> getOpenCommands() {
        return openCommands;
    }

    public List<?> getOpenArguments() {
        return openArguments;
    }

    public RequirementsContext getOpenArgsRequirement() {
        return openArgsRequirement;
    }

    public ClickActionHandler getOpenCommandsAction() {
        return openCommandsAction;
    }

    public List<String> getMessage() {
        return message;
    }

    public void setOpenCommands(List<String> openCommands) {
        this.openCommands = openCommands;
    }

    public void setOpenArguments(List<?> openArguments) {
        this.openArguments = openArguments;
    }

    public void setOpenArgsRequirement(RequirementsContext openArgsRequirement) {
        this.openArgsRequirement = openArgsRequirement;
    }

    public void setOpenAction(final ClickActionHandler openCommandsAction) {
        this.openCommandsAction = openCommandsAction;
    }

    public void setArgsMissingMessage(List<String> message) {
        this.message = message;
    }

    public void setOpenRequirement(final RequirementsContext openRequirement) {
        this.openRequirement = openRequirement;
    }
}