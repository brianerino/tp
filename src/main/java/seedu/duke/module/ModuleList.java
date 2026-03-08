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

    public boolean removeModule(String moduleCode) {
        for (int i = 0; i < completedModules.size(); i++){
            Module module = completedModules.get(i);
            if (module.getModuleCode().equalsIgnoreCase(moduleCode)) {
                module.markIncompleted();
                completedModules.remove(i);
                return true;
            }
        }
        return false;
    }


}
