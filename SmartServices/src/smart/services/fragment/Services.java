package smart.services.fragment;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import smart.services.adapter.ServiceAdapter;
import smart.services.functions.CarFunctions;
import smart.services.handler.DataBaseHandler;
import smart.services.handler.JSONParser;
import smart.services.model.Service;
import smart.services.model.ServiceNumber;
import smart.services.model.Setting;
import info.androidhive.slidingmenu.R;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Services extends Fragment {
	private static String URL = "http://smarty-trioplus.rhcloud.com/supportCenter/listServices?userId=";
	ArrayList<HashMap<String, String>> servicesList;
	ListView lv;
	SwipeRefreshLayout swipeView;
	private ProgressDialog pDialog;
	JSONParser jP = new JSONParser();
	String serviceDTOs, servicesNumbers, serviceId, descAr, descEn, id, number,
			titleAr, titleEn;
	private DataBaseHandler databaseHandler;
	private CarFunctions carFunctions;
	private Setting setting;
	private TextView servicesTV;

	public Services() {
	}

	@SuppressLint("InlinedApi") @SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		databaseHandler = new DataBaseHandler(getActivity());
		carFunctions = new CarFunctions(getActivity());
		setting = databaseHandler.getSetting();
		
		View view = inflater.inflate(R.layout.services, container, false);
		
		servicesTV = (TextView) view.findViewById(R.id.servicesTV);
		swipeView = (SwipeRefreshLayout) view
				.findViewById(R.id.swipeServices);
		lv = (ListView) view.findViewById(R.id.servicesLV);

		if (setting.getDuration() == 0) {
			servicesTV.setText(getResources().getString(R.string.services_en));
		} else {
			servicesTV.setText(getResources().getString(R.string.services_ar));
		}
		
		if (databaseHandler.getServicesCount() == 0) {
			if (carFunctions.isConnectingToInternet()) {
				new ServiceList(databaseHandler.getUser(1).getUserId()).execute();
			} else {
				Toast.makeText(getActivity(), "Check your internet connection",
						Toast.LENGTH_LONG).show();
			}
		} else {
			ServiceAdapter serviceAdapter = new ServiceAdapter(getActivity(),
					R.layout.service_adapter, databaseHandler.getAllService());
			lv.setAdapter(serviceAdapter);
		}

		swipeView
				.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
					@Override
					public void onRefresh() {
						swipeView.setRefreshing(true);
						new ServiceList(databaseHandler.getUser(1).getUserId()).execute();
					}
				});
		swipeView.setColorScheme(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		return view;
	}

	public class ServiceList extends AsyncTask<Void, Void, Void> {
		String userId = "";

		public ServiceList() {
		}

		public ServiceList(String userId) {
			this.userId = userId;
		}

		@Override
		public void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
//			servicesList.clear();
			pDialog = new ProgressDialog(getActivity());
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// Creating service handler class instance
			String url = URL + this.userId;
			try {
				databaseHandler.deleteAllService();
				JSONObject jsonObj = jP.getJSONFromUrl(url);
				Log.d("Response: ", "> " + jsonObj);

				serviceDTOs = jsonObj.getString("serviceDTOs");
				JSONArray serviceJsonObject = new JSONArray(serviceDTOs);
				for (int i = 0; i < serviceJsonObject.length(); i++) {
					JSONObject c = serviceJsonObject.getJSONObject(i);

					serviceId = c.getString("serviceId");
					descAr = c.getString("descAr");
					descEn = c.getString("descEn");
					titleAr = c.getString("titleAr");
					titleEn = c.getString("titleEn");

					// Services numbers json array
					servicesNumbers = c.getString("servicesNumbers");
					JSONArray serviceNumbersJsonObject = new JSONArray(
							servicesNumbers);
					for (int j = 0; j < serviceNumbersJsonObject.length(); j++) {
						JSONObject cursor = serviceNumbersJsonObject
								.getJSONObject(j);

						String serviceNumberId = cursor.getString("id");
						String serviceNumber = cursor.getString("number");

						// Save serviceNumber in database
						ServiceNumber serviceNumberRow = new ServiceNumber();
						serviceNumberRow.setServiceNumberId(serviceNumberId);
						serviceNumberRow.setServiceNumber(serviceNumber);
						serviceNumberRow.setServiceId(serviceId);

						databaseHandler.addServiceNumber(serviceNumberRow);

					}

					// Save service in database
					Service serviceRow = new Service();
					serviceRow.setServiceId(serviceId);
					serviceRow.setDescAr(descAr);
					serviceRow.setDescEn(descEn);
					serviceRow.setTitleAr(titleAr);
					serviceRow.setTitleEn(titleEn);

					databaseHandler.addService(serviceRow);

					// HashMap<String, String> service = new HashMap<String,
					// String>();
					// service.put("serviceId", serviceId);
					// service.put("descAr", descAr);
					// service.put("descEn", descEn);
					// service.put("titleAr", titleAr);
					// service.put("titleEn", titleEn);
					//
					// servicesList.add(service);
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (pDialog.isShowing()) {
				pDialog.dismiss();
			}
			swipeView.setRefreshing(false);
			// Toast.makeText(getActivity(), servicesList.size()+"",
			// Toast.LENGTH_LONG).show();
			//
			// ListAdapter adapter = new SimpleAdapter(getActivity(),
			// servicesList, R.layout.single_service_item, new String[] {
			// "serviceId", "descEn", "titleEn" }, new int[] {
			// R.id.serviceId, R.id.descEn, R.id.titleEn });

			ServiceAdapter serviceAdapter = new ServiceAdapter(getActivity(),
					R.layout.service_adapter, databaseHandler.getAllService());
			lv.setAdapter(serviceAdapter);
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
