package services;

import models.Product;
import models.Sale;
import java.io.*;
import java.util.List;

public class FileExportManager {
    
    public static void exportProductsToFormats(List<Product> products, String baseFilename) {
        exportProductsToCSV(products, baseFilename + ".csv");
        exportProductsToXML(products, baseFilename + ".xml");
        exportProductsToJSON(products, baseFilename + ".json");
        exportProductsToPlain(products, baseFilename + ".txt");
    }
    
    public static void exportSalesToFormats(List<Sale> sales, String baseFilename) {
        exportSalesToCSV(sales, baseFilename + ".csv");
        exportSalesToXML(sales, baseFilename + ".xml");
        exportSalesToJSON(sales, baseFilename + ".json");
        exportSalesToPlain(sales, baseFilename + ".txt");
    }
    
    private static void exportProductsToCSV(List<Product> products, String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("ID,Nombre,Descripción,Precio,Stock,Categoría,Disponible");
            for (Product product : products) {
                writer.println(String.format("%s,%s,%s,%.2f,%d,%s,%s",
                    product.getId(), product.getName(), product.getDescription(),
                    product.getPrice(), product.getStock(), product.getCategory(),
                    product.isAvailable() ? "Sí" : "No"));
            }
        } catch (Exception e) {
            System.out.println("Error exporting products to CSV: " + e.getMessage());
        }
    }
    
    private static void exportProductsToXML(List<Product> products, String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            writer.println("<productos>");
            for (Product product : products) {
                writer.println("  <producto>");
                writer.println("    <id>" + escapeXML(product.getId()) + "</id>");
                writer.println("    <nombre>" + escapeXML(product.getName()) + "</nombre>");
                writer.println("    <descripcion>" + escapeXML(product.getDescription()) + "</descripcion>");
                writer.println("    <precio>" + product.getPrice() + "</precio>");
                writer.println("    <stock>" + product.getStock() + "</stock>");
                writer.println("    <categoria>" + escapeXML(product.getCategory()) + "</categoria>");
                writer.println("    <disponible>" + product.isAvailable() + "</disponible>");
                writer.println("  </producto>");
            }
            writer.println("</productos>");
        } catch (Exception e) {
            System.out.println("Error exporting products to XML: " + e.getMessage());
        }
    }
    
    private static void exportProductsToJSON(List<Product> products, String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("[");
            for (int i = 0; i < products.size(); i++) {
                Product product = products.get(i);
                writer.println("  {");
                writer.println("    \"id\": \"" + escapeJSON(product.getId()) + "\",");
                writer.println("    \"nombre\": \"" + escapeJSON(product.getName()) + "\",");
                writer.println("    \"descripcion\": \"" + escapeJSON(product.getDescription()) + "\",");
                writer.println("    \"precio\": " + product.getPrice() + ",");
                writer.println("    \"stock\": " + product.getStock() + ",");
                writer.println("    \"categoria\": \"" + escapeJSON(product.getCategory()) + "\",");
                writer.println("    \"disponible\": " + product.isAvailable());
                writer.println("  }" + (i < products.size() - 1 ? "," : ""));
            }
            writer.println("]");
        } catch (Exception e) {
            System.out.println("Error exporting products to JSON: " + e.getMessage());
        }
    }
    
    private static void exportProductsToPlain(List<Product> products, String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("ID|Nombre|Descripción|Precio|Stock|Categoría|Disponible");
            for (Product product : products) {
                writer.println(String.format("%s|%s|%s|%.2f|%d|%s|%s",
                    product.getId(), product.getName(), product.getDescription(),
                    product.getPrice(), product.getStock(), product.getCategory(),
                    product.isAvailable() ? "Sí" : "No"));
            }
        } catch (Exception e) {
            System.out.println("Error exporting products to plain text: " + e.getMessage());
        }
    }
    
    private static void exportSalesToCSV(List<Sale> sales, String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("ID_Venta,ID_Producto,Cliente,Cantidad,Total,Fecha,Método_Pago");
            for (Sale sale : sales) {
                writer.println(String.format("%s,%s,%s,%d,%.2f,%s,%s",
                    sale.getSaleId(), sale.getProductId(), sale.getCustomerName(),
                    sale.getQuantity(), sale.getTotalAmount(), sale.getSaleDate(),
                    sale.getPaymentMethod()));
            }
        } catch (Exception e) {
            System.out.println("Error exporting sales to CSV: " + e.getMessage());
        }
    }
    
    private static void exportSalesToXML(List<Sale> sales, String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            writer.println("<ventas>");
            for (Sale sale : sales) {
                writer.println("  <venta>");
                writer.println("    <idVenta>" + escapeXML(sale.getSaleId()) + "</idVenta>");
                writer.println("    <idProducto>" + escapeXML(sale.getProductId()) + "</idProducto>");
                writer.println("    <cliente>" + escapeXML(sale.getCustomerName()) + "</cliente>");
                writer.println("    <cantidad>" + sale.getQuantity() + "</cantidad>");
                writer.println("    <total>" + sale.getTotalAmount() + "</total>");
                writer.println("    <fecha>" + sale.getSaleDate() + "</fecha>");
                writer.println("    <metodoPago>" + escapeXML(sale.getPaymentMethod()) + "</metodoPago>");
                writer.println("  </venta>");
            }
            writer.println("</ventas>");
        } catch (Exception e) {
            System.out.println("Error exporting sales to XML: " + e.getMessage());
        }
    }
    
    private static void exportSalesToJSON(List<Sale> sales, String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("[");
            for (int i = 0; i < sales.size(); i++) {
                Sale sale = sales.get(i);
                writer.println("  {");
                writer.println("    \"idVenta\": \"" + escapeJSON(sale.getSaleId()) + "\",");
                writer.println("    \"idProducto\": \"" + escapeJSON(sale.getProductId()) + "\",");
                writer.println("    \"cliente\": \"" + escapeJSON(sale.getCustomerName()) + "\",");
                writer.println("    \"cantidad\": " + sale.getQuantity() + ",");
                writer.println("    \"total\": " + sale.getTotalAmount() + ",");
                writer.println("    \"fecha\": \"" + sale.getSaleDate() + "\",");
                writer.println("    \"metodoPago\": \"" + escapeJSON(sale.getPaymentMethod()) + "\"");
                writer.println("  }" + (i < sales.size() - 1 ? "," : ""));
            }
            writer.println("]");
        } catch (Exception e) {
            System.out.println("Error exporting sales to JSON: " + e.getMessage());
        }
    }
    
    private static void exportSalesToPlain(List<Sale> sales, String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("ID_Venta|ID_Producto|Cliente|Cantidad|Total|Fecha|Método_Pago");
            for (Sale sale : sales) {
                writer.println(String.format("%s|%s|%s|%d|%.2f|%s|%s",
                    sale.getSaleId(), sale.getProductId(), sale.getCustomerName(),
                    sale.getQuantity(), sale.getTotalAmount(), sale.getSaleDate(),
                    sale.getPaymentMethod()));
            }
        } catch (Exception e) {
            System.out.println("Error exporting sales to plain text: " + e.getMessage());
        }
    }
    
    private static String escapeXML(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                  .replace("<", "&lt;")
                  .replace(">", "&gt;")
                  .replace("\"", "&quot;")
                  .replace("'", "&apos;");
    }
    
    private static String escapeJSON(String text) {
        if (text == null) return "";
        return text.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\b", "\\b")
                  .replace("\f", "\\f")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }
}