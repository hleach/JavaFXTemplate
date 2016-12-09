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

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import javafxports.android.FXActivity;

/**
 *
 * @author Heath Leach
 */
public class AndroidPlatformProvider implements PlatformProvider {
    
    private static final String APP_NAME = "AppTemplate";
    private final Context context = FXActivity.getInstance();
      
    @Override
    public String getSetting(String name) {
        SharedPreferences prefs = context.getSharedPreferences("com.my", Context.MODE_PRIVATE);
        return prefs.getString(name, "");
    }

    @Override
    public void saveSetting(String name, String value) {
        SharedPreferences prefs = context.getSharedPreferences("com.my", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(name, value);
        editor.commit();
    }

    @Override
    public void sendMessage(String text) {
      Intent smsIntent = new Intent(android.content.Intent.ACTION_VIEW);
        smsIntent.setType("vnd.android-dir/mms-sms");
        smsIntent.putExtra("sms_body", text);
        context.startActivity(Intent.createChooser(smsIntent, "SMS:"));
    }

    @Override
    public void openBrowser(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW);
        browserIntent.setData(Uri.parse(url));
        context.startActivity(browserIntent);        
    }
    
    @Override
    public void log(String text) {
        Log.d(APP_NAME, text);
    }

    @Override
    public boolean isIOS() {
        return false;
    }

    @Override
    public boolean isAndroid() {
        return true;
    }

    @Override
    public boolean isDesktop() {
        return false;
    }

    @Override
    public void hideOnScreenKeyboard() {
        try{
            View view = FXActivity.getInstance().getCurrentFocus();
            if (view != null) {  
                InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } catch (Exception ex) {
          log(ex.toString());
        }
    }

    @Override
    public void showOnScreenKeyboard() {
        try{
            View view = FXActivity.getInstance().getCurrentFocus();
            if (view != null) {  
                InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(view, 0);
            }
        } catch (Exception ex) {
          log(ex.toString());
        }
    }
    
    @Override
    public void platformRun(String exec) {
        
        PackageManager pm = context.getPackageManager();
        if (pm == null) {
            log("Null PackageManager in platformRun()");
            return;
        }
        
        Intent launchIntent = pm.getLaunchIntentForPackage(exec);
        
        if (launchIntent != null) { 
            launchIntent.setAction(Intent.ACTION_MAIN);
            launchIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        
            context.startActivity(launchIntent);
        } else {
            log("Null intent in platformRun()");
        }
    }
   
    @Override
    public boolean existsForRun(String exec) {
        PackageManager pm = context.getPackageManager();
        if (pm == null) {
            log("Null PackageManager in existsForRun()");
            return false;
        }
        
        Intent launchIntent = pm.getLaunchIntentForPackage(exec);
        
        return launchIntent != null;
    }
   
    @Override
    public Object test() {
        FXActivity.getInstance().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        return null;
    }
 }