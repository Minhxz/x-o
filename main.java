import GUI.MENU;

public class main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            MENU ui = new MENU();
            ui.setVisible(true);
        });
    }
}