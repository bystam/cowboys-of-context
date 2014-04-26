package gui;


import index.context.ContextIndex;
import index.context.ContextScore;
import index.context.DummyContextIndex;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class SearchWindow extends JFrame {


    private final ContextIndex dummyIndex = new DummyContextIndex();

    private ContextTree contextTree;

    public static void main(String[] args) {
        SearchWindow searchWindow = new SearchWindow();

        searchWindow.setPreferredSize(new Dimension(800, 600));
        searchWindow.pack();
        searchWindow.setVisible(true);
        searchWindow.displayContext(null);
    }

    public SearchWindow () {
        getContentPane().setLayout(new BorderLayout());
        addQueryArea();
        addContextTree();
    }

    public void displayContext (Map<String, java.util.List<ContextScore>> context) {
        contextTree.displayContextForWords(dummyIndex.getContextForWords(Arrays.asList("apa", "bil")));
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
        contextTree = new ContextTree();
        JScrollPane contextArea = new JScrollPane(contextTree);
        contextArea.setPreferredSize(contextTree.getPreferredSize());

        contextArea.setBorder(BorderFactory.createTitledBorder("Context"));
        getContentPane().add(contextArea, BorderLayout.EAST);
    }

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
    }
}
