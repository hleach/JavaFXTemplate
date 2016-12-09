//
// The MIT License
//
// Copyright (c) 2016 Heath Leach
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
// 
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.

package com.my.mobileapp;

import java.util.HashMap;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

/**
 * Pre-loads screens into a map and controls transitions between screens. 
 * @author heath
 */
public class ScreenMapPane extends StackPane {
    
    private final HashMap<String, Node> screenMap = new HashMap<>();;
    private String currentScreenKey = "";
    private boolean lockPane = false;
    private Region popOutScreen = null;
    private long popOutMSecs = 0;
    private String popOutLockScreen = "";
    private boolean popOutRetracted = true;
    
    /**
     * @param key       Map key for the screen to be loaded.
     * @param resource  FXML resource to be inserted into the screen map and loaded.
     */
    public ScreenMapPane(String key, String resource){
        super();
        addResource(key, resource);
        flipScreen(key);
    }
    
    /**
     * @param key       Map key for the screen to be loaded.
     * @param screen    Node to be inserted into the screen map and loaded.
     */
    public ScreenMapPane(String key, Pane screen){
        super();
        lockPane = false;
        addScreen(key, screen);
        flipScreen(key);
    }
    

    private void setLock(){
        lockPane = true;
    }
    
    private void clearLock(){
        lockPane = false;
    }
    
    private boolean isLocked(){
        return lockPane;
    }
    
    /**
     * Adds a screen to the screen map.
     * 
     * @param key       Key name for screen.
     * @param screen    Node representing the screen.
     */
    public void addScreen(String key, Pane screen) {
        screenMap.put(key, screen);
    }
    
    /**
     *
     * Remove a screen from the screen map.
     * 
     * @param key       Key of the screen to remove.
     * @return          The node representing the screen removed or null.
     */
    public Node dropScreen(String key) {
        return screenMap.remove(key);
    }
    
    /**
     *
     * Return the screen node represented by the passed key.
     * 
     * @param key       Key of the screen to return.
     * @return          Node representing the screen referenced by name or null.
     */
    public Node getScreen(String key) {
        return screenMap.get(key);
    }
    
    /**
     *
     * Return the currently displayed screen.
     * 
     * @return
     */
    public Node getCurrentScreen() {
        return screenMap.get(this.currentScreenKey);
    }
    
    /**
     *
     * Return the key of the currently displayed screen.
     * 
     * @return
     */
    public String getCurrentScreenKey() {
        return this.currentScreenKey;
    }
    
    /**
     *
     * Add an FXML resource as a screen.
     * 
     * @param key       Key name for screen.
     * @param resource  FXML resource to load for screen.
     * @return          True if successful, otherwise false;
     */
    public boolean addResource(String key, String resource) {
        return addResource(key, resource, null);
    }

    /**
     *
     * Add an FXML resource as a screen and set controller.
     * 
     * @param key           Key name for screen.
     * @param resource      FXML resource to load for screen.
     * @param controller    Set controller of resource.
     * @return              True if screen is added successfully
     */
    public boolean addResource(String key, String resource, Initializable controller) {
        try {
            FXMLLoader loader = new FXMLLoader(AppTemplate.class.getResource(resource));
            if (controller != null)
                loader.setController(controller);
            Pane screenParent = (Pane) loader.load();
            addScreen(key, screenParent);
            return true;
        } catch (Exception ex) {
            Log.e(ex.getMessage());
            return false;
        }
    }
    
    /**
     *
     * Change the current screen to the screen map entry represented by key, unloading
     * the exiting screen if there is one.
     * 
     * @param key       Key of the screen to switch to.
     * @return          True if the screen is loaded in the map, false if not.
     */
    public boolean flipScreen(String key) {
        Node screen = getScreen(key);
        
        if (isLocked()) {
            return false;
        }
         
        if (screen == null) {
            Log.e("Screen == null, key = "+key);
            return false;
        } else {
            if (getChildren().isEmpty()) {
                getChildren().add(screen);
            } else {
                getChildren().remove(0);
                getChildren().add(0, screen);
            }
            currentScreenKey = key;
            requestFocus();
            return true;
        }
    }
    
