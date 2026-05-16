package tests;

import java.nio.file.Files;
import java.nio.file.Path;

import app.launch.AppLauncher;
import app.service.BootstrapService;
import core.DataStorage;
import core.UniversitySystem;
import users.User;

public class PersistenceSmokeTest {
    public static void main(String[] args) throws Exception {
        String file = "target_smoke_test.ser";
        Files.deleteIfExists(Path.of(file));

        UniversitySystem system = UniversitySystem.getInstance();
        new BootstrapService().seedIfEmpty();
        int usersBefore = system.getUsers().size();
        new DataStorage().save(system, file);

        UniversitySystem.resetInstance();
        if (!UniversitySystem.getInstance().getUsers().isEmpty()) {
            throw new IllegalStateException("Expected empty system after reset");
        }

        Object loaded = new DataStorage().load(file);
        if (!(loaded instanceof UniversitySystem restored)) {
            throw new IllegalStateException("Expected UniversitySystem");
        }
        UniversitySystem.replaceInstance(restored);

        if (UniversitySystem.getInstance().getUsers().size() != usersBefore) {
            throw new IllegalStateException("User count mismatch after load");
        }

        User admin = UniversitySystem.getInstance().findUserByLogin("admin");
        if (admin == null || !admin.authenticate("admin", "admin123")) {
            throw new IllegalStateException("Admin auth failed after load");
        }

        Files.deleteIfExists(Path.of(file));
        System.out.println("PersistenceSmokeTest passed");
    }
}
