package org.brokenarrow.library.menusettings.command.modal;

import org.brokenarrow.library.menusettings.requirements.RequirementsContext;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class CommandData {
    private final List<Argument> argumentList = new ArrayList<>();
    private RequirementsContext argsRequirement;
    private String overridePermission;

    public List<Argument> getArgumentsList() {
        return argumentList;
    }

    public void addArgument(final Consumer<Argument> consumer) {
        Argument arg = new Argument();
        consumer.accept(arg);
        this.argumentList.add(arg);
    }

    public RequirementsContext getArgsRequirement() {
        return argsRequirement;
    }

    public void setArgsRequirement(final RequirementsContext argsRequirement) {
        this.argsRequirement = argsRequirement;
    }

    public String getOverridePermission() {
        return overridePermission;
    }

    public void setOverridePermission(final String overridePermission) {
        this.overridePermission = overridePermission;
    }

    @Override
    public String toString() {
        return "CommandData{" +
                "argumentList=" + argumentList +
                ", argsRequirement=" + argsRequirement +
                ", overridePermission='" + overridePermission + '\'' +
                '}';
    }
}