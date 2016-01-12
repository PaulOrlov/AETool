package ru.ptahi.cfe.stumile;

import com.google.gson.Gson;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author paulorlov
 */
public class Stimule {
    
    private int id;
    private String content;
    private float complexity;
    private String correctAnswer;
    

//    public int getStencilSize() {
//        return stencilSize;
//    }
//
//    public void setStencilSize(int stencilSize) {
//        int oldStencilSize = this.stencilSize;
//        this.stencilSize = stencilSize;
//        fire("stemcilSize", oldStencilSize, stencilSize);
//    }
//
//    public int getMaskType() {
//        return maskType;
//    }
//
//    public void setMaskType(int maskType) {
//        int oldMaskType = this.maskType;
//        this.maskType = maskType;
//        fire("maskType", oldMaskType, maskType);
//    }
    private final List listeners = Collections.synchronizedList(new LinkedList());
    private final Stimule.FF_Stimule ffStimuleObj = new FF_Stimule();

    public class FF_Stimule {
        public int id;
        public String content;
        public float complexity;
        public String correctAnswer;
        private int stencilSize;
        private int maskType;
    
        public String _ff_id;
        public int _ff_author_id;
    }
    
    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        listeners.add(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        listeners.remove(pcl);
    }

    private void fire(String propertyName, Object old, Object nue) {
        //Passing 0 below on purpose, so you only synchronize for one atomic call:
        PropertyChangeListener[] pcls = (PropertyChangeListener[]) listeners.toArray(new PropertyChangeListener[0]);
        for (int i = 0; i < pcls.length; i++) {
            pcls[i].propertyChange(new PropertyChangeEvent(this, propertyName, old, nue));
        }
    }
    
    public String getJSON(){
        String result = null;
        Gson gson = new Gson();
        ffStimuleObj.id = id;
        ffStimuleObj.content = content;
        ffStimuleObj.complexity = complexity;
        ffStimuleObj.correctAnswer = correctAnswer;
//        ffStimuleObj.stencilSize = stencilSize;
//        ffStimuleObj.maskType = maskType;
        result = gson.toJson(ffStimuleObj);
        return result;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        int oldId = this.id;
        this.id = id;
        fire("id", oldId, id);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        String oldContent = this.content;
        this.content = content;
        fire("content", oldContent, content);
    }

    public float getComplexity() {
        return complexity;
    }

    public void setComplexity(float complexity) {
        float oldComplexity = this.complexity;
        this.complexity = complexity;
        fire("complexity", oldComplexity, complexity);
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        String oldCorrectAnswer = this.correctAnswer;
        this.correctAnswer = correctAnswer;
        fire("correctAnswer", oldCorrectAnswer, correctAnswer);
    }
    
    public void upDate(Map<String, String> map) {
        Class<?> c = this.getClass();

        for (String fieldName : map.keySet()) {
            Field f = null;
            try {
                f = c.getDeclaredField(fieldName);
                if(fieldName == "complexity"){
                    f.set(this, Float.valueOf(map.get(fieldName)));    
                } else {
                    f.set(this, map.get(fieldName));
                }
            } catch (NoSuchFieldException ex) {
                Logger.getLogger(Stimule.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SecurityException ex) {
                Logger.getLogger(Stimule.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(Stimule.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(Stimule.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        for (String fieldName : map.keySet()) {
            fire(fieldName, null, map.get(fieldName));
        }
    }
    
}
