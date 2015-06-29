package smart.services.start;

import org.json.JSONException;
import org.json.JSONObject;

import smart.services.handler.DataBaseHandler;
import smart.services.handler.JSONParser;
import smart.services.model.Member;
import smart.services.model.SignUpInfo;

import info.androidhive.slidingmenu.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class SignIn extends Activity {
	EditText phoneNumberET, passWordET;
	String phoneNumberST, passWordST;
	String eCode, eDesc, userId, email, loyaltyId, msisdn, name, carId, addr;
	TextView errorMessage, errorMessage0;
	private ProgressDialog pDialog;
	private static String URL = "http://smarty-trioplus.rhcloud.com/supportCenter/userLogin?";
	JSONParser jP = new JSONParser();
	DataBaseHandler memberDb;
	String regId = "";
	Intent in;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sign_in);
		memberDb = new DataBaseHandler(this.getApplicationContext());
		in = getIntent();
		regId = in.getStringExtra("regId");
	}

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

	public void signIn(View v) {
		System.out.println("regd >>" + regId);
		phoneNumberET = (EditText) findViewById(R.id.phoneNumber);
		passWordET = (EditText) findViewById(R.id.passWord);
		errorMessage = (TextView) findViewById(R.id.errorTxt);
		errorMessage0 = (TextView) findViewById(R.id.errorTxt0);

		phoneNumberST = phoneNumberET.getText().toString();
		passWordST = passWordET.getText().toString();
		errorMessage0.setText("");
		errorMessage.setText("");
		if (phoneNumberST.equals("") || passWordST.equals("")) {
			if (phoneNumberST.equals("")) {
				errorMessage.setText("please enter your phone Number");
			}
			if (passWordST.equals("")) {
				errorMessage0.setText("please enter your password");
			}
		} else {
			if (isConnectingToInternet()) {
				new signIn(phoneNumberST, passWordST, regId).execute();
			} else {
				errorMessage.setText("Check your internet connection");
			}
		}
	}

	public class signIn extends AsyncTask<Void, Void, Void> {
		String phoneNumber = "";
		String passWord = "";
		String regId = "";

		public signIn() {
		}

		public signIn(String phoneNumber, String passWord, String regId) {
			this.phoneNumber = phoneNumber;
			this.passWord = passWord;
			this.regId = regId;
		}

		@Override
		public void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog(SignIn.this);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// Creating service handler class instance
			String url = URL + "msisdn=" + this.phoneNumber + "&password="
					+ this.passWord + "&notificationId=" + this.regId;
			try {
				JSONObject jsonObj = jP.getJSONFromUrl(url);

				eCode = jsonObj.getString("eCode");
				eDesc = jsonObj.getString("eDesc");
				userId = jsonObj.getString("userId");
				email = jsonObj.getString("email");
				msisdn = jsonObj.getString("msisdn");
				name = jsonObj.getString("name");
				carId = jsonObj.getString("CarId");

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// Dismiss the progress dialog
			if (eCode.equals("0")) {// eCode.equals("0")
				memberDb.deleteAllUsers();
				memberDb.addUser(new Member(userId, email, carId, msisdn, name));
				if (memberDb.getRegistersCount() > 0) {
					addr = memberDb.getRegister(1).getAddress();
				}
				System.out.println("Address >>>>>>>>>" + addr);
				memberDb.deleteAllRegisters();
				memberDb.addRegister(new SignUpInfo("", this.phoneNumber, name,
						"", "", addr, ""));
				Intent i = new Intent(SignIn.this,
						smart.services.activity.MainActivity.class);
				startActivity(i);
				overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
				finish();
			}
			errorMessage.setText(eDesc);
			if (pDialog.isShowing()) {
				pDialog.dismiss();
			}
		}
	}

	public void signUp(View v) {
		Intent in = getIntent();
		regId = in.getStringExtra("regId");

		Intent intent = new Intent(SignIn.this,
				smart.services.start.VerificationCode.class);
		intent.putExtra("regId", regId);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
	}

	public void forget(View v) {
		Intent intent = new Intent(SignIn.this,
				smart.services.start.ForgetPassWord.class);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (pDialog != null) {
			pDialog.dismiss();
			pDialog = null;
		}
	}
}
