package Logic;

import AI.AiDifficulty;
import Fancy.Characters;
import Fancy.Theme;
import GUI.MainMenu;
import GUI.Play;
import GUI.dangnhap; // <-- Imported dangnhap for Logout redirection
import Music.gameMusic;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class MenuActions {

    public static void openPlay(JFrame parent) {
        Play ui = new Play();
        ui.setVisible(true);
        if (parent != null) {
            parent.dispose();
        }
    }

    public static void showNameDialog(JFrame parent) {
        String p1 = themedTextInputDialog(parent, "Enter Player 1 Name:", "Names", logic.getP1Name());
        if (p1 == null) return;
        
        String p2 = themedTextInputDialog(parent, "Enter Player 2 Name:", "Names", logic.getP2Name());
        if (p2 == null) return;
        
        logic.setPlayerNames(p1, p2);
        themedMessageDialog(parent, "Names updated to:\n1. " + p1 + "\n2. " + p2, "Names");
    }

    public static void showTimerDialog(JFrame parent) {
        String[] options = {"Off", "5 seconds", "10 seconds", "15 seconds"};
        
        String initial = "Off";
        int currentLimit = logic.getTurnTimeLimit();
        if (currentLimit == 5) initial = "5 seconds";
        else if (currentLimit == 10) initial = "10 seconds";
        else if (currentLimit == 15) initial = "15 seconds";

        String choice = themedInputDialog(parent, "Select Turn Time Limit:", "Timer", options, initial);
        if (choice == null) return;

        if (choice.equals("Off")) logic.setTurnTimeLimit(0);
        else if (choice.startsWith("5")) logic.setTurnTimeLimit(5);
        else if (choice.startsWith("10")) logic.setTurnTimeLimit(10);
        else logic.setTurnTimeLimit(15);

        themedMessageDialog(parent, "Timer set to " + choice, "Timer");
    }

    public static void showModeDialog(JFrame parent) {
        String[] sizeOptions = {"3x3", "5x5"};
        String sizeChoice = themedInputDialog(parent, "Choose board size", "Mode", sizeOptions, sizeOptions[0]);
        if (sizeChoice == null) return;

        String[] modeOptions = {"Human vs Human", "Human vs AI"};
        String modeChoice = themedInputDialog(parent, "Choose game mode", "Mode", modeOptions, modeOptions[0]);
        if (modeChoice == null) return;

        if (sizeChoice.startsWith("3")) {
            logic.setBoardSize(3);
        } else {
            logic.setBoardSize(5);
        }

        if (modeChoice.startsWith("Human vs AI")) {
            String[] difficultyOptions = {"Easy", "Normal", "Hard"};
            String difficultyChoice = themedInputDialog(parent, "Choose AI difficulty", "Difficulty", difficultyOptions, difficultyOptions[1]);
            if (difficultyChoice == null) return;

            AiDifficulty difficulty;
            switch (difficultyChoice) {
                case "Easy": difficulty = AiDifficulty.EASY; break;
                case "Hard": difficulty = AiDifficulty.HARD; break;
                default: difficulty = AiDifficulty.NORMAL; break;
            }

            logic.setAiEnabled(true);
            logic.setAiDifficulty(difficulty);
            logic.setAiSymbol("🤖");
            themedMessageDialog(parent, "Mode set to " + sizeChoice + " - Human vs AI (" + difficultyChoice + ")", "Mode");
            return;
        }

        logic.setAiEnabled(false);
        themedMessageDialog(parent, "Mode set to " + sizeChoice + " - Human vs Human", "Mode");
    }

    public static void showThemeDialog(JFrame parent) {
        Theme[] themes = Theme.presets();
        String[] names = new String[themes.length];
        for (int i = 0; i < themes.length; i++) names[i] = themes[i].name;

        String choice = themedInputDialog(parent, "Choose a color theme", "Color", names, names[0]);
        if (choice == null) return;

        Theme selected = Theme.byName(choice);
        if (selected != null) {
            logic.setTheme(selected);
            themedMessageDialog(parent, "Theme set to " + selected.name, "Theme");
            if (parent != null) {
                SwingUtilities.invokeLater(() -> {
                    parent.dispose();
                    MainMenu menu = new MainMenu();
                    menu.setVisible(true);
                });
            }
        }
    }

    public static void showCharacterDialog(JFrame parent) {
        String[] options = Characters.names();
        String player1 = themedInputDialog(parent, "Player 1 character", "Character", options, options[0]);
        if (player1 == null) return;
        String player2 = themedInputDialog(parent, "Player 2 character", "Character", options, options.length > 1 ? options[1] : options[0]);
        if (player2 == null) return;

        logic.setPlayerSymbols(player1, player2);
        themedMessageDialog(parent, "Characters set to " + player1 + " and " + player2, "Character");
    }

    public static void showMusicDialog(JFrame parent) {
        String[] options = {"Toggle BGM", "Toggle SFX", "Cancel"};
        withTheme(logic.getTheme(), () -> {
            int choice = JOptionPane.showOptionDialog(
                    parent,
                    "Choose sound setting to toggle:",
                    "Music & Sound Settings",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    options,
                    options[0]
            );
            if (choice == 0) gameMusic.toggleBGM();
            else if (choice == 1) gameMusic.toggleSFX();
            return null;
        });
    }

    // --- NEW PROFILE / LOGOUT METHOD ---
    public static void showProfileDialog(JFrame parent) {
        String currentEmail = logic.getCurrentAccountEmail();
        if (currentEmail == null || currentEmail.isEmpty()) {
            currentEmail = "Not logged in (Guest)";
        }

        String[] options = {"Logout", "Close"};
        String message = "Logged in as: " + currentEmail + "\n\nDo you want to log out?";
        
        withTheme(logic.getTheme(), () -> {
            int choice = JOptionPane.showOptionDialog(
                    parent,
                    message,
                    "User Profile",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[1]
            );

            if (choice == 0) { // If user clicked "Logout"
                logic.setCurrentAccountEmail(""); // Clear the session
                if (parent != null) {
                    parent.dispose(); // Close current Main Menu
                }
                new dangnhap(); // Open Login Form
            }
            return null;
        });
    }

    public static void showHistory(JFrame parent) {
        String query = "SELECT * FROM history"; 

        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            Vector<String> columnNames = new Vector<>();
            for (int column = 1; column <= columnCount; column++) {
                columnNames.add(metaData.getColumnName(column));
            }

            Vector<Vector<Object>> data = new Vector<>();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                    row.add(rs.getObject(columnIndex));
                }
                data.add(row);
            }

            JTable table = new JTable(data, columnNames);
            table.setFillsViewportHeight(true);
            table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
            table.setFont(new Font("SansSerif", Font.PLAIN, 14));
            table.setRowHeight(25);
            table.setEnabled(false); 

            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setPreferredSize(new Dimension(600, 300));

            withTheme(logic.getTheme(), () -> {
                JOptionPane.showMessageDialog(parent, scrollPane, "Match History", JOptionPane.PLAIN_MESSAGE);
                return null;
            });

        } catch (SQLException e) {
            e.printStackTrace();
            themedMessageDialog(parent, "Failed to load history from database.\nMake sure XAMPP is running and the 'history' table exists.\n\nError: " + e.getMessage(), "Database Error");
        }
    }

    private static String themedInputDialog(JFrame parent, String message, String title, String[] options, String initial) {
        return withTheme(logic.getTheme(), () -> {
            JOptionPane pane = new JOptionPane(message, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
            pane.setWantsInput(true);
            pane.setSelectionValues(options);
            pane.setInitialSelectionValue(initial);
            JDialog dialog = pane.createDialog(parent, title);
            dialog.setAlwaysOnTop(true);
            dialog.setModal(true);
            dialog.setVisible(true);
            Object value = pane.getInputValue();
            if (value == null || value == JOptionPane.UNINITIALIZED_VALUE) return null;
            return value.toString();
        });
    }

    private static String themedTextInputDialog(JFrame parent, String message, String title, String initial) {
        return withTheme(logic.getTheme(), () -> {
            JOptionPane pane = new JOptionPane(message, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
            pane.setWantsInput(true);
            pane.setSelectionValues(null); 
            pane.setInitialSelectionValue(initial);
            JDialog dialog = pane.createDialog(parent, title);
            dialog.setAlwaysOnTop(true);
            dialog.setModal(true);
            dialog.setVisible(true);
            Object value = pane.getInputValue();
            if (value == null || value == JOptionPane.UNINITIALIZED_VALUE) return null;
            return value.toString();
        });
    }

    private static void themedMessageDialog(JFrame parent, String message, String title) {
        withTheme(logic.getTheme(), () -> {
            JOptionPane.showMessageDialog(parent, message, title, JOptionPane.PLAIN_MESSAGE);
            return null;
        });
    }

    private static <T> T withTheme(Theme theme, ThemeDialogAction<T> action) {
        Map<String, Object> oldValues = new HashMap<>();
        String[] keys = {"OptionPane.background", "Panel.background", "OptionPane.messageForeground", "OptionPane.messageFont", "OptionPane.buttonFont", "Button.foreground", "ComboBox.background", "ComboBox.foreground", "ComboBox.selectionBackground", "ComboBox.selectionForeground", "ComboBox.font"};
        for (String key : keys) oldValues.put(key, UIManager.get(key));

        UIManager.put("OptionPane.background", theme.primaryDark.darker());
        UIManager.put("Panel.background", theme.primaryDark.darker());
        UIManager.put("OptionPane.messageForeground", theme.text);
        UIManager.put("OptionPane.messageFont", new Font("SansSerif", Font.BOLD, 16));
        UIManager.put("OptionPane.buttonFont", new Font("SansSerif", Font.BOLD, 14));
        UIManager.put("Button.foreground", theme.textDark);
        UIManager.put("ComboBox.background", Color.WHITE);
        UIManager.put("ComboBox.foreground", theme.textDark);
        UIManager.put("ComboBox.selectionBackground", theme.accent);
        UIManager.put("ComboBox.selectionForeground", theme.textDark); 
        UIManager.put("ComboBox.font", new Font("SansSerif", Font.BOLD, 14));

        try {
            return action.run();
        } finally {
            for (Map.Entry<String, Object> entry : oldValues.entrySet()) {
                UIManager.put(entry.getKey(), entry.getValue());
            }
        }
    }

    private interface ThemeDialogAction<T> { T run(); }
}