import GUI.MENU;

public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            MENU ui = new MENU();
            ui.setVisible(true);
        });
    }
}