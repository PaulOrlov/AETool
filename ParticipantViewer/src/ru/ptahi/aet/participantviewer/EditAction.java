/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.ptahi.aet.participantviewer;

import java.awt.Event;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import static javax.swing.Action.NAME;
import javax.swing.JOptionPane;
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
        TopComponent tc = WindowManager.getDefault().findTopComponent("ParticipantEditorTopComponent");
        if (tc == null){
            return;
        }
        if (!tc.isOpened()){ 
            tc.open();
        }
        tc.requestActive();
    }

} 
