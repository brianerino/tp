package seedu.pathlock.command;

import seedu.pathlock.appstate.AppState;
import seedu.pathlock.module.ModuleValidator;
import seedu.pathlock.module.ModuleList;
import seedu.pathlock.exception.DuplicateException;
import seedu.pathlock.storage.ModStorage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DoneCommand extends Command {
    private final Logger logger = Logger.getLogger(DoneCommand.class.getName());

    private final String moduleCode;
    private final Integer mc;

    public DoneCommand(String moduleCode, Integer mc) {
        this.moduleCode = moduleCode.toUpperCase();
        this.mc = mc;
    }
    //@@author shiyong52
    @Override
    public String execute(AppState appState) {
        String username = appState.getProfile().getName();
        ModStorage storage = new ModStorage(username);

        ModuleList modules = appState.getModule();
        assert modules != null : "ModuleList should not be null";
        assert moduleCode != null && !moduleCode.isEmpty() : "ModuleCode should not be null";

        logger.log(Level.FINE, "Executing DoneCommand for {0}, mc={1}", new Object[]{moduleCode, mc});

        try {
            ModuleValidator.validateModuleCode(moduleCode);
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }

        try {
            if (modules.isRecognisedModule(moduleCode)) {
                return handleInternalModule(modules, storage);
            } else {
                return handleExternalModule(modules, storage);
            }
        } catch (DuplicateException e) {
            logger.log(Level.WARNING, "Duplicate module code: {0}", moduleCode);
            return e.getMessage();

        } catch (IllegalArgumentException e) {
            logger.log(Level.WARNING, "Invalid module code: {0}", moduleCode);
            return e.getMessage();

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Storage failure", e);
            throw new RuntimeException(e);
        }
    }

    private String handleInternalModule(ModuleList modules, ModStorage storage) throws DuplicateException, IOException {
        int expectedMc = modules.getMcForModule(moduleCode);

        ModuleValidator.validateInternalMc(mc, expectedMc, moduleCode);

        modules.addModule(moduleCode);
        storage.save(modules.getCompletedModules());

        logger.log(Level.FINE, "Internal module added: {0} ({1} MCs)",
                new Object[]{moduleCode, expectedMc});
        return moduleCode + " has been added.";
    }

    private String handleExternalModule(ModuleList modules, ModStorage storage) throws DuplicateException, IOException {
        if (mc == null) {
            return "\"" + moduleCode + "\" is not a recognised module. "
                    + "If this is an external module, provide its MCs using /mc. "
                    + "Example: done " + moduleCode + " /mc 4";
        }

        ModuleValidator.validateMc(mc);

        modules.addExternalModule(moduleCode, mc);
        storage.save(modules.getCompletedModules());

        logger.log(Level.FINE, "External module added: {0} ({1} MCs)",
                new Object[]{moduleCode, mc});
        return moduleCode + " has been added.";
    }
    //@@author
}
