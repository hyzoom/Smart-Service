package smart.services.GCM.notification;

public interface Config {
	// CONSTANTS
	// ==> Sharaf makes like register.php and give me URL
	static final String YOUR_SERVER_URL = "URL HERE"; // iam not using this
	// Google project id
	static final String GOOGLE_SENDER_ID = "335710064815"; // google id here
	/**
	 * Tag used on log messages.
	 */
	static final String TAG = "GCM Android Example";
	static final String DISPLAY_MESSAGE_ACTION = "com.gcm.gcm.DISPLAY_MESSAGE";
	static final String EXTRA_MESSAGE = "message";

}