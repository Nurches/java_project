package core;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class DataStorage {

    public void save(Object object, String fileName) {
        try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(fileName))) {
            output.writeObject(object);
            output.flush();
        } catch (IOException e) {
            throw new RuntimeException("Failed to save data to file: " + fileName, e);
        }
    }

    public Object load(String fileName) {
        try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(fileName))) {
            return input.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to load data from file: " + fileName, e);
        }
    }
}
