/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.ptahi.aet.aetproject;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "Tools",
        id = "ru.ptahi.aet.aetproject.AddParticipant")
@ActionRegistration(
        displayName = "#CTL_AddParticipant")
@ActionReference(path = "Menu/Tools", position = -250)
@Messages("CTL_AddParticipant=Add Participant")
public final class AddParticipant extends AbstractAction implements ActionListener {

    private final AETProject project;

    public AddParticipant(AETProject context) {
        project = context;
        putValue (NAME, "Add Participant");
    }

    @Override
    public void actionPerformed(ActionEvent ev) {        
        project.createParticipant();       
    }    
}
