package smart.services.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import smart.services.adapter.OfferAdapter;
import smart.services.handler.DataBaseHandler;
import smart.services.handler.JSONParser;
import smart.services.model.Offer;

import info.androidhive.slidingmenu.R;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class Offers extends Fragment {
	private static String URL = "http://smarty-trioplus.rhcloud.com/supportCenter/listOffers?userId=";
	ArrayList<HashMap<String, String>> offersList;
	ListView lv;
	TextView descEnTV;
	SwipeRefreshLayout swipeView;
	private ProgressDialog pDialog;
	JSONParser jP = new JSONParser();
	String offerDTOs, offerId, descAr, descEn, expireTime, expire, offerType,
			status, titleEn, titleAr;
	int count = 1;
	DataBaseHandler databaseHandler;
	HashMap<String, String> openOffer = new HashMap<String, String>();

	public Offers() {
	}

	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.offers, container, false);
		databaseHandler = new DataBaseHandler(getActivity());
		swipeView = (SwipeRefreshLayout) rootView
				.findViewById(R.id.swipeOffers);
		lv = (ListView) rootView.findViewById(R.id.offersLV);
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				descEnTV = (TextView) view.findViewById(R.id.descEn);
				if (((openOffer.get(id + "") == null) || openOffer.get(id + "")
						.equals("n"))) {
					descEnTV.setVisibility(View.VISIBLE);
					openOffer.put(id + "", "y");
				} else {
					descEnTV.setVisibility(View.GONE);
					openOffer.put(id + "", "n");
				}

			}
		});
		offersList = new ArrayList<HashMap<String, String>>();
		if (databaseHandler.getOffersCount() == 0) {
			if (isConnectingToInternet()) {
				new OffersList(databaseHandler.getUser(1).getUserId())
						.execute();
			} else {
				Toast.makeText(getActivity(), "Check your internet connection",
						Toast.LENGTH_LONG).show();
			}
		} else {
			List<Offer> offerCashList = databaseHandler.getAllOffers();

			offersList.clear();
			for (int i = 0; i < offerCashList.size(); i++) {
				HashMap<String, String> offer = new HashMap<String, String>();
				offer.put("offerId", offerCashList.get(i).getOfferId());
				offer.put("descAr", offerCashList.get(i).getDescAr());
				offer.put("descEn", offerCashList.get(i).getDescEn());
				offer.put("expireTime", offerCashList.get(i).getExpireTime());
				offer.put("expire", "Expired Date: "
						+ offerCashList.get(i).getExpire());
				offer.put("offerType", offerCashList.get(i).getOfferType());
				offer.put("status", offerCashList.get(i).getStatus());
				offer.put("titleAr", offerCashList.get(i).getTitleAr());
				offer.put("titleEn", offerCashList.get(i).getTitleEn());

				offersList.add(offer);

			}
			// OfferAdapter adapter = new OfferAdapter(offersList,
			// getActivity());
			ListAdapter adapter = new SimpleAdapter(getActivity(), offersList,
					R.layout.single_offer_item, new String[] { "expire",
							"titleEn", "descEn" }, new int[] { R.id.expire,
							R.id.titleEn, R.id.descEn });

			lv.setAdapter(adapter);
		}
		swipeView
				.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
					@Override
					public void onRefresh() {
						swipeView.setRefreshing(true);
						if (isConnectingToInternet()) {
							new OffersList(databaseHandler.getUser(1)
									.getUserId()).execute();
						} else {

						}

					}
				});
		swipeView.setColorScheme(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		return rootView;
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

	public class OffersList extends AsyncTask<Void, Void, Void> {
		String userId = "";

		public OffersList() {
		}

		public OffersList(String userId) {
			this.userId = userId;
		}

		@Override
		public void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			offersList.clear();
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
				databaseHandler.deleteAllOffers();
				JSONObject jsonObj = jP.getJSONFromUrl(url);
				Log.d("Response: ", "> " + jsonObj);

				offerDTOs = jsonObj.getString("offerDTOs");
				JSONArray offerJsonObject = new JSONArray(offerDTOs);
				for (int i = 0; i < offerJsonObject.length(); i++) {
					JSONObject c = offerJsonObject.getJSONObject(i);
					offerId = c.getString("offerId");
					descAr = c.getString("descAr");
					descEn = c.getString("descEn");
					expireTime = c.getString("expireTime");
					expire = c.getString("expire");
					offerType = c.getString("offerType");
					status = c.getString("status");
					titleAr = c.getString("titleAr");
					titleEn = c.getString("titleEn");

					Offer offerRow = new Offer();
					offerRow.setOfferId(offerId);
					offerRow.setDescAr(descAr);
					offerRow.setDescEn(Html.fromHtml(descEn) + "");
					offerRow.setExpireTime(expireTime);
					offerRow.setExpire(expire);
					offerRow.setOfferType(offerType);
					offerRow.setStatus(status);
					offerRow.setTitleAr(titleAr);
					offerRow.setTitleEn(titleEn);

					databaseHandler.addOffer(offerRow);

					HashMap<String, String> offer = new HashMap<String, String>();
					offer.put("offerId", offerId);
					offer.put("descAr", descAr);
					offer.put("descEn", Html.fromHtml(descEn) + "");
					offer.put("expireTime", expireTime);
					offer.put("expire", "Expired Date: " + expire);
					offer.put("offerType", offerType);
					offer.put("status", status);
					offer.put("titleAr", titleAr);
					offer.put("titleEn", titleEn);

					offersList.add(offer);

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
			// OfferAdapter adapter = new OfferAdapter(offersList,
			// getActivity());
			ListAdapter adapter = new SimpleAdapter(getActivity(), offersList,
					R.layout.single_offer_item, new String[] { "expire",
							"titleEn", "descEn" }, new int[] { R.id.expire,
							R.id.titleEn, R.id.descEn });

			lv.setAdapter(adapter);
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
