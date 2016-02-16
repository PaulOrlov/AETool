package ru.ptahi.aetexperiment;

import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Collection;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JDialog;
import javax.swing.SwingUtilities;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.nodes.Node;
import org.openide.util.ContextAwareAction;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import ru.ptahi.iconmanager.IconManager;


/**
 *
 * @author paulorlov
 */
@ActionID(category = "Delete", id = "ru.ptahi.aetexperiment.delete")
@ActionRegistration(displayName = "Delete Experiment", asynchronous=true)
@ActionReferences({
    @ActionReference(path = "myAction/Experiment", position = 0)
})
public class DeleteAction extends AbstractAction implements LookupListener, ContextAwareAction{

    private Lookup lookup;
    private Experiment exObj;
    private JFXPanel fxContainer;
    private String name;
    private JDialog dialog;
    private Lookup.Result<DeleteCookie> lkpInfo;
    private Lookup.Result<ExperimentNode> lkpInfoNode;

    DeleteAction(Lookup lookup) {
        super("Delete");
        //putValue(NAME, "Delete");
        //putValue(Action.NAME, NbBundle.getMessage(DeleteAction.class, "Delete"));
        this.lookup = lookup;
    }
    
    public DeleteAction() {
        this(Utilities.actionsGlobalContext());
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        init();
        //exObj = lookup.lookup(Experiment.class);
        for (DeleteCookie dc : lkpInfo.allInstances()) {
            if (dc != null) {
                dc.delete();
            }
        }
//        name = "With id " + exObj.getId();
//                            
//        fxContainer = new JFXPanel();
//        Platform.setImplicitExit(false);
//        Platform.runLater(new Runnable() {
//            @Override
//            public void run() {
//                createScene();
//            }
//        });
//        
//        dialog = new JDialog();
//        dialog.add(fxContainer);
//        
//        dialog.setLocation(MouseInfo.getPointerInfo().getLocation());
//        dialog.setTitle(name);        
//        dialog.pack();
//        dialog.setVisible(true);
    }
    
    private void closeDialog() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                dialog.setVisible(false);
            }
        });
    }
    
    private void deleteStimule() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                dialog.setVisible(false);
            }
        });
        
        Thread tr = new Thread(new Runnable() {
            @Override
            public void run() {
                DeleteCookie dc = lookup.lookup(DeleteCookie.class);
                if (dc != null) {
                    dc.delete();
                }                
            }
        });
        tr.start();        
    }

    private void createScene() {
        Parent root = null;
        
        try {
            root = FXMLLoader.load(getClass().getResource("DeleteDialog.fxml"));
            AnchorPane ap = (AnchorPane) root.lookup("#AnchorPane");
            dialog.setSize(new Dimension((int)ap.getPrefWidth()+20, (int)ap.getPrefHeight()+20));             
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        if (root == null) {
            return;
        }

        Label stimuleLabel = (Label) root.lookup("#experimentLabel");
        stimuleLabel.setText(name);

        ImageView icon = (ImageView) root.lookup("#icon");

        icon.setImage(IconManager.getFXIcon("delete-item.png", 32, 32));

        Button yButton = (Button) root.lookup("#yButton");
        yButton.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {
                deleteStimule();
            }
        });

        Button nButton = (Button) root.lookup("#nButton");
        nButton.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {
                closeDialog();
            }
            
        });

        Scene scene = new Scene(root);
        fxContainer.setScene(scene);
    }

    @Override
    public void resultChanged(LookupEvent le) {
 
//        setEnabled(!lkpInfo.allInstances().isEmpty());
//        
//        int selected = lkpInfo.allInstances().size();
//
//        if (selected == 0) {
//            setEnabled(false);
//            return;
//        }
//
//        for (Node node : Utilities.actionsGlobalContext().lookupAll(Node.class)) {
//            setEnabled(true);
//        }
        setEnabled(true);
    }

    @Override
    public Action createContextAwareInstance(Lookup lkp) {
        return new DeleteAction(lkp);
    }
    
    void init() {
        assert SwingUtilities.isEventDispatchThread() : "this shall be called just from AWT thread";
 
        if (lkpInfo != null) {
            return;
        }
 
        //The thing we want to listen for the presence or absence of
        //on the global selection
        lkpInfo = lookup.lookupResult(DeleteCookie.class);
        lkpInfo.addLookupListener(this);
        
        
        lkpInfoNode = lookup.lookupResult(ExperimentNode.class);
        lkpInfoNode.addLookupListener(this);
        
        resultChanged(null);
    }
    
    public boolean isEnabled() {
        init();
        return super.isEnabled();
    }
    
    
}
