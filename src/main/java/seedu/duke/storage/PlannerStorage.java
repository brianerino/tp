package seedu.duke.storage;

import seedu.duke.module.Module;
import seedu.duke.planner.PlannerList;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlannerStorage {
    private static final Logger logger = Logger.getLogger(PlannerStorage.class.getName());
    private final String filePath;

    public PlannerStorage(String username) {
        assert username != null && !username.trim().isEmpty() : "Username cannot be empty";
        this.filePath = "data/users/" + username.trim() + "/planner.txt";
    }

    public PlannerList load() throws IOException {
        File file = new File(filePath);

        logger.info("Loading planner from file: " + filePath);

        File parent = file.getParentFile();
        assert parent != null : "Parent directory should exist for planner file path";
        parent.mkdirs();

        if (!file.exists()) {
            file.createNewFile();
            logger.warning("Planner file not found. Created new file at " + filePath);
        }

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

    public void save(PlannerList planner) throws IOException {
        assert planner != null : "Planner should not be null";

        File file = new File(filePath);
        File parent = file.getParentFile();
        assert parent != null : "Parent directory should exist for planner file path";
        parent.mkdirs();

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
}
