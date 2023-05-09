package ua.knu.csc.iss.ynortman.gui;

import org.apache.commons.lang3.StringUtils;
import ua.knu.csc.iss.ynortman.protocol.sender.Sender;

import javax.swing.*;
import java.awt.*;

public class SenderApp extends JFrame{
    public void init(int k) {
        JPanel boxPanel = new JPanel();
        boxPanel.setLayout(new BoxLayout(boxPanel, BoxLayout.PAGE_AXIS));

        setTitle("Sender");
//        JTextField textField = new JTextField(20);
        JTextArea textField = new JTextArea();
        textField.setColumns(20);
        textField.setLineWrap(true);
        textField.setWrapStyleWord(true);
        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(e -> {
            String text = textField.getText();
            if(!StringUtils.isBlank(text)) {
                JLabel label = new JLabel("<html><div style='width: 200px; " +
                        "margin: 4px; overflow-wrap: break-word;'>"
                        + textField.getText() + "</div></html>");
                Sender sender = new Sender(7, 7, k);
                sender.setTextMsg(text);
                sender.init();
                label.setOpaque(true);
                label.setBackground(new Color(222, 238, 255));
                Dimension size = label.getPreferredSize();
                label.setPreferredSize(new Dimension(size.width, size.height));
                boxPanel.add(label);
                boxPanel.add(Box.createRigidArea(new Dimension(0, 5)));
                textField.setText("");
                boxPanel.revalidate();
            }
        });


        JPanel southPanel = new JPanel();
        southPanel.add(textField);
        southPanel.add(sendButton);


        JScrollPane scrollPane = new JScrollPane(boxPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane);
        add(southPanel, BorderLayout.PAGE_END);


        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setSize(300, 300);
        setVisible(true);
    }
}
