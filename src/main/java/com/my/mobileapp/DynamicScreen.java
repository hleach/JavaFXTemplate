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

import java.net.URL;
import java.util.ArrayList;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

/**
 *
 * @author Heath Leach
 */
public class DynamicScreen extends AnchorPane {
    
    public static final int OR_ERROR = -1;
    public static final int OR_NULL = 0;
    public static final int OR_PORTRAIT = 1;
    public static final int OR_LANDSCAPE = 2;
    public static final int OR_SLIMPORTRAIT = 3;
    public static final int OR_SLIMLANDSCAPE = 4;
    public static final int OR_TINY = 5;
    
    // List of nodes for auto resize of fonts.
    private class NodeFontSize {
        public Node node;
        public double fontSizePortrait, fontSizeLandscape;
        public NodeFontSize(Node n, double fp, double fl) {
            this.node = n;
            this.fontSizePortrait = fp;
            this.fontSizeLandscape = fl;
        }
    }
    private ArrayList<NodeFontSize> resizeFontList = new ArrayList<>();

    private Scene scene;                    // Parent Scene for call back for screen change
    private Pane landscape;                 // Resource to display in landscape orientation
    private Pane portrait;                  // Resource to display in portrait orientation
    private Pane slimLandscape;             // Resource to display in landscape orientation when height is slim
    private Pane slimPortrait;              // Resource to display in portrait orientation when width is slim
    private Pane tiny;                      // Resource to display when both height and width are slim
    private Pane active;                    // The current screen being displayed
    private int slimX;                      // Number of pixels where width is considered slim
    private int slimY;                      // Number of pixels where height is considered slim
    private String resourceName;            // Base name of the resource loaded.

    private DynamicScreenInterface contLandscape;      // Controller instances for the screens
    private DynamicScreenInterface contPortrait; 
    private DynamicScreenInterface contSlimLandscape; 
    private DynamicScreenInterface contSlimPortrait; 
    private DynamicScreenInterface contTiny; 
    
    ChangeListener<Number> sizeListener = 
        (ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) -> {
            flipToBestFit();
            adjustDynamicFonts();
        };
    
    /**
     * 
     * Loads resources representing a single screen in various layouts
     * and handles changing between these resources.
     * 
     * Pixel height and width which are considered slim are defaulted to 100 pixels.
     * 
     * @param resource      Base name of resource file. Looks for resources built using the following patterns:
     *                      \<resource>-land.fxml - Landscape
     *                      \<resource>-port.fxml - Portrait
     *                      \<resource>-lands.fxml - Slim Landscape
     *                      \<resource>-ports.fxml - Slim Portrait
     *                      \<resource>-tiny.fxml - Tiny
     * @param controller    Controller to be used for the loaded resources.
     * @param scene         Parent scene.
     */
    DynamicScreen(String resource, DynamicScreenInterface controller, Scene parent) {
        super();
        init(resource, controller, parent, 200, 200);
    }

    /**
     * 
     * Loads resources representing a single screen in various layouts
     * and handles changing between these resources.
     * 
     * @param resource      Base name of resource file. Looks for resources built using the following patterns:
     *                      \<resource>-land.fxml - Landscape
     *                      \<resource>-port.fxml - Portrait
     *                      \<resource>-lands.fxml - Slim Landscape
     *                      \<resource>-ports.fxml - Slim Portrait
     *                      \<resource>-tiny.fxml - Tiny
     * @param controller    Controller to be used for the loaded resources.
     * @param scene         Parent scene.
     * @param x             Pixels width at which the screen is considered slim.
     * @param y             Pixels height at which the screen is considered slim.
     */    
    DynamicScreen(String resource, DynamicScreenInterface controller, Scene parent, int x, int y) {
        super();
        init(resource, controller, parent, x, y);
    }

    // Loads an fxml resource
    private Pane loadPane(String resource, DynamicScreenInterface controller) {
        URL url;
        FXMLLoader fxm;

        url = AppTemplate.class.getResource(resource);
        if (url != null) {
            fxm = new FXMLLoader(url);
            if (controller != null)
                fxm.setController(controller);
            try {
                Pane pane = (Pane) fxm.load();
                return pane;
            } catch (Exception ex) {
                Log.e(ex);
            }
        }
        controller = null;
        return null;
    }
    
    private void init(String resource, DynamicScreenInterface controller, Scene parent, int x, int y) {
        resourceName = resource;
        slimX = x;
        slimY = y;
        scene = null;
        contPortrait = controller;
        portrait = loadPane("/"+resource+"-port.fxml", contPortrait);
        if (portrait != null && contPortrait != null) {
            contPortrait.onDynamicScreenInitialize(this);
        }
        contLandscape = controller.getNewInstance(this);
        landscape = loadPane("/"+resource+"-land.fxml", contLandscape);
        if (landscape != null && contLandscape != null) {
            contLandscape.onDynamicScreenInitialize(this);
        }
        contSlimLandscape = controller.getNewInstance(this);
        slimLandscape = loadPane("/"+resource+"-lands.fxml", contSlimLandscape);
        if (slimLandscape != null && contSlimLandscape != null) {
            contSlimLandscape.onDynamicScreenInitialize(this);
        }
        contSlimPortrait = controller.getNewInstance(this);
        slimPortrait = loadPane("/"+resource+"-ports.fxml", contSlimPortrait);
        if (slimPortrait != null && contSlimPortrait != null) {
            contSlimPortrait.onDynamicScreenInitialize(this);
        }
        contTiny = controller.getNewInstance(this);
        tiny = loadPane("/"+resource+"-tiny.fxml", contTiny);
        if (tiny != null && contTiny != null) {
            contTiny.onDynamicScreenInitialize(this);
        }
        active = null;
        setScene(parent);

        if (getController() != null)
            getController().onScreenFlip(this);
    }

