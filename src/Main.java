import gui.LoginFrame;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Establecer el look and feel del sistema
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Ejecutar la aplicación en el Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}