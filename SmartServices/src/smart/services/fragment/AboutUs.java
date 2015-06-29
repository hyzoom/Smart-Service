package smart.services.fragment;

import smart.services.functions.CarFunctions;
import smart.services.handler.DataBaseHandler;
import smart.services.model.Setting;
import info.androidhive.slidingmenu.R;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class AboutUs extends Fragment {
	CarFunctions carFunctions;
	DataBaseHandler databaseHandler;
	private ProgressDialog pDialog;
	
	private TextView aboutUsTV;
	private Setting setting;

	public AboutUs() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		carFunctions = new CarFunctions(getActivity());
		databaseHandler = new DataBaseHandler(getActivity());
		setting = databaseHandler.getSetting();
		
		View view = inflater.inflate(R.layout.about_us, container, false);
		aboutUsTV = (TextView) view.findViewById(R.id.aboutUsTv);
		
		if (setting.getDuration() == 0) {
			aboutUsTV.setText(getResources().getString(R.string.about_us_en));
		} else {
			aboutUsTV.setText(getResources().getString(R.string.about_us_ar));
		}
		
		if (databaseHandler.getBrandsCount() == 0) {
			if (carFunctions.isConnectingToInternet()) {
				new LoadData(databaseHandler.getUser(1).getUserId()).execute();
			} else {
				Toast.makeText(getActivity(), "Check your internet connection",
						Toast.LENGTH_LONG).show();
			}
		}

		return view;
	}

	public class LoadData extends AsyncTask<Void, Void, Void> {
		String userId = "";

		public LoadData() {
		}

		public LoadData(String userId) {
			this.userId = userId;
		}

		@Override
		public void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(getActivity());
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// Creating service handler class instance

			carFunctions.saveJSONBrandAndModels();
			carFunctions.saveJSONColors();
			carFunctions.saveJSONInsuranceCompany();

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (pDialog.isShowing()) {
				pDialog.dismiss();
			}
		}
	}

}
