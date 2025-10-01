package gui;

import models.Product;
import models.Sale;
import services.ProductService;
import services.SaleService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import javax.swing.KeyStroke;

public class MainFrame extends JFrame {
    private ProductService productService;
    private SaleService saleService;
    private JTable dataTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> objectSelector;
    
    public MainFrame() {
        this.productService = new ProductService();
        this.saleService = new SaleService(productService);
        initializeUI();
        setupKeyboardShortcuts();
    }
    
    private void initializeUI() {
        setTitle("Sistema de Gestión - Tienda");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        
        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Barra de herramientas
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        JButton editButton = new JButton("Editar");
        toolBar.add(editButton);
        editButton.addActionListener(e -> showEditDialog());
        
        // Selector de objeto
        toolBar.add(new JLabel("Gestionar: "));
        objectSelector = new JComboBox<>(new String[]{"Productos", "Ventas"});
        objectSelector.addActionListener(e -> refreshTable());
        toolBar.add(objectSelector);
        
        toolBar.addSeparator();
        
        // Botones de acción
        JButton addButton = new JButton("Agregar");
        JButton deleteButton = new JButton("Eliminar");
        JButton refreshButton = new JButton("Actualizar");
        
        toolBar.add(addButton);
        toolBar.add(deleteButton);
        toolBar.add(refreshButton);
        
        // Botones de exportación
        toolBar.addSeparator();
        JButton exportCSVButton = new JButton("Exportar CSV");
        JButton exportXMLButton = new JButton("Exportar XML");
        JButton exportJSONButton = new JButton("Exportar JSON");
        JButton exportPlainButton = new JButton("Exportar Plano");
        
        toolBar.add(exportCSVButton);
        toolBar.add(exportXMLButton);
        toolBar.add(exportJSONButton);
        toolBar.add(exportPlainButton);
        
        // Botones de carga
        toolBar.addSeparator();
        JButton loadCSVButton = new JButton("Cargar CSV");
        JButton loadXMLButton = new JButton("Cargar XML");
        JButton loadJSONButton = new JButton("Cargar JSON");
        JButton loadPlainButton = new JButton("Cargar Plano");
        
        toolBar.add(loadCSVButton);
        toolBar.add(loadXMLButton);
        toolBar.add(loadJSONButton);
        toolBar.add(loadPlainButton);
        
        mainPanel.add(toolBar, BorderLayout.NORTH);
        
        // Tabla de datos
        tableModel = new DefaultTableModel();
        dataTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(dataTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Action Listeners
        addButton.addActionListener(e -> showAddDialog());
        deleteButton.addActionListener(e -> deleteSelected());
        refreshButton.addActionListener(e -> refreshTable());
        
        exportCSVButton.addActionListener(e -> exportData("CSV"));
        exportXMLButton.addActionListener(e -> exportData("XML"));
        exportJSONButton.addActionListener(e -> exportData("JSON"));
        exportPlainButton.addActionListener(e -> exportData("PLAIN"));
        
        loadCSVButton.addActionListener(e -> loadData("CSV"));
        loadXMLButton.addActionListener(e -> loadData("XML"));
        loadJSONButton.addActionListener(e -> loadData("JSON"));
        loadPlainButton.addActionListener(e -> loadData("PLAIN"));
        
        // Inicializar tabla
        refreshTable();
        
        add(mainPanel);
    }
    
    private void refreshTable() {
        String selected = (String) objectSelector.getSelectedItem();
        tableModel.setRowCount(0);
        tableModel.setColumnCount(0);
        
        if ("Productos".equals(selected)) {
            // Configurar columnas para productos
            tableModel.addColumn("ID");
            tableModel.addColumn("Nombre");
            tableModel.addColumn("Descripción");
            tableModel.addColumn("Precio");
            tableModel.addColumn("Stock");
            tableModel.addColumn("Categoría");
            tableModel.addColumn("Disponible");
            
            // Llenar datos
            List<Product> products = productService.getProducts();
            for (Product product : products) {
                tableModel.addRow(new Object[]{
                    product.getId(),
                    product.getName(),
                    product.getDescription(),
                    product.getPrice(),
                    product.getStock(),
                    product.getCategory(),
                    product.isAvailable() ? "Sí" : "No"
                });
            }
        } else if ("Ventas".equals(selected)) {
            // Configurar columnas para ventas
            tableModel.addColumn("ID Venta");
            tableModel.addColumn("ID Producto");
            tableModel.addColumn("Cliente");
            tableModel.addColumn("Cantidad");
            tableModel.addColumn("Total");
            tableModel.addColumn("Fecha");
            tableModel.addColumn("Método Pago");
            
            // Llenar datos
            List<Sale> sales = saleService.getSales();
            for (Sale sale : sales) {
                tableModel.addRow(new Object[]{
                    sale.getSaleId(),
                    sale.getProductId(),
                    sale.getCustomerName(),
                    sale.getQuantity(),
                    sale.getTotalAmount(),
                    sale.getSaleDate(),
                    sale.getPaymentMethod()
                });
            }
        }
    }
    
    // Método para mostrar diálogo de edición
private void showEditDialog() {
    int selectedRow = dataTable.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Por favor, seleccione un elemento para editar.", 
                                    "Error", JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    String selected = (String) objectSelector.getSelectedItem();
    if ("Productos".equals(selected)) {
        showEditProductDialog(selectedRow);
    } else if ("Ventas".equals(selected)) {
        showEditSaleDialog(selectedRow);
    }
}

private void showEditSaleDialog(int row) {
    String saleId = (String) tableModel.getValueAt(row, 0);
    Sale saleToEdit = null;
    
    // Buscar la venta a editar
    for (Sale sale : saleService.getSales()) {
        if (sale.getSaleId().equals(saleId)) {
            saleToEdit = sale;
            break;
        }
    }
    
    if (saleToEdit != null) {
        JTextField productIdField = new JTextField(saleToEdit.getProductId());
        JTextField customerField = new JTextField(saleToEdit.getCustomerName());
        JTextField quantityField = new JTextField(String.valueOf(saleToEdit.getQuantity()));
        JTextField totalField = new JTextField(String.valueOf(saleToEdit.getTotalAmount()));
        JTextField paymentField = new JTextField(saleToEdit.getPaymentMethod());
        
        Object[] message = {
            "ID Producto:", productIdField,
            "Cliente:", customerField,
            "Cantidad:", quantityField,
            "Total:", totalField,
            "Método Pago:", paymentField
        };
        
        int option = JOptionPane.showConfirmDialog(this, message, "Editar Venta", 
                                                 JOptionPane.OK_CANCEL_OPTION);
        
        if (option == JOptionPane.OK_OPTION) {
            try {
                String productId = productIdField.getText().trim();
                String customer = customerField.getText().trim();
                int quantity = Integer.parseInt(quantityField.getText().trim());
                double total = Double.parseDouble(totalField.getText().trim());
                String payment = paymentField.getText().trim();
                
                if (productId.isEmpty() || customer.isEmpty() || payment.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", 
                                                "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Validar que el producto existe
                Product product = productService.getProductById(productId);
                if (product == null) {
                    JOptionPane.showMessageDialog(this, "El producto con ID " + productId + " no existe.", 
                                                "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Validar stock suficiente (considerando la cantidad original)
                int originalQuantity = saleToEdit.getQuantity();
                int stockChange = quantity - originalQuantity;
                
                if (product.getStock() < stockChange) {
                    JOptionPane.showMessageDialog(this, 
                        "Stock insuficiente. Stock disponible: " + product.getStock(), 
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Actualizar la venta
                saleToEdit.setProductId(productId);
                saleToEdit.setCustomerName(customer);
                saleToEdit.setQuantity(quantity);
                saleToEdit.setTotalAmount(total);
                saleToEdit.setPaymentMethod(payment);
                
                // Actualizar stock del producto
                product.setStock(product.getStock() - stockChange);
                
                saleService.updateSale(saleToEdit);
                refreshTable();
                JOptionPane.showMessageDialog(this, "Venta actualizada exitosamente.");
                
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Cantidad y Total deben ser números válidos.", 
                                            "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

private void showEditProductDialog(int row) {
    String productId = (String) tableModel.getValueAt(row, 0);
    Product product = productService.getProductById(productId);
    
    if (product != null) {
        JTextField nameField = new JTextField(product.getName());
        JTextField descField = new JTextField(product.getDescription());
        JTextField priceField = new JTextField(String.valueOf(product.getPrice()));
        JTextField stockField = new JTextField(String.valueOf(product.getStock()));
        JTextField categoryField = new JTextField(product.getCategory());
        
        Object[] message = {
            "Nombre:", nameField,
            "Descripción:", descField,
            "Precio:", priceField,
            "Stock:", stockField,
            "Categoría:", categoryField
        };
        
        int option = JOptionPane.showConfirmDialog(this, message, "Editar Producto", 
                                                 JOptionPane.OK_CANCEL_OPTION);
        
        if (option == JOptionPane.OK_OPTION) {
            try {
                product.setName(nameField.getText().trim());
                product.setDescription(descField.getText().trim());
                product.setPrice(Double.parseDouble(priceField.getText().trim()));
                product.setStock(Integer.parseInt(stockField.getText().trim()));
                product.setCategory(categoryField.getText().trim());
                
                productService.updateProduct(product);
                refreshTable();
                JOptionPane.showMessageDialog(this, "Producto actualizado exitosamente.");
                
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Precio y Stock deben ser números válidos.", 
                                            "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

    private void showAddDialog() {
        String selected = (String) objectSelector.getSelectedItem();
        
        if ("Productos".equals(selected)) {
            showAddProductDialog();
        } else if ("Ventas".equals(selected)) {
            showAddSaleDialog();
        }
    }
    
    private void showAddProductDialog() {
        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField descField = new JTextField();
        JTextField priceField = new JTextField();
        JTextField stockField = new JTextField();
        JTextField categoryField = new JTextField();
        
        Object[] message = {
            "ID:", idField,
            "Nombre:", nameField,
            "Descripción:", descField,
            "Precio:", priceField,
            "Stock:", stockField,
            "Categoría:", categoryField
        };
        
        int option = JOptionPane.showConfirmDialog(this, message, "Agregar Producto", 
                                                 JOptionPane.OK_CANCEL_OPTION);
        
        if (option == JOptionPane.OK_OPTION) {
            try {
                String id = idField.getText().trim();
                String name = nameField.getText().trim();
                String description = descField.getText().trim();
                double price = Double.parseDouble(priceField.getText().trim());
                int stock = Integer.parseInt(stockField.getText().trim());
                String category = categoryField.getText().trim();
                
                if (id.isEmpty() || name.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "ID y Nombre son obligatorios.", 
                                                "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                Product product = new Product(id, name, description, price, stock, category);
                productService.addProduct(product);
                refreshTable();
                JOptionPane.showMessageDialog(this, "Producto agregado exitosamente.");
                
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Precio y Stock deben ser números válidos.", 
                                            "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void showAddSaleDialog() {
        JTextField saleIdField = new JTextField();
        JTextField productIdField = new JTextField();
        JTextField customerField = new JTextField();
        JTextField quantityField = new JTextField();
        JTextField totalField = new JTextField();
        JTextField paymentField = new JTextField();
        
        Object[] message = {
            "ID Venta:", saleIdField,
            "ID Producto:", productIdField,
            "Cliente:", customerField,
            "Cantidad:", quantityField,
            "Total:", totalField,
            "Método Pago:", paymentField
        };
        
        int option = JOptionPane.showConfirmDialog(this, message, "Registrar Venta", 
                                                 JOptionPane.OK_CANCEL_OPTION);
        
        if (option == JOptionPane.OK_OPTION) {
            try {
                String saleId = saleIdField.getText().trim();
                String productId = productIdField.getText().trim();
                String customer = customerField.getText().trim();
                int quantity = Integer.parseInt(quantityField.getText().trim());
                double total = Double.parseDouble(totalField.getText().trim());
                String payment = paymentField.getText().trim();
                
                if (saleId.isEmpty() || productId.isEmpty() || customer.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", 
                                                "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Validar que el producto existe
                Product product = productService.getProductById(productId);
                if (product == null) {
                    JOptionPane.showMessageDialog(this, "El producto con ID " + productId + " no existe.", 
                                                "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Validar stock suficiente
                if (product.getStock() < quantity) {
                    JOptionPane.showMessageDialog(this, 
                        "Stock insuficiente. Stock disponible: " + product.getStock(), 
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                Sale sale = new Sale(saleId, productId, customer, quantity, total, new java.util.Date(), payment);
                saleService.addSale(sale);
                refreshTable();
                JOptionPane.showMessageDialog(this, "Venta registrada exitosamente.");
                
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Cantidad y Total deben ser números válidos.", 
                                            "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void deleteSelected() {
        int selectedRow = dataTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un elemento para eliminar.", 
                                        "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String selected = (String) objectSelector.getSelectedItem();
        int confirm = JOptionPane.showConfirmDialog(this, 
            "¿Está seguro de que desea eliminar este elemento?", 
            "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if ("Productos".equals(selected)) {
                String productId = (String) tableModel.getValueAt(selectedRow, 0);
                productService.deleteProduct(productId);
            } else if ("Ventas".equals(selected)) {
                String saleId = (String) tableModel.getValueAt(selectedRow, 0);
                saleService.deleteSale(saleId);
            }
            refreshTable();
            JOptionPane.showMessageDialog(this, "Elemento eliminado exitosamente.");
        }
    }
    
    private void exportData(String format) {
        String selected = (String) objectSelector.getSelectedItem();
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Exportar " + selected + " como " + format);
        
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            String filename = fileChooser.getSelectedFile().getAbsolutePath();
            
            // Añadir extensión si no la tiene
            if (!filename.toLowerCase().endsWith(getExtension(format))) {
                filename += getExtension(format);
            }
            
            try {
                if ("Productos".equals(selected)) {
                    List<Product> products = productService.getProducts();
                    switch (format) {
                        case "CSV":
                            productService.exportToCSV(products, filename);
                            break;
                        case "XML":
                            productService.exportToXML(products, filename);
                            break;
                        case "JSON":
                            productService.exportToJSON(products, filename);
                            break;
                        case "PLAIN":
                            productService.exportToPlain(products, filename);
                            break;
                    }
                } else if ("Ventas".equals(selected)) {
                    List<Sale> sales = saleService.getSales();
                    switch (format) {
                        case "CSV":
                            saleService.exportToCSV(sales, filename);
                            break;
                        case "XML":
                            saleService.exportToXML(sales, filename);
                            break;
                        case "JSON":
                            saleService.exportToJSON(sales, filename);
                            break;
                        case "PLAIN":
                            saleService.exportToPlain(sales, filename);
                            break;
                    }
                }
                
                JOptionPane.showMessageDialog(this, 
                    "Datos exportados exitosamente a: " + filename,
                    "Exportación Exitosa", JOptionPane.INFORMATION_MESSAGE);
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, 
                    "Error al exportar datos: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void loadData(String format) {
        String selected = (String) objectSelector.getSelectedItem();
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Cargar " + selected + " desde " + format);
        
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            String filename = fileChooser.getSelectedFile().getAbsolutePath();
            
            try {
                if ("Productos".equals(selected)) {
                    List<Product> loadedProducts = productService.loadFromFile(filename);
                    if (!loadedProducts.isEmpty()) {
                        // Limpiar tabla antes de cargar nuevos datos
                        productService.clearProducts();
                        for (Product product : loadedProducts) {
                            productService.addProduct(product);
                        }
                        JOptionPane.showMessageDialog(this, 
                            loadedProducts.size() + " productos cargados exitosamente.",
                            "Carga Exitosa", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else if ("Ventas".equals(selected)) {
                    List<Sale> loadedSales = saleService.loadFromFile(filename);
                    if (!loadedSales.isEmpty()) {
                        // Limpiar tabla antes de cargar nuevos datos
                        saleService.clearSales();
                        for (Sale sale : loadedSales) {
                            saleService.addSale(sale);
                        }
                        JOptionPane.showMessageDialog(this, 
                            loadedSales.size() + " ventas cargadas exitosamente.",
                            "Carga Exitosa", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
                
                refreshTable();
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, 
                    "Error al cargar datos: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private String getExtension(String format) {
        switch (format) {
            case "CSV": return ".csv";
            case "XML": return ".xml";
            case "JSON": return ".json";
            case "PLAIN": return ".txt";
            default: return ".txt";
        }
    }
    
    private void setupKeyboardShortcuts() {
        // Atajos de teclado básicos
        KeyStroke refreshKey = KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0);
        KeyStroke addKey = KeyStroke.getKeyStroke(KeyEvent.VK_INSERT, 0);
        KeyStroke deleteKey = KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0);
        
        getRootPane().registerKeyboardAction(e -> refreshTable(), refreshKey, JComponent.WHEN_IN_FOCUSED_WINDOW);
        getRootPane().registerKeyboardAction(e -> showAddDialog(), addKey, JComponent.WHEN_IN_FOCUSED_WINDOW);
        getRootPane().registerKeyboardAction(e -> deleteSelected(), deleteKey, JComponent.WHEN_IN_FOCUSED_WINDOW);
        
        // Atajos para carga de datos (requeridos en el enunciado)
        // Ctrl + P + 1 - Cargar productos desde archivo plano
        KeyStroke loadPlain1 = KeyStroke.getKeyStroke(KeyEvent.VK_1, InputEvent.CTRL_DOWN_MASK | InputEvent.ALT_DOWN_MASK);
        getRootPane().registerKeyboardAction(e -> loadProductsFromPlain(), loadPlain1, JComponent.WHEN_IN_FOCUSED_WINDOW);
        
        // Ctrl + P + 2 - Cargar ventas desde archivo plano  
        KeyStroke loadPlain2 = KeyStroke.getKeyStroke(KeyEvent.VK_2, InputEvent.CTRL_DOWN_MASK | InputEvent.ALT_DOWN_MASK);
        getRootPane().registerKeyboardAction(e -> loadSalesFromPlain(), loadPlain2, JComponent.WHEN_IN_FOCUSED_WINDOW);
        
        // Ctrl + J + 1 - Cargar productos desde JSON
        KeyStroke loadJson1 = KeyStroke.getKeyStroke(KeyEvent.VK_1, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK);
        getRootPane().registerKeyboardAction(e -> loadProductsFromJSON(), loadJson1, JComponent.WHEN_IN_FOCUSED_WINDOW);
        
        // Ctrl + J + 2 - Cargar ventas desde JSON
        KeyStroke loadJson2 = KeyStroke.getKeyStroke(KeyEvent.VK_2, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK);
        getRootPane().registerKeyboardAction(e -> loadSalesFromJSON(), loadJson2, JComponent.WHEN_IN_FOCUSED_WINDOW);
        
        // Ctrl + X + 1 - Cargar productos desde XML
        KeyStroke loadXml1 = KeyStroke.getKeyStroke(KeyEvent.VK_1, InputEvent.CTRL_DOWN_MASK | InputEvent.ALT_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK);
        getRootPane().registerKeyboardAction(e -> loadProductsFromXML(), loadXml1, JComponent.WHEN_IN_FOCUSED_WINDOW);
        
        // Ctrl + X + 2 - Cargar ventas desde XML
        KeyStroke loadXml2 = KeyStroke.getKeyStroke(KeyEvent.VK_2, InputEvent.CTRL_DOWN_MASK | InputEvent.ALT_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK);
        getRootPane().registerKeyboardAction(e -> loadSalesFromXML(), loadXml2, JComponent.WHEN_IN_FOCUSED_WINDOW);
        
        // Ctrl + U + 1 - Exportar a CSV con ruta personalizada
        KeyStroke exportCsv = KeyStroke.getKeyStroke(KeyEvent.VK_U, InputEvent.CTRL_DOWN_MASK);
        getRootPane().registerKeyboardAction(e -> exportToCustomCSV(), exportCsv, JComponent.WHEN_IN_FOCUSED_WINDOW);
    }
    
    // Métodos auxiliares para los atajos de teclado
    private void loadProductsFromPlain() {
        objectSelector.setSelectedItem("Productos");
        loadData("PLAIN");
    }
    
    private void loadSalesFromPlain() {
        objectSelector.setSelectedItem("Ventas");
        loadData("PLAIN");
    }
    
    private void loadProductsFromJSON() {
        objectSelector.setSelectedItem("Productos");
        loadData("JSON");
    }
    
    private void loadSalesFromJSON() {
        objectSelector.setSelectedItem("Ventas");
        loadData("JSON");
    }
    
    private void loadProductsFromXML() {
        objectSelector.setSelectedItem("Productos");
        loadData("XML");
    }
    
    private void loadSalesFromXML() {
        objectSelector.setSelectedItem("Ventas");
        loadData("XML");
    }
    
    private void exportToCustomCSV() {
        String selected = (String) objectSelector.getSelectedItem();
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Exportar " + selected + " como CSV - Especifique la ruta");
        
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            String filename = fileChooser.getSelectedFile().getAbsolutePath();
            if (!filename.toLowerCase().endsWith(".csv")) {
                filename += ".csv";
            }
            
            try {
                if ("Productos".equals(selected)) {
                    productService.exportToCSV(productService.getProducts(), filename);
                } else if ("Ventas".equals(selected)) {
                    saleService.exportToCSV(saleService.getSales(), filename);
                }
                
                JOptionPane.showMessageDialog(this, 
                    "Datos exportados exitosamente a: " + filename,
                    "Exportación CSV Exitosa", JOptionPane.INFORMATION_MESSAGE);
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, 
                    "Error al exportar datos: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}