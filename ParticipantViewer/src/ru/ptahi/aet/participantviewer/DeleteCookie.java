package ru.ptahi.aet.participantviewer;

import java.io.File;
import java.io.IOException;
import org.openide.loaders.DataObject;
import org.openide.util.Exceptions;

/**
 *
 * @author paulorlov
 */
class DeleteCookie {

    ParticipantNode pN;
            
    DeleteCookie(ParticipantNode pN) {
        this.pN = pN;
    }

    public void delete() {
        DataObject dObj = (DataObject) pN.getLookup().lookup(DataObject.class);
        if(dObj != null){
            
//            File file = new File(dObj.getPrimaryFile().getPath());
// 
//    		if(file.delete()){
//    			System.out.println(file.getName() + " is deleted!");
//    		}else{
//    			System.out.println("Delete operation is failed.");
//    		}
            
            try {
                dObj.getPrimaryFile().delete();
            } catch (Throwable ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }
}
