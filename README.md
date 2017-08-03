# AppiumManager

<b> Manages multiple running instances of appium for multiple devices to run tests in a concurrent manner. </b>


## Set Up

The following steps are required to use AppiumManager in order to spawn up and stop appium servers dynamically

Step 1 - Create a folder ([AppiumManagerSample](https://pswgithub.rds.lexmark.com/arysengupta/AppiumManager/tree/master/AppiumManagerSample)) similar to the example given below: 

```
Folder structure to the one we have over here: 
AppiumManagerSample
      config.properties
```

Step 2 - Make sure the contents of the file, ```config.properties``` is similar to this

```
#config.properties content

APPIUM_JS_PATH=/usr/local/lib/node_modules/appium/build/lib/main.js
TEST_CLASS=com.perceptivesoftware.mobile.systemtest.test.EULATest
IOS_APP_PATH=/Users/aryaksengupta/Documents/PSWGithub/AppiumManager/app/PerceptiveExperience-devbuild.ipa
ANDROID_APP_PATH=/Users/aryaksengupta/Documents/PSWGithub/AppiumManager/app/PerceptiveExperience.apk
IS_IOS_SIMULATOR_UP=true
ANDROID_PLATFORM_VERSION=6.0
IOS_PLATFORM_VERSION=9.3
SIMULATOR_DEVICE_NAME=iPhone 6s
```

Step 3 - Download the JAR file named [AppiumManager-all-1.0.jar](https://pswgithub.rds.lexmark.com/arysengupta/AppiumManager/raw/master/AppiumManagerSample/AppiumManager-all-1.0.jar)  into the folder (in this case ```AppiumManagerSample```)

You can download the latest JAR file from [IndiaJenkins AppiumManager job](http://indiajenkins.pvi.com/job/Experiments/job/Cyclops/job/AppiumManager/lastSuccessfulBuild/artifact/build/libs/AppiumManager-all-1.0.jar)

## Options for running the JAR file

Opt 1 - ```--spawn-servers``` => Spawns appium servers for all devices and also for iOS simulators and Android emulator
```
java -jar AppiumManager-all-1.0.jar --spawn-servers  config.properties (Change config.properties path to the path in your machine)
```

Opt 2 - ```--generate-testng-xml``` => Generates a testng.xml file in the root directory , i.e. in this case ```AppiumManagerSample``` directory. Always run this after running the --spawn-servers command.
```
java -jar AppiumManager-all-1.0.jar --generate-testng-xml
```
Opt3 - ```--stop-all-servers``` => Stops all running appium servers
```
java -jar AppiumManager-all-1.0.jar --stop-all-servers
```
Opt 4 - ```--stop <<Port number>>``` => Stops a specific server running on a particular port. You can generate the testng.xml file to find out which ports are occupied.
```
java -jar AppiumManager-all-1.0.jar --stop 55239
```
Opt 5 - ```--get-ports``` => Lists the ports on which the appium servers are listening to along with the device type and UDID
```
java -jar AppiumManager-all-1.0.jar --get-ports
```

Opt 6 - ```--help``` => Returns the list of available options
```
java -jar AppiumManager-all-1.0.jar --help
```

## Authors
 **Aryak Sengupta**
