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
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

/**
 * Example FXML Controller class for a DynamicScreen
 * 
 * Keep in mind that there is an instance of this class for each of
 * the fxml files loaded by a DynamicScreen. Some considerations need
 * to be taken to synchronize things that change across all the instances.
 *
 * @author Heath Leach
 */
public class MainController implements Initializable, DynamicScreenInterface {

    @FXML Label testLabel;
    @FXML Button testButton;
    @FXML TextField testTextField;    
    
    // I used this to synchronize textfields across all the instances.
    private static StringProperty testTextFieldProperty = new SimpleStringProperty();
    
    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // We need to bind the textField to a static property to keep the text fields
        // synchronized across the DynamicScreen. This will happen for each instance 
        // that is created.
        Bindings.bindBidirectional(testTextField.textProperty(), testTextFieldProperty);
 
        testTextFieldProperty.set("This TextField spans multiple columns.");
    }

    @FXML
    public void onClickFlipToStaticExample(MouseEvent event) {
        // We associate this function with the button in the fxml.
        AppTemplate.getScreenMap().flipScreen("static");
    }

    // When a DynamicScreen changes rotation, it calls back to this function. 
    @Override
    public void onScreenFlip(DynamicScreen caller) {
        int orientation = caller.getOrientation();
        
        if (orientation ==  DynamicScreen.OR_PORTRAIT) {
            testLabel.setText("Dynamic screen \nin portrait mode.");
        }

        if (orientation ==  DynamicScreen.OR_LANDSCAPE) {
            testLabel.setText("Now we are in landscape mode.");
        }
    }
   
    // This is used to create extra instances of this controller for the 
    // other screen(s) loaded by the DynamicScreen
    @Override
    public DynamicScreenInterface getNewInstance(DynamicScreen caller) {
        return new MainController();
    }

    @Override
    public void onDynamicScreenInitialize(DynamicScreen caller) {
        // We set dynamic fonts here
        /// node, portrait size, landscape size
        caller.setDynamicFont(testLabel, 12, 20);
        caller.setDynamicFont(testButton, 18, 24);
        caller.setDynamicFont(testTextField, 14, 20);
    }

}
