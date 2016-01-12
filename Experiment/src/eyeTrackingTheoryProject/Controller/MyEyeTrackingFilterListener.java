package eyeTrackingTheoryProject.Controller;

import eyeTrackingTheoryProject.app.MyEyeTrackingAppUI;
import java.awt.AWTException;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import rit.eyeTrackingAPI.ApplicationUtilities.EyeTrackingFilterListener;
import rit.eyeTrackingAPI.DataConstructs.Fixation;
import rit.eyeTrackingAPI.SmoothingFilters.DurationFixationAndLeastSquaresFilter;
import rit.eyeTrackingAPI.SmoothingFilters.Filter;
import rit.eyeTrackingAPI.SmoothingFilters.FixationAndLeastSquaresFilter;

/**
 *
 * @author paul.a.orlov@gmail.com
 */
public class MyEyeTrackingFilterListener extends EyeTrackingFilterListener{
    
    private DurationFixationAndLeastSquaresFilter myFilter;
    //private MyEyeTrackingAppUI mui;

    public MyEyeTrackingFilterListener(Filter filter, ActionListener actionListener, boolean drawGazePoints, int screenIndex) {
        super(filter, actionListener, drawGazePoints, screenIndex);
        myFilter = (DurationFixationAndLeastSquaresFilter) filter;
        //mui = (MyEyeTrackingAppUI)(((MyDelegator)actionListener).getUI());
        //FixationDurationDelegator fdd = new FixationDurationDelegator(this);
       // myFilter.setActionListener(fdd);
    }

    @Override
    protected void updateCursorCoordinates() {
        
    }

    @Override
    protected void newPoint(Point newGazePoint) {
        //System.currentTimeMillis();
        
       
        //this.mActionListener.actionPerformed(new ActionEvent(this, 0, "MakeWhiteAction"));
        //mui.setCursor(newGazePoint.x, newGazePoint.y); 
        
        //System.currentTimeMillis();
    }

    void makeCurrentFixationLong() {
        //this.mActionListener.actionPerformed(new ActionEvent(this, 0, "MakeRedAction"));
    }
    
}
