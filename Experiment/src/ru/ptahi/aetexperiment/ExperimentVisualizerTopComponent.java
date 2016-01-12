package ru.ptahi.aetexperiment;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import org.netbeans.core.spi.multiview.MultiViewElementCallback;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.util.Exceptions;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.OnShowing;
import org.openide.windows.WindowManager;

/**
 * Top component which displays something.
 */
//@ConvertAsProperties(
//        dtd = "-//ru.ptahi.aetexperiment//ExperimentVisualizer//EN",
//        autostore = false
//)
@TopComponent.Description(
        preferredID = "ExperimentVisualizerTopComponent",
        //iconBase="SET/PATH/TO/ICON/HERE", 
        //        persistenceType = TopComponent.PERSISTENCE_ALWAYS
        persistenceType = TopComponent.PERSISTENCE_ONLY_OPENED
)
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "ru.ptahi.aetexperiment.ExperimentVisualizerTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
//@TopComponent.OpenActionRegistration(
//        displayName = "#CTL_ExperimentVisualizerAction",
//        preferredID = "ExperimentVisualizerTopComponent"
//)
@Messages({
    "CTL_ExperimentVisualizerAction=ExperimentVisualizer",
    "CTL_ExperimentVisualizerTopComponent=ExperimentVisualizer Window",
    "HINT_ExperimentVisualizerTopComponent=This is a ExperimentVisualizer window"
})

public final class ExperimentVisualizerTopComponent extends TopComponent {

    private JFXPanel fxContainer;
    private final int JFXPANEL_WIDTH_INT = 800;
    private final int JFXPANEL_HEIGHT_INT = 900;
    private Experiment context;
    private ExperimentNode expNode;
    private AreaChart<Number, Number> ac;

    public ExperimentVisualizerTopComponent(ExperimentNode context) {
        initComponents();
        setName(Bundle.CTL_ExperimentVisualizerTopComponent());
        setToolTipText(Bundle.HINT_ExperimentVisualizerTopComponent());
        Experiment expObj = (Experiment) context.getLookup().lookup(Experiment.class);
        if (expObj == null) {
            return;
        }
        setDisplayName("Experiment" + expObj.getId());
        this.context = expObj;
        expNode = context;
        setLayout(new BorderLayout());
         expNode.addAnnotationPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                upDateScene();
            }

        });

        expNode.addVisibilityPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
//                if (expNode.isVisible()) {
//                    remove(fxContainer);
//                    add(fxContainer, BorderLayout.CENTER);
//                } else {
//                    remove(fxContainer);
//                }
//                repaint();

            }

        });        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    @Override
    public void componentOpened() {
        JButton jb = new JButton(expNode.getDisplayName());
        jb.setSize(0, 10);
        jb.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e); 
                VisualizerPropertiesTopComponent tc = (VisualizerPropertiesTopComponent) WindowManager.getDefault().findTopComponent("VisualizerPropertiesTopComponent");
                if (tc != null) {
                    tc.setExperimentNode(expNode);
                }
            }
            
        });
        add(jb, BorderLayout.NORTH);
        fxContainer = new JFXPanel();
        fxContainer.setPreferredSize(new Dimension(JFXPANEL_WIDTH_INT, JFXPANEL_HEIGHT_INT));
        add(fxContainer, BorderLayout.CENTER);

        Platform.setImplicitExit(false);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                createScene();
            }
        });

       
        
//        fxContainer.addMouseListener(new MouseAdapter() {
//
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                super.mouseClicked(e); 
//                VisualizerPropertiesTopComponent tc = (VisualizerPropertiesTopComponent) WindowManager.getDefault().findTopComponent("VisualizerPropertiesTopComponent");
//                if (tc != null) {
//                    tc.setExperimentNode(expNode);
//                }
//            }
//        
//        });
    }

    private void createScene() {
        StackPane root = new StackPane();
        ArrayList<Annotation> fixationDurations = context.getFdList();
        if (fixationDurations.size() > 0) {
            Annotation lastFixation = fixationDurations.get(fixationDurations.size() - 1);
            final NumberAxis xAxis = new NumberAxis(0, lastFixation.getStartTime() + lastFixation.getDuration() + 10000, 5000);
            final NumberAxis yAxis = new NumberAxis();

            ac = new AreaChart<Number, Number>(xAxis, yAxis);
            ac.setTitle("Total time line (msec)");

            upDateScene();
        }

        root.getChildren().add(ac);
        ac.setLegendVisible(false);

        Scene scene = new Scene(root);
        scene.getStylesheets().add("aef/chart.css");
        fxContainer.setScene(scene);

    }

    @Override
    public void componentClosed() {

    }

    @Override
    protected void componentShowing() {
        super.componentShowing();
        VisualizerPropertiesTopComponent tc = (VisualizerPropertiesTopComponent) WindowManager.getDefault().findTopComponent("VisualizerPropertiesTopComponent");
        if (tc != null) {
            tc.setExperimentNode(expNode);
        }
    }

//    @Override
//    protected void componentActivated() {
//        super.componentActivated(); //To change body of generated methods, choose Tools | Templates.
//        VisualizerPropertiesTopComponent tc = (VisualizerPropertiesTopComponent) WindowManager.getDefault().findTopComponent("VisualizerPropertiesTopComponent");
//        if (tc != null) {
//            tc.setExperimentNode(expNode);
//        }
//    }
    @Override
    protected void componentHidden() {
        super.componentHidden(); //To change body of generated methods, choose Tools | Templates.
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }

    private void removeAllAnnotationFromScene() {
        ac.getData().remove(0, ac.getData().size());
    }

    private void addAnnotationToScene(String activeAnnotationName) {
        ArrayList<Annotation> activeList = null;
        XYChart.Series seriesAnnotation = new XYChart.Series();

        if (activeAnnotationName.equals("Fixation Duration")) {
            activeList = context.getFdList();
        } else {
            activeList = context.getEaList();
        }

        seriesAnnotation.setName("Annotation '" + activeAnnotationName + "' duration (msec)");
        for (Annotation activeAnnotation : activeList) {
            if (activeAnnotation.getName().equals(activeAnnotationName)) {
                seriesAnnotation.getData().add(new XYChart.Data(activeAnnotation.getStartTime(), 0));
                seriesAnnotation.getData().add(new XYChart.Data(activeAnnotation.getStartTime(), activeAnnotation.getDuration()));
                seriesAnnotation.getData().add(new XYChart.Data(activeAnnotation.getStartTime() + activeAnnotation.getDuration(), activeAnnotation.getDuration()));
                seriesAnnotation.getData().add(new XYChart.Data(activeAnnotation.getStartTime() + activeAnnotation.getDuration(), 0));
            }
        }

        ac.getData().addAll(seriesAnnotation);

        if (activeAnnotationName.equals("Fixation Duration")) {
            seriesAnnotation.getNode().getStyleClass().add("series-FixationDuration");
        } else {
            seriesAnnotation.getNode().getStyleClass().add("series-" + activeAnnotationName);
        }

        Tooltip tooltip = new Tooltip(activeAnnotationName);
        Tooltip.install(seriesAnnotation.getNode(), tooltip);
    }

    private void upDateScene() {
        final ArrayList<String> aActiveNameList = expNode.getActiveAnnotations();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                removeAllAnnotationFromScene();
                for (String activeAnnotationName : aActiveNameList) {
                    addAnnotationToScene(activeAnnotationName);
                }
            }
        });

    }

}
