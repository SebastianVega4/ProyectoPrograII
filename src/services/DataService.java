package services;

import java.util.List;

public interface DataService<T> {
    void saveToFile(List<T> data, String filename);
    List<T> loadFromFile(String filename);
    void exportToCSV(List<T> data, String filename);
    void exportToXML(List<T> data, String filename);
    void exportToJSON(List<T> data, String filename);
    void exportToPlain(List<T> data, String filename);
}