/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.ptahi.aetexperiment;

/**
 *
 * @author paulorlov
 */
public class FixationDuration implements Annotation{
    public int startTime;
    public int length;

    @Override
    public String getName() {
        return "Fixation Duration";
    }

    @Override
    public int getStartTime() {
        return startTime;
    }

    @Override
    public int getDuration() {
        return length;
    }
}
