package seedu.duke.command;

import seedu.duke.module.ModuleList;

public class RemoveCommand extends Command {
    private final String moduleCode;
    public RemoveCommand(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    @Override
    public String execute(ModuleList modules) {
        boolean removed = modules.removeModule(moduleCode);

        if (removed) {
            return moduleCode + " has been removed";
        } else {
            return moduleCode + " is not in your module list";
        }
    }
}
