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

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;


/**
 *
 * Simple Splash Screen
 * 
 * @author Heath Leach
 */
public class SplashScreen extends StackPane {
   
    Label message = new Label("");
    
    public void setMessage(String s) {
        Platform.runLater(()-> {
            message.setText(s);
        });
    }
    
    public SplashScreen() {
        super();
        message.textFillProperty().set(Color.WHITESMOKE);
        setAlignment(message, Pos.BOTTOM_CENTER);
        getChildren().add(message);
        
        setStyle("-fx-background-color: #763233;"
                + "-fx-background-size: contain;"
                + "-fx-background-repeat: no-repeat;"
                + "-fx-background-position: center;"
                + "-fx-background-image: url(\"cow-48494_960_720.png\");");
    }

}
