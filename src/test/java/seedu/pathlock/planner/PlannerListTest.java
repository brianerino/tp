package seedu.pathlock.planner;

import org.junit.jupiter.api.Test;
import seedu.pathlock.module.Module;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PlannerListTest {

    @Test
    public void addModule_validSemester_success() {
        PlannerList planner = new PlannerList();
        Module module = new Module("CS1231", 4);
        module.setSemester("y1s1");

        planner.addModule(module);

        assertTrue(planner.containsModule("CS1231"));
        assertEquals("y1s1", planner.findSemesterOfModule("CS1231"));
    }

    @Test
    public void addModule_invalidSemester_throwsException() {
        PlannerList planner = new PlannerList();
        Module module = new Module("CS1231", 4);

        assertThrows(IllegalArgumentException.class, () -> module.setSemester("wrongSem"));
    }

    @Test
    public void containsModule_existingModule_returnsTrue() {
        PlannerList planner = new PlannerList();
        Module module = new Module("CS1231", 4);
        module.setSemester("y1s1");

        planner.addModule(module);

        assertTrue(planner.containsModule("CS1231"));
    }

    @Test
    public void containsModule_nonExistingModule_returnsFalse() {
        PlannerList planner = new PlannerList();

        assertFalse(planner.containsModule("CS9999"));
    }

    @Test
    public void containsModule_ignoreCase_returnsTrue() {
        PlannerList planner = new PlannerList();
        Module module = new Module("CS1231", 4);
        module.setSemester("y1s1");

        planner.addModule(module);

        assertTrue(planner.containsModule("cs1231"));
    }

    @Test
    public void findSemesterOfModule_existingModule_returnsSemester() {
        PlannerList planner = new PlannerList();
        Module module = new Module("CS1231", 4);
        module.setSemester("y2s1");

        planner.addModule(module);

        assertEquals("y2s1", planner.findSemesterOfModule("CS1231"));
    }

    @Test
    public void findSemesterOfModule_nonExistingModule_returnsNull() {
        PlannerList planner = new PlannerList();

        assertNull(planner.findSemesterOfModule("CS1231"));
    }

    @Test
    public void getSemesterWorkload_emptySemester_returnsZero() {
        PlannerList planner = new PlannerList();

        assertEquals(0, planner.getSemesterWorkload("y1s1"));
    }

    @Test
    public void getSemesterWorkload_multipleModules_returnsTotal() {
        PlannerList planner = new PlannerList();

        Module module1 = new Module("CS1231", 4);
        module1.setSemester("y1s1");

        Module module2 = new Module("CS2030", 4);
        module2.setSemester("y1s1");

        planner.addModule(module1);
        planner.addModule(module2);

        assertEquals(8, planner.getSemesterWorkload("y1s1"));
    }

    @Test
    public void removeModule_existingModule_success() {
        PlannerList planner = new PlannerList();
        Module module = new Module("CS1231", 4);
        module.setSemester("y1s1");

        planner.addModule(module);
        planner.removeModule("CS1231");

        assertFalse(planner.containsModule("CS1231"));
    }

    @Test
    public void removeModule_nonExistingModule_throwsException() {
        PlannerList planner = new PlannerList();

        assertThrows(NoSuchElementException.class, () -> planner.removeModule("CS1231"));
    }

    @Test
    public void editModule_validSemester_success() {
        PlannerList planner = new PlannerList();
        Module module = new Module("CS1231", 4);
        module.setSemester("y1s1");

        planner.addModule(module);
        planner.editModule(module, "y2s1", "CS1231");

        assertEquals("y2s1", planner.findSemesterOfModule("CS1231"));
    }

    @Test
    public void editModule_invalidSemester_throwsException() {
        PlannerList planner = new PlannerList();
        Module module = new Module("CS1231", 4);
        module.setSemester("y1s1");

        planner.addModule(module);

        assertThrows(IllegalArgumentException.class,
                () -> planner.editModule(module, "wrongSem", "CS1231"));
    }

    @Test
    public void editModule_moduleNotFound_throwsException() {
        PlannerList planner = new PlannerList();
        Module module = new Module("CS1231", 4);

        assertThrows(NoSuchElementException.class,
                () -> planner.editModule(module, "y2s1", "CS1231"));
    }

    @Test
    public void list_emptyPlanner_returnsFormattedOutput() {
        PlannerList planner = new PlannerList();

        String result = planner.list();

        assertTrue(result.contains("Y1"));
        assertTrue(result.contains("Y2"));
        assertTrue(result.contains("Y3"));
        assertTrue(result.contains("Y4"));
    }

    @Test
    public void list_moduleAdded_showsModuleCode() {
        PlannerList planner = new PlannerList();
        Module module = new Module("CS1231", 4);
        module.setSemester("y1s1");

        planner.addModule(module);

        String result = planner.list();

        assertTrue(result.contains("CS1231"));
    }

    @Test
    public void getAllModules_modulesAdded_returnsAllModules() {
        PlannerList planner = new PlannerList();

        Module module1 = new Module("CS1231", 4);
        module1.setSemester("y1s1");

        Module module2 = new Module("CS2030", 4);
        module2.setSemester("y1s2");

        planner.addModule(module1);
        planner.addModule(module2);

        assertEquals(2, planner.getAllModules().size());
    }
}
