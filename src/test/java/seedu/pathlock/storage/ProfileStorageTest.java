package seedu.pathlock.storage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import seedu.pathlock.profile.UserProfile;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProfileStorageTest {

    private static final String TEST_USERNAME = "testuser_profilestorage";
    private static final String PROFILE_PATH = "data/users/" + TEST_USERNAME + "/profile.txt";

    @BeforeEach
    public void cleanUp() {
        File file = new File(PROFILE_PATH);
        if (file.exists()) {
            file.delete();
        }
    }

    // loadProfile Tests

    @Test
    public void loadProfile_noExistingFile_returnsNull() throws IOException {
        ProfileStorage storage = new ProfileStorage(TEST_USERNAME);
        UserProfile result = storage.load();
        assertNull(result);
    }

    @Test
    public void loadProfile_savedProfile_returnsCorrectName() throws IOException {
        ProfileStorage storage = new ProfileStorage("Alice");
        UserProfile saved = new UserProfile("Alice", 4.0);
        storage.save(saved);

        UserProfile loaded = storage.load();
        assertNotNull(loaded);
        assertEquals("Alice", loaded.getName());
    }

    @Test
    public void loadProfile_savedProfile_returnsCorrectGpa() throws IOException {
        ProfileStorage storage = new ProfileStorage("Alice");
        UserProfile saved = new UserProfile("Alice", 4.0);
        storage.save(saved);

        UserProfile loaded = storage.load();
        assertNotNull(loaded);
        assertEquals(4.0, loaded.getGpa());
    }

    @Test
    public void loadProfile_savedProfileWithDecimalGpa_returnsCorrectGpa() throws IOException {
        ProfileStorage storage = new ProfileStorage("Bob");
        UserProfile saved = new UserProfile("Bob", 3.75);
        storage.save(saved);

        UserProfile loaded = storage.load();
        assertNotNull(loaded);
        assertEquals(3.75, loaded.getGpa());
    }

    // saveProfile Tests 

    @Test
    public void saveProfile_validProfile_fileIsCreated() throws IOException {
        ProfileStorage storage = new ProfileStorage("Alice");
        UserProfile profile = new UserProfile("Alice", 4.0);
        storage.save(profile);

        File file = new File("data/users/Alice/profile.txt");
        assertTrue(file.exists());
    }

    @Test
    public void saveProfile_overwriteExistingProfile_updatesGpa() throws IOException {
        ProfileStorage storage = new ProfileStorage("Alice");
        UserProfile original = new UserProfile("Alice", 3.0);
        storage.save(original);

        UserProfile updated = new UserProfile("Alice", 4.5);
        storage.save(updated);

        UserProfile loaded = storage.load();
        assertNotNull(loaded);
        assertEquals(4.5, loaded.getGpa());
    }

    @Test
    public void saveAndLoad_roundTrip_preservesAllFields() throws IOException {
        ProfileStorage storage = new ProfileStorage("Eve");
        UserProfile original = new UserProfile("Eve", 3.5);
        storage.save(original);

        UserProfile loaded = storage.load();
        assertNotNull(loaded);
        assertEquals(original.getName(), loaded.getName());
        assertEquals(original.getGpa(), loaded.getGpa());
        assertEquals(original.getRecommendedMaxWorkload(), loaded.getRecommendedMaxWorkload());
    }
    
}
