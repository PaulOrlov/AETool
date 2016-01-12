package ru.ptahi.cfe.stumile;

import java.io.OutputStream;
import org.openide.loaders.DataObject;
import org.openide.util.Exceptions;

/**
 *
 * @author paulorlov
 */
public class SerializeCookie {
    StimuleNode sN;

    SerializeCookie(StimuleNode sN) {
        this.sN = sN;
    }
    
    public void serialize() {

        DataObject dObj = (DataObject) sN.getLookup().lookup(DataObject.class);
        Stimule sObj = (Stimule) sN.getLookup().lookup(Stimule.class);
        if(dObj != null && sObj != null){
            String jsonStr = sObj.getJSON();
            OutputStream outpstr = null;
            try {
                outpstr = dObj.getPrimaryFile().getOutputStream();
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
