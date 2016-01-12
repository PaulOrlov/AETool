package eyeTrackingTheoryProject.app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import processing.core.PApplet;
import rit.eyeTrackingAPI.EyeTrackerUtilities.calibration.Calibrator;

//class MyPA extends PApplet{
//    
//    public float mColor;
//    int mx;
//    int my;
//    
//    
//    
//    @Override
//    public void setup(){
//        size(900,900);
//        background(50);
//        fill(200,50);
//        noStroke();
//        //noCursor();
//        ellipseMode(CENTER);
//        mColor = 200;
//    }
//    
//    @Override
//    public void draw(){
//        //background(50);
//        fill(0,5);
//        rect(0,0,width,height);
//        fill(250,20);
//        ellipse(mx, my, 10, 10);
//        fill(200,mColor,mColor, 50);
//        ellipse(width/2, height/2, 50, 50);
//    }
//    
//}
        
public class MyEyeTrackingAppUI extends JFrame
{
    //MyPA mpa;
    
   public MyEyeTrackingAppUI()
   {
      //construct ui here
       //mpa = new MyPA();
       //mpa.init();
       
       setLayout(new BorderLayout());
       final JPanel jP = new JPanel(true);
       jP.setPreferredSize(new Dimension(900, 900));
       
       JButton jB = new JButton("Start Tracking");
       jB.addMouseListener(new MouseAdapter() {

           @Override
           public void mouseClicked(MouseEvent e) {
               super.mouseClicked(e); 
               //MyApp.myL.setListening(true);
           }
           
       });
//       jP.add(mpa, BorderLayout.CENTER);
       add(jB, BorderLayout.NORTH);
       add(jP, BorderLayout.CENTER);
       pack();
       setVisible(true);
   }

//    public void setWhiteColor() {
//        mpa.mColor = 200;
//    }
//
//    public void setRedColor() {
//        mpa.mColor = 50;
//    }
//    
//    public void setCursor(int x,  int y){
//        mpa.mx = x;
//        mpa.my = y;
//    }

}
