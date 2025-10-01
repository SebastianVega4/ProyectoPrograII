package services;

import models.Sale;
import models.Product;
import java.util.*;
import java.io.*;

public class SaleService implements DataService<Sale> {
    private List<Sale> sales;
    private static final String SALES_FILE = "data/sales.dat";
    private ProductService productService;
    
    public SaleService(ProductService productService) {
        this.sales = new ArrayList<>();
        this.productService = productService;
        loadSales();
    }
    
    private void loadSales() {
        try {
            File file = new File(SALES_FILE);
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                sales = (List<Sale>) ois.readObject();
                ois.close();
            }
        } catch (Exception e) {
            System.out.println("Error loading sales: " + e.getMessage());
        }
    }
    
    public void clearSales() {
    sales.clear();
    saveSales();
}

    private void saveSales() {
        try {
            File file = new File(SALES_FILE);
            file.getParentFile().mkdirs();
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(sales);
            oos.close();
        } catch (Exception e) {
            System.out.println("Error saving sales: " + e.getMessage());
        }
    }
    
    public void addSale(Sale sale) {
        // Validar que el producto existe
        Product product = productService.getProductById(sale.getProductId());
        if (product != null && product.getStock() >= sale.getQuantity()) {
            sales.add(sale);
            // Actualizar stock del producto
            product.setStock(product.getStock() - sale.getQuantity());
            saveSales();
        }
    }
    
    public void deleteSale(String saleId) {
        sales.removeIf(sale -> sale.getSaleId().equals(saleId));
        saveSales();
    }
    
    public void updateSale(Sale updatedSale) {
    for (int i = 0; i < sales.size(); i++) {
        if (sales.get(i).getSaleId().equals(updatedSale.getSaleId())) {
            sales.set(i, updatedSale);
            saveSales();
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
                String saleId = parts[0];
                String productId = parts[1];
                String customerName = parts[2];
                int quantity = Integer.parseInt(parts[3]);
                double totalAmount = Double.parseDouble(parts[4]);
                Date saleDate = java.sql.Date.valueOf(parts[5]); // Asumiendo formato YYYY-MM-DD
                String paymentMethod = parts[6];
                
                Sale sale = new Sale(saleId, productId, customerName, quantity, totalAmount, saleDate, paymentMethod);
                sales.add(sale);
            }
        }
        saveSales(); // Guardar en el formato serializado
    } catch (Exception e) {
        System.out.println("Error loading sales from plain file: " + e.getMessage());
    }
}

    public List<Sale> getSales() {
        return new ArrayList<>(sales);
    }
    
    // Implementación de los métodos de DataService
    @Override
    public void saveToFile(List<Sale> data, String filename) {
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
    public List<Sale> loadFromFile(String filename) {
        List<Sale> loadedSales = new ArrayList<>();
        try {
            FileInputStream fis = new FileInputStream(filename);
            ObjectInputStream ois = new ObjectInputStream(fis);
            loadedSales = (List<Sale>) ois.readObject();
            ois.close();
        } catch (Exception e) {
            System.out.println("Error loading from file: " + e.getMessage());
        }
        return loadedSales;
    }
    
    @Override
    public void exportToCSV(List<Sale> data, String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("SaleID,ProductID,CustomerName,Quantity,TotalAmount,SaleDate,PaymentMethod");
            for (Sale sale : data) {
                writer.println(sale.getSaleId() + "," + sale.getProductId() + "," + 
                             sale.getCustomerName() + "," + sale.getQuantity() + "," + 
                             sale.getTotalAmount() + "," + sale.getSaleDate() + "," + 
                             sale.getPaymentMethod());
            }
        } catch (Exception e) {
            System.out.println("Error exporting to CSV: " + e.getMessage());
        }
    }
    
    @Override
    public void exportToXML(List<Sale> data, String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            writer.println("<sales>");
            for (Sale sale : data) {
                writer.println("  <sale>");
                writer.println("    <saleId>" + sale.getSaleId() + "</saleId>");
                writer.println("    <productId>" + sale.getProductId() + "</productId>");
                writer.println("    <customerName>" + sale.getCustomerName() + "</customerName>");
                writer.println("    <quantity>" + sale.getQuantity() + "</quantity>");
                writer.println("    <totalAmount>" + sale.getTotalAmount() + "</totalAmount>");
                writer.println("    <saleDate>" + sale.getSaleDate() + "</saleDate>");
                writer.println("    <paymentMethod>" + sale.getPaymentMethod() + "</paymentMethod>");
                writer.println("  </sale>");
            }
            writer.println("</sales>");
        } catch (Exception e) {
            System.out.println("Error exporting to XML: " + e.getMessage());
        }
    }
    
    @Override
    public void exportToJSON(List<Sale> data, String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("[");
            for (int i = 0; i < data.size(); i++) {
                Sale sale = data.get(i);
                writer.println("  {");
                writer.println("    \"saleId\": \"" + sale.getSaleId() + "\",");
                writer.println("    \"productId\": \"" + sale.getProductId() + "\",");
                writer.println("    \"customerName\": \"" + sale.getCustomerName() + "\",");
                writer.println("    \"quantity\": " + sale.getQuantity() + ",");
                writer.println("    \"totalAmount\": " + sale.getTotalAmount() + ",");
                writer.println("    \"saleDate\": \"" + sale.getSaleDate() + "\",");
                writer.println("    \"paymentMethod\": \"" + sale.getPaymentMethod() + "\"");
                writer.println("  }" + (i < data.size() - 1 ? "," : ""));
            }
            writer.println("]");
        } catch (Exception e) {
            System.out.println("Error exporting to JSON: " + e.getMessage());
        }
    }

    @Override
    public void exportToPlain(List<Sale> data, String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (Sale sale : data) {
                writer.println(sale.getSaleId() + "|" + sale.getProductId() + "|" + 
                             sale.getCustomerName() + "|" + sale.getQuantity() + "|" + 
                             sale.getTotalAmount() + "|" + sale.getSaleDate() + "|" + 
                             sale.getPaymentMethod());
            }
        } catch (Exception e) {
            System.out.println("Error exporting to plain text: " + e.getMessage());
        }
    }
}