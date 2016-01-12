package ru.ptahi.aetexperiment;

import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import org.openide.loaders.DataObject;
import org.openide.util.Exceptions;

/**
 *
 * @author paulorlov
 */
public class ExportToCSFileCookie {
    ExperimentNode exN;
            
    ExportToCSFileCookie(ExperimentNode exN) {
        this.exN = exN;
    }

    //expId, partId, questId, date, beginTime, endTime, duration(msec), complexity, isAnswerCorrect
    public void serialize() {

        DataObject dObj = (DataObject) exN.getLookup().lookup(DataObject.class);
        Experiment eObj = (Experiment) exN.getLookup().lookup(Experiment.class);
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Calendar calendar = Calendar.getInstance();
        if(dObj != null && eObj != null){
            String str = "expId, partId, questId, date, beginTime, endTime, duration, complexity, isAnswerCorrect, refusal\n";
            String currentStr = "";
            ArrayList<Experiment.StimuleInExperiment> aList = eObj.getStimuleINE();
            for(Experiment.StimuleInExperiment current : aList){
                currentStr = String.valueOf(eObj.getId()) + ", " ;
                currentStr += String.valueOf(eObj.getParticipant().getId()) + ", " ;
                currentStr += String.valueOf(current.sObj.getId()) + ", " ;
                
                calendar.setTimeInMillis(System.currentTimeMillis());
                currentStr += formatter.format(calendar.getTime()) + ", " ;
                
                currentStr += String.valueOf(current.beginTime) + ", " ;
                currentStr += String.valueOf(current.endTime) + ", " ;
                
                long l1 = Long.valueOf(current.endTime);
                long l2 = Long.valueOf(current.beginTime);

                currentStr += String.valueOf(l1-l2) + ", " ;
                currentStr += String.valueOf(current.sObj.getComplexity()) + ", " ;
                if(current.participantAnswer.equals(current.sObj.getCorrectAnswer())){
                    currentStr += "true, ";
                } else {
                    currentStr += "false, ";
                }
                
                if(current.participantAnswer.equals("NAN")){
                    currentStr += "true";
                } else {
                    currentStr += "false";
                }
                
                currentStr += "\n";
                
                str += currentStr;
                currentStr = "";
            }
            
            OutputStream outpstr = null;
            try {
                outpstr = dObj.getPrimaryFile().createAndOpen("exp_result.txt");
                outpstr.write(str.getBytes("UTF8"));
            } catch (Exception ex) {
                Exceptions.printStackTrace(ex);
            } finally {
                try {
                    outpstr.close();
                } catch (Throwable ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        }
    }
}
