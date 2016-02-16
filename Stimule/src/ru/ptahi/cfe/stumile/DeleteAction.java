package ru.ptahi.cfe.stumile;

import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.event.ActionEvent;
import java.io.IOException;
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
import javax.swing.JDialog;
import javax.swing.SwingUtilities;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import ru.ptahi.iconmanager.IconManager;

/**
 *
 * @author paulorlov
 */
public class DeleteAction extends AbstractAction {

    private Lookup lookup;
    private Stimule sObj;
    private JFXPanel fxContainer;
    private String name;
    private JDialog dialog;

    DeleteAction(Lookup lookup) {
        putValue(NAME, "Delete");
        this.lookup = lookup;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        sObj = lookup.lookup(Stimule.class);
        name = "With id " + sObj.getId() + ", and complexity " + sObj.getComplexity();
                            
        fxContainer = new JFXPanel();
        Platform.setImplicitExit(false);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                createScene();
            }
        });
        
        dialog = new JDialog();
        dialog.add(fxContainer);
        dialog.setLocation(MouseInfo.getPointerInfo().getLocation());
        dialog.setTitle(name);        
        dialog.pack();
        dialog.setVisible(true);
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

        Label stimuleLabel = (Label) root.lookup("#stimuliLabel");
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
}
