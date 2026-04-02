package seedu.duke.module;

import java.util.List;

public class Module {
    private final String code;
    private final int mc;
    private final String type;
    private final String orGroup;
    private final List<String> prerequisites;
    private final List<String> preclusions;
    private ModuleStatus status;
    private boolean isPlanned;
    private String semester;

    public Module(String code, int mc, String type, String orGroup,
                  List<String> prerequisites, List<String> preclusions) {
        this.code = code;
        this.mc = mc;
        this.type = type;
        this.orGroup = orGroup;
        this.prerequisites = prerequisites;
        this.preclusions = preclusions;
        this.status = ModuleStatus.INCOMPLETE;
        this.isPlanned = false;
        this.semester = "";
    }

    public Module(String code, int mc) {
        this(code, mc, null, null, List.of(), List.of());
        this.isPlanned = false;
    }

    public String getModuleCode() {
        return code;
    }

    public int getModularCredits() {
        return mc;
    }

    public String getType() {
        return type;
    }

    public String getOrGroup() {
        return orGroup;
    }

    public List<String> getPrerequisites() {
        return prerequisites;
    }

    public List<String> getPreclusions() {
        return preclusions;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        switch (semester) {
        case "y1s1", "y1s2", "y2s1", "y2s2", "y3s1", "y3s2", "y4s1", "y4s2": break;
        default:
            throw new IllegalArgumentException("Indicate semester correctly e.g. 'y3s1'");
        }
        this.semester = semester;
    }

    public ModuleStatus getStatus() {
        return status;
    }

    public void setStatus(ModuleStatus status) {
        this.status = status;
    }

    public boolean isCompleted() {
        return status == ModuleStatus.COMPLETED;
    }

    public void markCompleted() {
        this.status = ModuleStatus.COMPLETED;
    }

    public void markIncompleted() {
        this.status = ModuleStatus.INCOMPLETE;
    }

    public void setIsPlanned(boolean bool) {
        this.isPlanned = bool;
    }

    public boolean isPlanned() {
        return this.isPlanned;
    }

    @Override
    public String toString() {
        return code;
    }
}
