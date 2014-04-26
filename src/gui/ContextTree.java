package gui;


import javax.swing.*;
import java.awt.*;

public class ContextTree extends JTree {


    @Override
    public Dimension getPreferredSize() {
        Dimension preferredSize = super.getPreferredSize();
        preferredSize.width = 200;
        return preferredSize;
    }
}
