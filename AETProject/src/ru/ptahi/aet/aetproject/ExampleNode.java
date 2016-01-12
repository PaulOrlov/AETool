package ru.ptahi.aet.aetproject;

import java.awt.Image;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import ru.ptahi.iconmanager.IconManager;

/**
 *
 * @author paulorlov
 */
public class ExampleNode extends FilterNode {
    
    private final String iconName = "lookup.png";

    public ExampleNode(Node original) {
        super(original);
    }
    
    @Override
    public Image getIcon(int type) {
        return IconManager.getActiveIcon(iconName);
    }

    @Override
    public Image getOpenedIcon(int type) {
        return IconManager.getOpenedIcon(iconName);
    }
    
}
