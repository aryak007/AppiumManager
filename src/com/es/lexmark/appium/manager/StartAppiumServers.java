package com.es.lexmark.appium.manager;

public class StartAppiumServers implements Runnable{
	public String device;
	
	public StartAppiumServers(String device){
		this.device = device;
	}
	
	
	@Override
	public void run() {
		SpawnAppiumServers sas = new SpawnAppiumServers();
		try {
			sas.appiumServerForAndroid(device,"Somename");
			Thread.sleep(Long.MAX_VALUE);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		
	}

}
