package app.launch;

import java.io.File;
import java.nio.file.Path;

import app.cli.ConsoleApp;
import app.demo.DemoRunner;
import app.service.BootstrapService;
import core.DataStorage;
import core.UniversitySystem;

public final class AppLauncher {
    public static final String DEFAULT_DATA_FILE = "university_system.ser";

    private AppLauncher() {
    }

    public static void run(String[] args) {
        if (args != null && args.length > 0 && "--demo".equals(args[0])) {
            DemoRunner.run();
            return;
        }

        String dataFile = System.getenv().getOrDefault("UNIVERSITY_DATA_FILE", DEFAULT_DATA_FILE);
        loadOrInit(dataFile);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> save(dataFile)));

        try {
            new ConsoleApp().run();
        } finally {
            save(dataFile);
        }
    }

    private static void loadOrInit(String dataFile) {
        File file = Path.of(dataFile).toFile();
        if (file.exists()) {
            try {
                DataStorage storage = new DataStorage();
                Object loaded = storage.load(dataFile);
                if (loaded instanceof UniversitySystem system) {
                    UniversitySystem.replaceInstance(system);
                    System.out.println("Loaded data from " + dataFile);
                    return;
                }
            } catch (RuntimeException e) {
                System.err.println("Failed to load " + dataFile + ", starting fresh: " + e.getMessage());
            }
        }
        UniversitySystem.getInstance();
        new BootstrapService().seedIfEmpty();
    }

    private static void save(String dataFile) {
        try {
            new DataStorage().save(UniversitySystem.getInstance(), dataFile);
        } catch (RuntimeException e) {
            System.err.println("Failed to save data: " + e.getMessage());
        }
    }
}
