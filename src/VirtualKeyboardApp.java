import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

public class VirtualKeyboardApp extends JFrame implements ActionListener {
    private JTextArea textArea;
    private JButton[] buttons;
    JLabel correctLabel;
    JLabel incorrectLabel;
    String randomPangrama;
    private int correctKeystrokes;
    private int incorrectKeystrokes;
    private Set<Character> difficultKeys;

    public VirtualKeyboardApp() {
        difficultKeys = new HashSet<>();
        setTitle("Teclado Virtual");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        textArea = new JTextArea(5, 20);
        textArea.setEditable(false);
        add(new JScrollPane(textArea), BorderLayout.NORTH);

        buttons = new JButton[26];
        JPanel keyboardPanel = new JPanel(new GridLayout(2, 13));

        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new JButton(String.valueOf((char) ('A' + i)));
            buttons[i].addActionListener(this);
            keyboardPanel.add(buttons[i]);
        }

        add(keyboardPanel, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);

        textArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                char keyPressed = e.getKeyChar();
                if (Character.isLetter(keyPressed)) {
                    char upperCaseKey = Character.toUpperCase(keyPressed);
                    highlightButton(upperCaseKey);

                    // Check if the pressed key is correct
                    if (upperCaseKey == randomPangrama.charAt(0)) {
                        correctKeystrokes++;
                    } else {
                        incorrectKeystrokes++;
                        difficultKeys.add(upperCaseKey);
                    }

                    // Display the same random pangrama in the JTextArea
                    textArea.setText(randomPangrama);

                    // Update statistics display
                    updateStatisticsDisplay();
                }
            }
        });

        // Select a random pangrama from PangramasConstants
        int randomIndex = (int) (Math.random() * Constantes.PANGRAMAS.length);
        randomPangrama = Constantes.PANGRAMAS[randomIndex];

        // Display the random pangrama in the JTextArea
        textArea.setText(randomPangrama);

        correctLabel = new JLabel("Correct: 0");
        incorrectLabel = new JLabel("Incorrect: 0");

        JPanel statisticsPanel = new JPanel();
        statisticsPanel.add(correctLabel);
        statisticsPanel.add(incorrectLabel);

        add(statisticsPanel, BorderLayout.SOUTH);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton clickedButton = (JButton) e.getSource();
        String buttonText = clickedButton.getText();
        textArea.append(buttonText);

        char keyChar = buttonText.charAt(0);
        highlightButton(keyChar);

        // Check if the pressed key is correct
        if (keyChar == randomPangrama.charAt(0)) {
            correctKeystrokes++;
        } else {
            incorrectKeystrokes++;
            difficultKeys.add(keyChar);
        }

        // Display the same random pangrama in the JTextArea
        textArea.setText(randomPangrama);

        // Update statistics display
        updateStatisticsDisplay();
    }

    private void updateStatisticsDisplay() {
        // Update correct and incorrect keystrokes counters
        correctLabel.setText("Correct: " + correctKeystrokes);
        incorrectLabel.setText("Incorrect: " + incorrectKeystrokes);
        showDifficultKeys(); // Mostrar teclas difíciles
    }
    private void showDifficultKeys() {
        StringBuilder difficultKeysStr = new StringBuilder("Difficult Keys: ");
        for (Character key : difficultKeys) {
            difficultKeysStr.append(key).append(" ");
        }
        textArea.append("\n" + difficultKeysStr.toString());
    }


    private void highlightButton(char keyChar) {
        for (JButton button : buttons) {
            char buttonChar = button.getText().charAt(0);
            if (buttonChar == keyChar) {
                button.setBackground(Color.YELLOW);
            } else {
                button.setBackground(UIManager.getColor("Button.background"));
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new VirtualKeyboardApp().setVisible(true);
            }
        });
    }
}
