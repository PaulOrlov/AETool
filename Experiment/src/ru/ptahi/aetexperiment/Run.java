package ru.ptahi.aetexperiment;

import java.awt.Desktop.Action;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import static javax.swing.Action.NAME;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 *
 * @author paulorlov
 */
public class Run extends AbstractAction {
    private final ExperimentNode context;

    public Run(ExperimentNode context) {
        this.context = context;
        putValue (NAME, "Run Experiment");
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        TopComponent tc = new RunExperimentTopComponent(context);
        tc.open();
        tc.requestActive();                  
//        javax.swing.Action action=org.openide.awt.Actions.forID("Window", "org.netbeans.core.windows.actions.MaximizeWindowAction");
//        action.actionPerformed(null);
    }
}
