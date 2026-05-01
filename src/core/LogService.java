package core;

import java.time.LocalDateTime;

public class LogService {
    public void info(String message) {
        System.out.println("[INFO] " + LocalDateTime.now() + " - " + message);
    }

    public void error(String message) {
        System.err.println("[ERROR] " + LocalDateTime.now() + " - " + message);
    }
}