    /**
     * 
     * @return The current orientation of the screen
     */
    public int getOrientation() {
        if (active == null)
            return OR_NULL;
        if (active == portrait)
            return OR_PORTRAIT;
        if (active == landscape)
            return OR_LANDSCAPE;
        if (active == slimLandscape)
            return OR_SLIMLANDSCAPE;
        if (active == slimPortrait)
            return OR_SLIMPORTRAIT;
        if (active == tiny)
            return OR_TINY;
        
        return OR_ERROR;
    }
    
    /**
     * 
     * Makes a node the active node and adds it to the screen.
     * 
     * @param screen Pane to make active and show.
     */
    private void flip(Pane screen) {
        if (screen == null) {
            Log.e("Screen is null, resource = "+resourceName);
        } else {
                if (!getChildren().isEmpty())
                    getChildren().removeAll(getChildren());
                getChildren().add(screen);
                setTopAnchor(screen, 0.0);
                setBottomAnchor(screen, 0.0);
                setLeftAnchor(screen, 0.0);
                setRightAnchor(screen, 0.0);
                active = screen;
                if (getController() != null)
                    getController().onScreenFlip(this);
                adjustDynamicFonts();
            // Android needs a little time, this works so there are no glitches on switch.
            Platform.runLater( () -> {
                screen.requestFocus();
                screen.requestLayout();
            });
        }        
    }
    
    /**
     * Flip to a landscape configuration.
     */    
    private void flipToLandscape() {
        if ((scene.getHeight() < slimY) && (scene.getWidth() < slimX) && (tiny != null))
            flip(tiny);
        else if ((scene.getHeight() < slimY) && (slimLandscape != null))
            flip(slimLandscape);
        else {
            if (landscape == null)
                Log.e("Landscape orientation is null, resource = "+resourceName);
            else flip(landscape);
        }
    }

    /**
     * Flip to a portrait configuration.
     */
    private void flipToPortrait() {
        if ((scene.getHeight() < slimY) && (scene.getWidth() < slimX) && (tiny != null))
            flip(tiny);
        else if ((scene.getWidth() < slimX) && (slimPortrait != null))
            flip(slimPortrait);
        else {
            if (portrait == null)
                Log.e("Portrait orientation is null, resource = "+resourceName);
            else flip(portrait);
        }
    }

    /**
     * Flip to best fit configuration.
     */
    private void flipToBestFit() {
        if ((scene.getHeight() < scene.getWidth()))
            flipToLandscape();
        else flipToPortrait();
    }
    
    /**
     * 
     * Saves the parent scene and adds listeners to width and height property changes.
     * 
     * @param s Parent scene
     */
    public void setScene(Scene s) {
        if (scene == null) {
            scene = s;
            scene.heightProperty().addListener(sizeListener);
            scene.widthProperty().addListener(sizeListener);
            flipToBestFit();
        } else {
            Log.e("Cannot reasign scene.");
        }
    }
    
    /**
     * 
     * @return The controller for the current screen orientation.
     */
    public DynamicScreenInterface getController() {
        if (active == landscape)
            return contLandscape;
        if (active == portrait)
            return contPortrait;
        if (active == slimLandscape)
            return contSlimLandscape;
        if (active == slimPortrait)
            return contSlimPortrait;
        if (active == tiny)
            return contTiny;
        return null;
    }

    /**
     *
     * Adds a node to a list of nodes to have their font size dynamically resized.
     * 
     * @param n                     Node to adjust
     * @param fontSizePortrait      Base Portrait Font Size
     * @param fontSizeLandscape     Base Landscape/Other Font Size
     */
    public void setDynamicFont(Node n, double fontSizePortrait, double fontSizeLandscape) {
        resizeFontList.add(new NodeFontSize(n, fontSizePortrait, fontSizeLandscape));
    }
    
    /**
     * If the loaded node is an instance of DynamicNodeInterface, we let 
     * it handle it's own resizing. Otherwise we adjust based on saved settings.
     */
    private void adjustDynamicFonts() {
        Double baseSize = scene.getHeight() / 500;    
        for (NodeFontSize nfs : resizeFontList) {
            if (nfs.node instanceof DynamicNodeInterface)
                ((DynamicNodeInterface)nfs.node).onSetStyle(this);
            else if (getOrientation() > OR_PORTRAIT)
                nfs.node.setStyle("-fx-font-size:" + baseSize * nfs.fontSizeLandscape + ";");
            else {
                double size = baseSize / (1 + (AppTemplate.getScreenMap().getHeight() / AppTemplate.getScreenMap().getWidth() *.05));
                nfs.node.setStyle("-fx-font-size:" + size * nfs.fontSizePortrait + ";");
            }                
        }
    }
    
}
