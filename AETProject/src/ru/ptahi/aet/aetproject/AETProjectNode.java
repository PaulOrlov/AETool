package ru.ptahi.aet.aetproject;

import java.awt.Image;
import javax.swing.Action;
import org.netbeans.spi.project.ui.support.CommonProjectActions;
import org.netbeans.spi.project.ui.support.NodeFactorySupport;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;
import ru.ptahi.iconmanager.IconManager;

/**
 *
 * @author paulorlov
 */
public class AETProjectNode extends FilterNode {

    private final String iconName = "lookup.png";
    private final AETProject project;

    public AETProjectNode(Node node, final AETProject project) throws DataObjectNotFoundException {
        super(node,
              NodeFactorySupport.createCompositeChildren(project, "Projects/ru-ptahi-aet-aetproject/Nodes"),
//                                        new FilterNode.Children(node),
              new ProxyLookup( new Lookup[]{ Lookups.singleton(project), node.getLookup() })
        );
        this.project = project;        
        
    }

    @Override
    public Action[] getActions(boolean arg0) {
        return new Action[]{
            CommonProjectActions.newFileAction(),
            CommonProjectActions.copyProjectAction(),
            CommonProjectActions.deleteProjectAction(),
            CommonProjectActions.closeProjectAction(),
            new AddParticipant(project),
            new AddExperiment(project),
            new AddStimuli(project)
        };
    }

    @Override
    public Image getIcon(int type) {
        return IconManager.getActiveIcon(iconName);
    }

    @Override
    public Image getOpenedIcon(int type) {
        return IconManager.getOpenedIcon(iconName);
    }
    
    

    @Override
    public String getDisplayName() {
        return project.getProjectDirectory().getName();
    }
            
}