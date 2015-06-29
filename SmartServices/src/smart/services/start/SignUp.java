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

public class SignUp extends Activity {
	TextView phoneNumberET;
	EditText nameET, eMailET, passWordET, passWordConfET, addressET;
	String verificaionCodeST, phoneNumberST, nameST, eMailST, passWordST,
			passWordConfST, addressST;
	String eCode, eDesc, userId, email, loyaltyId, msisdn, name, userCars,
			regId;
	TextView errorMessage;
	private ProgressDialog pDialog;
	private static String URL = "http://smarty-trioplus.rhcloud.com/supportCenter/registerUser?";
	JSONParser jP = new JSONParser();
	DataBaseHandler memberDb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sign_up);
		memberDb = new DataBaseHandler(this.getApplicationContext());
		phoneNumberET = (TextView) findViewById(R.id.phoneNumber);
		Intent in = getIntent();
		phoneNumberST = in.getStringExtra("phoneNumber");
		phoneNumberET.setText(phoneNumberST);
		verificaionCodeST = in.getStringExtra("code");
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

	public void getVerifCode(View v) {
		Intent intent = new Intent(SignUp.this,
				smart.services.start.VerificationCode.class);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
		finish();
	}

	public void signUp(View v) {
		System.out.println("regId " + regId);
		nameET = (EditText) findViewById(R.id.name);
		eMailET = (EditText) findViewById(R.id.eMail);
		passWordET = (EditText) findViewById(R.id.passWord);
		passWordConfET = (EditText) findViewById(R.id.passWordConf);
		addressET = (EditText) findViewById(R.id.addressET);
		errorMessage = (TextView) findViewById(R.id.errorTxt);

		phoneNumberST = phoneNumberET.getText().toString();
		// phoneNumberST = phoneNumberST.replaceAll(" ", "");
		nameST = nameET.getText().toString();
		// nameST = nameST.replaceAll(" ", "");
		eMailST = eMailET.getText().toString();
		// eMailST = eMailST.replaceAll(" ", "");
		passWordST = passWordET.getText().toString();
		// passWordST = passWordST.replaceAll(" ", "");
		passWordConfST = passWordConfET.getText().toString();
		addressST = addressET.getText().toString();

		if (!passWordST.equals(passWordConfST)) {
			errorMessage.setText("Password doesnt match");
		} else {
			if (isConnectingToInternet()) {
				new signUp(phoneNumberST, passWordST, verificaionCodeST,
						eMailST, nameST, addressST, regId).execute();

			} else {
				errorMessage.setText("Check your internet connection");
			}
		}
	}

	public class signUp extends AsyncTask<Void, Void, Void> {
		String phoneNumber = "";
		String passWord = "";
		String verificationKey = "";
		String email = "";
		String name = "";
		String address = "";
		String regId = "";

		public signUp() {
		}

		// msisdn=01003365705&password=test@123&verificationKey=c38b78ad&email=test@123.com&name=ehab
		public signUp(String phoneNumber, String passWord,
				String verificationKey, String email, String name,
				String address, String regId) {
			this.phoneNumber = phoneNumber;
			this.passWord = passWord;
			this.verificationKey = verificationKey;
			this.email = email;
			this.name = name;
			this.address = address;
			this.regId = regId;
		}

		@Override
		public void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog(SignUp.this);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// Creating service handler class instance
			String url = URL + "msisdn=" + this.phoneNumber + "&password="
					+ this.passWord + "&verificationKey="
					+ this.verificationKey + "&email=" + this.email + "&name="
					+ this.name + "&address=" + this.name + "&notificationId="
					+ this.regId;
			// url = url.replaceAll(" ", "");
			try {
				JSONObject jsonObj = jP.getJSONFromUrl(url);

				eCode = jsonObj.getString("eCode");
				eDesc = jsonObj.getString("eDesc");
				userId = jsonObj.getString("userId");
				email = jsonObj.getString("email");
				loyaltyId = jsonObj.getString("loyaltyId");
				msisdn = jsonObj.getString("msisdn");
				name = jsonObj.getString("name");
				userCars = jsonObj.getString("userCars");

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (eCode.equals("0")) {// eCode.equals("0")
				memberDb.deleteAllRegisters();
				memberDb.addRegister(new SignUpInfo(this.verificationKey,
						this.phoneNumber, this.name, this.email, this.passWord,
						this.address, ""));
				memberDb.deleteAllUsers();
				memberDb.addUser(new Member(userId, email, loyaltyId, msisdn,
						name));
				Intent i = new Intent(SignUp.this,
						smart.services.activity.MainActivity.class);
				startActivity(i);
				overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
				finish();
			} else
				errorMessage.setText(eDesc);
			if (pDialog.isShowing()) {
				pDialog.dismiss();
			}
		}
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
