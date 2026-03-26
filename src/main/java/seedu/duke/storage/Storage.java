package seedu.duke.storage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import java.util.logging.Level;
import java.util.logging.Logger;


import seedu.duke.module.Module;

public class Storage {
    private static final Logger logger = Logger.getLogger(Storage.class.getName());
    private static String filePath;

    public Storage() {
        this("data/duke.txt"); // default
    }

    public Storage(String filePath) {
        this.filePath = filePath;
    }

    public List<Module> load() throws IOException {

        File file = new File(filePath);

        logger.info("Loading modules from file: " + filePath);

        File parent = file.getParentFile();
        assert parent != null : "Parent directory should exist for file path";
        parent.mkdirs();

        if (!file.exists()) {
            file.createNewFile();
            logger.warning("Save file not found. Created new file at " + filePath);
        }

        List<Module> modules = new ArrayList<>();

        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            logger.log(Level.FINE, "Reading line: {0}", line);

            assert !line.isBlank() : "Line in storage file should not be blank";

            String[] parts = line.split("\\|");
            assert parts.length == 2 : "Each line must have exactly 2 fields: " + line;

            String code = parts[0].trim();
            assert !code.isEmpty() : "Module code should not be empty";

            int mc = Integer.parseInt(parts[1].trim());
            assert mc > 0 : "Modular credits should be positive";

            Module module = new Module(code, mc);
            assert module != null : "Module object should be created successfully";

            module.markCompleted();
            modules.add(module);
        }
        scanner.close();
        return modules;
    }
    public static void save(List<Module> modules) throws IOException {

        FileWriter writer = new FileWriter(filePath);

        for (Module module : modules) {
            writer.write(module.getModuleCode() + "|" +
                    module.getModularCredits());
            writer.write("\n");
        }

        writer.close();
    }
}
