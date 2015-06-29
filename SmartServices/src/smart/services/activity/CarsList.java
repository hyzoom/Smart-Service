package smart.services.activity;

import java.util.ArrayList;
import java.util.HashMap;

import info.androidhive.slidingmenu.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class CarsList extends Activity {
	ListView lv;
	SwipeRefreshLayout swipeView;
	ArrayList<HashMap<String, String>> carsList;
	String carBrand, carModel, year, carColor, chase;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cars_list);
		carsList = new ArrayList<HashMap<String, String>>();

		list();
	}

	@SuppressWarnings("deprecation")
	public void list() {
		lv = (ListView) findViewById(R.id.carsList);
		swipeView = (SwipeRefreshLayout) findViewById(R.id.swipeOffers);
		swipeView
				.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
					@Override
					public void onRefresh() {
						swipeView.setRefreshing(true);

					}
				});
		swipeView.setColorScheme(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		Intent in = getIntent();

		carBrand = in.getStringExtra("carBrand");
		carModel = in.getStringExtra("carModel");
		year = in.getStringExtra("year");
		carColor = in.getStringExtra("carColor");
		chase = in.getStringExtra("chase");

		HashMap<String, String> cars = new HashMap<String, String>();

		cars.put("carBrand", carBrand);
		cars.put("carModel", carModel);
		cars.put("year", year);
		cars.put("carColor", carColor);
		cars.put("chase", chase);

		carsList.add(cars);
		ListAdapter adapter = new SimpleAdapter(getApplicationContext(),
				carsList, R.layout.single_car_item, new String[] { "carBrand",
						"carModel", "year", "carColor", "chase" }, new int[] {
						R.id.carBrand, R.id.carModel, R.id.year, R.id.carColor,
						R.id.chase });
		lv.setAdapter(adapter);
	}

}
