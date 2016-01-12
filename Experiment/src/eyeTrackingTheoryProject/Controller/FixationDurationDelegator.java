package eyeTrackingTheoryProject.Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import ru.ptahi.aetexperiment.RunExperimentTopComponent;

public class FixationDurationDelegator implements ActionListener{

   

   public FixationDurationDelegator(){
        
   }
   
   

   @Override
   public void actionPerformed(ActionEvent event)
   {
      if (event.getActionCommand().equals("makeCurrentFixationLong"))
      {
          RunExperimentTopComponent.makeItRed();
      }
      else if (event.getActionCommand().equals("makeCurrentFixationShort"))
      {
         RunExperimentTopComponent.makeItWhite();
      }
   }

}
