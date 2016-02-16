
package ru.ptahi.aetexperiment;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.SwingUtilities;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.ContextAwareAction;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.Utilities;

/**
 *
 * @author paulorlov
 */

//@ActionID(category = "Laminate", 
//
//@ActionReferences({ 
//    @ActionReference(path = "myActions/Laminate", position = 0) 
//})
        
@ActionID(category = "ShowEventsAction", id = "ru.ptahi.aetexperiment.ShowEventsAction")
@ActionRegistration(displayName = "My Action", asynchronous=true)
@ActionReferences({
    @ActionReference(path = "myAction/Experiment1", position = 0)
})
public class ShowEventsAction extends AbstractAction implements LookupListener, ContextAwareAction {

    private Lookup context;
    Lookup.Result<Experiment> lkpInfo;

    public ShowEventsAction() {
        this(Utilities.actionsGlobalContext());
    }

    public ShowEventsAction(Lookup context) {
        super("Show Audit Events");
        this.context = context;
    }

    void init() {
        assert SwingUtilities.isEventDispatchThread() : "this shall be called just from AWT thread";

        if (lkpInfo != null) {
            return;
        }

        //The thing we want to listen for the presence or absence of
        //on the global selection
        lkpInfo = context.lookupResult(Experiment.class);
        lkpInfo.addLookupListener(this);
        resultChanged(null);
    }

    @Override
    public boolean isEnabled() {
        init();
        return super.isEnabled();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        init();
        for (Experiment showAuditEventsCapability : lkpInfo.allInstances()) {
            showAuditEventsCapability.getComment();
            System.out.println(" * ");
        }
    }

    @Override
    public void resultChanged(LookupEvent ev) {
        setEnabled(!lkpInfo.allInstances().isEmpty());
//        for(Experiment capability : lkpInfo.allInstances()) {
//        if(!capability.isIsDone()) {
//            setEnabled(false);
//            return;
//        }
//    }
//    setEnabled(true);
    }

    @Override
    public Action createContextAwareInstance(Lookup actionContext) {
        return new ShowEventsAction(context);
    }
}
