/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.ptahi.aet.participantviewer;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.explorer.view.BeanTreeView;
import org.openide.explorer.view.IconView;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;
import ru.ptahi.aet.participant.Participant;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(
        dtd = "-//ru.ptahi.aet.participantviewer//ParticipantViewer//EN",
        autostore = false)
@TopComponent.Description(
        preferredID = "ParticipantViewerTopComponent",
        //iconBase="SET/PATH/TO/ICON/HERE", 
        persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(mode = "explorer", openAtStartup = false)
@ActionID(category = "Window", id = "ru.ptahi.aet.participantviewer.ParticipantViewerTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_ParticipantViewerAction",
        preferredID = "ParticipantViewerTopComponent")
@Messages({
    "CTL_ParticipantViewerAction=ParticipantViewer",
    "CTL_ParticipantViewerTopComponent=ParticipantViewer Window",
    "HINT_ParticipantViewerTopComponent=This is a ParticipantViewer window"
})
public final class ParticipantViewerTopComponent extends TopComponent implements ExplorerManager.Provider{
    
    private ExplorerManager em = new ExplorerManager();

    public ParticipantViewerTopComponent() {
        initComponents();
        setName(Bundle.CTL_ParticipantViewerTopComponent());
        setToolTipText(Bundle.HINT_ParticipantViewerTopComponent());
        
        setLayout(new BorderLayout());
        
        BeanTreeView participantViewer = new BeanTreeView();
        IconView participantIconViewer = new IconView();
        
//      em.setRootContext(new AbstractNode(Children.create(new ParticipantChildFactory(), true)));

        add(participantViewer, BorderLayout.CENTER);
        add(participantIconViewer, BorderLayout.EAST);
        
        associateLookup(ExplorerUtils.createLookup(em, getActionMap()));

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

    }

    @Override
    public void componentClosed() {

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
    public ExplorerManager getExplorerManager() {
        return em;
    }

}