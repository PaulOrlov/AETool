package ru.ptahi.aetexperiment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
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
import ru.ptahi.aet.participant.Participant;
import ru.ptahi.aet.participant.ParticipantList;

/**
 *
 * @author paulorlov
 */
public class ExperimentChildFactory extends ChildFactory<DataObject> {

    FileObject textsFolder;
    Project project;

    public ExperimentChildFactory(Project project) {
        textsFolder = (FileObject) project.getProjectDirectory().getFileObject("Experiments");
        this.project = project;
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
//                fe.runWhenDeliveryOver(new Runnable() {
//                    @Override
//                    public void run() {
//                        refresh(true);
//                    }
//                });
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
//                fae.runWhenDeliveryOver(new Runnable() {
//                    @Override
//                    public void run() {
//                        refresh(true);
//                    }
//                });
            }
        });
    }

    @Override
    protected boolean createKeys(List list) {
        //Runs on backround thread
        if (textsFolder != null) {
            for (FileObject fObj : textsFolder.getChildren()) {
                String nameExt = fObj.getNameExt();
                Pattern pattern = Pattern.compile("Experiment_id[0-9]+");
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
        ExperimentNode eNode = null;

        try {
            Experiment eObj = reconstructExperiment(key);
            InstanceContent experimentContent = new InstanceContent();
            experimentContent.add(key);
            experimentContent.add(project);
            eNode = new ExperimentNode(eObj, experimentContent);
//            System.out.println(pObj.getId() + "   "  + key.getPrimaryFile().getNameExt());

        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }
        return eNode;
    }

    private Experiment reconstructExperiment(DataObject dObj) {
        Experiment eObj = new Experiment();
        if (!dObj.getPrimaryFile().isFolder()) {
            return eObj;
        }

        if (dObj.getPrimaryFile().getFileObject("general_data.json").getSize() == 0) {
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
                eObj.setId(Integer.valueOf(idStr));
            }
        } else {
            Gson gson = new Gson();
            String sObj = null;
            try {
                sObj = dObj.getPrimaryFile().getFileObject("general_data.json").asText("UTF8");
            } catch (Exception ex) {
                //TODO...
            }

            Experiment.FF_Experiment ffExperimentObj = null;
            try {
                ffExperimentObj = gson.fromJson(sObj, Experiment.FF_Experiment.class);
                if (ffExperimentObj == null) {
                    return eObj;
                }
                eObj.setId(ffExperimentObj.id);
                eObj.setComment(ffExperimentObj.comment);
                eObj.setBeginTime(ffExperimentObj.beginTime);
                eObj.setEndTime(ffExperimentObj.endTime);
                eObj.setIsDone(ffExperimentObj.isDone);
                
                if(ffExperimentObj.stimuliIEList != null){
                    Gson gsonSt = new Gson();
                    Type alObject = new TypeToken<ArrayList<Experiment.StimuleInExperiment>>(){}.getType();
                    ArrayList<Experiment.StimuleInExperiment> aList = gsonSt.fromJson(ffExperimentObj.stimuliIEList, alObject);
                    eObj.setIEStimuli(aList);
//                    eObj.setIEStimuli(new ArrayList<Experiment.StimuleInExperiment>(Arrays.asList(ffExperimentObj.stimuliIEList)));    
                }
                
                
                Participant pObj = reconstructParticipant(ffExperimentObj.participant);
                if (pObj != null) {
                    eObj.setParticipant(pObj);
                }
                
            } catch (Throwable t) {
                System.err.println("GSON paarser error: " + t);
            }

            
        }

        ArrayList<Annotation> fdList = getEyetrackingData(dObj.getPrimaryFile().getFileObject("eyetracking_data.fixduration"));
        ArrayList<Annotation> eaList = getElanData(dObj.getPrimaryFile().getFileObject("elan_data.eaftxt"));
        eObj.setFixationDurationList(fdList);
        eObj.setELANAnnotationList(eaList);

        return eObj;
    }

    private Participant reconstructParticipant(String participantJSONString) {
        Participant pObj = null;
        Participant.FF_Participant ffParticipantObj = null;
        Gson gson = new Gson();
        try {
            ffParticipantObj = gson.fromJson(participantJSONString, Participant.FF_Participant.class);
            System.out.println(participantJSONString);
            if (ffParticipantObj == null) {
                return pObj;
            }
                        
            if(project != null){
                ParticipantList pList = (ParticipantList) project.getLookup().lookup(ParticipantList.class);
                pObj = pList.getParticipantById(ffParticipantObj.id);
            }
            
//            pObj.setId(ffParticipantObj.id);
//            pObj.setFirstName(ffParticipantObj.firstName);
//            pObj.setLastName(ffParticipantObj.lastName);
//            pObj.setFfParticipantObj(ffParticipantObj);
//            pObj.setComment(ffParticipantObj.comment);
//            pObj.setDateOfBirth(ffParticipantObj.dateOfBirth);
//            pObj.setExpertLevel(ffParticipantObj.expertLevel);
//            pObj.setGender(ffParticipantObj.gender);
        } catch (Throwable t) {
            System.err.println("GSON paarser error: " + t);
        }
        return pObj;
    }

    private ArrayList<Annotation> getEyetrackingData(FileObject fObj) {
        ArrayList<Annotation> fdList = new ArrayList<>();
        if(fObj == null){
            return fdList;
        }
        List<String> lines = new ArrayList<>();
        try {
            lines = fObj.asLines();
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        for (int i = 7; i < lines.size(); i++) {
            String currentLine = lines.get(i);
            FixationDuration fdObj = new FixationDuration();
            String currentString = "";
            int stringIndex = 0;
            for (int j = 0; j < currentLine.length(); j++) {
                char c = currentLine.charAt(j);
                if (c != '\t') {
                    currentString += c;
                } else {
                    if (currentString != "") {
                        if (stringIndex == 5) {
                            fdObj.startTime = Integer.parseInt(currentString);
                        } else if (stringIndex == 6) {
                            fdObj.length = Integer.parseInt(currentString);
                        }
                        stringIndex++;
                        currentString = "";
                    }
                }

                if (i == currentLine.length() - 1) {
                    currentString = "";
                }
            }
            fdList.add(fdObj);
        }
        return fdList;
    }

    private ArrayList<Annotation> getElanData(FileObject fObj) {
        ArrayList<Annotation> elanList = new ArrayList<>();
        if(fObj == null){
            return elanList;
        }
        List<String> lines = new ArrayList<>();
        try {
            lines = fObj.asLines();
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        for (String currentLine : lines) {
            ELANAnnotation currentAnnotation = new ELANAnnotation();
            String currentString = "";
            int stringIndex = 0;
            for (int i = 0; i < currentLine.length(); i++) {
                char c = currentLine.charAt(i);
                if (c != '\t') {
                    currentString += c;
                } else {
                    if (currentString != "") {
                        if (stringIndex == 0) {
                            currentAnnotation.tierName = currentString;
                        }

                        if (stringIndex == 2) {
                            currentAnnotation.beginTime = (int) (Double.valueOf(currentString) * 1000);
                        }

                        if (stringIndex == 4) {
                            currentAnnotation.endTime = (int) (Double.valueOf(currentString) * 1000);
                        }

                        if (stringIndex == 6) {
                            currentAnnotation.duration = (int) (Double.valueOf(currentString) * 1000);
                        }

                        stringIndex++;
                        currentString = "";
                    }
                }

                if (i == currentLine.length() - 1) {
                    currentAnnotation.name = currentString;
                    currentString = "";
                }
            }
            elanList.add(currentAnnotation);
        }
        return elanList;
    }
}
