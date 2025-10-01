package services;

import models.Product;
import java.util.*;
import java.io.*;

public class ProductService implements DataService<Product> {
    private List<Product> products;
    private static final String PRODUCTS_FILE = "data/products.dat";
    
    public ProductService() {
        this.products = new ArrayList<>();
        loadProducts();
    }
    
    private void loadProducts() {
        try {
            File file = new File(PRODUCTS_FILE);
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                products = (List<Product>) ois.readObject();
                ois.close();
            }
        } catch (Exception e) {
            System.out.println("Error loading products: " + e.getMessage());
        }
    }
   
    public void clearProducts() {
    products.clear();
    saveProducts();
}

    private void saveProducts() {
        try {
            File file = new File(PRODUCTS_FILE);
            file.getParentFile().mkdirs();
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(products);
            oos.close();
        } catch (Exception e) {
            System.out.println("Error saving products: " + e.getMessage());
        }
    }
    
    public void addProduct(Product product) {
        products.add(product);
        saveProducts();
    }
    
    public void deleteProduct(String productId) {
        products.removeIf(product -> product.getId().equals(productId));
        saveProducts();
    }
    
    public void updateProduct(Product updatedProduct) {
    for (int i = 0; i < products.size(); i++) {
        if (products.get(i).getId().equals(updatedProduct.getId())) {
            products.set(i, updatedProduct);
            saveProducts();
            return;
        }
    }
}

    public void loadFromPlainFile(String filename) {
    try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
        String line;
        // Saltar la primera línea si es encabezado
        reader.readLine();
        
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split("\\|");
            if (parts.length >= 7) {
                String id = parts[0];
                String name = parts[1];
                String description = parts[2];
                double price = Double.parseDouble(parts[3]);
                int stock = Integer.parseInt(parts[4]);
                String category = parts[5];
                boolean available = Boolean.parseBoolean(parts[6]);
                
                Product product = new Product(id, name, description, price, stock, category);
                product.setAvailable(available);
                products.add(product);
            }
        }
        saveProducts(); // Guardar en el formato serializado
    } catch (Exception e) {
        System.out.println("Error loading from plain file: " + e.getMessage());
    }
}

    public List<Product> getProducts() {
        return new ArrayList<>(products);
    }
    
    public Product getProductById(String productId) {
        for (Product product : products) {
            if (product.getId().equals(productId)) {
                return product;
            }
        }
        return null;
    }
    
    // Implementación de los métodos de DataService
    @Override
    public void saveToFile(List<Product> data, String filename) {
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
    public List<Product> loadFromFile(String filename) {
        List<Product> loadedProducts = new ArrayList<>();
        try {
            FileInputStream fis = new FileInputStream(filename);
            ObjectInputStream ois = new ObjectInputStream(fis);
            loadedProducts = (List<Product>) ois.readObject();
            ois.close();
        } catch (Exception e) {
            System.out.println("Error loading from file: " + e.getMessage());
        }
        return loadedProducts;
    }
    
    @Override
    public void exportToCSV(List<Product> data, String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("ID,Name,Description,Price,Stock,Category,Available");
            for (Product product : data) {
                writer.println(product.getId() + "," + product.getName() + "," + 
                             product.getDescription() + "," + product.getPrice() + "," + 
                             product.getStock() + "," + product.getCategory() + "," + 
                             product.isAvailable());
            }
        } catch (Exception e) {
            System.out.println("Error exporting to CSV: " + e.getMessage());
        }
    }
    
    @Override
    public void exportToXML(List<Product> data, String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            writer.println("<products>");
            for (Product product : data) {
                writer.println("  <product>");
                writer.println("    <id>" + product.getId() + "</id>");
                writer.println("    <name>" + product.getName() + "</name>");
                writer.println("    <description>" + product.getDescription() + "</description>");
                writer.println("    <price>" + product.getPrice() + "</price>");
                writer.println("    <stock>" + product.getStock() + "</stock>");
                writer.println("    <category>" + product.getCategory() + "</category>");
                writer.println("    <available>" + product.isAvailable() + "</available>");
                writer.println("  </product>");
            }
            writer.println("</products>");
        } catch (Exception e) {
            System.out.println("Error exporting to XML: " + e.getMessage());
        }
    }
    
    @Override
    public void exportToJSON(List<Product> data, String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("[");
            for (int i = 0; i < data.size(); i++) {
                Product product = data.get(i);
                writer.println("  {");
                writer.println("    \"id\": \"" + product.getId() + "\",");
                writer.println("    \"name\": \"" + product.getName() + "\",");
                writer.println("    \"description\": \"" + product.getDescription() + "\",");
                writer.println("    \"price\": " + product.getPrice() + ",");
                writer.println("    \"stock\": " + product.getStock() + ",");
                writer.println("    \"category\": \"" + product.getCategory() + "\",");
                writer.println("    \"available\": " + product.isAvailable());
                writer.println("  }" + (i < data.size() - 1 ? "," : ""));
            }
            writer.println("]");
        } catch (Exception e) {
            System.out.println("Error exporting to JSON: " + e.getMessage());
        }
    }
    
    @Override
    public void exportToPlain(List<Product> data, String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (Product product : data) {
                writer.println(product.getId() + "|" + product.getName() + "|" + 
                             product.getDescription() + "|" + product.getPrice() + "|" + 
                             product.getStock() + "|" + product.getCategory() + "|" + 
                             product.isAvailable());
            }
        } catch (Exception e) {
            System.out.println("Error exporting to plain text: " + e.getMessage());
        }
    }
}