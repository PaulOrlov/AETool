package ru.ptahi.cfe.stumile;

import java.awt.Image;
import java.beans.IntrospectionException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Action;
import org.openide.nodes.BeanNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.Utilities;
import org.openide.util.WeakListeners;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import ru.ptahi.iconmanager.IconManager;

/**
 *
 * @author paulorlov
 */
public class StimuleNode extends BeanNode<Stimule> implements PropertyChangeListener, LookupListener {
    private InstanceContent sContent;
    private Lookup.Result<Node> nodesOnLookup;
    private Stimule sObj;
    private boolean current = false;
    
    StimuleNode(Stimule sObj, InstanceContent sContent) throws IntrospectionException {
        super(sObj, Children.LEAF, new AbstractLookup(sContent));
        this.sContent = sContent;
        setDisplayName("Stimule " + sObj.getId() + ". CF(" + sObj.getComplexity() + ")");

        this.sContent.add(new DeleteCookie(this));
        this.sContent.add(new SerializeCookie(this));
        this.sContent.add(sObj);
        this.sContent.add(this);
        sObj.addPropertyChangeListener(WeakListeners.propertyChange(this, sObj));
        nodesOnLookup = Utilities.actionsGlobalContext().lookupResult(Node.class);
        nodesOnLookup.addLookupListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Stimule s = getBean();
        String str = "Stimule " + s.getId() + ". CF(" + s.getComplexity() + ")";
        String oldName = getDisplayName();
        this.setDisplayName(str);
        fireDisplayNameChange(oldName, str);
    }

    @Override
    public void resultChanged(LookupEvent le) {
        if (!nodesOnLookup.allInstances().isEmpty()) {
            Node nObj = nodesOnLookup.allInstances().iterator().next();
            current = nObj.equals(this);
            fireIconChange();
        }
    }
    
    @Override
    public Action[] getActions(boolean context) {
        return new Action[]{ 
            new EditAction(getLookup()),
            new DeleteAction(getLookup())
        };
    }
    
    @Override
    public Image getIcon(int type) {
        if(current){
            return getOpenedIcon(type);
        }
        return IconManager.getActiveIcon("screen.png");
    }

    @Override
    public Image getOpenedIcon(int type) {
        return IconManager.getOpenedIcon("screen.png");
    }
}
