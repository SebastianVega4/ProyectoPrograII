package models;

import java.io.Serializable;
import java.util.Date;

public class Sale implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String saleId;
    private String productId;
    private String customerName;
    private int quantity;
    private double totalAmount;
    private Date saleDate;
    private String paymentMethod;
    
    public Sale(String saleId, String productId, String customerName, int quantity, 
                double totalAmount, Date saleDate, String paymentMethod) {
        this.saleId = saleId;
        this.productId = productId;
        this.customerName = customerName;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
        this.saleDate = saleDate;
        this.paymentMethod = paymentMethod;
    }
    
    // Getters and Setters
    public String getSaleId() { return saleId; }
    public void setSaleId(String saleId) { this.saleId = saleId; }
    
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    
    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    
    public Date getSaleDate() { return saleDate; }
    public void setSaleDate(Date saleDate) { this.saleDate = saleDate; }
    
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    
    @Override
    public String toString() {
        return "Venta #" + saleId + " - Cliente: " + customerName + " - Total: $" + totalAmount;
    }
}