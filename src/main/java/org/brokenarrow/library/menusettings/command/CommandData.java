package org.brokenarrow.library.menusettings.command;

import org.brokenarrow.library.menusettings.requirements.RequirementsContext;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class CommandData {
    private final List<Argument> argumentList = new ArrayList<>();
    private RequirementsContext argsRequirement;

    public List<Argument> getArgumentsList() {
        return argumentList;
    }

    public void addArgument(Consumer<Argument> consumer) {
        Argument arg = new Argument();
        consumer.accept(arg);
        this.argumentList.add(arg);
    }

    public RequirementsContext getArgsRequirement() {
        return argsRequirement;
    }

    public void setArgsRequirement(RequirementsContext argsRequirement) {
        this.argsRequirement = argsRequirement;
    }


    @Override
    public String toString() {
        return "CommandData{" +
                "argumentList=" + argumentList +
                ", argsRequirement=" + argsRequirement +
                '}';
    }
}