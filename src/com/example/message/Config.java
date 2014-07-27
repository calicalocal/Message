package com.example.message;

public interface Config {
	// used to share GCM regId with application server - using php app server
	static final String APP_SERVER_URL = "http://talktocal.net/messaging/gcm.php";//?shareRegId=1";

	// GCM server using java
	// static final String APP_SERVER_URL =
	// "http://192.168.1.17:8080/GCM-App-Server/GCMNotification?shareRegId=1";
	
	// Google Project Number
	static final String GOOGLE_PROJECT_ID = "502588464505";
	static final String MESSAGE_KEY = "message";
	static final String REGISTER_NAME = "register_name";
	static final String TO_NAME = "to_name";
	static final String FROM_NAME = "from_name";
}
