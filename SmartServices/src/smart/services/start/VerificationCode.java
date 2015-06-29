package smart.services.start;

import org.json.JSONException;
import org.json.JSONObject;

import smart.services.handler.JSONParser;

import info.androidhive.slidingmenu.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class VerificationCode extends Activity {
	EditText phoneNum, codeET;
	String phoneNumString = "", codeST = "";
	TextView errorMessage, codeErrorTxt;
	private ProgressDialog pDialog;
	private static String URL = "http://smarty-trioplus.rhcloud.com/supportCenter/verifyUser?msisdn=";
	String eCode = "", eDesc = "";
	JSONParser jP = new JSONParser();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.verification_code);
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

	public void getCode(View v) {
		phoneNum = (EditText) findViewById(R.id.phoneNum);
		errorMessage = (TextView) findViewById(R.id.errorTxt);
		phoneNumString = phoneNum.getText().toString();

		if (phoneNumString.equals("")) {
			errorMessage.setText("please enter your Phone number");
		} else {
			if (isConnectingToInternet()) {
				new GetCode(phoneNumString).execute();
			} else {
				errorMessage.setText("Check your internet connection");
			}
		}
	}

	public void signUp(View v) {
		Intent in = getIntent();
		String regId = in.getStringExtra("regId");
		phoneNum = (EditText) findViewById(R.id.phoneNum);
		codeET = (EditText) findViewById(R.id.codeET);
		codeErrorTxt = (TextView) findViewById(R.id.codeErrorTxt);
		codeST = codeET.getText().toString();
		phoneNumString = phoneNum.getText().toString();
		if (phoneNumString.equals("") || codeST.equals("")) {
			if (phoneNumString.equals("")) {
				codeErrorTxt.setText("please enter your Phone number");
			}
			if (codeST.equals("")) {
				codeErrorTxt.setText("please enter received code");
			}
		} else {
			Intent intent = new Intent(VerificationCode.this,
					smart.services.start.SignUp.class);
			intent.putExtra("regId", regId);
			intent.putExtra("phoneNumber", phoneNumString);
			intent.putExtra("code", codeST);
			startActivity(intent);
			overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
		}
	}

	//
	public class GetCode extends AsyncTask<Void, Void, Void> {
		String phoneNumber = "";

		public GetCode() {
		}

		public GetCode(String phoneNumber) {
			this.phoneNumber = phoneNumber;
		}

		@Override
		public void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog(VerificationCode.this);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// Creating service handler class instance
			String url = URL + this.phoneNumber;
			try {
				JSONObject jsonObj = jP.getJSONFromUrl(url);
				eCode = jsonObj.getString("eCode");
				eDesc = jsonObj.getString("eDesc");
				Log.d("eCode is", eDesc);
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
