package gui;


import index.context.ContextPostingsList;
import index.context.ContextsMap;
import index.context.WordRelation;
import search.Query;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 * A UI element that can display contexts as a tree, where
 * words and leaves beneath them are context relations
 * with a score.
 */
public class ContextTree extends JTree {

    private static final DefaultMutableTreeNode ROOT =
            new DefaultMutableTreeNode("ContextsMap");

    public ContextTree () {
        super(ROOT);
    }

    public void displayContextForWords (Query query, ContextsMap contextsMap) {
        ROOT.removeAllChildren();
        for (String word : query.getTerms()) {
            ContextPostingsList contextScoresForWord = contextsMap.getContextScoresForWord(word);
            if (contextScoresForWord == null)
                continue;
            DefaultMutableTreeNode wordNode =
                    nodeWithContext (word, contextScoresForWord);
            ROOT.add(wordNode);
        }

		DefaultTreeModel model = (DefaultTreeModel)getModel();
		model.reload(ROOT);
        displayAll();
        setRootVisible(false);
    }

    private DefaultMutableTreeNode nodeWithContext(String word, ContextPostingsList contextPostingsList) {
        DefaultMutableTreeNode wordNode = new DefaultMutableTreeNode (word);
        for (WordRelation wordRelation : contextPostingsList) {

            String synonym = wordRelation.getSecondWord();
            double score = wordRelation.getScore();

            DefaultMutableTreeNode subNode =
                    new DefaultMutableTreeNode(synonym + " " + score);
            wordNode.add(subNode);
        }
        return wordNode;
    }

    private void displayAll () {
        for (int i = 0; i < getRowCount(); i++) {
            expandRow(i);
        }
    }
}