    /**
     *
     * @param key       Map key of the screen to fade to.
     * @param msecs     Milliseconds it will take for fade to complete.
     * @return          True if the screen is loaded, false otherwise.
     */
    public boolean fadeScreen(String key, long msecs) {
        final Node screen = getScreen(key);
        final DoubleProperty opacity = opacityProperty();
        
        if (isLocked()) {
            return false;
        }
        
        if (screen == null) {
            Log.e("Screen == null, key = "+key);
            return false;
        } else {
            if (getChildren().isEmpty()) {
                setOpacity(0.0);
                getChildren().add(screen);
                Timeline fadeIn = new Timeline(
                        new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
                        new KeyFrame(new Duration(msecs), new KeyValue(opacity, 1.0)));
                SequentialTransition fade = new SequentialTransition();
                fade.getChildren().add(fadeIn);
                fade.play();
            } else {
                setLock();
                Timeline fadeOut = new Timeline(
                        new KeyFrame(Duration.ZERO, new KeyValue(opacity, 1.0)),
                        new KeyFrame(Duration.millis(msecs), new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent t) {
                                getChildren().remove(0);
                                getChildren().add(0, screen);
                                clearLock();
                            }
                        }, new KeyValue(opacity, 0.0)));
                Timeline fadeIn = new Timeline(
                        new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
                        new KeyFrame(Duration.millis(msecs), new KeyValue(opacity, 1.0))
                );
                
                SequentialTransition fade = new SequentialTransition();

                fade.getChildren().addAll(fadeOut, fadeIn);
                fade.play();
            }
            currentScreenKey = key;
            return true;
        }
    }
    
    private boolean slideScreen(String key, long msecs, 
            double fromX1, double fromY1, double toX1, double toY1,
            double fromX2, double fromY2, double toX2, double toY2) {
        Node screen = getScreen(key);

        if (isLocked()) {
            return false;
        }
        
        if (getChildren().contains(screen)) {
            return false;
        }
        
        if (screen == null) {
            Log.e("Screen == null, key = "+key);
            return false;
        } else {
            if (getChildren().isEmpty()) {
                setOpacity(0.0);
                getChildren().add(screen);
            } else {
                setLock();
                getChildren().add(screen);
                
                TranslateTransition slideNew = new TranslateTransition(Duration.millis(msecs), screen);
                slideNew.setFromX(fromX1);
                slideNew.setToX(toX1);
                slideNew.setFromY(fromY1);
                slideNew.setToY(toY1);
 
                Node oldScreen = getChildren().get(0);
                TranslateTransition slideOld = new TranslateTransition(Duration.millis(msecs), oldScreen);
                slideOld.setFromX(fromX2);
                slideOld.setToX(toX2);
                slideOld.setFromY(fromY2);
                slideOld.setToY(toY2);
                
                Timeline cleanup = new Timeline(
                    new KeyFrame(Duration.millis(msecs), new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent t) {
                                Node node = getChildren().remove(0);
                                node.setTranslateX(0.0);
                                node.setTranslateY(0.0);
                                clearLock();
                            }
                        }));
                
                ParallelTransition slide = new ParallelTransition(slideNew, slideOld, cleanup);
                slide.play();
            }
            currentScreenKey = key;
            return true;
        }
    }

    public boolean fadeScreen(String key) {
        return fadeScreen(key, 200);
    }

    public boolean slideScreenRight(String key) {
        double width = this.getWidth();
        
        return slideScreen(key, 500, 0.0-width, 0.0, 0.0, 0.0, 0.0, 0.0, width, 0.0);
    }

    public boolean slideScreenLeft(String key) {
        double width = this.getWidth();
        
        return slideScreen(key, 500, width, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0-width, 0.0);
    }
    
    public boolean slideScreenDown(String key) {
        double height = this.getHeight();
        
        return slideScreen(key, 500, 0.0, 0.0-height, 0.0, 0.0, 0.0, 0.0, 0.0, height);
    }

    public boolean slideScreenUp(String key) {
        double height = this.getHeight();
        
        return slideScreen(key, 500, 0.0, height, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0-height);
    }
    
    public boolean slideScreenDownRight(String key) {
        double height = this.getHeight();
        double width = this.getWidth();
        
        return slideScreen(key, 3000, 0.0-width, 0.0-height, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
    }
    
    public boolean defaultTransition(String key) {
        return flipScreen(key);
    }
    
    public boolean retractPopOut() {
        if (popOutScreen == null || popOutRetracted)
            return false;
        
        popOutRetracted = true;
        
        TranslateTransition slide = new TranslateTransition(Duration.millis(popOutMSecs), popOutScreen);
        slide.setFromX(0.0);
        slide.setToX(0.0-popOutScreen.getWidth());
        slide.setFromY(0.0);
        slide.setToY(0.0);               

        Timeline cleanup = new Timeline(
            new KeyFrame(Duration.millis(1.0), new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent t) {
                        getChildren().remove(popOutScreen);
                        getScreen(popOutLockScreen).setDisable(false);
                        ((Region) popOutScreen).setMaxWidth(Double.MAX_VALUE);
                    }
                }));
        
        SequentialTransition retract = new SequentialTransition();

        retract.getChildren().addAll(slide, cleanup);
        retract.play();

        return true;
    }
    
    public boolean popOutScreen(String key, long msecs, double percentOfScreen) {
        Node screen = getScreen(key);

        if ((percentOfScreen <= 100.0) && (screen instanceof Region)) {
            popOutMSecs = msecs;
            
            if (isLocked() || getChildren().contains(screen)) {
                Log.e("Cannot place screen on screenmap, key = "+key);
                return false;
            }

            if (screen == null) {
                Log.e("Screen == null, key = "+key);
                return false;
            } else {
                popOutScreen = (Region) screen;
                popOutLockScreen = currentScreenKey;
                getCurrentScreen().setDisable(true);
                popOutScreen.setMaxWidth(getWidth() * percentOfScreen / 100);
                setAlignment(screen, Pos.CENTER_LEFT);
                getChildren().add(screen);

                TranslateTransition slide = new TranslateTransition(Duration.millis(msecs), screen);
                slide.setFromX(0.0-popOutScreen.getMaxWidth());
                slide.setToX(0.0);
                slide.setFromY(0.0);
                slide.setToY(0.0);               

                slide.play();
                popOutRetracted = false;
                return true;
            }
        } else {
            Log.e("Screen must be a region and percentOfScreen must be <= 100, key = "+key);
            return false;
        }
    }
    
}
