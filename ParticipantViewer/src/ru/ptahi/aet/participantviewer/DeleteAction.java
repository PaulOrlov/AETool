package ru.ptahi.aet.participantviewer;

import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
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
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.SwingUtilities;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import ru.ptahi.iconmanager.IconManager;
import ru.ptahi.aet.participant.Participant;

/**
 *
 * @author paulorlov
 */
public class DeleteAction extends AbstractAction {

    private Lookup lookup;
    private Participant p;
    private JFXPanel fxContainer;
    private String name;
    private JDialog dialog;

    DeleteAction(Lookup lookup) {
        putValue(NAME, "Delete");
        this.lookup = lookup;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        p = lookup.lookup(Participant.class);
        name = p.getFirstName() + " " + p.getLastName();
                            
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
        
//        JButton bt = new JButton("fdf");
//        bt.addActionListener(new ActionListener() {
//
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                DeleteCookie dc = lookup.lookup(DeleteCookie.class);
//                if (dc != null) {
//                    dc.delete();
//                }
//            }
//        });
//        dialog.add(bt);
        
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
    
    private void deleteParticipant() {
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

        Label participantLabel = (Label) root.lookup("#participantLabel");
        participantLabel.setText(name);

        ImageView icon = (ImageView) root.lookup("#icon");

        icon.setImage(IconManager.getFXIcon("delete-item.png", 32, 32));

        Button yButton = (Button) root.lookup("#yButton");
        yButton.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {
                deleteParticipant();
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
