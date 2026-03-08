package seedu.duke.command;

import seedu.duke.module.Module;
import seedu.duke.module.ModuleList;

public class DoneCommand extends Command {

    private final String moduleCode;

    public DoneCommand(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    @Override
    public String execute(ModuleList modules) {
        Module newModule = new Module(moduleCode);

        modules.addModule(newModule);
        return moduleCode + " has been added";

    }
}
