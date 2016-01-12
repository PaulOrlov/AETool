/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eyeTrackingTheoryProject.app;

import javax.swing.JFrame;
import processing.core.PApplet;

/**
 *
 * @author 316-t
 */
public class MyPApplet extends PApplet{
    public static void main(String[] args){
        PApplet.main(new String[]{"eyeTrackingTheoryProject.app.MyPApplet"});
    }
    
    public void setup(){
        size(300,300);
        background(50);
        fill(200,50);
        noStroke();
    }
    
    public void draw(){
        background(50);
        ellipse(random(20, width-20), random(20, height-20), 20, 20);
    }
    
}
