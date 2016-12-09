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

import org.robovm.apple.foundation.NSUserDefaults;

/**
 *
 * @author Heath Leach
 */
public class IosPlatformProvider implements PlatformProvider {

    @Override
    public String getSetting(String name) {
        
        NSUserDefaults defaults = NSUserDefaults.getStandardUserDefaults();
        
        if (defaults == null)
            return "";
        
        String s = defaults.getString(name);
        
        if (s == null)
            return "";      
        
        return s;
    }

    @Override
    public void saveSetting(String name, String value) {
        NSUserDefaults.getStandardUserDefaults().put(name, value);
    }

    @Override
    public void sendMessage(String text) {
    }

    @Override
    public void openBrowser(String url) {
    }

    @Override
    public void log(String text) {
    }

    @Override
    public boolean isIOS() {
        return true;
    }

    @Override
    public boolean isAndroid() {
        return false;
    }

    @Override
    public boolean isDesktop() {
        return false;
    }

    @Override
    public void hideOnScreenKeyboard() {

    }

    @Override
    public void showOnScreenKeyboard() {

    }

    @Override
    public void platformRun(String exec) {
    }

    @Override
    public boolean existsForRun(String exec) {
        return false;
    }

    @Override
    public Object test() {
        
        return null;
    }
}
