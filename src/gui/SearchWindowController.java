package gui;

import common.Document;
import index.TitleIndex;
import index.context.ContextIndex;
import index.context.ContextsMap;
import index.context.DummyContextIndex;
import search.Query;
import search.SearchEngine;
import search.SearchResults;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class SearchWindowController {

    private static final int DISPLAYED_RESULTS_AMOUNT = 20;
    private static final Image BROWSER_IMAGE = new ImageIcon("src/img/browser_icon.png").getImage();
    private static final Icon BROWSER_ICON_SMALL = new ImageIcon(BROWSER_IMAGE.getScaledInstance(14, 14, Image.SCALE_SMOOTH));

    private final ContextIndex dummyIndex = new DummyContextIndex();
    private final SearchEngine searchEngine;
    private final TitleIndex titleIndex;

    private final SimpleSwingBrowser browser = new SimpleSwingBrowser();

    private JTextField queryField;
    private JPanel resultsArea;
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
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.ipadx = gbc.ipady = 3;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        int resultNumber = 0;

        resultsArea.removeAll();
        for (Map.Entry<Document, Double> e : searchResults) {
            if (resultNumber++ == DISPLAYED_RESULTS_AMOUNT)
                break;
            gbc.gridy++;
            final String name = titleIndex.getTitle(e.getKey().getFilePath());

            JLabel nameLabel = new JLabel(name);
            JButton browserButton = new JButton(BROWSER_ICON_SMALL);
            browserButton.addActionListener((ae) -> {
                browser.setVisible(true);
                browser.loadURL(nameToWikiLink(name));
            });

            gbc.gridx = 0;
            gbc.weightx = 1d;
            resultsArea.add(nameLabel, gbc);
            gbc.gridx = 1;
            gbc.weightx = 4d;
            resultsArea.add(browserButton, gbc);
        }
        gbc.gridy++;
        gbc.weighty = 1d;
        JPanel fillSpace = new JPanel();
        fillSpace.setBackground(resultsArea.getBackground());
        resultsArea.add(fillSpace, gbc);
        resultsArea.revalidate();
        resultsArea.repaint();

        if (searchResults.hasContextsMap()) {
        	displayContext(searchResults.getContextsMap());
        	System.out.println("ContextsMap returned from search " + searchResults.getContextsMap().getOriginalWords());
        } else {
        	displayContext(null);
        	System.out.println("No contextsMap returned from search");
        }
    }

    private String nameToWikiLink (String name) {
        name = name.replaceAll(" ", "_");
        return String.format("http://sv.wikipedia.org/wiki/%s", name);
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

        resultsArea = new JPanel ();
        resultsArea.setLayout(new GridBagLayout());
        resultsArea.setBackground(Color.WHITE);
        resultsArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
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
