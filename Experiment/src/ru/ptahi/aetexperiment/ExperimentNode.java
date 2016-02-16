package ru.ptahi.aetexperiment;

import java.awt.Image;
import java.beans.IntrospectionException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.swing.Action;
import org.netbeans.api.project.Project;
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
class ExperimentNode extends BeanNode<Experiment> implements PropertyChangeListener, LookupListener{
    
    private InstanceContent experimentContent;
    private Lookup.Result<Node> nodesOnLookup;
    private boolean current = false;
    private Experiment eObj;
    private ArrayList<String> activeAnnotationsNames = new ArrayList<String>();
    boolean visibility = true;
    boolean shouldBeBold = false;
    
    private List listeners = Collections.synchronizedList(new LinkedList());
    private List vListeners = Collections.synchronizedList(new LinkedList());

    ExperimentNode(Experiment eObj, InstanceContent experimentContent) throws IntrospectionException {
        super(eObj, Children.LEAF, new AbstractLookup(experimentContent));
        this.experimentContent = experimentContent;
        
        if( eObj.getParticipant() == null){
            setDisplayName("Empty...");
            this.setPreferred(true);
            shouldBeBold = true;
        } else {
            setDisplayName("Experiment " + eObj.getId());
        }
        
        if(!eObj.isIsDone()){
            shouldBeBold = true;
        }
        
        this.experimentContent.add(new DeleteCookie(this));
        this.experimentContent.add(new SerializeCookie(this));
        this.experimentContent.add(new ExportToCSFileCookie(this));
        
        this.experimentContent.add(eObj);
//        this.experimentContent.add(this);
        eObj.addPropertyChangeListener(WeakListeners.propertyChange(this, eObj));
        nodesOnLookup = Utilities.actionsGlobalContext().lookupResult(Node.class);
        nodesOnLookup.addLookupListener(this);
        this.eObj = eObj;
    }

    @Override
    public String getHtmlDisplayName() {
        String result = getDisplayName();
        if(shouldBeBold){
            result = "<b>" + getDisplayName() + "</b>";
        }
        return result;
    }
    

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Experiment eObj = getBean();
        String oldName = getDisplayName();
        String str = "Experiment " + eObj.getId();
        shouldBeBold = !eObj.isIsDone();
        this.setDisplayName(str);
        fireDisplayNameChange(oldName, str);
        fireIconChange();
    }
    
    @Override
    public Action[] getActions(boolean context) {
        List<Action> nodeActions = new ArrayList<Action>();
        nodeActions.addAll(Utilities.actionsForPath("myAction/Experiment"));
        nodeActions.add(new Run(this));
        nodeActions.add(new EditAction(getLookup()));
        return nodeActions.toArray(new Action[nodeActions.size()]);
        
//        return new Action[]{ 
//            new Run(this),
//            new Visualize(this),
//            new EditAction(getLookup()),
//            new DeleteAction(getLookup()),
//            new ShowEventsAction()
//        };
    }
    
    @Override
    public Image getIcon(int type) {
        if(current){
            return getOpenedIcon(type);
        }
        return IconManager.getActiveIcon("connected.png");
    }

    @Override
    public Image getOpenedIcon(int type) {
        return IconManager.getOpenedIcon("connected.png");
    }

    @Override
    public void resultChanged(LookupEvent le) {
//        Lookup.Result<Node> projectOjb = Utilities.actionsGlobalContext().lookupResult(Project.class);
        
        //Lookup.Result<Node>  = Utilities.actionsGlobalContext().lookupResult(Node.class);
        nodesOnLookup = Utilities.actionsGlobalContext().lookupResult(Node.class);
        if (!nodesOnLookup.allInstances().isEmpty()) {
            Node nObj = nodesOnLookup.allInstances().iterator().next();
            
            if(nObj.equals(this)){
                current = true;
            } else {
                current = false;
            }
            fireIconChange();
        }
    }

    public ArrayList<String> getActiveAnnotations() {
        return activeAnnotationsNames;
    }

    public void setActiveEAnnotations(ArrayList<String> activeAnnotationsNames) {
        this.activeAnnotationsNames = activeAnnotationsNames;
        fireAnnotation("activeAnnotationsNames", null, activeAnnotationsNames);
    }

    void addActiveAnnotation(String annotationName) {
        if(!activeAnnotationsNames.contains(annotationName)){
            activeAnnotationsNames.add(annotationName);    
        }
        fireAnnotation("activeAnnotationsNames", null, activeAnnotationsNames);
    }

    void removeActiveAnnotation(String annotationName) {
        activeAnnotationsNames.remove(annotationName);
        fireAnnotation("activeAnnotationsNames", null, activeAnnotationsNames);
    }

    void addAnnotationPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        listeners.add(propertyChangeListener);
    }
    
    public void removeAnnotationPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        listeners.remove(propertyChangeListener);
    }
    
    private void fireAnnotation(String propertyName, Object old, Object nue) {
        //Passing 0 below on purpose, so you only synchronize for one atomic call:
        PropertyChangeListener[] pcls = (PropertyChangeListener[]) listeners.toArray(new PropertyChangeListener[0]);
        for (int i = 0; i < pcls.length; i++) {
            pcls[i].propertyChange(new PropertyChangeEvent(this, propertyName, old, nue));
        }
    }
    
    void addVisibilityPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        listeners.add(propertyChangeListener);
    }
    
    public void removeVisibilityPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        listeners.remove(propertyChangeListener);
    }
    
    private void fireVisibility(String propertyName, Object old, Object nue) {
        //Passing 0 below on purpose, so you only synchronize for one atomic call:
        PropertyChangeListener[] pcls = (PropertyChangeListener[]) vListeners.toArray(new PropertyChangeListener[0]);
        for (int i = 0; i < pcls.length; i++) {
            pcls[i].propertyChange(new PropertyChangeEvent(this, propertyName, old, nue));
        }
    }
    
    void changeVisibility() {
        if (visibility){
            visibility = false;
        } else {
            visibility = true;
        }
        fireAnnotation("visibility", null, visibility);
    }
    public boolean isVisible(){
        return visibility;
    }


}
