package seedu.duke.appstate;

import seedu.duke.module.ModuleList;
import seedu.duke.planner.PlannerList;
import seedu.duke.profile.UserProfile;
import seedu.duke.storage.PlannerStorage;

public class AppState {
    private ModuleList module;
    private PlannerList planner;
    private UserProfile profile;
    private PlannerStorage plannerStorage;
    private String activePlannerName;

    public AppState(ModuleList module, PlannerList planner, UserProfile profile, String username) {
        this(module, planner, profile, username, "plan1");
    }

    public AppState(ModuleList module, PlannerList planner, UserProfile profile,
                    String username, String plannerName) {
        this.module = module;
        this.planner = planner;
        this.profile = profile;
        this.activePlannerName = plannerName;
        this.plannerStorage = new PlannerStorage(username, plannerName);
    }

    public void setPlanner(PlannerList planner) {
        this.planner = planner;
    }

    public ModuleList getModule() {
        return module;
    }

    public PlannerList getPlanner() {
        return planner;
    }

    public UserProfile getProfile() {
        return profile;
    }

    public PlannerStorage getPlannerStorage() {
        return plannerStorage;
    }

    public String getActivePlannerName() {
        return activePlannerName;
    }

    public void switchPlanner(String username, String plannerName) {
        this.activePlannerName = plannerName;
        this.plannerStorage = new PlannerStorage(username, plannerName);
    }

    public void update(ModuleList module, PlannerList planner, UserProfile profile, PlannerStorage plannerStorage) {
        this.module = module;
        this.planner = planner;
        this.profile = profile;
        this.plannerStorage = plannerStorage;
    }
}
