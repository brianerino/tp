package seedu.pathlock.storage;

import seedu.pathlock.profile.UserProfile;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProfileStorage extends Storage<UserProfile> {
    private static final Logger logger = Logger.getLogger(ProfileStorage.class.getName());
    private final String username;

    public ProfileStorage(String username) {
        super("data/users/" + username.trim() + "/profile.txt");
        assert username != null && !username.trim().isEmpty() : "Username cannot be empty";
        this.username = username.trim();
    }

    @Override
    public UserProfile load() throws IOException {
        File file = ensureParentDirectoryExists();

        if (!file.exists()) {
            return null;
        }

        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) {
                continue;
            }

            String[] parts = line.split("\\|");
            if (parts.length != 2) {
                scanner.close();
                throw new IOException("Invalid profile format in " + filePath);
            }

            String name = parts[0].trim();
            double gpa = Double.parseDouble(parts[1].trim());

            scanner.close();
            logger.log(Level.INFO, "Loaded profile for user: {0}", name);
            return new UserProfile(name, gpa);
        }

        scanner.close();
        return null;
    }

    @Override
    public void save(UserProfile profile) throws IOException {
        assert profile != null : "Profile should not be null";

        ensureParentDirectoryExists();

        FileWriter writer = new FileWriter(filePath);
        writer.write(profile.getName() + "|" + profile.getGpa());
        writer.write(System.lineSeparator());
        writer.close();

        logger.log(Level.INFO, "Saved profile for user: {0}", profile.getName());
    }

    public String getUsername() {
        return username;
    }
}
