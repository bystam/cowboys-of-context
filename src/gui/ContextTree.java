package gui;


import index.context.ContextPostingsList;
import index.context.ContextsMap;
import index.context.WordRelation;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;

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

    @Override
    public Dimension getPreferredSize() {
        Dimension preferredSize = super.getPreferredSize();
        preferredSize.width = 200;
        return preferredSize;
    }

    public void displayContextForWords (ContextsMap contextsMap) {
        ROOT.removeAllChildren();
        for (String word : contextsMap.getOriginalWords()) {
            DefaultMutableTreeNode wordNode =
                    nodeWithContext (word, contextsMap.getContextScoresForWord(word));
            ROOT.add(wordNode);
        }

		DefaultTreeModel model = (DefaultTreeModel)getModel();
		model.reload(ROOT);
        displayAll();
        setRootVisible(false);
        revalidate();
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
