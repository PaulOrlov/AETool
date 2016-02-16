package ru.ptahi.aetexperiment;

import com.google.gson.Gson;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import ru.ptahi.aet.participant.Participant;
import ru.ptahi.cfe.stumile.Stimule;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

/**
 *
 * @author paulorlov
 */
class Experiment {
    private int id;
    private String comment;
    private Participant participant;
    private String beginTime;
    private String endTime;
    private ArrayList<StimuleInExperiment> stimuliIEList= new ArrayList<>();    
    private List listeners = Collections.synchronizedList(new LinkedList());
    private Experiment.FF_Experiment ffExperimentObj = new FF_Experiment();
    private ArrayList<Annotation> fdList;
    private ArrayList<Annotation> eaList;
    private boolean isDone = false;

    void setFixationDurationList(ArrayList<Annotation> fdList) {
        ArrayList<Annotation> oldfdList = this.fdList;
        this.fdList = fdList;
        fire("fdList", oldfdList, fdList);
    }
    
    public ArrayList<Annotation> getFdList() {
        return fdList;
    }

    void setELANAnnotationList(ArrayList<Annotation> eaList) {
        ArrayList<Annotation> oldEaList = this.eaList;
        this.eaList = eaList;
        fire("eaList", oldEaList, eaList);        
    }

    public ArrayList<Annotation> getEaList() {
        return eaList;
    }

    void upDate(Map<String, Object> map) {
        Class<?> c = this.getClass();

        for (String fieldName : map.keySet()) {
            Field f = null;
            try {
                f = c.getDeclaredField(fieldName);
                f.set(this, map.get(fieldName));
            } catch (NoSuchFieldException ex) {
                Logger.getLogger(Participant.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SecurityException ex) {
                Logger.getLogger(Participant.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(Participant.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(Participant.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        for (String fieldName : map.keySet()) {
            fire(fieldName, null, map.get(fieldName));
        }
    }
    
    public static class FF_Experiment {
        public int id;
        public String comment;
        public String participant;
        public String stimuliIEList;    
        public String beginTime;
        public String endTime;
        public boolean isDone;
        
        public String _ff_id;
        public int _ff_author_id;
    }

    public boolean isIsDone() {
        return isDone;
    }

    public void setIsDone(boolean isDone) {
        this.isDone = isDone;
    }
    
    public static class StimuleInExperiment{
        public transient Stimule sObj;
        public String stimuleObjJSON;
        public String beginTime;
        public String endTime;
        public int indexInTheSet;
        public String participantAnswer;
        public int stencilSize;
        public int maskType;
        
        public static String allToJSON(ArrayList<StimuleInExperiment> aList){
            for(StimuleInExperiment current : aList){
                current.stimuleObjJSON = current.sObj.getJSON();
            }
            String jsonStr = "";
            Gson gson = new Gson();
            Type alObject = new TypeToken<ArrayList<StimuleInExperiment>>(){}.getType();
            jsonStr = gson.toJson(aList, alObject);
            return jsonStr;
        }
    }
    
    public void setIEStimuli(ArrayList<StimuleInExperiment> stimuliIEList){
        this.stimuliIEList = stimuliIEList;
    }
    
    public void setStimuli(ArrayList<Stimule> stimuleList){
        for(Stimule current : stimuleList){
            Experiment.StimuleInExperiment stIEObj = new StimuleInExperiment();
            stIEObj.sObj = current;
            stimuliIEList.add(stIEObj);
        }
        suffleStimuli();
        
        ArrayList<Experiment.StimuleInExperiment> stListComplexity1 = new ArrayList<>();
        ArrayList<Experiment.StimuleInExperiment> stListComplexity2 = new ArrayList<>();
        
        for(Experiment.StimuleInExperiment current : stimuliIEList){
            if(current.sObj.getComplexity() == 1){
                stListComplexity1.add(current);
            } else {
                stListComplexity2.add(current);
            }
        }
        
        int size = stListComplexity1.size() - 1;
        
        System.out.println(size);
        
        for(int i = 0; i < size/2; i++){
            stListComplexity1.get(i).maskType = 1;
            stListComplexity2.get(i).maskType = 1;
            stListComplexity1.get(i).stencilSize = i+1;
            stListComplexity2.get(i).stencilSize = i+1;
        }
        
        for(int i = size/2; i < size; i++){
            stListComplexity1.get(i).maskType = 2;
            stListComplexity2.get(i).maskType = 2;
            stListComplexity1.get(i).stencilSize = i - size/2 + 1;
            stListComplexity2.get(i).stencilSize = i - size/2 + 1;
        }
        
        stListComplexity1.get(size).maskType = 0;
        stListComplexity1.get(size).stencilSize = 0;
        stListComplexity2.get(size).maskType = 0;
        stListComplexity2.get(size).stencilSize = 0;
        
        suffleStimuli();
    }
    
    private void suffleStimuli(){
        Collections.shuffle(stimuliIEList);
    }
    
    public ArrayList<Experiment.StimuleInExperiment> getStimuleINE(){
        return stimuliIEList;
    }
    
    public FF_Experiment getFfExperimentObj() {
        return ffExperimentObj;
    }

    public void setFfParticipantObj(FF_Experiment ffParticipantObj) {
        this.ffExperimentObj = ffParticipantObj;
    }
    
    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        listeners.add(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        listeners.remove(pcl);
    }

    private void fire(String propertyName, Object old, Object nue) {
        //Passing 0 below on purpose, so you only synchronize for one atomic call:
        PropertyChangeListener[] pcls = (PropertyChangeListener[]) listeners.toArray(new PropertyChangeListener[0]);
        for (int i = 0; i < pcls.length; i++) {
            pcls[i].propertyChange(new PropertyChangeEvent(this, propertyName, old, nue));
        }
    }
    
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        String oldComment = this.comment;
        this.comment = comment;
        fire("comment", oldComment, comment);
    }
    
    public String getJSON(){
        String result = null;
        Gson gson = new Gson();
        ffExperimentObj.id = id;
        ffExperimentObj.comment = comment;
        ffExperimentObj.participant = participant.getJSON();
        ffExperimentObj.stimuliIEList = StimuleInExperiment.allToJSON(stimuliIEList);
        ffExperimentObj.beginTime = beginTime;
        ffExperimentObj.endTime = endTime;
        ffExperimentObj.isDone = isDone;
        result = gson.toJson(ffExperimentObj);
        return result;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        int oldId = this.id;
        this.id = id;
        fire("id", oldId, id);
    }

    public Participant getParticipant() {
        return participant;
    }

    public void setParticipant(Participant participant) {
        Participant oldParticipant = this.participant;
        this.participant = participant;
        fire("participant", oldParticipant, participant);
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
