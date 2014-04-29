package gui;

import index.context.ContextsMap;
import index.context.ContextIndex;
import index.context.DummyContextIndex;
import search.ContextSearchEngine;
import search.Query;
import search.SearchEngine;
import search.SearchResults;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

public class SearchWindowController {

    private final ContextIndex dummyIndex = new DummyContextIndex();
    private final SearchEngine searchEngine;

    private JTextField queryField;
    private JTextArea resultsArea;
    private ContextTree contextTree;

    public static void main(String[] args) {
        SearchWindowController searchWindowController =
                new SearchWindowController(new ContextSearchEngine(null, null));

        searchWindowController.displayFrame();
        searchWindowController.displayContext(null);
    }

    public SearchWindowController(SearchEngine searchEngine) {
        this.searchEngine = searchEngine;
    }

    public void displayFrame () {
        JFrame frame = new JFrame ();
        frame.setLayout(new BorderLayout());
        addQueryArea(frame);
        addContextTree(frame);
        setupSearchListener();

        frame.setPreferredSize(new Dimension(800, 600));
        frame.pack();
        frame.setVisible(true);
    }

    public void displaySearchResults (SearchResults searchResults) {
        System.out.println("testing testing :)");
    }

    public void displayContext (ContextsMap contextsMap) {
        // TODO dummy
        contextsMap = dummyIndex.getContextsForWords(Arrays.asList("apa", "bil"));
        contextTree.displayContextForWords(contextsMap);
    }

    private void setupSearchListener() {
        queryField.addActionListener((e) -> {
            Query query = new Query(queryField.getText());
            new SearchWorker(query).execute();
        });
    }

    private void addQueryArea(JFrame frame) {
        queryField = new JTextField();
        queryField.setBorder(BorderFactory.createTitledBorder("Query"));

        resultsArea = new JTextArea();
        JScrollPane resultsScrollPane = new JScrollPane(resultsArea);
        resultsScrollPane.setBorder(BorderFactory.createTitledBorder("Search result"));

        JPanel queryArea = new JPanel(new BorderLayout());
        queryArea.add(queryField, BorderLayout.NORTH);
        queryArea.add(resultsScrollPane, BorderLayout.CENTER);
        frame.getContentPane().add(queryArea, BorderLayout.CENTER);
    }

    private void addContextTree(JFrame frame) {
        contextTree = new ContextTree();
        JScrollPane contextArea = new JScrollPane(contextTree);
        contextArea.setPreferredSize(contextTree.getPreferredSize());

        contextArea.setBorder(BorderFactory.createTitledBorder("ContextsMap"));
        frame.getContentPane().add(contextArea, BorderLayout.EAST);
    }

    private class SearchWorker extends SwingWorker<SearchResults, Void> {

        private final Query query;

        SearchWorker (Query query) {
            this.query = query;
        }

        @Override
        protected SearchResults doInBackground() throws Exception {
            return searchEngine.search(query);
        }

        @Override
        protected void done() {
            try {
                displaySearchResults(get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
}
