package gui;


import javax.swing.*;
import java.awt.*;

public class SearchWindow extends JFrame {

    public static void main(String[] args) {
        new SearchWindow();
    }

    public SearchWindow () {
        setLayout(new BorderLayout());
        JTextField queryField = getQueryField ();
        JComponent resultArea = getResultArea();
        JTree contextTree = new ContextTree();
        JPanel searchPanel = new JPanel(new BorderLayout(3, 3));
        searchPanel.add(queryField, BorderLayout.NORTH);
        searchPanel.add(resultArea, BorderLayout.CENTER);
        getContentPane().add(searchPanel, BorderLayout.CENTER);
        getContentPane().add(contextTree, BorderLayout.EAST);

        setPreferredSize(new Dimension(800, 600));
        pack();
        setVisible(true);
    }

    private JTextField getQueryField() {
        return new JTextField();
    }

    private JComponent getResultArea() {
        JComponent resultArea = new JScrollPane();
        resultArea.add(new JTextArea());
        return resultArea;
    }
}
