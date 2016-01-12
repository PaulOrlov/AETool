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
public class ELANAnnotation implements Annotation{
    public String tierName;
    public int beginTime;
    public int endTime;
    public int duration;
    public String name;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getStartTime() {
        return beginTime;
    }

    @Override
    public int getDuration() {
        return duration;
    }
}
