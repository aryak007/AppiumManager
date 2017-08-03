package com.es.lexmark.appium.manager;

import java.io.File;
import java.util.Properties;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.openqa.selenium.remote.DesiredCapabilities;

import com.es.lexmark.appium.commandprompt.CommandPrompt;
import com.es.lexmark.appium.configfile.createTestRunnerConfigFile;
import com.es.lexmark.appium.deviceconfigs.AndroidDeviceConfigurations;
import com.es.lexmark.appium.deviceconfigs.IOSDeviceConfigurations;
import com.es.lexmark.appium.ports.AvailabelPorts;

import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;


public class SpawnAppiumServers {
	
    public static Properties prop1 = new Properties();
    public static Properties prop2 = new Properties();
    public static String configPropertiesFilePath;
    public AppiumServiceBuilder builder = new AppiumServiceBuilder();
    public static InputStream input = null;
    public OutputStream output = null;
    public static CommandPrompt cp = new CommandPrompt();
    AvailabelPorts ap = new AvailabelPorts();
    public AppiumDriverLocalService appiumDriverLocalService;
	public static boolean simulator_appium=false;
	
    public static ArrayList<String> androidPorts=new ArrayList<String>();
    public static ArrayList<String> iosPorts=new ArrayList<String>();
    
    public static Properties props2 = new Properties();
    

