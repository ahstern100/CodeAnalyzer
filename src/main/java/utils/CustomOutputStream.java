package utils;

import javax.swing.*;
import java.io.OutputStream;
import java.io.PrintStream;

public class CustomOutputStream extends OutputStream {
    private JTextArea textArea;

    public CustomOutputStream(JTextArea textArea) {
        this.textArea = textArea;
    }

    @Override
    public void write(int b) {
        // מוסיף את התו ל-TextArea
        SwingUtilities.invokeLater(() -> {
            textArea.append(String.valueOf((char)b));
            // גלילה אוטומטית למטה
            textArea.setCaretPosition(textArea.getDocument().getLength());
        });
    }
}