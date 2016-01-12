/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
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
        category = "Edit",
        id = "ru.ptahi.aet.aetproject.AddExperiment"
)
@ActionRegistration(
        displayName = "#CTL_AddExperiment"
)
@ActionReference(path = "Menu/Tools", position = -350)
@Messages("CTL_AddExperiment=Add Experiment")
public final class AddExperiment extends AbstractAction implements ActionListener {

    private final AETProject context;

    public AddExperiment(AETProject context) {
        this.context = context;
        putValue (NAME, "Add Experiment");
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        context.createExperiment();  
    }
}
