package com.es.lexmark.appium.configfile;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;


public class createTestRunnerConfigFile {

	public void generateTestNgXmlFile(ArrayList<String> androidPorts, ArrayList<String> iosPorts) throws IOException{

		XmlSuite xmlSuite = new XmlSuite();
		xmlSuite.setName("Suite");
		xmlSuite.setParallel(XmlSuite.DEFAULT_PARALLEL);
		
		
		
		for(String ap:androidPorts){
			XmlTest xmlTest = new XmlTest(xmlSuite);
			xmlTest.setName("Test:"+ap);
			Map<String, String> parameters = new HashMap<String, String>();
			parameters.put("platform", "android");
			parameters.put("port", ap);
			XmlClass testClass = new XmlClass ();
	        testClass.setName ("com.perceptivesoftware.mobile.systemtest.test.EULATest");
	        ArrayList<XmlClass> classes = new ArrayList<XmlClass>();
	        classes.add(testClass);
	        xmlTest.setClasses(classes);
			xmlTest.setParameters(parameters);
		}
		
		for(String ip:iosPorts){
			XmlTest xmlTest = new XmlTest(xmlSuite);
			xmlTest.setName("Test:"+ip);
			Map<String, String> parameters = new HashMap<String, String>();
			parameters.put("platform", "ios");
			parameters.put("port", ip);
			XmlClass testClass = new XmlClass ();
	        testClass.setName ("com.perceptivesoftware.mobile.systemtest.test.EULATest");
	        ArrayList<XmlClass> classes = new ArrayList<XmlClass>();
	        classes.add(testClass);
	        xmlTest.setClasses(classes);
			xmlTest.setParameters(parameters);
		}
		
        File file = new File(System.getProperty("user.dir")+"/testng.xml");
        System.out.println("file"+file);

        FileWriter writer = new FileWriter(file);
        writer.write(xmlSuite.toXml());
        writer.close(); 
	}
	
}
	


