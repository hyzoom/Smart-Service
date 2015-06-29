package smart.services.fragment;

import java.util.ArrayList;
import java.util.HashMap;

import smart.services.adapter.ManageCarsAdapter;
import smart.services.functions.CarFunctions;
import smart.services.handler.DataBaseHandler;
import smart.services.handler.JSONParser;
import smart.services.model.CarTest;
import smart.services.model.Setting;

import info.androidhive.slidingmenu.R;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ManageCarInfo extends Fragment {
	ListView lv;
	SwipeRefreshLayout swipeView;
	ArrayList<HashMap<String, String>> carsList = new ArrayList<HashMap<String, String>>();
	ArrayList<CarTest> carList = new ArrayList<CarTest>();
	JSONParser jP = new JSONParser();
	String carBrand, carModel, year, carColor, chase;
	DataBaseHandler dataBaseHandler;
	Button delete;
	private TextView carsListTitleTV;
	
	private ProgressDialog progress;
	private CarFunctions carFunctions;
	private Setting setting;

	public ManageCarInfo() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		dataBaseHandler = new DataBaseHandler(getActivity());
		carFunctions = new CarFunctions(getActivity());
		setting = dataBaseHandler.getSetting();

		View rootView = inflater.inflate(R.layout.manage_car_info, container,
				false);

		carsListTitleTV = (TextView) rootView.findViewById(R.id.carsListTitleTV);
		lv = (ListView) rootView.findViewById(R.id.carsTestList);
		swipeView = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeOffersCarInfo);
		
		if (setting.getDuration() == 0) {
			carsListTitleTV.setText(getResources().getString(R.string.car_list_title_en));
		} else {
			carsListTitleTV.setText(getResources().getString(R.string.car_list_title_ar));
		}

		if (dataBaseHandler.getCarsCount() == 0) {
			if (carFunctions.isConnectingToInternet()) {
				new CarsAsyncTask().execute();
			} else {
				Toast.makeText(getActivity(), "Check your internet connection",
						Toast.LENGTH_LONG).show();
			}
		} else {
			carsList = new ArrayList<HashMap<String, String>>();
			list();
		}
		
		return rootView;
	}

	@SuppressLint("InlinedApi") 
	@SuppressWarnings("deprecation")
	public void list() {

		carsList = dataBaseHandler.getUserCars(dataBaseHandler.getUser(1)
				.getUserId());
		// dBCT.deleteAllCars();
		System.out.println("carsList " + carsList);

		ManageCarsAdapter adapter = new ManageCarsAdapter(getActivity(),
				R.layout.car_list_adapter, dataBaseHandler.getAllCarTest());

		lv.setAdapter(adapter);

		swipeView
				.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
					@Override
					public void onRefresh() {
						swipeView.setRefreshing(true);
						new CarsAsyncTask().execute();
						swipeView.setRefreshing(false);
					}
				});
		swipeView.setColorScheme(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);

	}

	private class CarsAsyncTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progress = ProgressDialog.show(ManageCarInfo.this.getActivity(),
					"", "Loading...");
		}

		@Override
		protected Void doInBackground(Void... params) {
			carFunctions.saveJSONUserCars();
			return null;
		}

		@Override
		protected void onPostExecute(Void aVoid) {
			super.onPostExecute(aVoid);
			if (progress.isShowing()) {
				progress.dismiss();
			}

			carsList = new ArrayList<HashMap<String, String>>();
			list();
		}
	}
}
