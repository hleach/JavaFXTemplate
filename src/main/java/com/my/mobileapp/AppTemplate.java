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

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * Template Application
 * 
 * You will need to change Build Scripts->Project->build.gradle to reflect
 * the location of your android sdk.
 * 
 * Change this line:
 * 
 * androidSdk = 'c:/Users/heath/AppData/Local/Android/android-sdk'
 * 
 * @author heath
 */
public class AppTemplate extends Application {

    private static final double DESKTOP_START_WIDTH = 400; 
    private static final double DESKTOP_START_HEIGHT = 680; 
    private static final double DESKTOP_MIN_WIDTH = 140; 
    private static final double DESKTOP_MIN_HEIGHT = 260; 
      
    private static Stage stage;
    private static Scene scene;
    private static final SplashScreen splash = new SplashScreen();
    private static final ScreenMapPane screenMap = new ScreenMapPane("splash", splash);
    
    public static ScreenMapPane getScreenMap() {
        return screenMap;
    }

    public static Scene getScene() {
        return scene;
    }
    
    public static Stage getStage() {
        return stage;
    }
    
    private static Rectangle2D getVisualBounds() {
        if (PlatformService.isDesktop()) {
            return new Rectangle2D(0, 0, DESKTOP_START_WIDTH, DESKTOP_START_HEIGHT);
        } else {
            return Screen.getPrimary().getVisualBounds();
        }
    }
    
    private static void loadScreens() {
        // Set the message on the splash screen.
        splash.setMessage("Loading screens...");
        
        // Load an example DynamicScreen. We have named this "main" and that is how we will reference it via the screenMap
        screenMap.addScreen("main", new DynamicScreen("DynamicExample", new MainController(), scene, 400, 400));
        
        // Here we will load a static screen into the screenMap:
        screenMap.addResource("static", "/StaticExample.fxml", new StaticExampleController());
    }

    @Override
    public void start(Stage newStage) {
        stage = newStage;
        Rectangle2D visualBounds = getVisualBounds();
        scene = new Scene(screenMap, visualBounds.getWidth(), visualBounds.getHeight());
        stage.getIcons().add(new Image(AppTemplate.class.getResourceAsStream("/icon.png")));
        stage.setScene(scene);
        stage.show();
        
        // PlatformService provides Platform dependent functions
        // In this case, we are checking if it is running on a desktop machine.
        if (PlatformService.isDesktop()) {
            newStage.setMinHeight(DESKTOP_MIN_HEIGHT);
            newStage.setMinWidth(DESKTOP_MIN_WIDTH);
        }
        
        loadScreens();
        
        splash.setMessage("You never even see me because it loads too quick...");
              
        screenMap.flipScreen("main");
        
        screenMap.setOnKeyPressed( keyEvent -> { 
            switch (keyEvent.getCode()) {
                case ESCAPE : 
                        // Back Key in Android
                    break;
                default:
                    break;
            }
        });
       
    }

}
