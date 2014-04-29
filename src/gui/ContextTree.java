package gui;


import index.context.Context;
import index.context.ContextScore;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.util.List;

/**
 * A UI element that can display contexts as a tree, where
 * words and leaves beneath them are context relations
 * with a score.
 */
public class ContextTree extends JTree {

    private static final DefaultMutableTreeNode ROOT =
            new DefaultMutableTreeNode("Context");

    public ContextTree () {
        super(ROOT);
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension preferredSize = super.getPreferredSize();
        preferredSize.width = 200;
        return preferredSize;
    }

    public void displayContextForWords (Context context) {
        ROOT.removeAllChildren();
        for (String word : context.getOriginalWords()) {
            DefaultMutableTreeNode wordNode =
                    nodeWithContext (word, context.getContextScoresForWord(word));
            ROOT.add(wordNode);
        }

        displayAll();
        setRootVisible(false);
    }

    private DefaultMutableTreeNode nodeWithContext(String word, List<ContextScore> contextScores) {
        DefaultMutableTreeNode wordNode = new DefaultMutableTreeNode (word);
        for (ContextScore contextScore : contextScores) {
            String synonym = contextScore.getSecondWord();
            double score = contextScore.getScore();

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
