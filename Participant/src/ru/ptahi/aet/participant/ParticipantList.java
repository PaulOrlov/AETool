package ru.ptahi.aet.participant;

import java.util.ArrayList;

/**
 *
 * @author paulorlov
 */
public class ParticipantList {

    private ArrayList<Participant> pList = new ArrayList<>();

    public void add(Participant pObj) {
        pList.add(pObj);
    }

    public ArrayList<Participant> getAllParticipants() {
        return pList;
    }
    
    public boolean checkParticipant(Participant p){
        boolean flug = false;
        for(Participant current : pList){
            if(p.getId() == current.getId()){
                return true;
            }
        }
        return flug;
    }

    public Participant getParticipantById(int selectedIndex) {
        Participant pObj = null;
        for(Participant current : pList){
            if(selectedIndex == current.getId()){
                return current;
            }
        }
        return pObj;
    }

}
