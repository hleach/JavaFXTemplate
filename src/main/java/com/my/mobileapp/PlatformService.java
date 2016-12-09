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

/**
 *
 * @author Heath Leach
 */

import java.util.Iterator;
import java.util.ServiceLoader;

public class PlatformService {

    private static PlatformService instance = getInstance();
    
    private final ServiceLoader<PlatformProvider> serviceLoader;
    private static PlatformProvider provider;

    public static synchronized PlatformService getInstance() {
        if (instance == null) {
            instance = new PlatformService();
        }
        return instance;
    }

    private PlatformService() {
        serviceLoader = ServiceLoader.load(PlatformProvider.class);

        Iterator<PlatformProvider> iterator = serviceLoader.iterator();
        while (iterator.hasNext()) {
            if (provider == null) {
                provider = iterator.next();
            }
        }

        if (provider == null) {
            Log.e("No PlatformProvider implementation could be found!");
        }
    }
     
    public static String getSetting(String name) {
        return provider == null ? "" : provider.getSetting(name);
    }
    
    public static void saveSetting(String name, String value) {
        if (provider != null)
            provider.saveSetting(name, value);
    }
    
    public static void sendMessage(String text) {
        if (provider != null)
            provider.sendMessage(text);
    }
    
    public static void openBrowser(String url) {
        if (provider != null)
            provider.openBrowser(url);
    }
    
    public static void log(String text) {
        if (provider != null)
            provider.log(text);
        else System.err.println(text);
    }
  
    public static boolean isIOS() {
        if (provider != null)
            return provider.isIOS();
        return false;
    }
    
    public static boolean isAndroid() {
        if (provider != null)
            return provider.isAndroid();
        return false;
    }
    
    public static boolean isDesktop() {
        if (provider != null)
            return provider.isDesktop();
        return false;
    }
    
    public static void hideOnScreenKeyboard() {
        if (provider != null)
            provider.hideOnScreenKeyboard();
    }
    
    public static void showOnScreenKeyboard() {
        if (provider != null)
            provider.showOnScreenKeyboard();
    }
    
    public static void platformRun(String exec) {
        if (provider != null)
            provider.platformRun(exec);
    }
    
    public static boolean existsForRun(String exec) {
        if (provider != null)
            return provider.existsForRun(exec);
        else {
            log("providor == null in existsForRun");
            return false;
        }
    }
    
    public static Object test() {
        return provider.test();
    }
}