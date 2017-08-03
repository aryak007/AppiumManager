package com.es.lexmark.appium.configfile;
import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;
public class createTestRunnerConfigFile {
	TestNG myTestNG = new TestNG();
	 
	//Create an instance of XML Suite and assign a name for it.
	 XmlSuite mySuite = new XmlSuite();
	 mySuite.setName("ASQA Sample Suite");
	 
	//Create an instance of XmlTest and assign a name for it.
	 XmlTest myTest = new XmlTest(mySuite);
	 myTest.setName("ASQA Sample Test");
	 
	//Add any parameters that you want to set to the Test.
	 myTest.setParameters(testngParams);
	 
	//Create a list which can contain the classes that you want to run.
	 List<XmlClass> myClasses = new ArrayList<XmlClass> ();
	 myClasses.add(new XmlClass("com.asqa.testng.examples.SampleTest1"));
	 
	//Assign that to the XmlTest Object created earlier.
	 myTest.setXmlClasses(myClasses);
	 
	//Create a list of XmlTests and add the Xmltest you created earlier to it.
	 List<XmlTest> myTests = new ArrayList<XmlTest>();
	 myTests.add(myTest);
	 
	//add the list of tests to your Suite.
	 mySuite.setTests(myTests);
	 
	//Add the suite to the list of suites.
	 List<XmlSuite> mySuites = new ArrayList<XmlSuite>();
	 mySuites.add(mySuite);
	 
	//Set the list of Suites to the testNG object you created earlier.
	 myTestNG.setXmlSuites(mySuites);
	 
	//invoke run() - this will run your class.
	 myTestNG.run();
}
