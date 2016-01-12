/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.ptahi.aet.aetproject;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import static javax.swing.Action.NAME;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle;

/**
 *
 * @author paulorlov
 */

@ActionID(
        category = "Edit",
        id = "ru.ptahi.aet.aetproject.AddStimuli"
)
@ActionRegistration(
        displayName = "#CTL_AddStimuli"
)
@ActionReference(path = "Menu/Tools", position = -400)
@NbBundle.Messages("CTL_AddStimuli=Add Stimule")

public class AddStimuli extends AbstractAction implements ActionListener {

    private final AETProject context;

    public AddStimuli(AETProject context) {
        this.context = context;
        putValue (NAME, "Add Stimule");
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        context.createStimule();
    }
}