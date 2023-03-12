package ua.knu.csc.iss.ynortman.gui;

import ua.knu.csc.iss.ynortman.protocol.receiver.Receiver;

import javax.swing.*;
import java.awt.*;

public class ReceiverApp extends JFrame {
    public void init(int k) {
        JPanel boxPanel = new JPanel();
        boxPanel.setLayout(new BoxLayout(boxPanel, BoxLayout.PAGE_AXIS));

        JScrollPane scrollPane = new JScrollPane(boxPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setSize(300, 300);
        setVisible(true);

        while (true) {
            Receiver receiver = new Receiver(7, 7, k);
            String message = receiver.init();
            receiver.createReport();
            JLabel label = new JLabel("<html><div style='width: 200px; " +
                    "margin: 4px; overflow-wrap: break-word;'>"
                    + message + "</div></html>");
            label.setOpaque(true);
            label.setBackground(new Color(222, 238, 255));
            Dimension size = label.getPreferredSize();
            label.setPreferredSize(new Dimension(size.width, size.height));
            boxPanel.add(label);
            boxPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            boxPanel.revalidate();
        }
    }
}
