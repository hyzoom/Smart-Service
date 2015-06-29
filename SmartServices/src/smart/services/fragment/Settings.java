package smart.services.fragment;

import org.json.JSONException;
import org.json.JSONObject;

import smart.services.functions.CarFunctions;
import smart.services.handler.DataBaseHandler;
import smart.services.handler.JSONParser;
import smart.services.model.Member;
import smart.services.model.Setting;
import info.androidhive.slidingmenu.R;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Settings extends Fragment {
	Button logOut, updateUserInfo, save;
	TextView settingsTV, updateProfileTV, phoneNumberUpd, errorTxtUpt;
	EditText nameUpd, eMailUpd, addressETUpd;
	private DataBaseHandler databaseHandler;
	private CarFunctions carFunctions;
	private Setting setting;
	private ProgressDialog pDialog;
	private static String URL = "http://smarty-trioplus.rhcloud.com/supportCenter/updateUserProfile?";
	JSONParser jP = new JSONParser();
	String eCode, eDesc;

	public Settings() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		databaseHandler = new DataBaseHandler(getActivity());
		setting = databaseHandler.getSetting();
		carFunctions = new CarFunctions(getActivity());

		View view = inflater.inflate(R.layout.settings, container, false);
		settingsTV = (TextView) view.findViewById(R.id.settingsTV);
		logOut = (Button) view.findViewById(R.id.logOut);
		updateUserInfo = (Button) view.findViewById(R.id.updateInfo);

		if (setting.getDuration() == 1) {
			settingsTV.setText(getResources().getString(R.string.settings_ar));
			updateUserInfo.setText(getResources().getString(
					R.string.update_user_info_ar));
			logOut.setText(getResources().getString(R.string.log_out_ar));
		} else {
			settingsTV.setText(getResources().getString(R.string.settings_en));
			updateUserInfo.setText(getResources().getString(
					R.string.update_user_info_en));
			logOut.setText(getResources().getString(R.string.log_out_en));
		}

		logOut.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showAlertDialog();
			}
		});
		updateUserInfo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showUpdateDialog();
			}
		});
		return view;
	}

	public void showAlertDialog() {
		final TextView input = new TextView(getActivity());
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				getActivity());
		alertDialogBuilder.setTitle("Log Out?");

		if (setting.getDuration() == 1) {
			alertDialogBuilder
					.setCancelable(false)
					.setPositiveButton(getResources().getString(R.string.yes_ar),
							new DialogInterface.OnClickListener() {
								@SuppressLint("DefaultLocale")
								public void onClick(DialogInterface dialog,
										int id) {
									databaseHandler.deleteAllUsers();
									databaseHandler.deleteAllCars();
									Intent i = new Intent(getActivity(),
											smart.services.start.SignIn.class);
									startActivity(i);
									getActivity().overridePendingTransition(
											R.anim.slide_up, R.anim.slide_down);
									getActivity().finish();
								}
							})
					.setNegativeButton(getResources().getString(R.string.cancel_ar),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							});

			// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();

			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.MATCH_PARENT);
			input.setText(getResources().getString(R.string.sure_ar));
			input.setGravity(Gravity.CENTER_VERTICAL
					| Gravity.CENTER_HORIZONTAL);
			input.setTextSize(20);
			input.setLayoutParams(lp);
			alertDialog.setView(input);
			alertDialog.show();
		} else {
			alertDialogBuilder
					.setCancelable(false)
					.setPositiveButton("Ok",
							new DialogInterface.OnClickListener() {
								@SuppressLint("DefaultLocale")
								public void onClick(DialogInterface dialog,
										int id) {
									databaseHandler.deleteAllUsers();
									databaseHandler.deleteAllCars();
									Intent i = new Intent(getActivity(),
											smart.services.start.SignIn.class);
									startActivity(i);
									getActivity().overridePendingTransition(
											R.anim.slide_up, R.anim.slide_down);
									getActivity().finish();
								}
							})
					.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							});

			// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();

			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.MATCH_PARENT);
			input.setText("Are you sure?");
			input.setGravity(Gravity.CENTER_VERTICAL
					| Gravity.CENTER_HORIZONTAL);
			input.setTextSize(20);
			input.setLayoutParams(lp);
			alertDialog.setView(input);
			alertDialog.show();
		}

	}

	public void showUpdateDialog() {
		final Dialog dialog = new Dialog(getActivity());
		dialog.setContentView(R.layout.edit_user_info);
		dialog.setCancelable(true);

		updateProfileTV = (TextView) dialog.findViewById(R.id.udpateProfileTV);
		phoneNumberUpd = (TextView) dialog.findViewById(R.id.phoneNumberUpd);
		errorTxtUpt = (TextView) dialog.findViewById(R.id.errorTxtUpt);
		nameUpd = (EditText) dialog.findViewById(R.id.nameUpd);
		eMailUpd = (EditText) dialog.findViewById(R.id.eMailUpd);
		addressETUpd = (EditText) dialog.findViewById(R.id.addressETUpd);
		save = (Button) dialog.findViewById(R.id.saveUpt);

		phoneNumberUpd.setText(databaseHandler.getUser(1).getMsisdn());
		nameUpd.setText(databaseHandler.getUser(1).getName());
		eMailUpd.setText(databaseHandler.getUser(1).getEmail());
		addressETUpd.setText(databaseHandler.getRegister(1).getAddress());
		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Hit here to update
				if (carFunctions.isConnectingToInternet()) {
					new UpdateProfile(databaseHandler.getUser(1).getMsisdn(),
							eMailUpd.getText().toString(), nameUpd.getText()
									.toString(), addressETUpd.getText()
									.toString()).execute();
					// getActivity().finish();
					// Intent intent = new Intent(getActivity(), getActivity()
					// .getClass());
					// intent.putExtra("fragNum", "0");
					// getActivity().startActivity(intent);
				} else {
					Toast.makeText(getActivity(),
							"Check your internet connection", Toast.LENGTH_LONG)
							.show();

				}
				dialog.dismiss();
			}
		});

		if (setting.getDuration() == 1) {
			dialog.setTitle(getResources()
					.getString(R.string.update_profile_ar));
			updateProfileTV.setText(getResources().getString(
					R.string.update_profile_ar));
			save.setText(getResources().getString(R.string.save_ar));
		} else {
			dialog.setTitle(getResources()
					.getString(R.string.update_profile_en));
			updateProfileTV.setText(getResources().getString(
					R.string.update_profile_en));
		}

		dialog.show();
	}

	public class UpdateProfile extends AsyncTask<Void, Void, Void> {
		String phoneNumber = "";
		String email = "";
		String name = "";
		String address = "";

		public UpdateProfile() {
		}

		public UpdateProfile(String phoneNumber, String email, String name,
				String address) {
			this.phoneNumber = phoneNumber;
			this.email = email;
			this.name = name;
			this.address = address;
		}

		@Override
		public void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog(getActivity());
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// Creating service handler class instance
			String url = URL + "msisdn=" + this.phoneNumber + "&email="
					+ this.email + "&name=" + this.name + "&address="
					+ this.address;
			try {
				JSONObject jsonObj = jP.getJSONFromUrl(url);
				eCode = jsonObj.getString("eCode");
				eDesc = jsonObj.getString("eDesc");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// Dismiss the progress dialog
			if (eCode.equals("0")) {// eCode.equals("0")
				Member memb = new Member(databaseHandler.getUser(1).getUserId()
						+ "", eMailUpd.getText().toString(), "", phoneNumberUpd
						.getText().toString(), nameUpd.getText().toString());
				databaseHandler.updateUserInfo(memb, databaseHandler.getUser(1)
						.getId() + "");
				databaseHandler.updateRegisterAddress(addressETUpd.getText()
						.toString(), databaseHandler.getUser(1).getId() + "");
				Toast.makeText(getActivity(), "Profile updated",
						Toast.LENGTH_LONG).show();

			} else {
				errorTxtUpt.setText(eDesc);
			}
			if (pDialog.isShowing()) {
				pDialog.dismiss();
			}
		}
	}

	public void showUpdateAlertDialog() {
		databaseHandler = new DataBaseHandler(getActivity());
		final TextView input = new TextView(getActivity());
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				getActivity());
		alertDialogBuilder.setTitle("Update Info");

		alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					@SuppressLint("DefaultLocale")
					public void onClick(DialogInterface dialog, int id) {

						dialog.dismiss();
					}
				})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		input.setText("Are you sure?");
		input.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
		input.setTextSize(20);
		input.setLayoutParams(lp);
		alertDialog.setView(input);
		alertDialog.show();
	}
}
