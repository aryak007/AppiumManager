package com.es.lexmark.appium.manager;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.openqa.selenium.remote.DesiredCapabilities;

import com.es.lexmark.appium.commandprompt.CommandPrompt;
import com.es.lexmark.appium.deviceconfigs.AndroidDeviceConfigurations;
import com.es.lexmark.appium.deviceconfigs.IOSDeviceConfigurations;
import com.es.lexmark.appium.manager.*;
import com.es.lexmark.appium.ports.AvailabelPorts;

import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;


public class SpawnAppiumServers {
	
    public static Properties prop = new Properties();
    public AppiumServiceBuilder builder = new AppiumServiceBuilder();
    public InputStream input = null;
    CommandPrompt cp = new CommandPrompt();
    AvailabelPorts ap = new AvailabelPorts();
    public AppiumDriverLocalService appiumDriverLocalService;
    
    
    public static IOSDeviceConfigurations iosDevice = new IOSDeviceConfigurations();
    public static AndroidDeviceConfigurations androidDevice = new AndroidDeviceConfigurations();
    private static ConcurrentHashMap<ArrayList<String>,String> devices = new ConcurrentHashMap<ArrayList<String>,String>();
    public static ConcurrentHashMap<String, String> deviceMapping = new ConcurrentHashMap<String, String>();
    public static int deviceCount;
	static {
        try {
            if (System.getProperty("os.name").toLowerCase().contains("mac")) {
                if (iosDevice.getIOSUDID() != null) {
                    System.out.println("Adding iOS devices");
                    devices.put(iosDevice.getIOSUDID(),"ios");
                }
                if (androidDevice.getDeviceSerial() != null) {
                    System.out.println("Adding Android devices");
                    devices.put(androidDevice.getDeviceSerial(),"android");

                }
            } else {
                if (androidDevice.getDeviceSerial() != null) {
                    System.out.println("Adding Android devices");
                    devices.put(androidDevice.getDeviceSerial(),"android");
                }
            }
            for (ArrayList<String> deviceList : devices.keySet()) {
            	for(String device:deviceList){
            		deviceMapping.put(device,devices.get(deviceList));
            	}
            }
            System.out.println("Mapping - "+deviceMapping);
            deviceCount = deviceMapping.size();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to initialize framework");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to initialize framework");
        }
    }
	
	public void appiumServerForAndroid(String deviceID, String methodName)
	        throws Exception {
	        System.out.println("**************************************************************************\n");
	        System.out.println("Starting Appium Server to handle Android Device::" + deviceID + "\n");
	        System.out.println(
	            "**************************************************************************\n");
	        input = new FileInputStream("config.properties");
	        prop.load(input);
	        int port = ap.getPort();
	        
	       DesiredCapabilities ds = new DesiredCapabilities();
	       ds.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
	       ds.setCapability(MobileCapabilityType.DEVICE_NAME, "Android Emulator");
	       ds.setCapability(MobileCapabilityType.AUTOMATION_NAME, "Appium");
	       ds.setCapability(MobileCapabilityType.PLATFORM_VERSION,prop.getProperty("ANDROID_PLATFORM_VERSION") );
	       ds.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 7200);
	       ds.setCapability(MobileCapabilityType.APP, prop.getProperty("ANDROID_APP_PATH"));
	       ds.setCapability(MobileCapabilityType.UDID, deviceID);
	       
	        
	        
	        AppiumServiceBuilder builder =
	            new AppiumServiceBuilder().withAppiumJS(new File(prop.getProperty("APPIUM_JS_PATH")))
	                .withArgument(GeneralServerFlag.LOG_LEVEL, "info").withLogFile(new File(
	                System.getProperty("user.dir")  + deviceID
	                .replaceAll("\\W", "_") + "__" + methodName + ".txt"))
	                .withCapabilities(ds)
	                .usingPort(port);
	       
	        appiumDriverLocalService = builder.build();
	        appiumDriverLocalService.start();
	        //return builder;

	    }
	
	
	 public void appiumServerForIOS(String deviceID, String methodName,
		        String webKitPort) throws Exception {
		        System.out
		            .println("**********************************************************************\n");
		        System.out.println("Starting Appium Server to handle IOS::" + deviceID + "\n");
		        System.out
		            .println("**********************************************************************\n");
		        File classPathRoot = new File(System.getProperty("user.dir"));
		        input = new FileInputStream("config.properties");
		        prop.load(input);
		        int port = ap.getPort();
		        
			      DesiredCapabilities ds = new DesiredCapabilities();
			       ds.setCapability(MobileCapabilityType.PLATFORM_NAME, "iOS");
			       ds.setCapability(MobileCapabilityType.DEVICE_NAME, "iOS device");
			       ds.setCapability(MobileCapabilityType.AUTOMATION_NAME, "Appium");
			       ds.setCapability(MobileCapabilityType.PLATFORM_VERSION,prop.getProperty("IOS_PLATFORM_VERSION") );
			       ds.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 7200);
			       ds.setCapability(MobileCapabilityType.APP, prop.getProperty("IOS_APP_PATH"));
			       ds.setCapability(MobileCapabilityType.UDID, deviceID);
			       
		        
		        AppiumServiceBuilder builder =
		            new AppiumServiceBuilder().withAppiumJS(new File(prop.getProperty("APPIUM_JS_PATH")))
		                .withArgument(GeneralServerFlag.LOG_LEVEL, "info").withLogFile(new File(
		                System.getProperty("user.dir") + "/target/appiumlogs/" + deviceID
		                    .replaceAll("\\W", "_") + "__" + methodName + ".txt"))
		                .withCapabilities(ds)
		                .withArgument(GeneralServerFlag.LOG_LEVEL, "debug")
		                .withArgument(GeneralServerFlag.TEMP_DIRECTORY,
		                    new File(String.valueOf(classPathRoot)).getAbsolutePath() + "/target/" + "tmp_"
		                        + port).usingPort(port);
		        appiumDriverLocalService = builder.build();
		        appiumDriverLocalService.start();
		        //return builder;

		    }
	 public URL getAppiumUrl() {
	        return appiumDriverLocalService.getUrl();
	    }
	 
	 /*public synchronized Runnable startAppiumServers(String device) throws Exception{
		 appiumServerForAndroid(device,"Somename");
		 //Thread.sleep(Long.MAX_VALUE);
		return null;
			 
			 
	 }
	 */
	 public void destroyAppiumNode() {
	        appiumDriverLocalService.stop();
	        if (appiumDriverLocalService.isRunning()) {
	            System.out.println("AppiumServer didn't shut... Trying to quit again....");
	            appiumDriverLocalService.stop();
	        }
	    }
	 
	 public static void main(String args[]) throws Exception{
		 //SpawnAppiumServers sas = new SpawnAppiumServers();
		
			 
			 ExecutorService executorService = Executors.newFixedThreadPool(deviceCount);
			/*  executorService.submit(new Runnable() {
	              public void run() {
	            	  try {

	         			 for (String device:deviceMapping.keySet()){
	         				 sas.startAppiumServers(device);
	         			 
	         			 }

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	     			
	              }
	          });*/
			 for (String device:deviceMapping.keySet()){
				 
				Runnable appiumServer = new StartAppiumServers(device);
				 executorService.execute(appiumServer);
			 }
 				 
 			 
			 
	 }
}
