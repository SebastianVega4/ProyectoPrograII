package services;

import models.User;
import java.util.*;
import java.io.*;

public class UserService implements DataService<User> {
    private List<User> users;
    private static final String USERS_FILE = "data/users.dat";
    
    public UserService() {
        this.users = new ArrayList<>();
        loadUsers();
        // Crear usuario admin por defecto si no existe
        if (users.isEmpty()) {
            users.add(new User("admin", "admin123", "admin@system.com", "Administrador"));
            saveUsers();
        }
    }
    
    private void loadUsers() {
        try {
            File file = new File(USERS_FILE);
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                users = (List<User>) ois.readObject();
                ois.close();
            }
        } catch (Exception e) {
            System.out.println("Error loading users: " + e.getMessage());
        }
    }
    
    private void saveUsers() {
        try {
            File file = new File(USERS_FILE);
            file.getParentFile().mkdirs();
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(users);
            oos.close();
        } catch (Exception e) {
            System.out.println("Error saving users: " + e.getMessage());
        }
    }
    
    public boolean authenticate(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password) && user.isActive()) {
                return true;
            }
        }
        return false;
    }
    
    public void addUser(User user) {
        users.add(user);
        saveUsers();
    }
    
    public void deleteUser(String username) {
        users.removeIf(user -> user.getUsername().equals(username));
        saveUsers();
    }
    
    public List<User> getUsers() {
        return new ArrayList<>(users);
    }
    
    @Override
    public void saveToFile(List<User> data, String filename) {
        try {
            FileOutputStream fos = new FileOutputStream(filename);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(data);
            oos.close();
        } catch (Exception e) {
            System.out.println("Error saving to file: " + e.getMessage());
        }
    }
    
    @Override
    public List<User> loadFromFile(String filename) {
        List<User> loadedUsers = new ArrayList<>();
        try {
            FileInputStream fis = new FileInputStream(filename);
            ObjectInputStream ois = new ObjectInputStream(fis);
            loadedUsers = (List<User>) ois.readObject();
            ois.close();
        } catch (Exception e) {
            System.out.println("Error loading from file: " + e.getMessage());
        }
        return loadedUsers;
    }
    
    @Override
    public void exportToCSV(List<User> data, String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("Username,Password,Email,Role,Active");
            for (User user : data) {
                writer.println(user.getUsername() + "," + user.getPassword() + "," + 
                             user.getEmail() + "," + user.getRole() + "," + user.isActive());
            }
        } catch (Exception e) {
            System.out.println("Error exporting to CSV: " + e.getMessage());
        }
    }
    
    @Override
    public void exportToXML(List<User> data, String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            writer.println("<users>");
            for (User user : data) {
                writer.println("  <user>");
                writer.println("    <username>" + user.getUsername() + "</username>");
                writer.println("    <password>" + user.getPassword() + "</password>");
                writer.println("    <email>" + user.getEmail() + "</email>");
                writer.println("    <role>" + user.getRole() + "</role>");
                writer.println("    <active>" + user.isActive() + "</active>");
                writer.println("  </user>");
            }
            writer.println("</users>");
        } catch (Exception e) {
            System.out.println("Error exporting to XML: " + e.getMessage());
        }
    }
    
    @Override
    public void exportToJSON(List<User> data, String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("[");
            for (int i = 0; i < data.size(); i++) {
                User user = data.get(i);
                writer.println("  {");
                writer.println("    \"username\": \"" + user.getUsername() + "\",");
                writer.println("    \"password\": \"" + user.getPassword() + "\",");
                writer.println("    \"email\": \"" + user.getEmail() + "\",");
                writer.println("    \"role\": \"" + user.getRole() + "\",");
                writer.println("    \"active\": " + user.isActive());
                writer.println("  }" + (i < data.size() - 1 ? "," : ""));
            }
            writer.println("]");
        } catch (Exception e) {
            System.out.println("Error exporting to JSON: " + e.getMessage());
        }
    }
    
    @Override
    public void exportToPlain(List<User> data, String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (User user : data) {
                writer.println(user.getUsername() + "|" + user.getPassword() + "|" + 
                             user.getEmail() + "|" + user.getRole() + "|" + user.isActive());
            }
        } catch (Exception e) {
            System.out.println("Error exporting to plain text: " + e.getMessage());
        }
    }
}