/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.ptahi.cfe.stumile;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javafx.animation.FadeTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(
        dtd = "-//ru.ptahi.cfe.stumile//StimuleEditor//EN",
        autostore = false
)
@TopComponent.Description(
        preferredID = "StimuleEditorTopComponent",
        //iconBase="SET/PATH/TO/ICON/HERE", 
        persistenceType = TopComponent.PERSISTENCE_ALWAYS
)
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "ru.ptahi.cfe.stumile.StimuleEditorTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_StimuleEditorAction",
        preferredID = "StimuleEditorTopComponent"
)
@Messages({
    "CTL_StimuleEditorAction=StimuleEditor",
    "CTL_StimuleEditorTopComponent=StimuleEditor Window",
    "HINT_StimuleEditorTopComponent=This is a StimuleEditor window"
})
public final class StimuleEditorTopComponent extends TopComponent implements LookupListener {
    
    private PropertyChangeListener pcl;
    private Lookup.Result<Node> projectOnLookup;
    private Stimule sObj;
    private Node sN;
    
    private JFXPanel fxContainer;
    private TextField complexityfactor;
    private TextField correctanswer;
    private TextArea content;
    private GridPane sGridPane;

    public StimuleEditorTopComponent() {
        initComponents();
        setName(Bundle.CTL_StimuleEditorTopComponent());
        setToolTipText(Bundle.HINT_StimuleEditorTopComponent());
        setLayout(new BorderLayout());
        fxContainer = new JFXPanel();
        fxContainer.setPreferredSize(new Dimension(600, 400));
        add(fxContainer, BorderLayout.CENTER);

        Platform.setImplicitExit(false);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    createScene();
                } catch (Exception e) {
                    System.out.println("Found an exception ************************************");
                }
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
        projectOnLookup = Utilities.actionsGlobalContext().lookupResult(Node.class);
        projectOnLookup.addLookupListener(this);

        if (!projectOnLookup.allInstances().isEmpty()) {
            sN = projectOnLookup.allInstances().iterator().next();
            sObj = (Stimule) sN.getLookup().lookup(Stimule.class);
        }
        if (sObj == null) {
            System.out.println("sObj == null");
            return;
        }
        upDateScene();
        pcl = new PCL();
        sObj.addPropertyChangeListener(pcl);
    }

    @Override
    public void componentClosed() {
        projectOnLookup.removeLookupListener(this);
        if (sObj != null && pcl != null) {
            sObj.removePropertyChangeListener(pcl);
        }
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

    @Override
    public void resultChanged(LookupEvent le) {
        if (!projectOnLookup.allInstances().isEmpty()) {
            sN = projectOnLookup.allInstances().iterator().next();
            sObj = (Stimule) sN.getLookup().lookup(Stimule.class);
            if (sObj != null) {
                upDateScene();
                pcl = new PCL();
                sObj.addPropertyChangeListener(pcl);
            }
        }
    }
    
    private void createScene() {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("Editor.fxml"));
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        if (root == null) {
            return;
        }
        
        complexityfactor = (TextField) root.lookup("#complexityfactor");
        correctanswer = (TextField) root.lookup("#correctanswer");
        content = (TextArea) root.lookup("#content");
        sGridPane = (GridPane) root.lookup("#Grid");
        
        Button saveStimuleBtn = (Button) root.lookup("#saveStimuleBtn");
        saveStimuleBtn.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("complexity", complexityfactor.getText());
                map.put("correctAnswer", correctanswer.getText());
                map.put("content", content.getText());
                sObj.upDate(map);
                serialize();
                upDateScene();
            }
        });

        Scene scene = new Scene(root);
        fxContainer.setScene(scene);

        if (sObj != null) {
            upDateScene();
        }
    }

    private void upDateScene() {
        Platform.runLater(new Runnable() {
            Timeline timeline = new Timeline();

            private void animateHide() {
                FadeTransition ft = new FadeTransition(Duration.millis(200), sGridPane);
                ft.setFromValue(1.0);
                ft.setToValue(0.0);
                ft.setCycleCount(1);
                ft.setAutoReverse(false);
                ft.playFromStart();
                ft.setOnFinished(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        doUpdateLogic();
                        animateShow();
                    }
                });
            }
            
            private void doUpdateLogic(){
                if (complexityfactor != null) {
                    complexityfactor.setText(String.valueOf(sObj.getComplexity()));
                }
                if (content != null) {
                    content.setText(sObj.getContent());
                }
                if (correctanswer != null) {
                    correctanswer.setText(sObj.getCorrectAnswer());
                }
            }

            private void animateShow() {
                FadeTransition ft = new FadeTransition(Duration.millis(600), sGridPane);
                ft.setFromValue(0.0);
                ft.setToValue(1.0);
                ft.setCycleCount(1);
                ft.setAutoReverse(false);
                ft.playFromStart();
            }

            @Override
            public void run() {
                animateHide();
            }
        });
    }
    
    private void serialize() {
        SerializeCookie sc = sN.getLookup().lookup(SerializeCookie.class);
        if (sc != null) {
            sc.serialize();
        }
    }
    
    class PCL implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            upDateScene();
        }
    }
}
