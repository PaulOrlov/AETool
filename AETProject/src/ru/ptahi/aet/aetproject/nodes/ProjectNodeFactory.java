package ru.ptahi.aet.aetproject.nodes;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.ChangeListener;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.ui.support.NodeFactory;
import org.netbeans.spi.project.ui.support.NodeList;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import ru.ptahi.aet.aetproject.AETProject;
import ru.ptahi.iconmanager.IconManager;
import ru.ptahi.aet.participantviewer.ParticipantChildFactory;
import javax.swing.Action;
import javax.swing.SwingUtilities;
import org.openide.explorer.view.BeanTreeView;
import org.openide.util.Utilities;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import ru.ptahi.aet.aetproject.AddExperiment;
import ru.ptahi.aet.aetproject.AddParticipant;
import ru.ptahi.aet.aetproject.AddStimuli;
import ru.ptahi.aetexperiment.ExperimentChildFactory;
import ru.ptahi.cfe.stumile.StimuleChildFactory;


/**
 *
 * @author paulorlov
 */
@NodeFactory.Registration(projectType = "ru-ptahi-aet-aetproject", position = 10)
public class ProjectNodeFactory implements NodeFactory {

    @Override
    public NodeList<?> createNodes(Project project) {
        AETProject p = project.getLookup().lookup(AETProject.class);
        assert p != null;
        return new ProjectNodeList(p);
    }

    private class ProjectNodeList implements NodeList<Node> {

        private final AETProject project;

        public ProjectNodeList(AETProject project) {
            this.project = project;
        }

        @Override
        public List<Node> keys() {
            List<Node> result = new ArrayList<>();
            ParticipantProjectNode nParticipants = new ParticipantProjectNode(Children.create(new ParticipantChildFactory(project), true));
            nParticipants.setProject(project);
            ExperimentProjectNode nExperiments = new ExperimentProjectNode(Children.create(new ExperimentChildFactory(project), true));
            nExperiments.setProject(project);
            StimuleProjectNode nStimule = new StimuleProjectNode(Children.create(new StimuleChildFactory(project), true));
            nStimule.setProject(project);
            result.add(nParticipants);
            result.add(nExperiments);
            result.add(nStimule);
            
            //This is no good idea! but it needs to recode ParticipantList and StimuleList
            Children ch = nParticipants.getChildren();
            ch.getNodes();
            Children ch1 = nStimule.getChildren();
            ch1.getNodes();
            
            return result;
        }

        @Override
        public Node node(Node node) {
            return new FilterNode(node);
        }

        @Override
        public void addNotify() {
        }

        @Override
        public void removeNotify() {
        }

        @Override
        public void addChangeListener(ChangeListener cl) {
        }

        @Override
        public void removeChangeListener(ChangeListener cl) {
        }
    }

    class ParticipantProjectNode extends AbstractNode {

        private Project project;
        private final String iconName = "multi-agents.png";

        ParticipantProjectNode(Children children) {
            super(children);
            setDisplayName("Participants");
            
        }

        @Override
        public Image getIcon(int type) {
            return IconManager.getActiveIcon(iconName);
        }

        @Override
        public Image getOpenedIcon(int type) {
            return IconManager.getOpenedIcon(iconName);
        }

        @Override
        public Action[] getActions(boolean arg0) {
            return new Action[]{
                new AddParticipant((AETProject) project)
            };
        }

        private void setProject(Project project) {
            this.project = project;
        }
    }
    
    class ExperimentProjectNode extends AbstractNode {

        private Project project;
        private final String iconName = "connections.png";

        ExperimentProjectNode(Children children) {
            super(children);
            setDisplayName("Experiments");
        }

        @Override
        public Image getIcon(int type) {
            return IconManager.getActiveIcon(iconName);
        }

        @Override
        public Image getOpenedIcon(int type) {
            return IconManager.getOpenedIcon(iconName);
        }

        @Override
        public Action[] getActions(boolean arg0) {
            return new Action[]{
                new AddExperiment((AETProject) project)
            };
        }

        private void setProject(Project project) {
            this.project = project;
        }
        
        
    }
    
    class StimuleProjectNode extends AbstractNode {

        private Project project;
        private final String iconName = "star.png";

        StimuleProjectNode(Children children) {
            super(children);
            setDisplayName("Stimuli");
        }

        @Override
        public Image getIcon(int type) {
            return IconManager.getActiveIcon(iconName);
        }

        @Override
        public Image getOpenedIcon(int type) {
            return IconManager.getOpenedIcon(iconName);
        }

        @Override
        public Action[] getActions(boolean arg0) {
            return new Action[]{
                new AddStimuli((AETProject) project)
            };
        }

        private void setProject(Project project) {
            this.project = project;
        }
    }
}
