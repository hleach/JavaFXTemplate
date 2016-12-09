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

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.prefs.Preferences;

/**
 *
 * @author Heath Leach
 */
public class DesktopPlatformProvider implements PlatformProvider {

    Preferences prefs = Preferences.userNodeForPackage(this.getClass());

    @Override
    public String getSetting(String name) {
        return prefs.get(name, "");
    }

    @Override
    public void saveSetting(String name, String value) {
        prefs.put(name, value);
    }

    @Override
    public void sendMessage(String text) {
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.MAIL)) {
                try {
                    URI uri = new URI("mailto:?body=" + URLEncoder.encode(text, "UTF-8"));
                    desktop.mail(uri);
                } catch (Exception ex) {
                    System.out.println(ex.toString());
                }
            }
        }    
    }

    @Override
    public void openBrowser(String url) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(new URL(url).toURI());
            } catch (Exception ex) {
                Log.e(ex);
            }
        }
    }
   
    @Override
    public void log(String text) {
        System.err.print(text);
    }
    
    @Override
    public boolean isIOS() {
        return false;
    }

    @Override
    public boolean isAndroid() {
        return false;
    }

    @Override
    public boolean isDesktop() {
        return true;
    }

    @Override
    public void hideOnScreenKeyboard() {
        String system = System.getProperty("os.name").toLowerCase();

        if (system.contains("windows")) {
            try {
                // Requires elevation as osk.exe gets run at an intermediate
                // access level in order to work with system software.
                // It's not clear if there is an easy way to close osk.exe
                // from a non admin process.
                Runtime.getRuntime().exec("taskkill /im /f osk.exe");
            } catch (IOException ex) {
                Log.e(ex);
            }
        }
    }

    @Override
    public void showOnScreenKeyboard() {
        String system = System.getProperty("os.name").toLowerCase();

        if (system.contains("windows")) {
            try {
                Runtime.getRuntime().exec("cmd.exe /c osk.exe");
            } catch (IOException ex) {
                Log.e(ex);
            }
        }
    }
    
    @Override
    public void platformRun(String exec) {
        try {
            Runtime.getRuntime().exec(exec);
        } catch (IOException ex) {
            Log.e(ex);
        }
    }

    @Override
    public boolean existsForRun(String exec) {
        File f = new File(exec);
        return (f.exists() && !f.isDirectory());
    }

    @Override
    public Object test() {
        
        return null;
    }
 }