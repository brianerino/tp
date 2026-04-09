package seedu.pathlock.command;

import seedu.pathlock.appstate.AppState;
import seedu.pathlock.module.ModuleList;
import seedu.pathlock.storage.ModStorage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RemoveCommand extends Command {
    private final String moduleCode;
    private final Logger logger = Logger.getLogger(RemoveCommand.class.getName());

    public RemoveCommand(String moduleCode) {
        this.moduleCode = moduleCode.toUpperCase();
    }

    @Override
    public String execute(AppState appState) {
        assert appState != null : "AppState should not be null";

        ModuleList modules = appState.getModule();
        String username = appState.getProfile().getName();
        ModStorage storage = new ModStorage(username);

        assert modules != null : "ModuleList should not be null";
        assert moduleCode != null && !moduleCode.isEmpty() : "ModuleCode should not be null";

        logger.log(Level.FINE, "Executing RemoveCommand for {0}", moduleCode);

        boolean removed = modules.removeModule(moduleCode);
        saveModules(modules, storage);

        return buildResultMessage(removed);
    }

    private void saveModules(ModuleList modules, ModStorage storage) {
        try {
            storage.save(modules.getCompletedModules());
            logger.log(Level.FINE, "Storage updated after removing module: {0}", moduleCode);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to save after removing module: {0}", moduleCode);
            throw new RuntimeException(e);
        }
    }

    private String buildResultMessage(boolean removed) {
        if (removed) {
            logger.log(Level.FINE, "Module removed successfully: {0}", moduleCode);
            return moduleCode + " has been removed";
        } else {
            logger.log(Level.WARNING, "Attempted to remove non-existing module: {0}", moduleCode);
            return moduleCode + " is not in your module list";
        }
    }
}
