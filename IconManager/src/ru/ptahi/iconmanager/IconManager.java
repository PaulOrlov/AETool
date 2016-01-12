/**
 * 
 * Icon Manager for NetBeans Platform
 * 
 * Copyright (c) 20013-14 Paul Orlov
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation, version 2.1.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General
 * Public License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA  02111-1307  USA
 * 
 * All default icons are made by Sarfraz Shoukat via gcons project.
 * More information and licence of use, can be found 
 * http://www.greepit.com/open-source-icons-gcons/
 * 
 * License: gcons is free for personal and commercial designs / applications 
 * but the icons may not be sold, sublicensed, rented or transferred.
 */

package ru.ptahi.iconmanager;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;
import org.openide.util.ImageUtilities;

public class IconManager {

    /*
    *     Two hash maps for caching images.
    */
    private static final Map<String, java.awt.Image> mapAWT = new HashMap();
    private static final Map<String, javafx.scene.image.Image> mapFX = new HashMap();

    private static String ACTIVEICONPATH = "icons/black/";
    private static String OPENEDICONPATH = "icons/blue/";
    private static String DEFAULTICONPATH = "icons/black/";
            
    private static IconManager iconManager;
    
   /** 
    * Class constructor. It is private for the singleton reason.
    * The singleton is necessary to have easy access to IconManager:
    * <p>
    * IconManager2.getOpenedIcon("connected.png");
    * <p>
    * The next reason is - caching images.
    */
    
    private IconManager(){}
    
   /**
    * The way to get IconManager.
    *
    * @return      the singleton instance of IconManager2
    */
    private static IconManager getIconManager(){
        if(iconManager == null){
            iconManager = new IconManager();
        }
        return iconManager;
    }
    
    /**
    *   Return cached image (java.awt.Image) from specified name
    *   and 16x16px from active path ("icons/black/" by default). 
    *
    *   @param  iconName the name of image file with type, like .png
    *   @return      the image at the specified path+iconName
    *   @see         Image
    */
    
    public static java.awt.Image getActiveIcon(String iconName){
        return getIcon(IconManager.ACTIVEICONPATH, iconName, 16, 16);
    }
    
   /**
    *   Return cached image (java.awt.Image) from specified name
    *   and width x height from active path ("icons/black/" by default). 
    *
    *   @param  iconName the name of image file with type, like .png
    *   @param  width necessary image width
    *   @param  height necessary image height
    *   @return      the image at the specified path+iconName
    *   @see         Image
    */
    
    public static java.awt.Image getActiveIcon(String iconName, int width, int height){
        return getIcon(IconManager.ACTIVEICONPATH, iconName, width, height);
    }
    
   /**
    *   Return cached image (java.awt.Image) from specified name
    *   and 16x16px from path for opened nodes ("icons/blue/" by default).
    *
    *   @param  iconName the name of image file with type, like .png
    *   @return      the image at the specified path+iconName
    *   @see         Image
    */
    
    public static java.awt.Image getOpenedIcon(String iconName){
        return getIcon(IconManager.OPENEDICONPATH, iconName, 16, 16);
    }
    
    /**
    *   Return cached image (java.awt.Image) from specified name
    *   and width x height from path for opened nodes ("icons/blue/" by default). 
    *
    *   @param  iconName the name of image file with type, like .png
    *   @param  width necessary image width
    *   @param  height necessary image height
    *   @return      the image at the specified path+iconName
    *   @see         Image
    */
    
    public static java.awt.Image getOpenedIcon(String iconName, int width, int height){
        return getIcon(IconManager.OPENEDICONPATH, iconName, width, height);
    }
    
   /**
    *   Return cached image (java.awt.Image) from specified path
    *   with file name and size (width, height). Path is relative to the module
    *   file structure, by default all images are in the icons folder
    *   and then in the separated by color folders, like: "icons/black/"
    *
    *   @param  path the relative path to the image
    *   @param  iconName the name of image file with type, like .png
    *   @param  width necessary image width
    *   @param  height necessary image height
    *   @return      the image at the specified path+iconName
    *   @see         Image
    */
    
    public static java.awt.Image getIcon(String path, String iconName, int width, int height){
        IconManager currentIMObj = getIconManager();
        String key = path + iconName + width + height;
        if(!IconManager.mapAWT.containsKey(key)){
            currentIMObj.registerAWTImage(path, iconName, width, height);
        }
        return (java.awt.Image) IconManager.mapAWT.get(key);
    }
    
