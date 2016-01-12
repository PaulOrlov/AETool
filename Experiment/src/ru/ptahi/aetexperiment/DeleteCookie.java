package ru.ptahi.aetexperiment;

import org.openide.loaders.DataObject;
import org.openide.util.Exceptions;

/**
 *
 * @author paulorlov
 */
public class DeleteCookie {
    ExperimentNode exN;
            
    DeleteCookie(ExperimentNode exN) {
        this.exN = exN;
    }

    public void delete() {
        DataObject dObj = (DataObject) exN.getLookup().lookup(DataObject.class);
        if(dObj != null){
            try {
                dObj.getPrimaryFile().delete();
            } catch (Throwable ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }
}
