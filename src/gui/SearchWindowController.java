package gui;

import index.TitleIndex;
import index.context.ContextIndex;
import index.context.ContextsMap;
import index.context.DummyContextIndex;
import search.Query;
import search.SearchEngine;
import search.SearchResults;

import javax.swing.*;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

public class SearchWindowController {

    private final ContextIndex dummyIndex = new DummyContextIndex();
    private final SearchEngine searchEngine;
    private final TitleIndex titleIndex;

    private final SimpleSwingBrowser browser = new SimpleSwingBrowser();

    private JTextField queryField;
    private JEditorPane resultsArea;
    private ContextTree contextTree;

    public SearchWindowController(SearchEngine searchEngine, TitleIndex titleIndex) {
        this.searchEngine = searchEngine;
        this.titleIndex = titleIndex;
    }

    public void displayFrame () {
        JFrame frame = new JFrame ();
        frame.setLayout(new BorderLayout());
        addQueryArea(frame);
        addContextTree(frame);
        setupSearchListener();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(800, 600));
        frame.pack();
        frame.setVisible(true);
    }

    public void displaySearchResults (SearchResults searchResults) {
        StringBuilder results = new StringBuilder ("<html><ul>");
        searchResults.forEach((e) -> {
            results.append("<li>").
                    append("<a href='http://sv.wikipedia.org/wiki/").
                    append(titleIndex.getTitle(e.getKey().getFilePath())).
                    append("'>").
                    append(titleIndex.getTitle(e.getKey().getFilePath())).
                    append("</a>").
                    append("<b>").
                    append(e.getValue()).
                    append("</b>").
                    append("</li>");
        });
        results.append("</ul></html>");
        resultsArea.setText(results.toString());
        resultsArea.setCaretPosition(0);
        
        if(searchResults.hasContextsMap()){
        	displayContext(searchResults.getContextsMap());
        	System.out.println("ContextsMap returned from search " + searchResults.getContextsMap().getOriginalWords());
        }else{
        	displayContext(null);
        	System.out.println("No contextsMap returned from search");
        }
        
    }

    public void displayContext (ContextsMap contextsMap) {
        if(contextsMap == null){ //TODO
        	contextsMap = dummyIndex.getContextsForWords(Arrays.asList("apa", "bil"));
        }
        contextTree.displayContextForWords(contextsMap);
        //TODO Verkar inte uppdater grafiken "efter första contextMappen"
    }

    private void setupSearchListener() {
        queryField.addActionListener((e) -> {
            Query query = new Query(queryField.getText());
            new SearchWorker(query).execute();
        });
        KeyStroke ctrlL = KeyStroke.getKeyStroke(KeyEvent.VK_L,
                                                 Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
        queryField.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(ctrlL, "focus search");
        queryField.getActionMap().put("focus search", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                queryField.requestFocus();
                queryField.selectAll();
            }
        });
    }

    private void addQueryArea(JFrame frame) {
        queryField = new JTextField();
        queryField.setBorder(BorderFactory.createTitledBorder("Query"));

        resultsArea = new JEditorPane();
        resultsArea.setEditorKit(new HTMLEditorKit());
        resultsArea.setEditable(false);
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