    private void registerAWTImage(String path, String iconName, int width, int height) {
        java.awt.Image iObj = loadImage(path, iconName, width, height);
        String key = path + iconName + width + height;
        mapAWT.put(key, iObj);
    }
    
    private java.awt.Image loadImage(String path, String iconName, int width, int height){
        java.awt.Image iObj = ImageUtilities.loadImage(path + iconName);
        if(iObj.getWidth(null) != width || iObj.getHeight(null) != height){
            iObj = scaleImage(iObj, width, height);
        }
        return iObj;
    }
    
    private java.awt.Image scaleImage(java.awt.Image iObj, int width, int height) {
        int bType = BufferedImage.TYPE_INT_ARGB;
        BufferedImage iconImage = new BufferedImage(width, height, bType);
        Graphics2D g = iconImage.createGraphics();

        g.setComposite(AlphaComposite.Src);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.drawImage(iObj, 0, 0, width, height, null);
        g.dispose();
        return iconImage;
    }
    
    /**
    *   Return cached image (javafx.scene.image.Image) from specified file name
    *   and size 16x16 from default path for JavaFX ("icons/blue/" by default).
    *
    *   @param  iconName the name of image file with type, like .png
    *   @return      the image at the specified path+iconName
    *   @see         javafx.scene.image.Image
    */
    
    public static javafx.scene.image.Image getFXIcon(String iconName){
        return getFXIcon(IconManager.DEFAULTICONPATH, iconName, 16, 16);
    }

   /**
    *   Return cached image (javafx.scene.image.Image) from specified file name
    *   and size (width, height) from default path for JavaFX ("icons/blue/" by default).
    *
    *   @param  iconName the name of image file with type, like .png
    *   @param  width necessary image width
    *   @param  height necessary image height
    *   @return      the image at the specified path+iconName
    *   @see         javafx.scene.image.Image
    */
    
    public static javafx.scene.image.Image getFXIcon(String iconName, int width, int height){
        return getFXIcon(IconManager.DEFAULTICONPATH, iconName, width, height);
    }
    
    /**
    *   Return cached image (javafx.scene.image.Image) from specified path
    *   with file name and size (width, height) for JavaFX. Path is relative to the module
    *   file structure, by default all images are in the icons folder
    *   and then in the separated by color folders, like: "icons/black/"
    *
    *   @param  path the relative path to the image
    *   @param  iconName the name of image file with type, like .png
    *   @param  width necessary image width
    *   @param  height necessary image height
    *   @return      the image at the specified path+iconName
    *   @see         javafx.scene.image.Image
    */

    public static javafx.scene.image.Image getFXIcon(String path, String iconName, int width, int height){
        IconManager currentIMObj = getIconManager();
        String key = path + iconName + width + height;
        if(!IconManager.mapFX.containsKey(key)){
            currentIMObj.registerFXImage(path, iconName, width, height);
        }
        return (javafx.scene.image.Image) IconManager.mapFX.get(key);
    }

    private void registerFXImage(String path, String iconName, int width, int height) {
        java.awt.Image iObj = loadImage(path, iconName, width, height);
        
        final WritableImage imageFX = new WritableImage(width, height);
        SwingFXUtils.toFXImage((BufferedImage) iObj, imageFX);
        String key = path + iconName + width + height;
        mapFX.put(key, imageFX);
    }

    public static String getACTIVEICONPATH() {
        return ACTIVEICONPATH;
    }

    public static void setACTIVEICONPATH(String ACTIVEICONPATH) {
        IconManager.ACTIVEICONPATH = ACTIVEICONPATH;
    }

    public static String getOPENEDICONPATH() {
        return OPENEDICONPATH;
    }

    public static void setOPENEDICONPATH(String OPENEDICONPATH) {
        IconManager.OPENEDICONPATH = OPENEDICONPATH;
    }

    public static String getDEFAULTICONPATH() {
        return DEFAULTICONPATH;
    }

    public static void setDEFAULTICONPATH(String DEFAULTICONPATH) {
        IconManager.DEFAULTICONPATH = DEFAULTICONPATH;
    }
    
}
