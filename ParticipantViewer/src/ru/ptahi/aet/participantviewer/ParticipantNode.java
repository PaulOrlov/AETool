package ru.ptahi.aet.participantviewer;

import java.awt.Image;
import java.awt.image.BufferedImage;
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
import ru.ptahi.aet.participant.Participant;

/**
 *
 * @author paulorlov
 */
public class ParticipantNode extends BeanNode<Participant> implements PropertyChangeListener, LookupListener{    
    
    private InstanceContent participantContent;
//    private static BufferedImage iconImage;
    private boolean current = false;
    
    public ParticipantNode(final Participant bean, InstanceContent participantContent) throws IntrospectionException {
        super(bean, Children.LEAF, new AbstractLookup(participantContent));
        this.participantContent = participantContent;
        
        if(bean.getFirstName() == null || bean.getLastName() == null){
            setDisplayName("Empty...");
        } else {
            setDisplayName(bean.getFirstName() + " " + bean.getLastName());
        }
        this.participantContent.add(new DeleteCookie(this));
        this.participantContent.add(new SerializeCookie(this));
        this.participantContent.add(bean);
        bean.addPropertyChangeListener(WeakListeners.propertyChange(this, bean));
        nodesOnLookup = Utilities.actionsGlobalContext().lookupResult(Node.class);
        nodesOnLookup.addLookupListener(this);
    }
    
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
//        Participant p = getLookup().lookup(Participant.class);
        Participant p = getBean();
        String str = p.getFirstName() + " " + p.getLastName();
        String oldName = getDisplayName();
        this.setDisplayName(str);
        fireDisplayNameChange(oldName, str);
        fireIconChange();
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
        Participant p = getBean();
        if(current){
            return getOpenedIcon(type);
        }
        if(p.getGender() == null){
            return IconManager.getActiveIcon("male-user.png");
        } 
        if(p.getGender().equals("male")){
            return IconManager.getActiveIcon("male-user.png");
        }
        return IconManager.getActiveIcon("female-user.png");
    }

    @Override
    public Image getOpenedIcon(int type) {
        Participant p = getBean();
        if(p.getGender() == null){
            return IconManager.getOpenedIcon("male-user.png");
        }
        if(p.getGender().equals("male")){
            return IconManager.getOpenedIcon("male-user.png");
        }
        return IconManager.getOpenedIcon("female-user.png");
    }

    private Lookup.Result<Node> nodesOnLookup;
    
    @Override
    public void resultChanged(LookupEvent le) {
        if (!nodesOnLookup.allInstances().isEmpty()) {
            Node pN = nodesOnLookup.allInstances().iterator().next();
            if(pN.equals(this)){
                current = true;
            } else {
                current = false;
            }
            fireIconChange();
        }
    }
    
    
}