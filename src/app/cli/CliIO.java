package app.cli;

import java.util.List;
import java.util.Scanner;
import java.util.function.Function;

public class CliIO {
    private final Scanner scanner = new Scanner(System.in);

    public void println(String line) {
        System.out.println(line);
    }

    public void print(String line) {
        System.out.print(line);
    }

    public String readLine(String prompt) {
        print(prompt);
        if (!scanner.hasNextLine()) {
            return "";
        }
        return scanner.nextLine().trim();
    }

    public int readInt(String prompt) {
        while (true) {
            String raw = readLine(prompt);
            if (raw.isBlank()) {
                return 0;
            }
            try {
                return Integer.parseInt(raw);
            } catch (NumberFormatException e) {
                println("Enter a valid number.");
            }
        }
    }

    public double readDouble(String prompt) {
        while (true) {
            String raw = readLine(prompt);
            try {
                return Double.parseDouble(raw);
            } catch (NumberFormatException e) {
                println("Enter a valid number.");
            }
        }
    }

    public void pause() {
        readLine("Press Enter to continue...");
    }

    public void printNumberedMenu(String title, List<String> options) {
        println("\n=== " + title + " ===");
        for (int i = 0; i < options.size(); i++) {
            println((i + 1) + ") " + options.get(i));
        }
        println("0) Back");
    }

    public int chooseMenu(String title, List<String> options) {
        printNumberedMenu(title, options);
        int choice = readInt("Choose: ");
        if (choice < 0 || choice > options.size()) {
            return -1;
        }
        return choice;
    }

    public <T> T chooseFromList(String title, List<T> items, Function<T, String> label) {
        if (items.isEmpty()) {
            println("No items.");
            return null;
        }
        for (int i = 0; i < items.size(); i++) {
            println((i + 1) + ") " + label.apply(items.get(i)));
        }
        int idx = readInt("Select number (0 to cancel): ") - 1;
        if (idx < 0 || idx >= items.size()) {
            return null;
        }
        return items.get(idx);
    }
}
