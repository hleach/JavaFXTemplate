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
public interface PlatformProvider {
    String getSetting(String name);
    void saveSetting(String name, String value);
    void sendMessage(String text);
    void openBrowser(String url);
    void log(String text);
    boolean isIOS();
    boolean isAndroid();
    boolean isDesktop();
    void hideOnScreenKeyboard();
    void showOnScreenKeyboard();
    void platformRun(String exec);
    boolean existsForRun(String exec);
    Object test();
}