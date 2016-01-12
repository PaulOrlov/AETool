/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.ptahi.aet.participant;

import com.google.gson.Gson;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 *
 * @author paulorlov
 */
public class Participant {

    private int id;
    private String firstName;
    private String lastName;
    private List listeners = Collections.synchronizedList(new LinkedList());
    private Participant.FF_Participant ffParticipantObj = new FF_Participant();
    private String gender;
    private String expertLevel;
    private String dateOfBirth;
    private String comment;
    
    public Participant() {
        
    }
    
    public String getJSON(){
        String result = null;
        Gson gson = new Gson();
        ffParticipantObj.id = id;
        ffParticipantObj.firstName = firstName;
        ffParticipantObj.lastName = lastName;
        ffParticipantObj.gender = gender;
        ffParticipantObj.expertLevel = expertLevel;
        ffParticipantObj.dateOfBirth = dateOfBirth;
        ffParticipantObj.comment = comment;
        
        result = gson.toJson(ffParticipantObj);
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        String oldFirstName = this.firstName;
        this.firstName = firstName;
        fire("firstName", oldFirstName, firstName);
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        String oldLastName = this.lastName;
        this.lastName = lastName;
        fire("lastName", oldLastName, lastName);
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        String oldGender = this.gender;
        this.gender = gender;
        fire("gender", oldGender, gender);
    }

    public String getExpertLevel() {
        return expertLevel;
    }

    public void setExpertLevel(String expertLevel) {
        String oldExpertLevel = this.expertLevel;
        this.expertLevel = expertLevel;
        fire("expertLevel", oldExpertLevel, expertLevel);
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        String oldDateOfBirth = this.dateOfBirth;
        this.dateOfBirth = dateOfBirth;
        fire("dateOfBirth", oldDateOfBirth, dateOfBirth);
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        String oldComment = this.comment;
        this.comment = comment;
        fire("comment", oldComment, comment);
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

    public void upDate(Map<String, String> map) {
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

    public FF_Participant getFfParticipantObj() {
        return ffParticipantObj;
    }

    public void setFfParticipantObj(FF_Participant ffParticipantObj) {
        this.ffParticipantObj = ffParticipantObj;
    }
    
    public class FF_Participant{
        
        public int id;
        public String firstName;
        public String lastName;
        public String gender;
        public String expertLevel;
        public String dateOfBirth;
        public String comment;
        
        public String _ff_id;
        public int _ff_author_id;
        
    }

    @Override
    public String toString() {
        return getFirstName() + " " + getLastName();
    }
    
    
}
