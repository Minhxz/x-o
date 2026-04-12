package Logic;

import AI.AiDifficulty;
import Fancy.Characters;
import Fancy.Theme;
import GUI.MainMenu;
import GUI.Play;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class MenuActions {

    public static void openPlay(JFrame parent) {
        Play ui = new Play();
        ui.setVisible(true);
        if (parent != null) {
            parent.dispose();
        }
    }

    public static void showModeDialog(JFrame parent) {
        String[] sizeOptions = {"3x3", "5x5"};
        String sizeChoice = themedInputDialog(
                parent,
                "Choose board size",
                "Mode",
                sizeOptions,
                sizeOptions[0]
        );
        if (sizeChoice == null) return;

        String[] modeOptions = {"Human vs Human", "Human vs AI"};
        String modeChoice = themedInputDialog(
                parent,
                "Choose game mode",
                "Mode",
                modeOptions,
                modeOptions[0]
        );
        if (modeChoice == null) return;

        if (sizeChoice.startsWith("3")) {
            logic.setBoardSize(3);
        } else {
            logic.setBoardSize(5);
        }

        if (modeChoice.startsWith("Human vs AI")) {
            String[] difficultyOptions = {"Easy", "Normal", "Hard"};
            String difficultyChoice = themedInputDialog(
                    parent,
                    "Choose AI difficulty",
                    "Difficulty",
                    difficultyOptions,
                    difficultyOptions[1]
            );
            if (difficultyChoice == null) return;

            AiDifficulty difficulty;
            switch (difficultyChoice) {
                case "Easy":
                    difficulty = AiDifficulty.EASY;
                    break;
                case "Hard":
                    difficulty = AiDifficulty.HARD;
                    break;
                case "Normal":
                default:
                    difficulty = AiDifficulty.NORMAL;
                    break;
            }

            logic.setAiEnabled(true);
            logic.setAiDifficulty(difficulty);
            logic.setAiSymbol("🤖");
            themedMessageDialog(parent,
                    "Mode set to " + sizeChoice + " - Human vs AI (" + difficultyChoice + ")",
                    "Mode");
            return;
        }

        logic.setAiEnabled(false);
        themedMessageDialog(parent, "Mode set to " + sizeChoice + " - Human vs Human", "Mode");
    }

    public static void showThemeDialog(JFrame parent) {
        Theme[] themes = Theme.presets();
        String[] names = new String[themes.length];
        for (int i = 0; i < themes.length; i++) {
            names[i] = themes[i].name;
        }

        String choice = themedInputDialog(
                parent,
                "Choose a color theme",
                "Color",
                names,
                names[0]
        );
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

        String player1 = themedInputDialog(
                parent,
                "Player 1 character",
                "Character",
                options,
                options[0]
        );
        if (player1 == null) return;

        String player2 = themedInputDialog(
                parent,
                "Player 2 character",
                "Character",
                options,
                options.length > 1 ? options[1] : options[0]
        );
        if (player2 == null) return;

        logic.setPlayerSymbols(player1, player2);
        themedMessageDialog(parent, "Characters set to " + player1 + " and " + player2, "Character");
    }

    private static String themedInputDialog(JFrame parent, String message, String title, String[] options, String initial) {
        Theme theme = logic.getTheme();
        return withTheme(theme, () -> {
            JOptionPane pane = new JOptionPane(message, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
            pane.setWantsInput(true);
            pane.setSelectionValues(options);
            pane.setInitialSelectionValue(initial);
            JDialog dialog = pane.createDialog(parent, title);
            dialog.setAlwaysOnTop(true);
            dialog.setModal(true);
            dialog.setVisible(true);
            Object value = pane.getInputValue();
            if (value == null || value == JOptionPane.UNINITIALIZED_VALUE) {
                return null;
            }
            return value.toString();
        });
    }

    private static void themedMessageDialog(JFrame parent, String message, String title) {
        Theme theme = logic.getTheme();
        withTheme(theme, () -> {
            JOptionPane.showMessageDialog(parent, message, title, JOptionPane.PLAIN_MESSAGE);
            return null;
        });
    }

    private static <T> T withTheme(Theme theme, ThemeDialogAction<T> action) {
        Map<String, Object> oldValues = new HashMap<>();
        String[] keys = {
                "OptionPane.background",
                "Panel.background",
                "OptionPane.messageForeground",
                "OptionPane.messageFont",
                "OptionPane.buttonFont",
                "Button.background",
                "Button.foreground",
                "Button.select",
                "Button.focus"
        };
        for (String key : keys) {
            oldValues.put(key, UIManager.get(key));
        }

        UIManager.put("OptionPane.background", theme.primaryDark.darker());
        UIManager.put("Panel.background", theme.primaryDark.darker());
        UIManager.put("OptionPane.messageForeground", theme.text);
        UIManager.put("OptionPane.messageFont", new Font("SansSerif", Font.BOLD, 16));
        UIManager.put("OptionPane.buttonFont", new Font("SansSerif", Font.BOLD, 14));
        UIManager.put("Button.background", theme.primary);
        UIManager.put("Button.foreground", theme.text);
        UIManager.put("Button.select", theme.accent);
        UIManager.put("Button.focus", theme.accent);

        try {
            return action.run();
        } finally {
            for (Map.Entry<String, Object> entry : oldValues.entrySet()) {
                UIManager.put(entry.getKey(), entry.getValue());
            }
        }
    }

    private interface ThemeDialogAction<T> {
        T run();
    }
}