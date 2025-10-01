package utils;

import javax.swing.JTextField;
import javax.swing.JOptionPane;

public class ValidationUtils {
    
    public static boolean validateRequiredField(JTextField field, String fieldName) {
        if (field.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, 
                "El campo " + fieldName + " es obligatorio.", 
                "Error de Validación", JOptionPane.ERROR_MESSAGE);
            field.requestFocus();
            return false;
        }
        return true;
    }
    
    public static boolean validateNumericField(JTextField field, String fieldName) {
        try {
            Double.parseDouble(field.getText().trim());
            return true;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, 
                "El campo " + fieldName + " debe ser un número válido.", 
                "Error de Validación", JOptionPane.ERROR_MESSAGE);
            field.requestFocus();
            return false;
        }
    }
    
    public static boolean validateIntegerField(JTextField field, String fieldName) {
        try {
            Integer.parseInt(field.getText().trim());
            return true;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, 
                "El campo " + fieldName + " debe ser un número entero válido.", 
                "Error de Validación", JOptionPane.ERROR_MESSAGE);
            field.requestFocus();
            return false;
        }
    }
}