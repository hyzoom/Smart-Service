package smart.services.fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import smart.services.fragment.AddCar.AddCars;
import smart.services.fragment.AddCar.CarColor;
import smart.services.fragment.AddCar.CarModel;
import smart.services.handler.DataBaseHandler;
import smart.services.handler.JSONParser;
import smart.services.model.Brand;
import smart.services.model.CarTest;
import smart.services.model.Color;
import smart.services.model.Model;
import info.androidhive.slidingmenu.R;
import android.app.Dialog;
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
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Maintenance extends Fragment {
	TextView brandMainTV, brandMainIdTV, modelMainTV, modelIdMainTV,
			kiloMainTV, kiloMainIdTV;
	private DataBaseHandler dBCT;
	private Dialog dialog;
	ListView lv;
	SwipeRefreshLayout swipeView;
	ArrayList<HashMap<String, String>> carsBrandList, carsModelList, kilosList,
			mainatainaceList;
	Button submit;
	String finalModelId, finalKilos, eCode, eDesc, kilos,
			maintainceServiceTypes, mainatainaceTypeEn, mainatainaceTypeAr,
			mainatainaceDescEn, mainatainaceDescAr;
	String kiloListURL = "http://smarty-trioplus.rhcloud.com/supportCenter/listMainatianceKilo";
	String maintenanceURL = "http://smarty-trioplus.rhcloud.com/supportCenter/listMainatiance?";
	private ProgressDialog progress;
	JSONParser jP = new JSONParser();
	List<String> myList;
	private ProgressDialog pDialog;

	public Maintenance() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater
				.inflate(R.layout.maintenance, container, false);
		brandMainTV = (TextView) rootView.findViewById(R.id.brandMainTV);
		brandMainIdTV = (TextView) rootView.findViewById(R.id.brandMainIdTV);
		modelMainTV = (TextView) rootView.findViewById(R.id.modelMainTV);
		modelIdMainTV = (TextView) rootView.findViewById(R.id.modelIdMainTV);
		kiloMainTV = (TextView) rootView.findViewById(R.id.kiloMainTV);
		dialog = new Dialog(getActivity());
		dBCT = new DataBaseHandler(getActivity());
		submit = (Button) rootView.findViewById(R.id.submitMain);
		init();
		submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				addMaintenance();
			}
		});
		return rootView;
	}

	public void addMaintenance() {
		finalModelId = modelIdMainTV.getText().toString();
		finalKilos = kiloMainTV.getText().toString();
		if (brandMainTV.getText().toString().equals("")
				|| finalModelId.equals("") || finalKilos.equals("")) {
			if (brandMainTV.getText().toString().equals("")) {
				Toast.makeText(getActivity(), "please enter your Car Brand",
						Toast.LENGTH_SHORT).show();
			} else if (finalModelId.equals("")) {
				Toast.makeText(getActivity(), "please enter your Car Model",
						Toast.LENGTH_SHORT).show();
			} else if (finalKilos.equals("")) {
				Toast.makeText(getActivity(), "please enter Maintenance",
						Toast.LENGTH_SHORT).show();
			}
		} else {
			new AddMaintenance(finalModelId, finalKilos).execute();
		}
	}

	public void init() {
		carsBrandList = new ArrayList<HashMap<String, String>>();
		carsModelList = new ArrayList<HashMap<String, String>>();
		kilosList = new ArrayList<HashMap<String, String>>();
		mainatainaceList = new ArrayList<HashMap<String, String>>();
		brandMainTV.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showBrandDialog();
				if (!modelMainTV.getText().toString().equals("")) {
					modelMainTV.setText("");
				}

			}
		});
		modelMainTV.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!brandMainTV.getText().toString().equals("")) {
					if (dBCT.getModelsCount() == 0) {
						if (isConnectingToInternet()) {
							// TODO
							// new CarModel().execute();
						} else {
							Toast.makeText(getActivity(),
									"Check your internet connection",
									Toast.LENGTH_LONG).show();
						}
					} else {
						showModelDialog();
					}
				} else {
					Toast.makeText(getActivity(),
							"please enter your Car Brand first",
							Toast.LENGTH_LONG).show();
				}
			}
		});
		kiloMainTV.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showKilosDialog();
			}
		});
	}

	public class AddMaintenance extends AsyncTask<Void, Void, Void> {
		String finalModelId = "";
		String finalKilos = "";

		public AddMaintenance() {
		}

		public AddMaintenance(String finalModelId, String finalKilos) {
			this.finalModelId = finalModelId;
			this.finalKilos = finalKilos;

		}

		@Override
		public void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			mainatainaceList.clear();
			pDialog = new ProgressDialog(getActivity());
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// Creating service handler class instance
			String url = maintenanceURL + "carModelId=" + this.finalModelId
					+ "&kilos=" + this.finalKilos;
			try {
				JSONObject jsonObj = jP.getJSONFromUrl(url);
				Log.d("Response: ", "> " + jsonObj);
				eCode = jsonObj.getString("eCode");
				eDesc = jsonObj.getString("eDesc");
				maintainceServiceTypes = jsonObj
						.getString("maintainceServiceTypes");
				JSONArray maintainceJsonObject = new JSONArray(
						maintainceServiceTypes);
				for (int i = 0; i < maintainceJsonObject.length(); i++) {
					JSONObject c = maintainceJsonObject.getJSONObject(i);
					mainatainaceTypeEn = c.getString("mainatainaceTypeEn");
					mainatainaceDescEn = c.getString("mainatainaceDescEn");
					mainatainaceTypeAr = c.getString("mainatainaceTypeAr");
					mainatainaceDescAr = c.getString("mainatainaceDescAr");

					// Color colorRow = new Color();
					// colorRow.setColorId(id);
					// colorRow.setColorAr(colorAr);
					// colorRow.setColorEn(colorEn);
					//
					// dBCT.addColor(colorRow);

					HashMap<String, String> mainatainace = new HashMap<String, String>();
					mainatainace.put("mainatainaceTypeEn", mainatainaceTypeEn);
					mainatainace.put("mainatainaceDescEn", mainatainaceDescEn);
					mainatainace.put("mainatainaceTypeAr", mainatainaceTypeAr);
					mainatainace.put("mainatainaceDescAr", mainatainaceDescAr);

					mainatainaceList.add(mainatainace);
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (eCode.equals("0")) {
				Toast.makeText(getActivity(), "Your request added succeffully",
						Toast.LENGTH_LONG).show();
				showMainatainaceDialog();
			}
			if (pDialog.isShowing()) {
				pDialog.dismiss();
			}
		}
	}

	public void showBrandDialog() {
		dialog.setContentView(R.layout.add_car_dialog);
		dialog.setCancelable(true);
		dialog.setTitle("Choose a Brand");
		Booking.changeWidthHeight(getActivity(), dialog);

		lv = (ListView) dialog.findViewById(R.id.addLV);
		swipeView = (SwipeRefreshLayout) dialog.findViewById(R.id.swipeDialog);
		swipeView
				.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
					@Override
					public void onRefresh() {
						swipeView.setRefreshing(true);
						if (dBCT.getBrandsCount() == 0) {
							Toast.makeText(getActivity(),
									"load Brands from car first",
									Toast.LENGTH_LONG).show();
						}
					}
				});

		List<Brand> brandList = dBCT.getAllBrands();
		carsBrandList.clear();
		for (int i = 0; i < brandList.size(); i++) {
			HashMap<String, String> brand = new HashMap<String, String>();
			brand.put("typeId", brandList.get(i).getTypeId());
			brand.put("typeNameAr", brandList.get(i).getTypeNameAr());
			brand.put("typeNameEn", brandList.get(i).getTypeNameEn());
			brand.put("carModels", brandList.get(i).getCarModels());

			carsBrandList.add(brand);
		}

		ListAdapter adapter = new SimpleAdapter(getActivity(), carsBrandList,
				R.layout.single_brand_item, new String[] { "typeId",
						"typeNameEn" }, new int[] { R.id.typeId,
						R.id.typeNameEn });

		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String s = ((TextView) view.findViewById(R.id.typeNameEn))
						.getText().toString();
				brandMainTV.setText(s);
				// String sId = ((TextView) view.findViewById(R.id.typeId))
				// .getText().toString();
				// brandMainIdTV.setText(sId);
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	public void showModelDialog() {
		dialog.setContentView(R.layout.add_car_dialog);
		dialog.setCancelable(true);
		dialog.setTitle("Choose a Model");
		Booking.changeWidthHeight(getActivity(), dialog);
		// dialog.setP
		lv = (ListView) dialog.findViewById(R.id.addLV);

		List<Model> modelList = dBCT.getAllModels();
		carsModelList.clear();
		for (int i = 0; i < modelList.size(); i++) {
			if (modelList.get(i).getParentBrand()
					.equals(brandMainTV.getText().toString())) {
				HashMap<String, String> model = new HashMap<String, String>();

				model.put("modelId", modelList.get(i).getTypeId());
				model.put("modelNameAr", modelList.get(i).getTypeNameAr());
				model.put("modelNameEn", modelList.get(i).getTypeNameEn());

				carsModelList.add(model);
			}
		}
		ListAdapter adapter = new SimpleAdapter(getActivity(), carsModelList,
				R.layout.single_model_item, new String[] { "modelId",
						"modelNameEn" }, new int[] { R.id.modelId,
						R.id.modelNameEn });
		lv.setAdapter(adapter);
		dialog.show();

		swipeView = (SwipeRefreshLayout) dialog.findViewById(R.id.swipeDialog);
		swipeView
				.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
					@Override
					public void onRefresh() {
						swipeView.setRefreshing(false);
					}
				});

		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String s = ((TextView) view.findViewById(R.id.modelNameEn))
						.getText().toString();
				modelMainTV.setText(s);
				String sId = ((TextView) view.findViewById(R.id.modelId))
						.getText().toString();
				// modelTV.setText(s);
				modelIdMainTV.setText(sId);
				dialog.dismiss();
			}
		});
	}

	public void showKilosDialog() {
		dialog.setContentView(R.layout.add_car_dialog);
		dialog.setCancelable(true);
		dialog.setTitle("Choose maintenance kilos");
		Booking.changeWidthHeight(getActivity(), dialog);
		dialog.show();
		lv = (ListView) dialog.findViewById(R.id.addLV);
		// dialog.setP
		if (isConnectingToInternet()) {
			new ListMaitanenanceKilo().execute();
		} else {
			Toast.makeText(getActivity(), "Check your internet connection",
					Toast.LENGTH_LONG).show();
		}

	}

	public void showMainatainaceDialog() {
		dialog.setContentView(R.layout.mainatainace_dialog);
		dialog.setCancelable(true);
		dialog.setTitle("Mainatainace Table");
		Booking.changeWidthHeight(getActivity(), dialog);
		lv = (ListView) dialog.findViewById(R.id.mainatainaceLV);
		TextView tv = (TextView) dialog.findViewById(R.id.mainatainaceTV);
		tv.setText(brandMainTV.getText().toString() + " "
				+ kiloMainTV.getText().toString());
		// dialog.setP
		ListAdapter adapter = new SimpleAdapter(getActivity(),
				mainatainaceList, R.layout.single_mainatainace_item,
				new String[] { "mainatainaceTypeEn", "mainatainaceDescEn" },
				new int[] { R.id.mainatainaceTypeEn, R.id.mainatainaceDescEn });

		lv.setAdapter(adapter);
		dialog.show();
	}

	public class ListMaitanenanceKilo extends AsyncTask<Void, Void, Void> {

		@Override
		public void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			kilosList.clear();
			progress = ProgressDialog.show(Maintenance.this.getActivity(), "",
					"Loading...");
		}

		// carModelId=1&kilos=1000
		protected Void doInBackground(Void... arg0) {
			// Creating service handler class instance
			String url = kiloListURL;
			try {
				JSONObject jsonObj = jP.getJSONFromUrl(url);

				eCode = jsonObj.getString("eCode");
				eDesc = jsonObj.getString("eDesc");
				kilos = jsonObj.getString("kilos");

				myList = new ArrayList<String>(Arrays.asList(kilos.split(",")));

				for (int i = 0; i < myList.size(); i++) {
					HashMap<String, String> kilo = new HashMap<String, String>();
					String k = myList.get(i).replace("[", "").replace("]", "")
							.replace("\"", "");
					kilo.put("kilos", k);
					kilosList.add(kilo);
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		@SuppressWarnings("deprecation")
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// Dismiss the progress dialog
			if (progress.isShowing()) {
				progress.dismiss();
			}
			ListAdapter adapter = new SimpleAdapter(getActivity(), kilosList,
					R.layout.single_kilos_item, new String[] { "eCode",
							"eDesc", "kilos" }, new int[] { R.id.eCode,
							R.id.eDesc, R.id.kilos });

			lv.setAdapter(adapter);
			swipeView = (SwipeRefreshLayout) dialog
					.findViewById(R.id.swipeDialog);
			swipeView
					.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
						@Override
						public void onRefresh() {
							swipeView.setRefreshing(true);
							if (isConnectingToInternet()) {
								new ListMaitanenanceKilo().execute();
							} else {
								Toast.makeText(getActivity(),
										"Check your internet connection",
										Toast.LENGTH_LONG).show();
							}
						}
					});
			swipeView.setColorScheme(android.R.color.holo_blue_bright,
					android.R.color.holo_green_light,
					android.R.color.holo_orange_light,
					android.R.color.holo_red_light);
			lv.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					String kilosStr = ((TextView) view.findViewById(R.id.kilos))
							.getText().toString();
					kiloMainTV.setText(kilosStr);
					dialog.dismiss();
				}
			});
			dialog.show();
		}
	}

	public boolean isConnectingToInternet() {
		ConnectivityManager connectivity = (ConnectivityManager) getActivity()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
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
}
