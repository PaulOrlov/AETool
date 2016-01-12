/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.ptahi.cfe.stumile;

import java.util.ArrayList;

/**
 *
 * @author paulorlov
 */
public class StimuleList {
    private ArrayList<Stimule> sList = new ArrayList<>();

    public void add(Stimule pObj) {
        sList.add(pObj);
    }

    public ArrayList<Stimule> getAllStimuli() {
        return sList;
    }
    
    public boolean checkStimule(Stimule s){
        boolean flug = false;
        for(Stimule current : sList){
            if(s.getId() == current.getId()){
                return true;
            }
        }
        return flug;
    }

    public Stimule getParticipantById(int selectedIndex) {
        Stimule pObj = null;
        for(Stimule current : sList){
            if(selectedIndex == current.getId()){
                return current;
            }
        }
        return pObj;
    }
}
