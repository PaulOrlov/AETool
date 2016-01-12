package eyeTrackingTheoryProject.Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import eyeTrackingTheoryProject.myEyeTrackingFunctions.*;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import ru.ptahi.aetexperiment.RunExperimentTopComponent;

public class MyDelegator implements ActionListener
{

   private JFrame ui;

   public MyDelegator()
   {
      //this.ui = ui;
   }
   
   public JFrame getUI(){
       return ui;
   }

   @Override
   public void actionPerformed(ActionEvent event)
   {
       
      if (event.getActionCommand().equals("MakeWhiteAction"))
      {
         RunExperimentTopComponent.makeItWhite();
      }
      else if (event.getActionCommand().equals("MakeRedAction"))
      {
         RunExperimentTopComponent.makeItRed();
      }
   }

}
