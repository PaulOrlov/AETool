package ru.ptahi.aetexperiment;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import static javax.swing.Action.NAME;
import org.openide.util.Lookup;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 *
 * @author paulorlov
 */
public class EditAction extends AbstractAction {

    Lookup lookup;
    
    EditAction(Lookup lookup) {
        putValue (NAME, "Edit");
        this.lookup = lookup;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        TopComponent tc = WindowManager.getDefault().findTopComponent("ExperimentEditorTopComponent");
        if (tc == null){
            return;
        }
        if (!tc.isOpened()){ 
            tc.open();
        }
        tc.requestActive();
    }
} 
