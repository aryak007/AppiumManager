package com.es.lexmark.appium.deviceconfigs;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    /* Information of connected devices */
    public Map<String, String> getDevices() throws Exception {

        startADB(); // start adb service
        String output = cmd.executeCommand("adb devices");
        String[] lines = output.split("\n");

        if (lines.length <= 1) {
            System.out.println("No Device Connected");
            stopADB();
            return null;
        } else {
            System.out.println("HHH");

            for (int i = 1; i < lines.length; i++) {
                lines[i] = lines[i].replaceAll("\\s+", "");

                if (lines[i].contains("device")) {
                    lines[i] = lines[i].replaceAll("device", "");
                    String deviceID = lines[i];
                    String model =
                        cmd.executeCommand("adb -s " + deviceID + " shell getprop ro.product.model")
                            .replaceAll("\\s+", "");
                    String brand =
                        cmd.executeCommand("adb -s " + deviceID + " shell getprop ro.product.brand")
                            .replaceAll("\\s+", "");
                    String osVersion = cmd.executeCommand(
                        "adb -s " + deviceID + " shell getprop ro.build.version.release")
                        .replaceAll("\\s+", "");
                    String deviceName = brand + " " + model;
                    String apiLevel =
                        cmd.executeCommand("adb -s " + deviceID + " shell getprop ro.build.version.sdk")
                            .replaceAll("\n", "");

                    devices.put("deviceID" + i, deviceID);
                    devices.put("deviceName" + i, deviceName);
                    devices.put("osVersion" + i, osVersion);
                    devices.put(deviceID, apiLevel);
                } else if (lines[i].contains("unauthorized")) {
                    lines[i] = lines[i].replaceAll("unauthorized", "");
                    String deviceID = lines[i];
                } else if (lines[i].contains("offline")) {
                    lines[i] = lines[i].replaceAll("offline", "");
                    String deviceID = lines[i];
                }
            }
            return devices;
        }
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
                    String deviceID = lines[i];
                } else if (lines[i].contains("offline")) {
                    lines[i] = lines[i].replaceAll("offline", "");
                    String deviceID = lines[i];
                }
            }
            return deviceSerail;
        }
    }

    
    /*public ArrayList<String> fetchIosDeviceUdids(){
		String line = null;  
		String mat = null;
		 String out = cmd.executeCommand("idevice_id -l");
		 ArrayList<String> al = new ArrayList<String>();
	try {
        Pattern pattern = Pattern.compile("^([a-zA-Z0-9\\-]+)");
        Matcher matcher;
       
        BufferedReader br = new BufferedReader(new StringReader(out));
        while ((line=br.readLine()) != null ) {  
            
                matcher = pattern.matcher(line);
        
                	mat = matcher.group(1);
                	if(!mat.contains("emulator")){
                		System.out.println(matcher.group(1));
                		al.add(matcher.group(1));
                }
        }
                    
	}

        
    catch (IOException e) {
        e.printStackTrace();
    }
	System.out.println("");
	return al;
}*/
    /*
     * Main method for test
     
 
    public static void main(String args[]) throws Exception{
    	AndroidDeviceConfigurations adc = new AndroidDeviceConfigurations();
    	adc.startADB();
    	System.out.println("List android - "+adc.getDevices());
    	System.out.println("List ios- "+adc.fetchIosDeviceUdids());
    	adc.stopADB();
    }*/
}

