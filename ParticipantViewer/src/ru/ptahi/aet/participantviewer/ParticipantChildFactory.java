package ru.ptahi.aet.participantviewer;

import com.google.gson.Gson;
import java.beans.IntrospectionException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
//public class ParticipantChildFactory extends ChildFactory<Participant>{
public class ParticipantChildFactory extends ChildFactory<DataObject>{
    FileObject textsFolder; 
    ParticipantList pList;
            
    public ParticipantChildFactory(Project project) {
        textsFolder = project.getProjectDirectory().getFileObject("Participants");
        pList = project.getLookup().lookup(ParticipantList.class);
        pList.getAllParticipants().clear();
        
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
                Pattern pattern = Pattern.compile("participant_id[0-9]+\\.json");
                Matcher matcher = pattern.matcher(nameExt);
                boolean found = false;
                while (matcher.find()) {
                    found = true;
                }
                if(found){
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
        ParticipantNode pNode = null;
        
        try {
            Participant pObj = reconstructParticipant(key); 
            pList.add(pObj);
            InstanceContent participantContent = new InstanceContent();
            participantContent.add(key);
            pNode = new ParticipantNode(pObj, participantContent);
//            System.out.println(pObj.getId() + "   "  + key.getPrimaryFile().getNameExt());
            
        } catch (IntrospectionException ex) {
            Exceptions.printStackTrace(ex);
        }
        return pNode;
    }

    private Participant reconstructParticipant(DataObject dObj) {
        Participant patricipant = new Participant();  
        if(dObj.getPrimaryFile().getSize() == 0){
            String nameExt = dObj.getPrimaryFile().getNameExt();
            Pattern pattern = Pattern.compile("[0-9]+");
            Matcher matcher = pattern.matcher(nameExt);
            String idStr = "";
            boolean found = false;
            while (matcher.find()) {
                idStr = matcher.group();
                found = true;
            }
            if(found){
                patricipant.setId(Integer.valueOf(idStr));
            }
            return patricipant;
        }
        
        Gson gson = new Gson();
        String sObj = null;
        try {
            sObj = dObj.getPrimaryFile().asText("UTF8");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Participant.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Participant.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Participant.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Participant.FF_Participant ffParticipantObj = null;
                
        try {
            ffParticipantObj = gson.fromJson(sObj, Participant.FF_Participant.class);
            if(ffParticipantObj == null){
                return patricipant;
            }
            patricipant.setFirstName(ffParticipantObj.firstName);
            patricipant.setLastName(ffParticipantObj.lastName);
            patricipant.setFfParticipantObj(ffParticipantObj);
            patricipant.setId(ffParticipantObj.id);
            patricipant.setComment(ffParticipantObj.comment);
            patricipant.setDateOfBirth(ffParticipantObj.dateOfBirth);
            patricipant.setExpertLevel(ffParticipantObj.expertLevel);
            patricipant.setGender(ffParticipantObj.gender);
        } catch (Throwable t) {
            System.err.println("GSON paarser error: " + t);
        }        
        return patricipant;
    }
    
    public void refresh(){
        
        refresh(true);
    }
}