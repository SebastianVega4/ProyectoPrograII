package utils;

import java.io.*;
import java.util.List;

public class DataPersistence {
    
    public static <T> void saveToFile(List<T> data, String filename) {
        try {
            File file = new File(filename);
            file.getParentFile().mkdirs();
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(data);
            oos.close();
        } catch (Exception e) {
            System.out.println("Error saving to file: " + e.getMessage());
        }
    }
    
    @SuppressWarnings("unchecked")
    public static <T> List<T> loadFromFile(String filename) {
        try {
            File file = new File(filename);
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                List<T> data = (List<T>) ois.readObject();
                ois.close();
                return data;
            }
        } catch (Exception e) {
            System.out.println("Error loading from file: " + e.getMessage());
        }
        return null;
    }
}