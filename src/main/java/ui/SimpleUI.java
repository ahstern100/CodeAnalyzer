package ui;

import process.EntryPoint;
import utils.CustomOutputStream;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;

import static utils.Utils.prop;

public class SimpleUI extends JFrame {

    // הגדרת הרכיבים
    private JTextField urlField;
    private JTextField tokenField;
    private JTextField queryField;
    private JTextField languageModelName;
    private JTextField embedModelName;
    private JTextArea resultArea;
    private JButton runButton;
    private JTextArea logArea;
    private JCheckBox isRepositoryAlreadyExist;
    public SimpleUI() {
        // הגדרות חלון בסיסיות
        setTitle("GitHub Repo Analyzer");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // מרכז את החלון במסך
        setLayout(new BorderLayout(10, 10));

        // פאנל עליון לקלטים
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(6, 1, 5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inputPanel.add(new JLabel("GitHub Repository URL:"));
        urlField = new JTextField(prop.getProperty("repoUrl"));
        inputPanel.add(urlField);

        inputPanel.add(new JLabel("GitHub Repository token:"));
        tokenField = new JTextField(prop.getProperty("repoToken"));
        inputPanel.add(tokenField);

        inputPanel.add(new JLabel("Your Query:"));
        queryField = new JTextField("queue");
        inputPanel.add(queryField);

        inputPanel.add(new JLabel("Language Model Name"));
        languageModelName = new JTextField("OLLAMA");
        inputPanel.add(languageModelName);

        inputPanel.add(new JLabel("Embedding Model Name"));
        embedModelName = new JTextField("ONNX");
        inputPanel.add(embedModelName);

        inputPanel.add(new JLabel("Repository already exists"));
        isRepositoryAlreadyExist = new JCheckBox("", true);
        inputPanel.add(isRepositoryAlreadyExist);

        // כפתור הרצה
        runButton = new JButton("Run Analysis");

        // פאנל תחתון לתוצאה
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Result:"));

        logArea = new JTextArea();
        logArea.setBackground(Color.BLACK); // שייראה כמו טרמינל
        logArea.setForeground(Color.GREEN);
        logArea.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(logArea);
        logScrollPane.setPreferredSize(new Dimension(480, 300));
        logScrollPane.setBorder(BorderFactory.createTitledBorder("Live Console Logs:"));

        PrintStream printStream = new PrintStream(new CustomOutputStream(logArea));
        System.setOut(printStream);
        System.setErr(printStream);

        add(logScrollPane, BorderLayout.CENTER);

        // הוספת פונקציונליות לכפתור
        runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleRun();
            }
        });

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS)); // סידור אנכי

        runButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(Box.createVerticalStrut(10)); // רווח קטן למעלה
        centerPanel.add(runButton);
        centerPanel.add(Box.createVerticalStrut(10)); // רווח קטן בין הכפתור ללוגים

        centerPanel.add(logScrollPane);

        // הוספת רכיבים לחלון הראשי
        add(inputPanel, BorderLayout.NORTH);   // הקלטים למעלה
        add(centerPanel, BorderLayout.CENTER); // הכפתור והלוגים באמצע
        add(scrollPane, BorderLayout.SOUTH);   // התוצאה הסופית למטה

        // התאמת גובה ה-scrollPane
        scrollPane.setPreferredSize(new Dimension(480, 200));
    }

    private void handleRun() {
        String url = urlField.getText();
        String token = tokenField.getText();
        String query = queryField.getText();
        String langModelName = languageModelName.getText();
        String embedModelName = this.embedModelName.getText();
        boolean isRepositoryAlreadyExist = this.isRepositoryAlreadyExist.isSelected();

        if (url.isEmpty() || query.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // כאן אתה קורא לפונקציה הלוגית שלך מהפרויקט
        resultArea.setText("Analyzing " + url + "...\nWait for response...");

        new Thread(() -> {
            try {
                // קריאה לפונקציית הניתוח המקורית שלך
                String response = EntryPoint.runProcessOnRepo(url, token, isRepositoryAlreadyExist, langModelName, embedModelName, query);

                // עדכון ה-UI עם התוצאה חייב לחזור ל-Thread של ה-UI
                SwingUtilities.invokeLater(() -> {
                    resultArea.setText(response);
                    runButton.setEnabled(true);
                });
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    resultArea.setText("שגיאה: " + ex.getMessage());
                    runButton.setEnabled(true);
                });
            }
        }).start();


    }
}