package ru.ptahi.aetexperiment;

import java.io.OutputStream;
import org.openide.loaders.DataObject;
import org.openide.util.Exceptions;

/**
 *
 * @author paulorlov
 */
public class SerializeCookie {
    ExperimentNode exN;

    SerializeCookie(ExperimentNode exN) {
        this.exN = exN;
    }
    
    public void serialize() {

        DataObject dObj = (DataObject) exN.getLookup().lookup(DataObject.class);
        Experiment sObj = (Experiment) exN.getLookup().lookup(Experiment.class);
        if(dObj != null && sObj != null){
            String jsonStr = sObj.getJSON();
            OutputStream outpstr = null;
            try {
                outpstr = dObj.getPrimaryFile().getFileObject("general_data.json").getOutputStream();
                outpstr.write(jsonStr.getBytes("UTF8"));
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
