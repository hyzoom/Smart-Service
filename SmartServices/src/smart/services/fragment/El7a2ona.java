package smart.services.fragment;

import org.json.JSONException;
import org.json.JSONObject;

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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import smart.services.handler.DataBaseHandler;
import smart.services.handler.GPSTracker;
import smart.services.handler.JSONParser;

public class El7a2ona extends Fragment {

	MapView mMapView;
	private GoogleMap googleMap;
	GPSTracker gps;
	Button refreshLocation, el7a2ona;
	JSONParser jP = new JSONParser();
	private ProgressDialog pDialog;
	private static String URL = "http://smarty-trioplus.rhcloud.com/supportCenter/el7a2ony?";
	String eCode, eDesc;
	DataBaseHandler dBCT;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.el7a2ona, container, false);
		mMapView = (MapView) v.findViewById(R.id.map);
		dBCT = new DataBaseHandler(getActivity());
		refreshLocation = (Button) v.findViewById(R.id.refreshLocation);
		el7a2ona = (Button) v.findViewById(R.id.el7a2ona);
		mMapView.onCreate(savedInstanceState);
		mMapView.onResume();
		showMap();
		refreshLocation.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showMap();
			}
		});
		el7a2ona.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String userId = dBCT.getUser(1).getUserId();
				String lati = getLatitude() + "";
				String longi = getLongitude() + "";
				String zoom = "12";
				if (isConnectingToInternet()) {
					new El7a2ony(userId, lati, longi, zoom).execute();
				} else {
					Toast.makeText(getActivity(),
							"Check your internet connection", Toast.LENGTH_LONG)
							.show();
				}
			}
		});
		return v;
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

	public void showMap() {
		double latitude = getLatitude();
		double longitude = getLongitude();
		try {
			MapsInitializer.initialize(getActivity().getApplicationContext());
		} catch (Exception e) {
			e.printStackTrace();
		}
		googleMap = mMapView.getMap();
		// latitude and longitude
		// create marker
		MarkerOptions marker = new MarkerOptions().position(
				new LatLng(latitude, longitude)).title("El7a2ona");

		// Changing marker icon
		marker.icon(BitmapDescriptorFactory
				.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));

		// adding marker
		googleMap.addMarker(marker);
		CameraPosition cameraPosition = new CameraPosition.Builder()
				.target(new LatLng(latitude, longitude)).zoom(12).build();

		// googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
		googleMap.animateCamera(CameraUpdateFactory
				.newCameraPosition(cameraPosition));
	}

	public double getLatitude() {
		gps = new GPSTracker(getActivity());
		double latitude = 0;
		if (gps.canGetLocation()) {
			latitude = gps.getLatitude();
		} else {
			gps.showSettingsAlert();
		}
		return latitude;
	}

	public double getLongitude() {
		gps = new GPSTracker(getActivity());
		double longitude = 0;
		if (gps.canGetLocation()) {
			longitude = gps.getLongitude();
		} else {
			gps.showSettingsAlert();
		}
		return longitude;
	}

	public class El7a2ony extends AsyncTask<Void, Void, Void> {
		String userId = "";
		String latitude = "";
		String longitude = "";
		String zoom = "";

		public El7a2ony() {
		}

		public El7a2ony(String userId, String latitude, String longitude,
				String zoom) {
			this.userId = userId;
			this.latitude = latitude;
			this.longitude = longitude;
			this.zoom = zoom;
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
			String url = URL + "userId=" + this.userId + "&latitude="
					+ this.latitude + "&longitude=" + this.longitude + "&zoom="
					+ this.zoom;
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
				Booking.showSubmitDialog(getActivity(),
						"Location sent... help is coming");
//				Toast.makeText(getActivity(),
//						"Location sent... help is coming", Toast.LENGTH_LONG)
//						.show();
			}
			if (pDialog.isShowing()) {
				pDialog.dismiss();
			}
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		mMapView.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
		mMapView.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mMapView.onDestroy();
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		mMapView.onLowMemory();
	}
}