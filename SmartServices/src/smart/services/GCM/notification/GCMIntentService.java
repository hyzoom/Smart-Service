package smart.services.GCM.notification;

import smart.services.activity.MainActivity;
import smart.services.handler.DataBaseHandler;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.google.android.gcm.GCMBaseIntentService;
import info.androidhive.slidingmenu.R;

public class GCMIntentService extends GCMBaseIntentService {
	private static final String TAG = "GCMIntentService";
	private Controller aController = null;
	DataBaseHandler member;

	public GCMIntentService() {
		// Call extended class Constructor GCMBaseIntentService
		super(Config.GOOGLE_SENDER_ID);
	}

	/**
	 * Method called on device registered
	 **/
	@Override
	protected void onRegistered(Context context, String registrationId) {
		if (aController == null)
			aController = (Controller) getApplicationContext();

		Log.i(TAG, "Device registered: regId = " + registrationId);
		aController.displayMessageOnScreen(context,
				"Your device registred with GCM");
		aController.register(context, registrationId);
	}

	@Override
	protected void onUnregistered(Context context, String registrationId) {
		if (aController == null)
			aController = (Controller) getApplicationContext();
		Log.i(TAG, "Device unregistered");
		aController.displayMessageOnScreen(context,
				getString(R.string.gcm_unregistered));
		aController.unregister(context, registrationId);
	}

	@Override
	protected void onMessage(Context context, Intent intent) {
		member = new DataBaseHandler(this.getApplicationContext());
		if (aController == null)
			aController = (Controller) getApplicationContext();

		Log.i(TAG, "Received message");
		String message = intent.getExtras().getString("message");
		// String title = intent.getExtras().getString("title");
		String eventId = intent.getExtras().getString("EventId");
		aController.displayMessageOnScreen(context, message);
		// notifies user
		generateNotification(context, message, "smart service", eventId);
		// if (member.getUsersCount() > 0) {
		// generateNotification(context, "Hello! "
		// + member.getUser(1).getName(), "smart service", eventId);
		// } else {
		// generateNotification(context, "Hello! ", "smart service", eventId);
		// }

	}

	/**
	 * Method called on receiving a deleted message
	 * */
	@Override
	protected void onDeletedMessages(Context context, int total) {

		if (aController == null)
			aController = (Controller) getApplicationContext();

		Log.i(TAG, "Received deleted messages notification");
		String message = getString(R.string.gcm_deleted, total);
		aController.displayMessageOnScreen(context, message);
		// notifies user
		generateNotification(context, message, "title", "");
	}

	/**
	 * Method called on Error
	 * */
	@Override
	public void onError(Context context, String errorId) {

		if (aController == null)
			aController = (Controller) getApplicationContext();

		Log.i(TAG, "Received error: " + errorId);
		aController.displayMessageOnScreen(context,
				getString(R.string.gcm_error, errorId));
	}

	@Override
	protected boolean onRecoverableError(Context context, String errorId) {

		if (aController == null)
			aController = (Controller) getApplicationContext();

		// log message
		Log.i(TAG, "Received recoverable error: " + errorId);
		aController.displayMessageOnScreen(context,
				getString(R.string.gcm_recoverable_error, errorId));
		return super.onRecoverableError(context, errorId);
	}

	/**
	 * Create a notification to inform the user that server has sent a message.
	 */
	@SuppressWarnings("deprecation")
	private void generateNotification(Context context, String message,
			String title, String eventId) {

		Intent notificationIntent = new Intent(context, MainActivity.class);
		notificationIntent.putExtra("fragNum", "7");
		int icon = R.drawable.icon;
		long when = System.currentTimeMillis();

		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(icon, message, when);
		// set intent so it does not start a new activity
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent intent = PendingIntent.getActivity(context, 0,
				notificationIntent, Intent.FLAG_ACTIVITY_NEW_TASK);
		notification.setLatestEventInfo(context, title, message, intent);

		notification.flags |= Notification.FLAG_AUTO_CANCEL;

		// Play default notification sound
		notification.defaults |= Notification.DEFAULT_SOUND;

		// notification.sound = Uri.parse("android.resource://"
		// + context.getPackageName() + "your_sound_file_name.mp3");

		// Vibrate if vibrate is enabled
		notification.defaults |= Notification.DEFAULT_VIBRATE;
		notificationManager.notify(0, notification);

	}
}