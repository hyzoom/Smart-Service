package smart.services.fragment;

import java.util.ArrayList;

import smart.services.adapter.BookingAdapter;
import smart.services.functions.BookingFunctions;
import smart.services.functions.CarFunctions;
import smart.services.handler.DataBaseHandler;
import smart.services.model.BookingModel;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ManageBookings extends Fragment {

	private BookingFunctions bookingFunctions;
	private CarFunctions carFunctions;
	private ArrayList<BookingModel> bookArrayList = new ArrayList<BookingModel>();

	private ListView lv;
	private TextView bookingsTitleTV;
	private SwipeRefreshLayout swipeView;
	private ProgressDialog pDialog;

	private DataBaseHandler databaseHandler;
	private Setting setting;

	public ManageBookings() {
	}

	@SuppressLint("InlinedApi") 
	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		bookingFunctions = new BookingFunctions(getActivity());
		carFunctions = new CarFunctions(getActivity());
		databaseHandler = new DataBaseHandler(getActivity());
		setting = databaseHandler.getSetting();
		
		View rootView = inflater.inflate(R.layout.manage_booking, container,
				false);
		lv = (ListView) rootView.findViewById(R.id.bookingHistoryLV);
		bookingsTitleTV = (TextView) rootView.findViewById(R.id.bookingsTitleTV);
		
		if (setting.getDuration() == 1) {
			bookingsTitleTV.setText(getResources().getString(R.string.booking_manage_info_ar));
		}
		
		if (bookingFunctions.isConnectingToInternet()) {
			new BookingAsyncTask().execute();
		} else {
			Toast.makeText(getActivity(), "Check your internet connection",
					Toast.LENGTH_LONG).show();
		}
		
		swipeView = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeBookingHistory);

		swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
					@Override
					public void onRefresh() {
						swipeView.setRefreshing(true);
						if (bookingFunctions.isConnectingToInternet()) {
							new BookingAsyncTask().execute();
						} else {
							Toast.makeText(getActivity(), "Check your internet connection",
									Toast.LENGTH_LONG).show();
						}
						swipeView.setRefreshing(false);
					}
				});
		swipeView.setColorScheme(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);

		return rootView;
	}

	public class BookingAsyncTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			databaseHandler.deleteAllCars();
			
			pDialog = new ProgressDialog(getActivity());
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			carFunctions.saveJSONUserCars();
			bookArrayList = bookingFunctions.saveJSONBookings();
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (pDialog.isShowing()) {
				pDialog.dismiss();
			}
			
			BookingAdapter bookingAdapter = new BookingAdapter(getActivity(), R.layout.booking_adapter, bookArrayList);
			lv.setAdapter(bookingAdapter);
			
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
