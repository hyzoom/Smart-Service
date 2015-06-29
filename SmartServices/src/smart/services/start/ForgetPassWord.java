package smart.services.start;

import org.json.JSONException;
import org.json.JSONObject;

import smart.services.fragment.AddCar.AddCars;
import smart.services.handler.DataBaseHandler;
import smart.services.handler.JSONParser;
import smart.services.model.Member;
import smart.services.model.SignUpInfo;
import info.androidhive.slidingmenu.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class ForgetPassWord extends Activity {
	EditText phoneNumET;
	String phoneNumST;
	TextView errorMessage;
	DataBaseHandler signUpDb;
	private ProgressDialog pDialog;
	private static String URL = "http://smarty-trioplus.rhcloud.com/supportCenter/forgetPassword?";
	JSONParser jP = new JSONParser();
	String eCode, eDesc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forget_password);
	}

	public void getPassWord(View v) {
		signUpDb = new DataBaseHandler(this.getApplicationContext());
		phoneNumET = (EditText) findViewById(R.id.phoneNum);
		phoneNumST = phoneNumET.getText().toString();
		errorMessage = (TextView) findViewById(R.id.errorTxt);
		new forgetPassWord(phoneNumST).execute();
	}

	public class forgetPassWord extends AsyncTask<Void, Void, Void> {
		String phoneNumber = "";
		String passWord = "";

		public forgetPassWord() {
		}

		public forgetPassWord(String phoneNumber) {
			this.phoneNumber = phoneNumber;
		}

		@Override
		public void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog(ForgetPassWord.this);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// Creating service handler class instance
			String url = URL + "msisdn=" + this.phoneNumber;
			try {
				JSONObject jsonObj = jP.getJSONFromUrl(url);

				eCode = jsonObj.getString("eCode");
				eDesc = jsonObj.getString("eDesc");

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
			Intent intent = new Intent(ForgetPassWord.this,
					smart.services.start.SetNewPassword.class);
			startActivity(intent);
			overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
			if (eCode.equals("0")) {

			}
			if (pDialog.isShowing()) {
				pDialog.dismiss();
			}
		}
	}
}
