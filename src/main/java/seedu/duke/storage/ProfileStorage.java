package seedu.duke.storage;

import seedu.duke.profile.UserProfile;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProfileStorage {
    private static final Logger logger = Logger.getLogger(ProfileStorage.class.getName());
    private String getProfilePath(String username) {
        return "data/users/" + username.trim() + "_profile.txt";
    }


    public UserProfile loadProfile(String username) throws IOException {
        assert username != null && !username.trim().isEmpty() : "Username cannot be empty";

        String profilePath = getProfilePath(username);
        File file = new File(profilePath);

        File parent = file.getParentFile();
        assert parent != null : "Parent directory should exist for profile path";
        parent.mkdirs();

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
                throw new IOException("Invalid profile format in " + profilePath);
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


    public void saveProfile(UserProfile profile) throws IOException {
        assert profile != null : "Profile should not be null";

        String profilePath = getProfilePath(profile.getName());
        File file = new File(profilePath);

        File parent = file.getParentFile();
        assert parent != null : "Parent directory should exist for profile path";
        parent.mkdirs();

        FileWriter writer = new FileWriter(file);
        writer.write(profile.getName() + "|" + profile.getGpa());
        writer.write(System.lineSeparator());
        writer.close();

        logger.log(Level.INFO, "Saved profile for user: {0}", profile.getName());
    }
}
