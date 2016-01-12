package ru.ptahi.aetexperiment;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

@ActionID(
        category = "Edit",
        id = "ru.ptahi.aetexperiment.Visualize"
)
@ActionRegistration(
        displayName = "#CTL_Visualize"
)
@ActionReference(path = "Menu/Tools", position = -450)
@Messages("CTL_Visualize=Visualize Experiment")
public final class Visualize extends AbstractAction implements ActionListener {

        private final ExperimentNode context;

        public Visualize(ExperimentNode context) {
            this.context = context;
            putValue (NAME, "Visualize Experiment");
        }

        @Override
        public void actionPerformed(ActionEvent ev) {
            TopComponent tc = new ExperimentVisualizerTopComponent(context);
            tc.open();
            tc.requestActive();
        }
}
