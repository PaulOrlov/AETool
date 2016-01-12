package ru.ptahi.aet.participantviewer;

import java.io.OutputStream;
import org.openide.loaders.DataObject;
import org.openide.util.Exceptions;
import ru.ptahi.aet.participant.Participant;

/**
 *
 * @author paulorlov
 */
public class SerializeCookie {

    ParticipantNode pN;

    SerializeCookie(ParticipantNode pN) {
        this.pN = pN;
    }

    public void serialize() {

        DataObject dObj = (DataObject) pN.getLookup().lookup(DataObject.class);
        Participant p = (Participant) pN.getLookup().lookup(Participant.class);
        if(dObj != null && p != null){
            String jsonStr = p.getJSON();
//            System.out.println(jsonStr);
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
