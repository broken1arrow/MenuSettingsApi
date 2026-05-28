package org.brokenarrow.library.menusettings.command.modal;

public class Argument {
    private String argument;
    private boolean optional;
    private String value;

    public String getArgument() {
        return argument;
    }

    public void setArgument(String argument) {
        this.argument = argument;
    }

    public boolean isOptional() {
        return optional;
    }

    public void setOptional(boolean optional) {
        this.optional = optional;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Argument{" +
                "argument='" + argument + '\'' +
                ", optional=" + optional +
                ", value='" + value + '\'' +
                '}';
    }
}
