package ru.ptahi.cfe.stumile;

import org.openide.loaders.DataObject;
import org.openide.util.Exceptions;

/**
 *
 * @author paulorlov
 */
public class DeleteCookie {
    StimuleNode sN;
            
    DeleteCookie(StimuleNode sN) {
        this.sN = sN;
    }

    public void delete() {
        DataObject dObj = (DataObject) sN.getLookup().lookup(DataObject.class);
        if(dObj != null){
            try {
                dObj.getPrimaryFile().delete();
            } catch (Throwable ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }
}
