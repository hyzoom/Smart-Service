package smart.services.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import smart.services.handler.DataBaseHandler;
import smart.services.handler.JSONParser;
import smart.services.model.Complain;
import smart.services.model.ServiceType;
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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ComplainFragment extends Fragment {

	private static String URL = "http://smarty-trioplus.rhcloud.com/supportCenter/bookingTypeList";
	private static String complainURL = "http://smarty-trioplus.rhcloud.com/supportCenter/complain?";
	JSONParser jP = new JSONParser();

	private TextView complainType, complainTypeId;
	private EditText complainET;
	private Button submetComplain;
	private DataBaseHandler databaseHandler;
	ArrayList<HashMap<String, String>> serviceTypeList;
	private ProgressDialog pDialog;
	ListView lv;

	String finalCarId, finalTypeId, dateFromTV, finalDate, finalHour,
			finalComment, list, typeId, typeNameAr, typeNameEn, eCode, eDesc,
			msg_en, msg_ar, userCarsDTOs, carId, chaseNumber, modelIdFromCars,
			plateNumber, year, carId0, complainDesc;

	private Complain complain;
	private ServiceType serviceType;

	public ComplainFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		complain = new Complain();
		databaseHandler = new DataBaseHandler(getActivity());
		serviceTypeList = new ArrayList<HashMap<String, String>>();
		View view = inflater.inflate(R.layout.complains, container, false);
		complainType = (TextView) view.findViewById(R.id.complainTypeTV);
		complainTypeId = (TextView) view.findViewById(R.id.complainTypeIdTV);
		complainET = (EditText) view.findViewById(R.id.complainET);
		submetComplain = (Button) view.findViewById(R.id.submitComplainButton);
		complainDesc = ((EditText) view.findViewById(R.id.complainET))
				.getText().toString();
		complainType.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showServiceDialog();
			}
		});

		submetComplain.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (complain.getServiceType() <= 0) {
					Toast.makeText(getActivity(),
							"You must determine your complain",
							Toast.LENGTH_SHORT).show();
				} else {
					if (complainET.getText().toString() == null
							|| complainET.getText().toString().equals("")) {
						Toast.makeText(getActivity(),
								"You must explain your complain",
								Toast.LENGTH_SHORT).show();
					} else {
						complain.setComplain(complainET.getText().toString());
						complain.setUserId(databaseHandler.getUser(1)
								.getUserId());
						if (isConnectingToInternet()) {
							new SubmitComplain(databaseHandler.getUser(1)
									.getUserId(), complainTypeId.getText()
									.toString(), complainET.getText()
									.toString()).execute();
						} else {
							Toast.makeText(getActivity(),
									"Check your internet connection",
									Toast.LENGTH_LONG).show();
						}
						databaseHandler.addComplain(complain);
					}
				}
			}
		});
		return view;
	}

	@SuppressWarnings("deprecation")
	public void showServiceDialog() {
		final Dialog dialog = new Dialog(getActivity());
		dialog.setContentView(R.layout.service_type_dialog);
		dialog.setCancelable(true);
		dialog.setTitle("Choose Service Type");
		Booking.changeWidthHeight(getActivity(), dialog);
		// dialog.setP
		lv = (ListView) dialog.findViewById(R.id.serviceTypeLV);
		if (databaseHandler.getServiceTypesCount() == 0) {
			if (isConnectingToInternet()) {
				new ServiceTypes().execute();
			} else {
				Toast.makeText(getActivity(), "Check your internet connection",
						Toast.LENGTH_LONG).show();
			}
		} else {
			List<ServiceType> typeList = databaseHandler.getAllServiceTypes();
			serviceTypeList.clear();
			for (int i = 0; i < typeList.size(); i++) {
				HashMap<String, String> serviceType = new HashMap<String, String>();
				serviceType.put("typeId", typeList.get(i).getTypeId());
				serviceType.put("typeNameAr", typeList.get(i).getTypeNameAr());
				serviceType.put("typeNameEn", typeList.get(i).getTypeNameEn());

				serviceTypeList.add(serviceType);
			}

			ListAdapter adapter = new SimpleAdapter(getActivity(),
					serviceTypeList, R.layout.single_service_type_item,
					new String[] { "typeId", "typeNameAr", "typeNameEn" },
					new int[] { R.id.serTypeId, R.id.serTypeNameAr,
							R.id.serTypeNameEn });
			lv.setAdapter(adapter);
		}
		final SwipeRefreshLayout swipeView = (SwipeRefreshLayout) dialog
				.findViewById(R.id.swipeDialogST);
		swipeView
				.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
					@Override
					public void onRefresh() {
						swipeView.setRefreshing(true);
						if (isConnectingToInternet()) {
							new ServiceTypes().execute();
						} else {
							Toast.makeText(getActivity(),
									"Check your internet connection",
									Toast.LENGTH_LONG).show();
						}
						swipeView.setRefreshing(false);
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
				serviceType = databaseHandler.getServiceType(serviceTypeList
						.get(position).get("typeId"));
				complain.setServiceType(serviceType.getId());

				String serEn = ((TextView) view
						.findViewById(R.id.serTypeNameEn)).getText().toString();
				String serTypeId = ((TextView) view
						.findViewById(R.id.serTypeId)).getText().toString();
				complainType.setText(serEn);
				complainTypeId.setText(serTypeId);
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	public class ServiceTypes extends AsyncTask<Void, Void, Void> {

		public ServiceTypes() {
		}

		@Override
		public void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			serviceTypeList.clear();
			pDialog = new ProgressDialog(ComplainFragment.this.getActivity());
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected Void doInBackground(Void... arg0) {
			// Creating service handler class instance
			String url = URL;

			try {
				databaseHandler.deleteAllServiceTypes();
				JSONObject jsonObj = jP.getJSONFromUrl(url);
				Log.d("Response: ", "> " + jsonObj);
				list = jsonObj.getString("list");
				JSONArray listJsonObject = new JSONArray(list);
				for (int i = 0; i < listJsonObject.length(); i++) {
					JSONObject c = listJsonObject.getJSONObject(i);
					typeId = c.getString("typeId");
					typeNameAr = c.getString("typeNameAr");
					typeNameEn = c.getString("typeNameEn");

					ServiceType serviceType = new ServiceType();
					serviceType.setTypeId(typeId);
					serviceType.setTypeNameAr(typeNameAr);
					serviceType.setTypeNameEn(typeNameEn);

					databaseHandler.addServiceType(serviceType);

					HashMap<String, String> service = new HashMap<String, String>();
					service.put("typeId", typeId);
					service.put("typeNameAr", typeNameAr);
					service.put("typeNameEn", typeNameEn);

					serviceTypeList.add(service);
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// Dismiss the progress dialog
			ListAdapter adapter = new SimpleAdapter(getActivity(),
					serviceTypeList, R.layout.single_service_type_item,
					new String[] { "typeId", "typeNameAr", "typeNameEn" },
					new int[] { R.id.serTypeId, R.id.serTypeNameAr,
							R.id.serTypeNameEn });
			lv.setAdapter(adapter);

			if (pDialog.isShowing()) {
				pDialog.dismiss();
			}
		}
	}

	public class SubmitComplain extends AsyncTask<Void, Void, Void> {
		String userId, complainTypeId, complainDesc;

		public SubmitComplain() {
		}

		public SubmitComplain(String userId, String complainTypeId,
				String complainDesc) {
			this.userId = userId;
			this.complainTypeId = complainTypeId;
			this.complainDesc = complainDesc;
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

		protected Void doInBackground(Void... arg0) {
			// Creating service handler class instance
			String url = complainURL + "userId=" + this.userId
					+ "&complainType=" + this.complainTypeId + "&desc="
					+ this.complainDesc;

			try {
				JSONObject jsonObj = jP.getJSONFromUrl(url);
				Log.d("Response: ", "> " + jsonObj);
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
			if (eCode.equals("0")) {
				Booking.showSubmitDialog(getActivity(),
						"thanks for your Complain,\n we will contact you SOON");
				complainType.setText("");
				complainET.setText("");
				// Toast.makeText(getActivity(), eDesc,
				// Toast.LENGTH_LONG).show();
			}
			if (pDialog.isShowing()) {
				pDialog.dismiss();
			}
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
