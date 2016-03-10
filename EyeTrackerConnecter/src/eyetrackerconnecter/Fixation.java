package eyetrackerconnecter;

import java.util.ArrayList;

/**
 *
 * @author paulorlov
 */
public class Fixation {

    private final long startTime;
    private long duration, endTime;
    private boolean isAlife = true;

    Fixation() {
        startTime = System.currentTimeMillis();
    }
    
    public long getStartTime(){
        return startTime;
    }
    
    public long getEndTime(){
        return endTime;
    }

    public synchronized long getDuration() {
        return duration;
    }
    
    public synchronized void updateDuration(){
        if(isAlife){
            duration = System.currentTimeMillis() - startTime;
        }
    }
    
    public synchronized void endFixation(){
        isAlife = false;
        endTime = System.currentTimeMillis();
    }
    
}
