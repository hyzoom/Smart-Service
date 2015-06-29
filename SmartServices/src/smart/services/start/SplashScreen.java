package smart.services.start;

import com.google.android.gcm.GCMRegistrar;

import smart.services.GCM.notification.Config;
import smart.services.GCM.notification.Controller;
import smart.services.handler.DataBaseHandler;
import android.app.ActionBar;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import info.androidhive.slidingmenu.R;

public class SplashScreen extends Activity {
	DataBaseHandler memberDB;
	protected boolean _active = true;
	protected int _splashTime = 10;
	Thread splashTread;
	private boolean stop = false;
	String regId = "";
	Controller aController;
	AsyncTask<Void, Void, Void> mRegisterTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen);
		ActionBar actionBar = getActionBar();
		actionBar.hide();
		memberDB = new DataBaseHandler(this);
		registerGCM();
		splashTread = new Thread() {
			@Override
			public void run() {
				try {
					int waited = 0;
					while (_active && (waited < _splashTime)) {
						sleep(1500);
						if (_active) {
							waited += 100;
						}
					}

				} catch (InterruptedException e) {
				} finally {
					if (!stop) {
						System.out.println("signUpDB.getTriplesCount()"
								+ memberDB.getRegistersCount());
						System.out.println("memberDB.getTriplesCount()"
								+ memberDB.getUsersCount());
						if (memberDB.getRegistersCount() > 0
								&& memberDB.getUsersCount() > 0) {
							Intent i = new Intent(
									SplashScreen.this,
									smart.services.activity.MainActivity.class);
							startActivity(i);
							overridePendingTransition(R.anim.slide_up,
									R.anim.slide_down);
						} else if (memberDB.getRegistersCount() == 0
								|| memberDB.getUsersCount() > 0) {
							Intent i = new Intent(SplashScreen.this,
							// smart.services.slidingmenu.classes.MainActivity.class);
									smart.services.start.SignIn.class);
							i.putExtra("regId", getRegId());
							startActivity(i);
							overridePendingTransition(R.anim.slide_up,
									R.anim.slide_down);
						} else if (memberDB.getUsersCount() == 0) {
							Intent i = new Intent(SplashScreen.this,
							// smart.services.slidingmenu.classes.MainActivity.class);
									smart.services.start.SignIn.class);
							i.putExtra("regId", getRegId());
							startActivity(i);
							overridePendingTransition(R.anim.slide_up,
									R.anim.slide_down);
						}

						finish();
					} else
						finish();
				}
			}
		};
		splashTread.start();

	}

	public void registerGCM() {
		aController = (Controller) getApplicationContext();
		if (!aController.isConnectingToInternet()) {
			Toast.makeText(getApplicationContext(),
					"Internet Connection Error", Toast.LENGTH_LONG).show();
			return;
		}
		GCMRegistrar.checkDevice(this);
		GCMRegistrar.checkManifest(this);
		registerReceiver(mHandleMessageReceiver, new IntentFilter(
				Config.DISPLAY_MESSAGE_ACTION));

		// Get GCM registration id
		regId = GCMRegistrar.getRegistrationId(this);
		if (regId.equals("")) {
			// Register with GCM
			GCMRegistrar.register(this, Config.GOOGLE_SENDER_ID);
		} else {
			if (GCMRegistrar.isRegisteredOnServer(this)) {
				final Context context = this;
				aController.register(context, regId);
			} else {
				final Context context = this;
				mRegisterTask = new AsyncTask<Void, Void, Void>() {
					@Override
					protected Void doInBackground(Void... params) {
						aController.register(context, regId);
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						mRegisterTask = null;
					}
				};
				mRegisterTask.execute(null, null, null);
			}
		}
	}

	public String getRegId() {
		return regId;
	}

	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			aController.acquireWakeLock(getApplicationContext());
			aController.releaseWakeLock();
		}
	};

	@Override
	protected void onDestroy() {
		// Cancel AsyncTask
		if (mRegisterTask != null) {
			mRegisterTask.cancel(true);
		}
		try {
			unregisterReceiver(mHandleMessageReceiver);
			GCMRegistrar.onDestroy(this);
		} catch (Exception e) {
			Log.e("UnRegister Receiver Error", "> " + e.getMessage());
		}
		super.onDestroy();
	}
}
