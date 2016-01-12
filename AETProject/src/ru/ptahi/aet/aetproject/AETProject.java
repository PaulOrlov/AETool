package ru.ptahi.aet.aetproject;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Properties;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.netbeans.api.annotations.common.StaticResource;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectInformation;
import org.netbeans.spi.project.ProjectState;
import org.openide.filesystems.FileObject;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;
import ru.ptahi.iconmanager.IconManager;
import ru.ptahi.aet.participant.Participant;
import ru.ptahi.aet.participant.ParticipantList;
import ru.ptahi.cfe.stumile.StimuleList;

/**
 *
 * @author paulorlov
 */
public class AETProject implements Project {

    private final FileObject projectDir;
    private final ProjectState state;
    private Lookup lkp;
    private ParticipantList pList = new ParticipantList();
    private StimuleList sList = new StimuleList();

    AETProject(FileObject dir, ProjectState state) {
        this.projectDir = dir;
        this.state = state;
    }

    @Override
    public FileObject getProjectDirectory() {
        return projectDir;
    }

    @Override
    public Lookup getLookup() {
        if (lkp == null) {
            lkp = Lookups.fixed(new Object[]{
                this,
                new Info(),
                new AETProjectLogicalView(this),
                state,
                loadProperties(),
                pList,
                sList
            });
        }
        return lkp;
    }

    private Properties loadProperties() {
        FileObject fob = projectDir.getFileObject("aetproject.aet");
        Properties properties = new NotifyProperties(state);
        if (fob != null) {
            try {
                properties.load(fob.getInputStream());
            } catch (Exception e) {
                Exceptions.printStackTrace(e);
            }
        }
        return properties;
    }

    void createParticipant() {
        Properties props = (Properties) getLookup().lookup(Properties.class);
        int participantId = 0;
        if (props.getProperty("lastUserId") != null) {
            participantId = Integer.parseInt(props.getProperty("lastUserId"));
        }

        participantId++;

        try {
            getProjectDirectory().getFileObject("Participants").createData("participant_id" + participantId + ".json");
        } catch (Throwable t) {
//            Exceptions.printStackTrace(ex);
        }
        props.put("lastUserId", String.valueOf(participantId));
    }

    void createExperiment() {
        Properties props = (Properties) getLookup().lookup(Properties.class);
        int experimentId = 0;
        if (props.getProperty("lastExperimentId") != null) {
            experimentId = Integer.parseInt(props.getProperty("lastExperimentId"));
        }

        experimentId++;

        try {
            getProjectDirectory().getFileObject("Experiments").createFolder("Experiment_id" + experimentId).createData("general_data.json");
        } catch (Throwable t) {
//            Exceptions.printStackTrace(ex);
        }
        props.put("lastExperimentId", String.valueOf(experimentId));
    }
    
    void createStimule() {
        Properties props = (Properties) getLookup().lookup(Properties.class);
        int stimuleId = 0;
        if (props.getProperty("lastStimuleId") != null) {
            stimuleId = Integer.parseInt(props.getProperty("lastStimuleId"));
        }

        stimuleId++;

        try {
            getProjectDirectory().getFileObject("Stimuli").createData("stimul_id" + stimuleId + ".json");
        } catch (Throwable t) {

        }
        props.put("lastStimuleId", String.valueOf(stimuleId));
    }

    private static class NotifyProperties extends Properties {

        private final ProjectState state;

        NotifyProperties(ProjectState state) {
            this.state = state;
        }

        @Override
        public Object put(Object key, Object val) {
            Object result = super.put(key, val);
            if (((result == null) != (val == null)) || (result != null && val != null && !val.equals(result))) {
                state.markModified();
            }
            return result;
        }
    }
    
    

    private final class Info implements ProjectInformation {

        @Override
        public Icon getIcon() {
            return new ImageIcon(IconManager.getActiveIcon("lookup.png"));
        }

        @Override
        public String getName() {
            return getProjectDirectory().getName();
        }

        @Override
        public String getDisplayName() {
            return getName();
        }

        @Override
        public void addPropertyChangeListener(PropertyChangeListener pcl) {
            //do nothing, won't change
        }

        @Override
        public void removePropertyChangeListener(PropertyChangeListener pcl) {
            //do nothing, won't change
        }

        @Override
        public Project getProject() {
            return AETProject.this;
        }
    }
}