    public static IOSDeviceConfigurations iosDevice = new IOSDeviceConfigurations();
    public static AndroidDeviceConfigurations androidDevice = new AndroidDeviceConfigurations();
    private static ConcurrentHashMap<ArrayList<String>,String> devices = new ConcurrentHashMap<ArrayList<String>,String>();
    public static ConcurrentHashMap<String, String> deviceMapping = new ConcurrentHashMap<String, String>();
    public static int deviceCount;
	public void collectDeviceConfigurations(String path){
		
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
	            if(isIosSimulatorUp(path)){
	    			deviceMapping.put("simulator", "ios");
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
	
	public void appiumServerForAndroid(String deviceID, String path)
	        throws Exception {
	        System.out.println("**************************************************************************\n");
	        System.out.println("Starting Appium Server to handle Android Device::" + deviceID + "\n");
	        
	        //input = new FileInputStream(path);
	        //input = SpawnAppiumServers.class.getResourceAsStream(path);
	        input = new FileInputStream(new File(path));
	        output = new FileOutputStream("ports.properties");

	        prop1.load(input);
	       int port = ap.getPort();
	       prop2.setProperty("android--"+deviceID, Integer.toString(port));
	       prop2.store(output, null);
	       
	       
	       DesiredCapabilities ds = new DesiredCapabilities();
	       ds.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
	       ds.setCapability(MobileCapabilityType.DEVICE_NAME, "Android device");
	       ds.setCapability(MobileCapabilityType.AUTOMATION_NAME, "Appium");
	       ds.setCapability(MobileCapabilityType.PLATFORM_VERSION,prop1.getProperty("ANDROID_PLATFORM_VERSION").trim());
	       ds.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 7200);
	       ds.setCapability(MobileCapabilityType.APP,prop1.getProperty("ANDROID_APP_PATH").trim());
	       if(!deviceID.equals("emulator-5554"))
	    	   ds.setCapability(MobileCapabilityType.UDID, deviceID);
	       
	       File logFileAndroid = new File(
	                System.getProperty("user.dir") + "/log/" + deviceID
	                .replaceAll("\\W", "_") + "__" + "android" +"_"+port+".txt");
	       
	       if(!logFileAndroid.exists()){
	    	   logFileAndroid.getParentFile().mkdirs();
	    	   logFileAndroid.createNewFile();
	       }
	    	   
	        AppiumServiceBuilder builder =
	            new AppiumServiceBuilder().withAppiumJS(new File(prop1.getProperty("APPIUM_JS_PATH").trim()))
	                .withArgument(GeneralServerFlag.LOG_LEVEL, "info").withLogFile(logFileAndroid)
	                .withCapabilities(ds)
	                .usingPort(port);
	       
	        appiumDriverLocalService = builder.build();
	        appiumDriverLocalService.start();
	        input.close();
		    output.close();
	    }
	
	
	 public void appiumServerForIOS(String deviceID,String path) throws Exception {
		        System.out.println("**********************************************************************\n");
		        System.out.println("Starting Appium Server to handle IOS::" + deviceID + "\n");
		        
		        
		        
		        //File classPathRoot = new File(System.getProperty("user.dir"));
		        //input = new FileInputStream(path);
		        //input = SpawnAppiumServers.class.getResourceAsStream(new File(path));
		        input = new FileInputStream(new File(path));
		        output = new FileOutputStream("ports.properties");
		       String iosDeviceName="",iosDeviceVersion="";
		        if(!deviceID.equals("simulator")){
		        	 iosDeviceName =  iosDevice.getDeviceName(deviceID);
		             iosDeviceVersion =  iosDevice.getIOSDeviceVersion(deviceID);
		        }
		        	
		        System.out.println("qqqqqqqq - "+iosDeviceName);
		        System.out.println("xxxxxxx - "+iosDeviceVersion);
		        prop1.load(input);
		        
		        int port = ap.getPort();
		        
	        
		        prop2.setProperty("ios--"+deviceID, Integer.toString(port));
		        prop2.store(output, null);
		       

		        
		       DesiredCapabilities ds = new DesiredCapabilities();
		       
		       ds.setCapability(MobileCapabilityType.PLATFORM_NAME, "iOS");
		       if(deviceID.equals("simulator")){
		    	   ds.setCapability(MobileCapabilityType.DEVICE_NAME,prop1.getProperty("SIMULATOR_DEVICE_NAME").trim());
		    	   ds.setCapability(MobileCapabilityType.PLATFORM_VERSION,prop1.getProperty("IOS_SIMULATOR_PLATFORM_VERSION").trim());
		       }
		       else{
		    	   ds.setCapability(MobileCapabilityType.DEVICE_NAME,iosDeviceName.trim());
		    	   ds.setCapability(MobileCapabilityType.PLATFORM_VERSION,iosDeviceVersion);
		    	   ds.setCapability(MobileCapabilityType.UDID, deviceID);
		       }
		       
		       ds.setCapability(MobileCapabilityType.AUTOMATION_NAME, "Appium");
		       
		       ds.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 7200);
		       ds.setCapability(MobileCapabilityType.APP, prop1.getProperty("IOS_APP_PATH").trim());

		       File logFileIos = new File(
		                System.getProperty("user.dir") +"/log/" + deviceID
	                    .replaceAll("\\W", "_") + "__"+ "ios" +"_"+port+ ".txt");
		       if(!logFileIos.exists()){
		    	   logFileIos.getParentFile().mkdirs();
		    	   logFileIos.createNewFile();
		       }
		    	  
		       
		        AppiumServiceBuilder builder =
		            new AppiumServiceBuilder().withAppiumJS(new File(prop1.getProperty("APPIUM_JS_PATH")))
		                .withArgument(GeneralServerFlag.LOG_LEVEL, "info").withLogFile(logFileIos)
		                .withCapabilities(ds)
		                .usingPort(port);
		        appiumDriverLocalService = builder.build();
		        appiumDriverLocalService.start();
		        //return builder;
		       
		        input.close();
			    output.close();
		        

		    }
	 
	 
	 public URL getAppiumUrl() {
	        return appiumDriverLocalService.getUrl();
	    }

	 public void destroyAppiumNode() {
		 System.out.println("\n----------Destroying all appium nodes----------\n");
		 if (System.getProperty("os.name").toLowerCase().contains("mac")) {
			 cp.executeCommand("killall node");
		 }
		 else if(System.getProperty("os.name").toLowerCase().contains("win")){
			 cp.executeCommand("taskkill /F /IM node.exe");
		 }
		 else{
			 cp.executeCommand("killall node");
		 }
	    }

