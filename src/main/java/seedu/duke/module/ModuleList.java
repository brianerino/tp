package seedu.duke.module;
import java.util.List;

public class ModuleList {
    private final List<Module> completedModules;

    public ModuleList(List<Module> completedModules) {
        this.completedModules = completedModules;
    }

    public void addModule(Module newModule) {
        newModule.markCompleted();
        completedModules.add(newModule);
    }

}
