package seedu.duke.module;

public class Module {
    private final String moduleCode;
    private boolean isCompleted;


    public Module(String moduleCode) {
        this.moduleCode = moduleCode;
        this.isCompleted = false;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void markCompleted() {
        this.isCompleted = true;
    }

    public void markIncompleted() {
        this.isCompleted = false;
    }

    @Override
    public String toString() {
        return moduleCode;
    }

}
