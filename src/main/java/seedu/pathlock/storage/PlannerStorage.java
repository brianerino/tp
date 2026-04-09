package seedu.pathlock.storage;

import seedu.pathlock.module.Module;
import seedu.pathlock.planner.PlannerList;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlannerStorage extends Storage<PlannerList> {
    private static final Logger logger = Logger.getLogger(PlannerStorage.class.getName());

    private final String username;
    private String plannerName;

    public PlannerStorage(String username) {
        this(username, "plan1");
    }

    public PlannerStorage(String username, String plannerName) {
        super("data/users/" + username.trim() + "/plans/" + plannerName.trim() + ".txt");

        assert username != null && !username.trim().isEmpty() : "Username cannot be empty";
        assert plannerName != null && !plannerName.trim().isEmpty() : "Planner name cannot be empty";

        this.username = username.trim();
        this.plannerName = plannerName.trim();
    }

    private String buildFilePath(String username, String plannerName) {
        return "data/users/" + username + "/plans/" + plannerName + ".txt";
    }

    public void setPlannerName(String plannerName) {
        assert plannerName != null && !plannerName.trim().isEmpty() : "Planner name cannot be empty";
        this.plannerName = plannerName.trim();
        this.filePath = buildFilePath(username, this.plannerName);
    }

    public String getPlannerName() {
        return plannerName;
    }

    @Override
    public PlannerList load() throws IOException {
        File file = ensureFileExists();

        logger.info("Loading planner from file: " + filePath);

        PlannerList planner = new PlannerList();

        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) {
                continue;
            }

            logger.log(Level.FINE, "Reading planner line: {0}", line);
            Module module = getModuleFromLine(line);
            planner.addModule(module);
        }
        scanner.close();

        return planner;
    }

    private Module getModuleFromLine(String line) {
        assert !line.isBlank() : "Planner line should not be blank";

        String[] parts = line.split("\\|");
        assert parts.length == 3 : "Each planner line must have exactly 3 fields: " + line;

        String code = parts[0].trim();
        String semester = parts[1].trim();
        int mc = Integer.parseInt(parts[2].trim());

        assert !code.isEmpty() : "Module code should not be empty";
        assert !semester.isEmpty() : "Semester should not be empty";
        assert mc > 0 : "Modular credits should be positive";

        Module module = new Module(code, mc);
        module.setSemester(semester);
        return module;
    }

    @Override
    public void save(PlannerList planner) throws IOException {
        assert planner != null : "Planner should not be null";

        ensureParentDirectoryExists();

        FileWriter writer = new FileWriter(filePath);

        for (Module module : planner.getAllModules()) {
            writer.write(module.getModuleCode() + "|"
                    + module.getSemester() + "|"
                    + module.getModularCredits());
            writer.write(System.lineSeparator());
        }

        writer.close();
        logger.info("Saved planner to file: " + filePath);
    }

    public ArrayList<String> listPlannerNames() {
        ArrayList<String> plannerNames = new ArrayList<>();
        File folder = new File("data/users/" + username + "/plans");

        if (!folder.exists()) {
            return plannerNames;
        }

        File[] files = folder.listFiles();
        if (files == null) {
            return plannerNames;
        }

        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".txt")) {
                String fileName = file.getName();
                plannerNames.add(fileName.substring(0, fileName.length() - 4));
            }
        }

        return plannerNames;
    }
}
