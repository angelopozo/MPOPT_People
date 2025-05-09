/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import java.awt.*;

public class PromptSupport extends JLabel implements DocumentListener {
    private final JTextComponent component;

    public PromptSupport(String prompt, JTextComponent component) {
        this.component = component;
        setText(prompt);
        setFont(component.getFont().deriveFont(Font.ITALIC));
        setForeground(Color.GRAY);
        setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
        component.setLayout(new BorderLayout());
        component.add(this, BorderLayout.WEST);
        component.getDocument().addDocumentListener(this);
        setVisible(component.getText().isEmpty());
    }

    @Override public void insertUpdate(DocumentEvent e) { toggle(); }
    @Override public void removeUpdate(DocumentEvent e) { toggle(); }
    @Override public void changedUpdate(DocumentEvent e) { toggle(); }

    private void toggle() {
        setVisible(component.getText().isEmpty());
    }
}
