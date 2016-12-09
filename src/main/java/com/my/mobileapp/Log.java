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
public class Log {
    
    private static final int LOG_NORMAL = 0;
    private static final int LOG_VERBOSE = 1;
    private static final int LOG_DEBUG = 2;
    
    private static int level = LOG_DEBUG | LOG_VERBOSE;
    
    private static boolean isVerbose() {
        return (level & LOG_VERBOSE) == LOG_VERBOSE;
    }
    
    private static boolean isDebug() {
        return (level & LOG_DEBUG) == LOG_DEBUG;
    }
    
    /**
     * 
     * Write a string to debug logging facilities.
     * 
     * @param s String to write to log
     */
    public static void d(String s) {
        if (isDebug()) {
            StackTraceElement[] e = Thread.currentThread().getStackTrace();
            StringBuilder sb = new StringBuilder();

            sb.append("Debug: ");

            sb.append(e[2].getClassName()).append(".").append(e[2].getMethodName())
                .append(" Line: ").append(e[2].getLineNumber()).append(", ");

            sb.append(s).append(System.lineSeparator());

            PlatformService.log(sb.toString());
        }
    }

    /**
     * 
     * Write a string to error logging facilities. If log level > LOG_NORMAL, log stack trace as well.
     * 
     * @param s String to write to log
     */
    public static void e(String s) {
        StackTraceElement[] e = Thread.currentThread().getStackTrace();
        StringBuilder sb = new StringBuilder();
        
        sb.append("Error: ");
        
        if (!isVerbose())
            sb.append(e[2].getClassName()).append(".").append(e[2].getMethodName())
              .append(" Line: ").append(e[2].getLineNumber()).append(", ");
    
        sb.append(s).append(System.lineSeparator());
        
        if (isVerbose()) {
            for (int i = 2; i < e.length; i++)
                sb.append(e[i].toString()).append(System.lineSeparator());
        }
    
        PlatformService.log(sb.toString());
    }

    /**
     * 
     * Log exception message to error logging. If log level > LOG_NORMAL, log stack trace as well.
     * 
     * @param ex Exception to log
     */
    public static void e(Exception ex) {
        StackTraceElement[] e = ex.getStackTrace();
        StringBuilder sb = new StringBuilder();
        
        sb.append("Error: ");
        
        if (!isVerbose())
            sb.append(e[0].getClassName()).append(".").append(e[0].getMethodName())
              .append(" Line: ").append(e[0].getLineNumber()).append(", ");
    
        sb.append(ex.getMessage()).append(System.lineSeparator());
        
        if (isVerbose()) {
            for (int i = 0; i < e.length; i++)
                sb.append(e[i].toString()).append(System.lineSeparator());
        }

        PlatformService.log(sb.toString());
    }

    /**
     * 
     * Write a string to error logging and exit.
     * 
     * @param s 
     */
    public static void f(String s) {
        StackTraceElement[] e = Thread.currentThread().getStackTrace();
        StringBuilder sb = new StringBuilder();
        
        sb.append("Fault: ");
        
        if (!isVerbose())
            sb.append(e[2].getClassName()).append(".").append(e[2].getMethodName())
              .append(" Line: ").append(e[2].getLineNumber()).append(", ");
    
        sb.append(s).append(System.lineSeparator());
        
        if (isVerbose()) {
            for (int i = 2; i < e.length; i++)
                sb.append(e[i].toString()).append(System.lineSeparator());
        }
    
        PlatformService.log(sb.toString());
        System.exit(-1);
    }

    public static void f(Exception ex) {
        StackTraceElement[] e = ex.getStackTrace();
        StringBuilder sb = new StringBuilder();
        
        sb.append("Fault: ");
        
        if (!isVerbose())
            sb.append(e[0].getClassName()).append(".").append(e[0].getMethodName())
              .append(" Line: ").append(e[0].getLineNumber()).append(", ");
    
        sb.append(ex.getMessage()).append(System.lineSeparator());
        
        if (isVerbose()) {
            for (int i = 0; i < e.length; i++)
                sb.append(e[i].toString()).append(System.lineSeparator());
        }

        PlatformService.log(sb.toString());
        System.exit(-1);
    }

}
