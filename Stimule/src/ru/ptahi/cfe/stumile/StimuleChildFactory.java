package ru.ptahi.cfe.stumile;

import com.google.gson.Gson;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.netbeans.api.project.Project;
import org.openide.filesystems.FileAttributeEvent;
import org.openide.filesystems.FileChangeListener;
import org.openide.filesystems.FileEvent;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileRenameEvent;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.lookup.InstanceContent;

/**
 *
 * @author paulorlov
 */
public class StimuleChildFactory extends ChildFactory<DataObject> {

    FileObject textsFolder;
    StimuleList sList;

    public StimuleChildFactory(Project project) {
        textsFolder = (FileObject) project.getProjectDirectory().getFileObject("Stimuli");
        sList = project.getLookup().lookup(StimuleList.class);
        sList.getAllStimuli().clear();
        textsFolder.addFileChangeListener(new FileChangeListener() {

            @Override
            public void fileFolderCreated(FileEvent fe) {
                fe.runWhenDeliveryOver(new Runnable() {
                    @Override
                    public void run() {
                        refresh(true);
                    }
                });
            }

            @Override
            public void fileDataCreated(FileEvent fe) {
                fe.runWhenDeliveryOver(new Runnable() {
                    @Override
                    public void run() {
                        refresh(true);
                    }
                });
            }

            @Override
            public void fileChanged(FileEvent fe) {
            }

            @Override
            public void fileDeleted(FileEvent fe) {
                fe.runWhenDeliveryOver(new Runnable() {
                    @Override
                    public void run() {
                        refresh(true);
                    }
                });
            }

            @Override
            public void fileRenamed(FileRenameEvent fre) {
                fre.runWhenDeliveryOver(new Runnable() {
                    @Override
                    public void run() {
                        refresh(true);
                    }
                });
            }

            @Override
            public void fileAttributeChanged(FileAttributeEvent fae) {
            }
        });
    }

    @Override
    protected boolean createKeys(List list) {
        //Runs on backround thread
        if (textsFolder != null) {
            for (FileObject fObj : textsFolder.getChildren()) {
                String nameExt = fObj.getNameExt();
                Pattern pattern = Pattern.compile("stimul_id[0-9]+\\.json");
                Matcher matcher = pattern.matcher(nameExt);
                boolean found = false;
                while (matcher.find()) {
                    found = true;
                }
                if (found) {
                    try {
                        list.add(DataObject.find(fObj));
                    } catch (DataObjectNotFoundException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
            }
        }

        return true;
    }

    @Override
    protected Node createNodeForKey(DataObject key) {
        StimuleNode eNode = null;

        try {
            Stimule sObj = reconstructExperiment(key);
            sList.add(sObj);
            InstanceContent sContent = new InstanceContent();
            sContent.add(key);
            eNode = new StimuleNode(sObj, sContent);

        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }
        return eNode;
    }

    private Stimule reconstructExperiment(DataObject dObj) {
        Stimule sObj = new Stimule();
        
        if (dObj.getPrimaryFile().getSize() == 0) {
            String nameExt = dObj.getPrimaryFile().getNameExt();
            Pattern pattern = Pattern.compile("[0-9]+");
            Matcher matcher = pattern.matcher(nameExt);
            String idStr = "";
            boolean found = false;
            while (matcher.find()) {
                idStr = matcher.group();
                found = true;
            }
            if (found) {
                sObj.setId(Integer.valueOf(idStr));
            }
        }
        Gson gson = new Gson();
        String jsObj = null;
        try {
            jsObj = dObj.getPrimaryFile().asText("UTF8");
        } catch (Exception ex) {
            //TODO...
        }

        Stimule.FF_Stimule ffSObj = null;
        try {
            ffSObj = gson.fromJson(jsObj, Stimule.FF_Stimule.class);
            if (ffSObj == null) {
                return sObj;
            }
            sObj.setId(ffSObj.id);
            sObj.setContent(ffSObj.content);
            sObj.setComplexity(ffSObj.complexity);
            sObj.setCorrectAnswer(ffSObj.correctAnswer);

        } catch (Throwable t) {
            System.err.println("GSON paarser error: " + t);
        }
        return sObj;
    }

}
