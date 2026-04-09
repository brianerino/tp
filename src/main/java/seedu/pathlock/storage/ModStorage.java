package seedu.pathlock.storage;

import seedu.pathlock.module.Module;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ModStorage extends Storage<List<Module>> {
    private static final Logger logger = Logger.getLogger(Storage.class.getName());

    public ModStorage(String username) {
        super("data/users/" + username.trim() + "/modules.txt");
        assert username != null && !username.trim().isEmpty() : "Username cannot be empty";
    }

    @Override
    public List<Module> load() throws IOException {
        File file = ensureFileExists();

        logger.info("Loading modules from file: " + filePath);

        List<Module> modules = new ArrayList<>();

        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) {
                continue;
            }

            logger.log(Level.FINE, "Reading line: {0}", line);
            Module module = getModule(line);
            modules.add(module);
        }
        scanner.close();
        return modules;
    }

    private Module getModule(String line) {
        assert !line.isBlank() : "Line in storage file should not be blank";

        String[] parts = line.split("\\|");
        assert parts.length == 2 : "Each line must have exactly 2 fields: " + line;

        String code = parts[0].trim();
        assert !code.isEmpty() : "Module code should not be empty";

        int mc = Integer.parseInt(parts[1].trim());
        assert mc > 0 : "Modular credits should be positive";

        Module module = new Module(code, mc);
        module.markCompleted();
        return module;
    }

    @Override
    public void save(List<Module> modules) throws IOException {
        assert modules != null : "Modules list should not be null";

        ensureParentDirectoryExists();

        FileWriter writer = new FileWriter(filePath);

        for (Module module : modules) {
            writer.write(module.getModuleCode() + "|" + module.getModularCredits());
            writer.write(System.lineSeparator());
        }

        writer.close();
        logger.info("Saved modules to file: " + filePath);
    }
}
