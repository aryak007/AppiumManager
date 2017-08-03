package com.es.lexmark.appium.deviceconfigs;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import com.es.lexmark.appium.commandprompt.CommandPrompt;
import com.es.lexmark.appium.ports.AvailabelPorts;

public class IOSDeviceConfigurations {
    public ArrayList<String> deviceUDIDiOS = new ArrayList<String>();
    CommandPrompt commandPrompt = new CommandPrompt();
    AvailabelPorts ap = new AvailabelPorts();
    public HashMap<String, String> deviceMap = new HashMap<String, String>();
    Map<String, String> devices = new HashMap<>();
    public Process p;
    public Process p1;
    public Properties prop = new Properties();
    public InputStream input = null;

    public final static int IOS_UDID_LENGTH = 40;

    public static ConcurrentHashMap<Long, Integer> appiumServerProcess = new ConcurrentHashMap<>();

    public void checkIfiDeviceApiIsInstalled() throws InterruptedException, IOException {
        boolean checkMobileDevice =
                commandPrompt.runCommand("brew list").contains("ideviceinstaller");
        if (checkMobileDevice) {
            System.out.println("iDeviceInstaller already exists");
        } else {
            System.out.println("Brewing iDeviceInstaller API....");
            commandPrompt.runCommand("brew install ideviceinstaller");
        }

    }

    public ArrayList<String> getIOSUDID() {

        try {
            int startPos = 0;
            int endPos = IOS_UDID_LENGTH - 1;
            String profile = "system_profiler SPUSBDataType | sed -n -E -e '/(iPhone|iPad|iPod)/,"
                    + "/Serial/s/ Serial Number: (.+)/\\1/p'";
            String getIOSDeviceID = commandPrompt.runProcessCommandToGetDeviceID(profile);
            if (getIOSDeviceID == null || getIOSDeviceID.equalsIgnoreCase("") || getIOSDeviceID
                    .isEmpty()) {
                return null;
            } else {
                while (endPos < getIOSDeviceID.length()) {
                    deviceUDIDiOS.add(getIOSDeviceID.substring(startPos, endPos + 1));
                    startPos += IOS_UDID_LENGTH;
                    endPos += IOS_UDID_LENGTH;
                }
                return deviceUDIDiOS;
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public String getIOSDeviceVersion(String udid)
            throws InterruptedException, IOException {
    	String command = "ideviceinfo --udid " + udid + " | grep ProductVersion";
    	System.out.println("Command fired - "+command);
        return commandPrompt.runCommandThruProcessBuilder(command);
    }
    
    public String getDeviceName(String udid) throws InterruptedException, IOException {
        String deviceName =
                commandPrompt.runCommand("idevicename --udid " + udid);
        return deviceName;
    }
    
}
