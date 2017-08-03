package com.es.lexmark.appium.manager;

public class StartAppiumServers implements Runnable{
	public String device;
	public String os;
	public String path;

	public StartAppiumServers(String device,String os,String path){
		this.device = device;
		this.os = os;
		this.path = path;
	}
	
	
	@Override
	public void run() {
		SpawnAppiumServers sas = new SpawnAppiumServers();
		try {
			if(os=="android")
				sas.appiumServerForAndroid(device,path);
			else
				sas.appiumServerForIOS(device,path);
				
			Thread.sleep(Long.MAX_VALUE);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
		
	}

}