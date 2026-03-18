package seedu.duke.command;

import seedu.duke.module.Module;
import seedu.duke.module.ModuleList;
import seedu.duke.exception.DuplicateException;
import seedu.duke.storage.Storage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DoneCommand extends Command {
    private final String moduleCode;
    private final Logger logger = Logger.getLogger(DoneCommand.class.getName());

    public DoneCommand(String moduleCode) {
        this.moduleCode = moduleCode.toUpperCase();
    }

    @Override
    public String execute(ModuleList modules) {
        assert modules != null : "ModuleList should not be null";
        assert moduleCode != null && !moduleCode.isEmpty() : "ModuleCode should not be null";

        logger.log(Level.FINE, "Executing DoneCommand for {0}", moduleCode);

        int modularCredits = ModuleList.getMcForModule(moduleCode);
        Module newModule = new Module(moduleCode, modularCredits);
        try {
            modules.addModule(newModule);
            logger.log(Level.INFO, "Module added: {0} (MC={1})", new Object[]{moduleCode, modularCredits});

            Storage.save(modules.completedModules);
            logger.log(Level.INFO, "Storage updated after adding module: {0}", moduleCode);

            return moduleCode + " has been added";

        } catch (DuplicateException e) {
            logger.log(Level.WARNING, "Duplicate module code: {0}", moduleCode);
            return e.getMessage();

        } catch (IllegalArgumentException e){
            logger.log(Level.WARNING, "Invalid module code: {0}", moduleCode);
            return e.getMessage();

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Storage failure", e);
            throw new RuntimeException(e);
        }
    }

}
