package smart.services.GCM.notification;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import com.google.android.gcm.GCMRegistrar;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.util.Log;
import info.androidhive.slidingmenu.R;

public class Controller extends Application {

	private final int MAX_ATTEMPTS = 5;
	static String serverUrl = "";

	// Register this account with the server.
	public void register(final Context context, final String regId) {

		Log.i(Config.TAG, "registering device (regId = " + regId + ")");

		Map<String, String> params = new HashMap<String, String>();
		params.put("notificationId", regId);
		serverUrl = "http://smarty-trioplus.rhcloud.com/supportCenter/pushNotifications?notificationId="
				+ regId + "&userMessage=test";
		try {
			post(serverUrl, params);
			GCMRegistrar.setRegisteredOnServer(context, true);
			// Send Broadcast to Show message on screen
			String message = context.getString(R.string.server_registered);
			displayMessageOnScreen(context, message);

			return;
		} catch (IOException e) {

		}

		String message = context.getString(R.string.server_register_error,
				MAX_ATTEMPTS);

		// Send Broadcast to Show message on screen
		displayMessageOnScreen(context, message);
	}

	// Unregister this account/device pair within the server.
	void unregister(final Context context, final String regId) {

		Log.i(Config.TAG, "unregistering device (regId = " + regId + ")");

		String serverUrl = Config.YOUR_SERVER_URL + "/unregister";
		Map<String, String> params = new HashMap<String, String>();
		params.put("regId", regId);

		try {
			post(serverUrl, params);
			GCMRegistrar.setRegisteredOnServer(context, false);
			String message = context.getString(R.string.server_unregistered);
			displayMessageOnScreen(context, message);
		} catch (IOException e) {

			String message = context.getString(
					R.string.server_unregister_error, e.getMessage());
			displayMessageOnScreen(context, message);
		}
	}

	// Issue a POST request to the server.
	// ==> post request to server with the parms
	private static void post(String registerationURL, Map<String, String> params)
			throws IOException {
		URL url;
		try {
			url = new URL(registerationURL);
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException("invalid url: "
					+ registerationURL);
		}
		StringBuilder bodyBuilder = new StringBuilder();
		Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
		// constructs the POST body using the parameters
		while (iterator.hasNext()) {
			Entry<String, String> param = iterator.next();
			bodyBuilder.append(param.getKey()).append('=')
					.append(param.getValue());
			if (iterator.hasNext()) {
				bodyBuilder.append('&');
			}
		}
		String body = bodyBuilder.toString();
		Log.v(Config.TAG, "Posting '" + body + "' to " + url);
		// byte[] bytes = body.getBytes();
		// HttpURLConnection conn = null;
		// try {
		// ==> Sharaf make post request
		Log.e("URL", "> " + url);
		Controller a = new Controller();
		a.new GetNotification(url + "").execute();
	}

	// Checking for all possible internet providers
	public boolean isConnectingToInternet() {

		ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}

		}
		return false;
	}

	// Notifies UI to display a message.
	void displayMessageOnScreen(Context context, String message) {

		Intent intent = new Intent(Config.DISPLAY_MESSAGE_ACTION);
		intent.putExtra(Config.EXTRA_MESSAGE, message);

		// Send Broadcast to Broadcast receiver with message
		context.sendBroadcast(intent);

	}

	// Function to display simple Alert Dialog
	@SuppressWarnings("deprecation")
	public void showAlertDialog(Context context, String title, String message,
			Boolean status) {
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();

		// Set Dialog Title
		alertDialog.setTitle(title);

		// Set Dialog Message
		alertDialog.setMessage(message);

		if (status != null)
			// Set alert dialog icon
			alertDialog.setIcon((status) ? R.drawable.ic_launcher
					: R.drawable.ic_launcher);

		// Set OK Button
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

			}
		});

		// Show Alert Message
		alertDialog.show();
	}

	private PowerManager.WakeLock wakeLock;

	@SuppressLint("Wakelock")
	public void acquireWakeLock(Context context) {
		if (wakeLock != null)
			wakeLock.release();

		PowerManager pm = (PowerManager) context
				.getSystemService(Context.POWER_SERVICE);

		wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
				| PowerManager.ACQUIRE_CAUSES_WAKEUP
				| PowerManager.ON_AFTER_RELEASE, "WakeLock");

		wakeLock.acquire();
	}

	public void releaseWakeLock() {
		if (wakeLock != null)
			wakeLock.release();
		wakeLock = null;
	}

	class GetNotification extends AsyncTask<Void, Void, Void> {
		String notificationURL = "";

		public GetNotification() {

		}

		public GetNotification(String notificationURL) {
			this.notificationURL = notificationURL;
		}

		@Override
		protected Void doInBackground(Void... params) {
			ServiceHandler sh = new ServiceHandler();
			String jsonStr = sh.makeServiceCall(this.notificationURL,
					ServiceHandler.GET);
			System.out.println("jsonStr from notification " + jsonStr);
			return null;
		}

	}
}