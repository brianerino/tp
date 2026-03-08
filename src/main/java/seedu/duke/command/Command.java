package seedu.duke.command;

import seedu.duke.module.ModuleList;

public abstract class Command {
    public abstract String execute(ModuleList modules);
}
