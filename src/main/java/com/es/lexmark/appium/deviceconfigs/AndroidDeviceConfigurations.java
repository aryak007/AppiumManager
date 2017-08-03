package com.es.lexmark.appium.deviceconfigs;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.es.lexmark.appium.commandprompt.CommandPrompt;

public class AndroidDeviceConfigurations {

    CommandPrompt cmd = new CommandPrompt();
    Map<String, String> devices = new HashMap<String, String>();
    ArrayList<String> deviceSerail = new ArrayList<String>();
    ArrayList<String> deviceModel = new ArrayList<String>();

    /* This method start adb server  */
    public void startADB() throws Exception {
        String output = cmd.executeCommand("adb start-server");
        String[] lines = output.split("\n");
        if (lines[0].contains("internal or external command")) {
            System.out.println("Please set ANDROID_HOME in your system variables");
        }
    }

    /*This method stop adb server */

    public void stopADB() throws Exception {
        cmd.executeCommand("adb kill-server");
    }


    public ArrayList<String> getDeviceSerial() throws Exception {

        startADB(); // start adb service
        String output = cmd.runCommand("adb devices");
        String[] lines = output.split("\n");

        if (lines.length <= 1) {
            System.out.println("No Device Connected");
            return null;
        } else {
            for (int i = 1; i < lines.length; i++) {
                lines[i] = lines[i].replaceAll("\\s+", "");

                if (lines[i].contains("device")) {
                    lines[i] = lines[i].replaceAll("device", "");
                    String deviceID = lines[i];
                    deviceSerail.add(deviceID);
                } else if (lines[i].contains("unauthorized")) {
                    lines[i] = lines[i].replaceAll("unauthorized", "");
                    //String deviceID = lines[i];
                } else if (lines[i].contains("offline")) {
                    lines[i] = lines[i].replaceAll("offline", "");
                   // String deviceID = lines[i];
                }
            }
            return deviceSerail;
        }
    }

}  
  