		public void stopServerForSpecificPort(String port) {
			String command = "kill -9 $(lsof -t -i tcp:"+port+")";
			System.out.println("---Executing command---\n kill -9 $(lsof -t -i tcp:"+port+")");
			cp.executeCommand(command);
			
		}
		public void getHelpInformation(){
			System.out.println("\n----------List of allowed commands----------\n");
			System.out.println("--spawn-servers => Spawns appium servers for all devices and also for iOS simulators and Android emulator");
			System.out.println("\n--generate-testng-xml => Generates a testng.xml file in the root directory. Always run this after running the --spawn-servers command");
			System.out.println("\n--stop-all-servers => Stops all running appium servers");
			System.out.println("\n--stop <<Port number>> => Stops a specific server running on a particular port. You can generate the testng.xml file to find out which ports are occupied.");
			System.out.println("\n--get-ports => Lists the ports on which the appium servers are listening to along with the device type and UDID");
		}
	 
	 public boolean isIosSimulatorUp(String path) throws IOException{
		 System.out.println("Path of config - "+path);
     	 String classpath = System.getProperty("java.class.path");
     	 System.out.println(classpath+" -----  ");
		 //input = new FileInputStream(path);
	      //input = SpawnAppiumServers.class.getResourceAsStream(path);
     	input = new FileInputStream(new File(path));
	      prop1.load(input);
		 
		if(prop1.getProperty("IS_IOS_SIMULATOR_UP").equals("true")){
			return true;
		}
		return false;
		 
	 }
	 
	 public void getAllPorts() throws FileNotFoundException{
		 Scanner scan = new Scanner(new File("ports.properties"));
		 scan.nextLine();
		 if(!scan.hasNextLine()){
			 System.out.println("No servers are spawned yet!!!!");
			 return;
		 }
		 while(scan.hasNextLine()){
			 System.out.println(scan.nextLine());
		 }
	 }
	 
	 public static void main(String args[]) throws Exception{
		 SpawnAppiumServers sas = new SpawnAppiumServers();
		 if(args[0].equals("--spawn-servers"))
		 {
			 
			 sas.collectDeviceConfigurations(args[1]);
			 System.out.println("size"+deviceCount);
			 ExecutorService executorService = Executors.newFixedThreadPool(deviceCount);
			 //System.out.println("ios ports - "+iosPorts);
			 
			 for (String device:deviceMapping.keySet()){
				 
				 Runnable appiumServer = new StartAppiumServers(device,deviceMapping.get(device),args[1]);
				 executorService.execute(appiumServer);
			 }
		 }
		 else if(args[0].equals("--generate-testng-xml")){
			 createTestRunnerConfigFile c = new createTestRunnerConfigFile();
				Scanner scan = new Scanner(new File("ports.properties"));
				if(scan.hasNextLine()){
					if(scan.nextLine().isEmpty()){
						System.out.println("No servers are spawned yet. Create atleast one appium server before generating the Testng xml");
						sas.getHelpInformation();
						System.exit(1);
					}
				}
				else{
					System.out.println("No servers are spawned yet. Create atleast one appium server before generating the Testng xml");
					sas.getHelpInformation();
					System.exit(1);
				}
				 while(scan.hasNextLine()) {
					 String split[] = scan.nextLine().trim().split("--");
					 if(split[0].equals("ios")){
						 iosPorts.add(split[1].split("=")[1]);					
						 System.out.println(iosPorts.get(0));
					 }
					 if(split[0].equals("android")){
						 androidPorts.add(split[1].split("=")[1]);
						 System.out.println(androidPorts.get(0));

					 }
					 
				 }
				 
				 scan.close();
				 c.generateTestNgXmlFile(androidPorts,iosPorts);
		 }
		 else if(args[0].equals("--stop-all-servers")){
			 
			 sas.destroyAppiumNode();
		 }
		 
		 else if(args[0].equals("--stop")){
			 sas.stopServerForSpecificPort(args[1]);
		 }
		 
		 else if(args[0].equals("--help")){
			 sas.getHelpInformation();
			 
		 }
		 else if(args[0].equals("--get-ports")){
			 System.out.println("Warning: The list will be outdated if new servers are not spawned. Spawn new servers via --spawn-server");
			 sas.getAllPorts();
		 }
		 else{
			 System.out.println("Invalid switch!! Try again!!");
			 sas.getHelpInformation();
		 }
	 
	 }


}