package gui;


import javax.swing.*;
import java.awt.*;

public class SearchWindow extends JFrame {

    public static void main(String[] args) {
        JFrame searchWindow = new SearchWindow();

        searchWindow.setPreferredSize(new Dimension(800, 600));
        searchWindow.pack();
        searchWindow.setVisible(true);
    }

    public SearchWindow () {
        getContentPane().setLayout(new BorderLayout());
        addQueryArea();
        addContextTree();
    }

    private void addQueryArea() {
        JTextField queryField = new JTextField();
        queryField.setBorder(BorderFactory.createTitledBorder("Query"));
        JComponent resultArea = new JScrollPane();
        resultArea.setBorder(BorderFactory.createTitledBorder("Search result"));
        resultArea.add(new JTextArea());

        JPanel queryArea = new JPanel(new BorderLayout());
        queryArea.add(queryField, BorderLayout.NORTH);
        queryArea.add(resultArea, BorderLayout.CENTER);
        getContentPane().add(queryArea, BorderLayout.CENTER);
    }

    private void addContextTree() {
        ContextTree contextTree = new ContextTree();
        JScrollPane contextArea = new JScrollPane(contextTree);
        contextArea.setPreferredSize(contextTree.getPreferredSize());

        contextArea.setBorder(BorderFactory.createTitledBorder("Context"));
        getContentPane().add(contextArea, BorderLayout.EAST);
    }
}
