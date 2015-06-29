package smart.services.GCM.notification;

import android.content.Context;

public class GCMBroadcastReceiver extends
		com.google.android.gcm.GCMBroadcastReceiver {

	@Override
	protected String getGCMIntentServiceClassName(Context context) {

		return "smart.services.GCM.notification.GCMIntentService";
	}
}