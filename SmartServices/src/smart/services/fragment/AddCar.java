package smart.services.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import smart.services.functions.CarFunctions;
import smart.services.handler.DataBaseHandler;
import smart.services.handler.JSONParser;
import smart.services.model.Brand;
import smart.services.model.CarTest;
import smart.services.model.Color;
import smart.services.model.Insurance;
import smart.services.model.Model;
import smart.services.model.Setting;
import info.androidhive.slidingmenu.R;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class AddCar extends Fragment {
	ListView lv;
	SwipeRefreshLayout swipeView;
	TextView addCarTitleTV, brandTV, brandIdTV, modelTV, modelIdTV,
			colorNameTV, yearTV, insuranceTV, insuranceIdTV, errorMessageTV,
			colorIdTV;
	EditText memberName, chassisEt, licenseNumberET;
	Button submit;
	private ProgressDialog pDialog;
	private static String URL = "http://smarty-trioplus.rhcloud.com/supportCenter/CarList";
	private static String colorURL = "http://smarty-trioplus.rhcloud.com/supportCenter/getCarColors";
	// private static String insuranceURL =
	// "http://smarty-trioplus.rhcloud.com/supportCenter/insuranceCompanyList";
	private static String addCarURL = "http://smarty-trioplus.rhcloud.com/supportCenter/addCar?";

	JSONParser jP = new JSONParser();
	String carsList, typeId, typeNameAr, typeNameEn;
	String carModels, modelId, modelNameAr, modelNameEn;
	String userCarsDTOs, id, colorAr, colorEn;
	String finalUserId, finalChase, finalLicenseNumber, finalModelId,
			finalYear, finalPlateNum, finalTypeId, finalColorId,
			insuranceCompany, nameEn, nameAr, finalInsuranceId;
	String eCode, eDesc, carId;
	ArrayList<HashMap<String, String>> carsBrandList, carsModelList,
			carsColorList, insuracneCompanyList;
	HashMap<String, String> model;

	private ImageView brandIVEn, modelIVEn, yearIVEn, colorIVEn,
			insuranceCopanyIVEn, brandIVAr, modelIVAr, yearIVAr, colorIVAr,
			insuranceCopanyIVAr;
	private DataBaseHandler dataBaseHandler;
	private Dialog dialog;
	private CarFunctions carFunctions;
	private ProgressDialog progress;
	private Setting setting;

	private String brandIdSelected;

	public AddCar() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		dataBaseHandler = new DataBaseHandler(getActivity());
		carFunctions = new CarFunctions(getActivity());
		dialog = new Dialog(getActivity());
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setting = dataBaseHandler.getSetting();

		View rootView = inflater.inflate(R.layout.add_car, container, false);
		submit = (Button) rootView.findViewById(R.id.submit);
		addCarTitleTV = (TextView) rootView.findViewById(R.id.addCarTitleTV);
		brandTV = (TextView) rootView.findViewById(R.id.brand);
		brandIdTV = (TextView) rootView.findViewById(R.id.brandId);
		modelTV = (TextView) rootView.findViewById(R.id.model);
		modelIdTV = (TextView) rootView.findViewById(R.id.modelId);
		colorNameTV = (TextView) rootView.findViewById(R.id.color);
		colorIdTV = (TextView) rootView.findViewById(R.id.colorId);
		yearTV = (TextView) rootView.findViewById(R.id.year);
		insuranceTV = (TextView) rootView.findViewById(R.id.insuranceTV);
		insuranceIdTV = (TextView) rootView.findViewById(R.id.insuranceIdTV);
		errorMessageTV = (TextView) rootView.findViewById(R.id.errorMsg);

		// Image views
		brandIVEn = (ImageView) rootView.findViewById(R.id.brandImageViewEn);
		modelIVEn = (ImageView) rootView.findViewById(R.id.modelIVEn);
		yearIVEn = (ImageView) rootView.findViewById(R.id.yearIVEn);
		colorIVEn = (ImageView) rootView.findViewById(R.id.colorIVEn);
		insuranceCopanyIVEn = (ImageView) rootView
				.findViewById(R.id.insuranceCompanyIVEn);
		brandIVAr = (ImageView) rootView.findViewById(R.id.brandImageViewAr);
		modelIVAr = (ImageView) rootView.findViewById(R.id.modelIVAr);
		yearIVAr = (ImageView) rootView.findViewById(R.id.yearIVAr);
		colorIVAr = (ImageView) rootView.findViewById(R.id.colorIVAr);
		insuranceCopanyIVAr = (ImageView) rootView
				.findViewById(R.id.insuranceCompanyIVAr);

		chassisEt = (EditText) rootView.findViewById(R.id.chassis);
		memberName = (EditText) rootView.findViewById(R.id.memberName);
		licenseNumberET = (EditText) rootView.findViewById(R.id.licenseNumber);

		init();

		submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				addCar();
			}
		});
		return rootView;
	}

	public void init() {
		if (setting.getDuration() == 1) {
			addCarTitleTV.setText(getResources().getString(
					R.string.add_car_title_ar));
			brandTV.setHint(getResources().getString(
					R.string.add_car_hint_brand_ar));
			modelTV.setHint(getResources().getString(
					R.string.add_car_hint_model_ar));
			yearTV.setHint(getResources().getString(
					R.string.add_car_hint_year_ar));
			colorNameTV.setHint(getResources().getString(
					R.string.add_car_hint_color_ar));
			insuranceTV.setHint(getResources().getString(
					R.string.add_car_hint_insurance_company_ar));
			licenseNumberET.setHint(getResources().getString(
					R.string.add_car_hint_licence_ar));
			chassisEt.setHint(getResources().getString(
					R.string.add_car_hint_chass_ar));

			brandIVEn.setVisibility(View.GONE);
			modelIVEn.setVisibility(View.GONE);
			yearIVEn.setVisibility(View.GONE);
			colorIVEn.setVisibility(View.GONE);
			insuranceCopanyIVEn.setVisibility(View.GONE);

			submit.setText(getResources().getString(R.string.booking_submet_ar));
		} else {
			brandIVAr.setVisibility(View.GONE);
			modelIVAr.setVisibility(View.GONE);
			yearIVAr.setVisibility(View.GONE);
			colorIVAr.setVisibility(View.GONE);
			insuranceCopanyIVAr.setVisibility(View.GONE);
		}
		carsBrandList = new ArrayList<HashMap<String, String>>();
		carsModelList = new ArrayList<HashMap<String, String>>();
		carsColorList = new ArrayList<HashMap<String, String>>();
		insuracneCompanyList = new ArrayList<HashMap<String, String>>();

		memberName.setText(dataBaseHandler.getUser(1).getName());
		brandTV.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!modelTV.getText().toString().equals("")) {
					modelTV.setText("");
				}
				showBrandDialog();
			}
		});
		modelTV.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!brandTV.getText().toString().equals("")) {
					showModelDialog();
				} else {
					Toast.makeText(getActivity(),
							"please enter your Car Brand first",
							Toast.LENGTH_LONG).show();
				}
			}
		});
		colorNameTV.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showColorDialog();
			}
		});
		yearTV.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showYearDialog();
			}
		});
		insuranceTV.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showInsuranceDialog();
			}
		});
	}

	public void addCar() {
		finalUserId = dataBaseHandler.getUser(1).getUserId();
		System.out.println("finalUserId " + finalUserId);
		finalChase = chassisEt.getText().toString();
		finalPlateNum = licenseNumberET.getText().toString();
		finalColorId = colorIdTV.getText().toString();
		finalModelId = modelIdTV.getText().toString();
		finalTypeId = brandIdTV.getText().toString();
		finalYear = yearTV.getText().toString();
		finalInsuranceId = insuranceIdTV.getText().toString();
		// errorMessageTV.setText(finalUserId);
		if (finalPlateNum.equals("") || brandTV.getText().toString().equals("")
				|| finalModelId.equals("") || finalYear.equals("")
				|| finalColorId.equals("") || finalChase.equals("")
				|| finalInsuranceId.equals("")) {

			if (finalPlateNum.equals("")) {
				Toast.makeText(getActivity(),
						"please enter your license number", Toast.LENGTH_SHORT)
						.show();
			} else if (brandTV.getText().toString().equals("")) {
				Toast.makeText(getActivity(), "please enter your Car Brand",
						Toast.LENGTH_SHORT).show();
			} else if (finalModelId.equals("")) {
				Toast.makeText(getActivity(), "please enter your Car model",
						Toast.LENGTH_SHORT).show();
			} else if (finalYear.equals("")) {
				Toast.makeText(getActivity(),
						"please enter Year of manufacturing",
						Toast.LENGTH_SHORT).show();
			} else if (finalColorId.equals("")) {
				Toast.makeText(getActivity(), "please enter your Car Color",
						Toast.LENGTH_SHORT).show();
			} else if (finalChase.equals("")) {
				Toast.makeText(getActivity(),
						"please enter your car Chase Number",
						Toast.LENGTH_SHORT).show();
			} else if (finalInsuranceId.equals("")) {
				Toast.makeText(getActivity(),
						"please enter your car Insurance Company",
						Toast.LENGTH_SHORT).show();
			}
		} else {
			new AddCars(finalUserId, finalChase, finalColorId, finalModelId,
					finalPlateNum, finalTypeId, finalYear, finalInsuranceId)
					.execute();
		}

	}

	public class AddCars extends AsyncTask<Void, Void, Void> {
		String finalUserId = "";
		String finalChase = "";
		String finalPlateNum = "";
		String finalColorId = "";
		String finalModelId = "";
		String finalTypeId = "";
		String finalYear = "";
		String finalInsuranceId = "";

		public AddCars() {
		}

		// supportCenter/addCar?userId=4&chaseNumber=12121&color=1&modelId=1&plateNumber=123er&typeId=1&year=2005
		public AddCars(String finalUserId, String finalChase,
				String finalColorId, String finalModelId, String finalPlateNum,
				String finalTypeId, String finalYear, String finalInsuranceId) {
			this.finalUserId = finalUserId;
			this.finalChase = finalChase;
			this.finalColorId = finalColorId;
			this.finalModelId = finalModelId;
			this.finalPlateNum = finalPlateNum;
			this.finalTypeId = finalTypeId;
			this.finalYear = finalYear;
			this.finalInsuranceId = finalInsuranceId;
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
			String url = addCarURL + "userId=" + this.finalUserId
					+ "&chaseNumber=" + this.finalChase + "&color="
					+ this.finalColorId + "&modelId=" + this.finalModelId
					+ "&plateNumber=" + this.finalPlateNum + "&typeId="
					+ this.finalTypeId + "&year=" + this.finalYear
					+ "&insuranceId=" + this.finalInsuranceId;
			try {
				JSONObject jsonObj = jP.getJSONFromUrl(url);
				Log.d("Response: ", "> " + jsonObj);
				eCode = jsonObj.getString("eCode");
				eDesc = jsonObj.getString("eDesc");
				carId = jsonObj.getString("id");

			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			errorMessageTV.setText(eDesc);
			if (eCode.equals("0")) {
				CarTest newC = new CarTest();
				newC.setCarId(carId);
				newC.setChase(this.finalChase);
				newC.setPlateNumber(this.finalPlateNum);
				newC.setCarColor(this.finalColorId);
				newC.setCarModelId(this.finalModelId);
				newC.setCarBrand(this.finalTypeId);
				newC.setYear(this.finalYear);
				newC.setInsuranceId(Integer.parseInt(this.finalInsuranceId));

				// CarTest newCar = new CarTest(carId,
				// dataBaseHandler.getUser(1).getUserId(), brandTV.getText()
				// .toString(), this.finalModelId, modelTV
				// .getText().toString(), this.finalYear,
				// colorNameTV.getText().toString(), this.finalChase);
				// newCar.setUserId(dataBaseHandler.getUser(1).getUserId());
				dataBaseHandler.addCar(newC);
				new RefreshCars().execute();
				// getActivity().finish();
				// Intent intent = new Intent(getActivity(), getActivity()
				// .getClass());
				// intent.putExtra("fragNum", "6");
				// getActivity().startActivity(intent);
			}
			if (pDialog.isShowing()) {
				pDialog.dismiss();
			}
		}
	}

	private class RefreshCars extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progress = ProgressDialog.show(AddCar.this.getActivity(), "",
					"Loading...");
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
			getActivity().finish();
			Intent intent = new Intent(getActivity(), getActivity().getClass());
			intent.putExtra("fragNum", "6");
			getActivity().startActivity(intent);
		}
	}

	@SuppressLint("InlinedApi")
	@SuppressWarnings("deprecation")
	public void showYearDialog() {
		dialog.setContentView(R.layout.add_car_dialog);
		dialog.setCancelable(true);

		TextView dialogTitleTV = (TextView) dialog
				.findViewById(R.id.brandsListTitleTV);

		if (setting.getDuration() == 0) {
			dialog.findViewById(R.id.titleIVAr).setVisibility(View.GONE);
			dialogTitleTV.setText(getResources().getString(
					R.string.year_list_en));
		} else {
			dialog.findViewById(R.id.titleIVEn).setVisibility(View.GONE);
			dialogTitleTV.setText(getResources().getString(
					R.string.year_list_ar));
		}

		Booking.changeWidthHeight(getActivity(), dialog);
		lv = (ListView) dialog.findViewById(R.id.addLV);
		swipeView = (SwipeRefreshLayout) dialog.findViewById(R.id.swipeDialog);
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
		String years[] = { "2016", "2015", "2014", "2013", "2012", "2011",
				"2010", "2009", "2008", "2007", "2007", "2005", "2004", "2003",
				"2002", "2001", "2000" };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, years);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				yearTV.setText(lv.getItemAtPosition(position).toString());
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	@SuppressLint("InlinedApi")
	@SuppressWarnings("deprecation")
	public void showBrandDialog() {
		if (dataBaseHandler.getBrandsCount() == 0) {
			if (carFunctions.isConnectingToInternet()) {
				new BrandAsyncTask().execute();
			} else {
				Toast.makeText(getActivity(), "Check your internet connection",
						Toast.LENGTH_LONG).show();
			}

		} else {
			dialog.setContentView(R.layout.add_car_dialog);
			dialog.setCancelable(true);

			Booking.changeWidthHeight(getActivity(), dialog);
			// dialog.setP
			lv = (ListView) dialog.findViewById(R.id.addLV);
			TextView dialogTitle = (TextView) dialog
					.findViewById(R.id.brandsListTitleTV);

			List<Brand> brandList = dataBaseHandler.getAllBrands();

			carsBrandList.clear();
			for (int i = 0; i < brandList.size(); i++) {
				HashMap<String, String> brand = new HashMap<String, String>();
				brand.put("typeId", brandList.get(i).getTypeId());
				brand.put("typeNameAr", brandList.get(i).getTypeNameAr());
				brand.put("typeNameEn", brandList.get(i).getTypeNameEn());
				brand.put("carModels", brandList.get(i).getCarModels());

				carsBrandList.add(brand);
			}

			ListAdapter adapter = null;

			if (setting.getDuration() == 0) {
				dialog.findViewById(R.id.titleIVAr).setVisibility(View.GONE);
				adapter = new SimpleAdapter(getActivity(), carsBrandList,
						R.layout.single_brand_item, new String[] { "typeId",
								"typeNameEn" }, new int[] { R.id.typeId,
								R.id.typeNameEn });
			} else {
				dialog.findViewById(R.id.titleIVEn).setVisibility(View.GONE);
				dialogTitle.setText(getResources().getString(
						R.string.add_car_brand_dialog_ar));

				adapter = new SimpleAdapter(getActivity(), carsBrandList,
						R.layout.single_brand_item, new String[] { "typeId",
								"typeNameAr" }, new int[] { R.id.typeId,
								R.id.typeNameEn });
			}

			lv.setAdapter(adapter);

			swipeView = (SwipeRefreshLayout) dialog
					.findViewById(R.id.swipeDialog);
			swipeView
					.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
						@Override
						public void onRefresh() {
							swipeView.setRefreshing(true);
							if (carFunctions.isConnectingToInternet()) {
								new BrandAsyncTask().execute();
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
					String s = ((TextView) view.findViewById(R.id.typeNameEn))
							.getText().toString();
					brandTV.setText(s);
					String sId = ((TextView) view.findViewById(R.id.typeId))
							.getText().toString();
					brandIdSelected = sId;

					// Toast.makeText(getActivity(), sId,
					// Toast.LENGTH_SHORT).show();
					brandIdTV.setText(sId);
					dialog.dismiss();
				}
			});

			dialog.show();
		}
	}

	private class BrandAsyncTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progress = ProgressDialog.show(AddCar.this.getActivity(), "",
					"Loading...");
		}

		@Override
		protected Void doInBackground(Void... params) {
			carFunctions.saveJSONBrandAndModels();
			return null;
		}

		@SuppressLint("InlinedApi")
		@SuppressWarnings("deprecation")
		@Override
		protected void onPostExecute(Void aVoid) {
			super.onPostExecute(aVoid);
			if (progress.isShowing()) {
				progress.dismiss();
			}

			dialog.setContentView(R.layout.add_car_dialog);
			dialog.setCancelable(true);

			Booking.changeWidthHeight(getActivity(), dialog);
			// dialog.setP
			lv = (ListView) dialog.findViewById(R.id.addLV);
			TextView dialogTitle = (TextView) dialog
					.findViewById(R.id.brandsListTitleTV);

			List<Brand> brandList = dataBaseHandler.getAllBrands();

			carsBrandList.clear();
			for (int i = 0; i < brandList.size(); i++) {
				HashMap<String, String> brand = new HashMap<String, String>();
				brand.put("typeId", brandList.get(i).getTypeId());
				brand.put("typeNameAr", brandList.get(i).getTypeNameAr());
				brand.put("typeNameEn", brandList.get(i).getTypeNameEn());
				brand.put("carModels", brandList.get(i).getCarModels());

				carsBrandList.add(brand);
			}

			ListAdapter adapter = null;

			if (setting.getDuration() == 0) {
				dialog.findViewById(R.id.titleIVAr).setVisibility(View.GONE);
				adapter = new SimpleAdapter(getActivity(), carsBrandList,
						R.layout.single_brand_item, new String[] { "typeId",
								"typeNameEn" }, new int[] { R.id.typeId,
								R.id.typeNameEn });
			} else {
				dialog.findViewById(R.id.titleIVEn).setVisibility(View.GONE);
				dialogTitle.setText(getResources().getString(
						R.string.add_car_brand_dialog_ar));

				adapter = new SimpleAdapter(getActivity(), carsBrandList,
						R.layout.single_brand_item, new String[] { "typeId",
								"typeNameAr" }, new int[] { R.id.typeId,
								R.id.typeNameEn });
			}

			lv.setAdapter(adapter);

			swipeView = (SwipeRefreshLayout) dialog
					.findViewById(R.id.swipeDialog);
			swipeView
					.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
						@Override
						public void onRefresh() {
							swipeView.setRefreshing(true);
							if (carFunctions.isConnectingToInternet()) {
								new BrandAsyncTask().execute();
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
					String s = ((TextView) view.findViewById(R.id.typeNameEn))
							.getText().toString();
					brandTV.setText(s);
					String sId = ((TextView) view.findViewById(R.id.typeId))
							.getText().toString();
					brandIdTV.setText(sId);
					dialog.dismiss();
				}
			});

			dialog.show();
		}
	}

	@SuppressLint("InlinedApi")
	@SuppressWarnings("deprecation")
	public void showModelDialog() {
		dialog.setContentView(R.layout.add_car_dialog);
		dialog.setCancelable(true);

		TextView dialogTitleTV = (TextView) dialog
				.findViewById(R.id.brandsListTitleTV);

		Booking.changeWidthHeight(getActivity(), dialog);
		// dialog.setP
		lv = (ListView) dialog.findViewById(R.id.addLV);

		if (dataBaseHandler.getModelsCount() == 0) {
			if (carFunctions.isConnectingToInternet()) {
				new CarModel().execute();
			} else {
				Toast.makeText(getActivity(), "Check your internet connection",
						Toast.LENGTH_LONG).show();
			}
		} else {
			List<Model> modelList = dataBaseHandler.getAllModels();
			carsModelList.clear();
			for (int i = 0; i < modelList.size(); i++) {
				if (modelList.get(i).getParentBrand()
						.equals(brandTV.getText().toString())
						|| modelList
								.get(i)
								.getParentBrand()
								.equals(dataBaseHandler.getBrand(
										brandIdSelected).getTypeNameEn())) {
					HashMap<String, String> model = new HashMap<String, String>();

					model.put("modelId", modelList.get(i).getTypeId());
					model.put("modelNameAr", modelList.get(i).getTypeNameAr());
					model.put("modelNameEn", modelList.get(i).getTypeNameEn());

					carsModelList.add(model);
				}
			}

			if (carsModelList.size() == 0) {
				if (carFunctions.isConnectingToInternet()) {
					for (int i = 0; i < modelList.size(); i++) {
						if (modelList.get(i).getParentBrand()
								.equals(brandTV.getText().toString())
								|| modelList
										.get(i)
										.getParentBrand()
										.equals(dataBaseHandler.getBrand(
												brandIdSelected)
												.getTypeNameEn())) {
							dataBaseHandler.deleteSingleBrand(modelList.get(i)
									.getId());
						}
					}
					new CarModel().execute();
				} else {
					Toast.makeText(getActivity(),
							"Check your internet connection", Toast.LENGTH_LONG)
							.show();
				}
			} else {
				ListAdapter adapter = null;
				if (setting.getDuration() == 0) {
					dialog.findViewById(R.id.titleIVAr)
							.setVisibility(View.GONE);
					dialogTitleTV.setText(getResources().getString(
							R.string.model_list_en));

					adapter = new SimpleAdapter(getActivity(), carsModelList,
							R.layout.single_model_item, new String[] {
									"modelId", "modelNameEn" }, new int[] {
									R.id.modelId, R.id.modelNameEn });
				} else {
					dialog.findViewById(R.id.titleIVEn)
							.setVisibility(View.GONE);
					dialogTitleTV.setText(getResources().getString(
							R.string.model_list_ar));

					adapter = new SimpleAdapter(getActivity(), carsModelList,
							R.layout.single_model_item, new String[] {
									"modelId", "modelNameAr" }, new int[] {
									R.id.modelId, R.id.modelNameEn });
				}

				lv.setAdapter(adapter);
				dialog.show();
			}

		}
		swipeView = (SwipeRefreshLayout) dialog.findViewById(R.id.swipeDialog);
		swipeView
				.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
					@Override
					public void onRefresh() {
						swipeView.setRefreshing(true);
						if (carFunctions.isConnectingToInternet()) {
							// new CarModel().execute();
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
				String s = ((TextView) view.findViewById(R.id.modelNameEn))
						.getText().toString();
				modelTV.setText(s);
				String sId = ((TextView) view.findViewById(R.id.modelId))
						.getText().toString();
				// modelTV.setText(s);
				modelIdTV.setText(sId);
				dialog.dismiss();
			}
		});

	}

	public class CarModel extends AsyncTask<Void, Void, Void> {
		@Override
		public void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			carsModelList.clear();
			progress = ProgressDialog.show(AddCar.this.getActivity(), "",
					"Loading...");
		}

		protected Void doInBackground(Void... arg0) {
			// Creating service handler class instance
			String url = URL;
			try {
				JSONObject jsonObj = jP.getJSONFromUrl(url);
				Log.d("Response: ", "> " + jsonObj);
				carsList = jsonObj.getString("carsList");
				JSONArray carsJsonObject = new JSONArray(carsList);
				for (int i = 0; i < carsJsonObject.length(); i++) {
					JSONObject c = carsJsonObject.getJSONObject(i);
					typeNameEn = c.getString("typeNameEn");
					carModels = c.getString("carModels");
					JSONArray modelsJsonObject = new JSONArray(carModels);
					for (int ii = 0; ii < modelsJsonObject.length(); ii++) {
						JSONObject cc = modelsJsonObject.getJSONObject(ii);

						modelId = cc.getString("modelId");
						modelNameAr = cc.getString("modelNameAr");
						modelNameEn = cc.getString("modelNameEn");

						String s = brandTV.getText().toString();
						if (s.equals(typeNameEn)) {
							model = new HashMap<String, String>();
							model.put("modelId", modelId);
							model.put("modelNameAr", modelNameAr);
							model.put("modelNameEn", modelNameEn);

							// Save in database
							Model modelRow = new Model();
							modelRow.setTypeId(modelId);
							modelRow.setTypeNameAr(modelNameAr);
							modelRow.setTypeNameEn(modelNameEn);
							modelRow.setParentBrand(s);
							dataBaseHandler.addModel(modelRow);

							carsModelList.add(model);
							continue;
						}
					}
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
			if (progress.isShowing()) {
				progress.dismiss();
			}
			ListAdapter adapter = new SimpleAdapter(getActivity(),
					carsModelList, R.layout.single_model_item, new String[] {
							"modelId", "modelNameEn" }, new int[] {
							R.id.modelId, R.id.modelNameEn });

			lv.setAdapter(adapter);
			dialog.show();
		}
	}

	@SuppressLint("InlinedApi")
	@SuppressWarnings("deprecation")
	public void showColorDialog() {
		dialog.setContentView(R.layout.add_car_dialog);
		dialog.setCancelable(true);

		TextView dialogTitleTV = (TextView) dialog
				.findViewById(R.id.brandsListTitleTV);

		Booking.changeWidthHeight(getActivity(), dialog);
		// dialog.setP
		lv = (ListView) dialog.findViewById(R.id.addLV);

		if (dataBaseHandler.getColorsCount() == 0) {
			if (carFunctions.isConnectingToInternet()) {
				new CarColor().execute();
			} else {
				Toast.makeText(getActivity(), "Check your internet connection",
						Toast.LENGTH_LONG).show();
			}
		} else {

			List<Color> colorList = dataBaseHandler.getAllColors();

			carsColorList.clear();
			for (int i = 0; i < colorList.size(); i++) {
				HashMap<String, String> color = new HashMap<String, String>();
				color.put("id", colorList.get(i).getColorId());
				color.put("coloAr", colorList.get(i).getColorAr());
				color.put("colorEn", colorList.get(i).getColorEn());

				carsColorList.add(color);
			}
			ListAdapter adapter = null;

			if (setting.getDuration() == 0) {
				dialog.findViewById(R.id.titleIVAr).setVisibility(View.GONE);
				dialogTitleTV.setText(getResources().getString(
						R.string.color_list_en));

				adapter = new SimpleAdapter(getActivity(), carsColorList,
						R.layout.single_color_item, new String[] { "id",
								"colorEn" },
						new int[] { R.id.id, R.id.colorEn });
			} else {
				dialog.findViewById(R.id.titleIVEn).setVisibility(View.GONE);
				dialogTitleTV.setText(getResources().getString(
						R.string.color_list_ar));

				adapter = new SimpleAdapter(getActivity(), carsColorList,
						R.layout.single_color_item, new String[] { "id",
								"coloAr" }, new int[] { R.id.id, R.id.colorEn });
			}

			lv.setAdapter(adapter);

			swipeView = (SwipeRefreshLayout) dialog
					.findViewById(R.id.swipeDialog);
			swipeView
					.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
						@Override
						public void onRefresh() {
							swipeView.setRefreshing(true);
							if (carFunctions.isConnectingToInternet()) {
								new CarColor().execute();
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
					String colorEn = ((TextView) view
							.findViewById(R.id.colorEn)).getText().toString();
					String idC = ((TextView) view.findViewById(R.id.id))
							.getText().toString();
					colorNameTV.setText(colorEn);
					colorIdTV.setText(idC);
					dialog.dismiss();
				}
			});

			dialog.show();
		}
	}

	public class CarColor extends AsyncTask<Void, Void, Void> {

		@Override
		public void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			carsColorList.clear();
			progress = ProgressDialog.show(AddCar.this.getActivity(), "",
					"Loading...");
		}

		protected Void doInBackground(Void... arg0) {
			// Creating service handler class instance
			String url = colorURL;
			try {
				dataBaseHandler.deleteAllColors();
				JSONObject jsonObj = jP.getJSONFromUrl(url);
				Log.d("Response: ", "> " + jsonObj);
				userCarsDTOs = jsonObj.getString("userCarsDTOs");
				JSONArray colorJsonObject = new JSONArray(userCarsDTOs);
				for (int i = 0; i < colorJsonObject.length(); i++) {
					JSONObject c = colorJsonObject.getJSONObject(i);
					id = c.getString("id");
					colorAr = c.getString("coloAr");
					colorEn = c.getString("colorEn");

					Color colorRow = new Color();
					colorRow.setColorId(id);
					colorRow.setColorAr(colorAr);
					colorRow.setColorEn(colorEn);

					dataBaseHandler.addColor(colorRow);

					HashMap<String, String> color = new HashMap<String, String>();
					color.put("id", id);
					color.put("coloAr", colorAr);
					color.put("colorEn", colorEn);

					carsColorList.add(color);
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		@SuppressLint("InlinedApi")
		@SuppressWarnings("deprecation")
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// Dismiss the progress dialog
			if (progress.isShowing()) {
				progress.dismiss();
			}
			TextView dialogTitleTV = (TextView) dialog
					.findViewById(R.id.brandsListTitleTV);
			List<Color> colorList = dataBaseHandler.getAllColors();

			carsColorList.clear();
			for (int i = 0; i < colorList.size(); i++) {
				HashMap<String, String> color = new HashMap<String, String>();
				color.put("id", colorList.get(i).getColorId());
				color.put("coloAr", colorList.get(i).getColorAr());
				color.put("colorEn", colorList.get(i).getColorEn());

				carsColorList.add(color);
			}
			ListAdapter adapter = null;

			if (setting.getDuration() == 0) {
				dialog.findViewById(R.id.titleIVAr).setVisibility(View.GONE);
				dialogTitleTV.setText(getResources().getString(
						R.string.color_list_en));

				adapter = new SimpleAdapter(getActivity(), carsColorList,
						R.layout.single_color_item, new String[] { "id",
								"colorEn" },
						new int[] { R.id.id, R.id.colorEn });
			} else {
				dialog.findViewById(R.id.titleIVEn).setVisibility(View.GONE);
				dialogTitleTV.setText(getResources().getString(
						R.string.color_list_ar));

				adapter = new SimpleAdapter(getActivity(), carsColorList,
						R.layout.single_color_item, new String[] { "id",
								"coloAr" }, new int[] { R.id.id, R.id.colorEn });
			}

			lv.setAdapter(adapter);

			swipeView = (SwipeRefreshLayout) dialog
					.findViewById(R.id.swipeDialog);
			swipeView
					.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
						@Override
						public void onRefresh() {
							swipeView.setRefreshing(true);
							if (carFunctions.isConnectingToInternet()) {
								new CarColor().execute();
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
					String colorEn = ((TextView) view
							.findViewById(R.id.colorEn)).getText().toString();
					String idC = ((TextView) view.findViewById(R.id.id))
							.getText().toString();
					colorNameTV.setText(colorEn);
					colorIdTV.setText(idC);
					dialog.dismiss();
				}
			});

			dialog.show();
		}
	}

	@SuppressLint("InlinedApi")
	@SuppressWarnings("deprecation")
	public void showInsuranceDialog() {
		dialog.setContentView(R.layout.add_car_dialog);
		dialog.setCancelable(true);
		dialog.setTitle("Choose an Insurance company");

		TextView dialogTitleTV = (TextView) dialog
				.findViewById(R.id.brandsListTitleTV);

		Booking.changeWidthHeight(getActivity(), dialog);
		// dialog.setP
		lv = (ListView) dialog.findViewById(R.id.addLV);
		swipeView = (SwipeRefreshLayout) dialog.findViewById(R.id.swipeDialog);
		if (dataBaseHandler.getInsuranceCount() == 0) {
			if (carFunctions.isConnectingToInternet()) {
				new InsuranceAsyncTask().execute();
			} else {
				Toast.makeText(getActivity(), "Check your internet connection",
						Toast.LENGTH_LONG).show();
			}
		} else {
			insuracneCompanyList.clear();
			ArrayList<Insurance> insuranceArrayList = dataBaseHandler
					.getAllInsurance();
			for (int i = 0; i < insuranceArrayList.size(); i++) {
				HashMap<String, String> insurance = new HashMap<String, String>();
				insurance.put("id", insuranceArrayList.get(i).getId() + "");
				insurance.put("nameAr", insuranceArrayList.get(i).getNameAr());
				insurance.put("nameEn", insuranceArrayList.get(i).getNameEn());

				insuracneCompanyList.add(insurance);
			}

			ListAdapter adapter = null;

			if (setting.getDuration() == 0) {
				dialog.findViewById(R.id.titleIVAr).setVisibility(View.GONE);
				dialogTitleTV.setText(getResources().getString(
						R.string.company_list_en));

				adapter = new SimpleAdapter(getActivity(),
						insuracneCompanyList, R.layout.single_insurance_item,
						new String[] { "id", "nameEn" }, new int[] {
								R.id.insuranceId, R.id.nameEn });
			} else {
				dialog.findViewById(R.id.titleIVEn).setVisibility(View.GONE);
				dialogTitleTV.setText(getResources().getString(
						R.string.company_list_ar));

				adapter = new SimpleAdapter(getActivity(),
						insuracneCompanyList, R.layout.single_insurance_item,
						new String[] { "id", "nameAr" }, new int[] {
								R.id.insuranceId, R.id.nameEn });
			}

			lv.setAdapter(adapter);

			swipeView
					.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
						@Override
						public void onRefresh() {
							swipeView.setRefreshing(true);
							if (carFunctions.isConnectingToInternet()) {
								new InsuranceAsyncTask().execute();
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
					String nameEn = ((TextView) view.findViewById(R.id.nameEn))
							.getText().toString();
					String idC = ((TextView) view
							.findViewById(R.id.insuranceId)).getText()
							.toString();
					insuranceTV.setText(nameEn);
					insuranceIdTV.setText(idC);
					dialog.dismiss();
				}
			});
			dialog.show();
		}
	}

	public class InsuranceAsyncTask extends AsyncTask<Void, Void, Void> {

		@Override
		public void onPreExecute() {
			super.onPreExecute();
			insuracneCompanyList.clear();
			progress = ProgressDialog.show(AddCar.this.getActivity(), "",
					"Loading...");
		}

		protected Void doInBackground(Void... arg0) {
			carFunctions.saveJSONInsuranceCompany();
			return null;
		}

		@SuppressLint("InlinedApi")
		@SuppressWarnings("deprecation")
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// Dismiss the progress dialog
			if (progress.isShowing()) {
				progress.dismiss();
			}

			TextView dialogTitleTV = (TextView) dialog
					.findViewById(R.id.brandsListTitleTV);

			insuracneCompanyList.clear();
			ArrayList<Insurance> insuranceArrayList = dataBaseHandler
					.getAllInsurance();
			for (int i = 0; i < insuranceArrayList.size(); i++) {
				HashMap<String, String> insurance = new HashMap<String, String>();
				insurance.put("id", insuranceArrayList.get(i).getId() + "");
				insurance.put("nameAr", insuranceArrayList.get(i).getNameAr());
				insurance.put("nameEn", insuranceArrayList.get(i).getNameEn());

				insuracneCompanyList.add(insurance);
			}

			ListAdapter adapter = null;

			if (setting.getDuration() == 0) {
				dialog.findViewById(R.id.titleIVAr).setVisibility(View.GONE);
				dialogTitleTV.setText(getResources().getString(
						R.string.company_list_en));

				adapter = new SimpleAdapter(getActivity(),
						insuracneCompanyList, R.layout.single_insurance_item,
						new String[] { "id", "nameEn" }, new int[] {
								R.id.insuranceId, R.id.nameEn });
			} else {
				dialog.findViewById(R.id.titleIVEn).setVisibility(View.GONE);
				dialogTitleTV.setText(getResources().getString(
						R.string.company_list_ar));

				adapter = new SimpleAdapter(getActivity(),
						insuracneCompanyList, R.layout.single_insurance_item,
						new String[] { "id", "nameAr" }, new int[] {
								R.id.insuranceId, R.id.nameEn });
			}

			lv.setAdapter(adapter);

			swipeView
					.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
						@Override
						public void onRefresh() {
							swipeView.setRefreshing(true);
							if (carFunctions.isConnectingToInternet()) {
								new InsuranceAsyncTask().execute();
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
					String nameEn = ((TextView) view.findViewById(R.id.nameEn))
							.getText().toString();
					String idC = ((TextView) view
							.findViewById(R.id.insuranceId)).getText()
							.toString();
					insuranceTV.setText(nameEn);
					insuranceIdTV.setText(idC);
					dialog.dismiss();
				}
			});
			dialog.show();
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
