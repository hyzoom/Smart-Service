package smart.services.adapter;

import info.androidhive.slidingmenu.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import smart.services.fragment.Booking;
import smart.services.functions.CarFunctions;
import smart.services.handler.DataBaseHandler;
import smart.services.handler.JSONParser;
import smart.services.model.Brand;
import smart.services.model.CarTest;
import smart.services.model.Color;
import smart.services.model.Insurance;
import smart.services.model.Model;
import smart.services.model.Setting;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class ManageCarsAdapter extends ArrayAdapter<CarTest> {
	private Context context;
	private TextView brandNameTV, modelNameTV, yearTV, licenseNumberTV,
			colorTV, dialogYearTV;
	private LinearLayout editLayout, deleteLayout;
	private ArrayList<CarTest> carList = new ArrayList<CarTest>();
	private LayoutInflater inflator;
	private JSONParser jsonParser;
	private DataBaseHandler dataBaseHandler;
	private ProgressDialog progress;
	private Dialog dialog, editDialog;
	private Setting setting;

	private static String URL = "http://smarty-trioplus.rhcloud.com/supportCenter/CarList";
	private static String colorURL = "http://smarty-trioplus.rhcloud.com/supportCenter/getCarColors";
	private static String insuranceURL = "http://smarty-trioplus.rhcloud.com/supportCenter/insuranceCompanyList";
	private static String updateCarURL = "http://smarty-trioplus.rhcloud.com/supportCenter/updateCar?";

	private CarFunctions carFunctions;

	private String finalUserId, finalCarID, finalChase, finalLicenseNumber,
			finalColorId, finalModelId, finalPlateNum, insuranceCompany,
			nameEn, nameAr, finalTypeId, finalYear, finalChaseNumber,
			finalLicenceNumber, finalInsuranceId;

	String carsList, typeId, typeNameAr, typeNameEn;
	String carModels, modelId, modelNameAr, modelNameEn;
	String userCarsDTOs, id, colorAr, colorEn;

	TextView brandTV, brandIdTV, modelTV, modelIdTV, colorNameTV, insuranceTV,
			insuranceIdTV, errorMessageTV, colorIdTV;
	EditText memberName, chassisEt, licenseNumberET;
	Button submit;
	ArrayList<HashMap<String, String>> carsBrandList, carsModelList,
			carsColorList, insuracneCompanyList;
	HashMap<String, String> model;

	ListView lv;

	public ManageCarsAdapter(Context context, int textViewResourceId,
			ArrayList<CarTest> carList) {
		super(context, textViewResourceId, carList);
		this.carList = carList;
		this.context = context;
		inflator = LayoutInflater.from(context);
	}

	@SuppressLint({ "ViewHolder", "InflateParams" })
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		jsonParser = new JSONParser();
		dataBaseHandler = new DataBaseHandler(context);
		dialog = new Dialog(context);
		editDialog = new Dialog(context);
		carFunctions = new CarFunctions(context);
		setting = dataBaseHandler.getSetting();

		View view = inflator.inflate(R.layout.car_list_adapter, parent, false);

		brandNameTV = (TextView) view.findViewById(R.id.brand_name);
		modelNameTV = (TextView) view.findViewById(R.id.model_text_view);
		yearTV = (TextView) view.findViewById(R.id.year_textView);
		licenseNumberTV = (TextView) view.findViewById(R.id.licence_number_text_view);
		colorTV = (TextView) view.findViewById(R.id.color_textView);

		editLayout = (LinearLayout) view.findViewById(R.id.edit_layout);
		deleteLayout = (LinearLayout) view.findViewById(R.id.delete_layout);

		if (setting.getDuration() == 0) {
			brandNameTV.setText(dataBaseHandler.getBrand(
					dataBaseHandler.getModel(carList.get(position).getCarModelId())
							.getParentBrand()).getTypeNameEn());
			modelNameTV.setText(dataBaseHandler.getModel(
					carList.get(position).getCarModelId()).getTypeNameEn());
			yearTV.setText(carList.get(position).getYear());

			licenseNumberTV.setText(carList.get(position).getPlateNumber());
			colorTV.setText(dataBaseHandler.getColor(
					carList.get(position).getCarColor()).getColorEn());
			
		} else {
			brandNameTV.setText(dataBaseHandler.getBrand(
					dataBaseHandler.getModel(carList.get(position).getCarModelId())
							.getParentBrand()).getTypeNameAr());
			modelNameTV.setText(dataBaseHandler.getModel(
					carList.get(position).getCarModelId()).getTypeNameAr());
			yearTV.setText(carList.get(position).getYear());

			licenseNumberTV.setText(carList.get(position).getPlateNumber());
			colorTV.setText(dataBaseHandler.getColor(
					carList.get(position).getCarColor()).getColorAr());
		}
		
		deleteLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				deleteDialog(position);
			}
		});

		editLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				updateDialog(position);
			}
		});
		return view;
	}

	private void deleteDialog(final int position) {
		final TextView input = new TextView(context);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				context);
		alertDialogBuilder.setTitle("Delete");

		alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					@SuppressLint("DefaultLocale")
					public void onClick(DialogInterface dialog, int id) {
						DeleteCar deleteCar = new DeleteCar(position);
						deleteCar.execute();
					}
				})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		input.setText("Are You sure You want to delete this record ?");
		input.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
		input.setTextSize(20);
		input.setLayoutParams(lp);
		alertDialog.setView(input);
		alertDialog.show();
	}

	public class DeleteCar extends AsyncTask<Void, Void, Void> {
		private int position;
		private String carId;
		private String url, eCode;

		public DeleteCar(int position) {
			this.position = position;
			this.carId = carList.get(position).getCarId();
			this.url = "http://smarty-trioplus.rhcloud.com/supportCenter/deleteCar?carId="
					+ carId;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progress = ProgressDialog.show(context, "", "Loading...");
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				JSONObject jsonObj = jsonParser.getJSONFromUrl(url);
				Log.d("Response: ", "> " + jsonObj);
				eCode = jsonObj.getString("eCode");

			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// Log.e("carId", carList.get(position).getCarId());
			// Log.e("befor", carList.get(position).getId()+"");

//			Toast.makeText(context, eCode, Toast.LENGTH_LONG).show();

			if (eCode.equals("0")) {
				Toast.makeText(context, "Record deleted Successfully",
						Toast.LENGTH_LONG).show();
				Log.e("android", carList.get(position).getId() + "");
				dataBaseHandler.deleteSingleCar(carList.get(position).getId());
				carList.remove(position);
				notifyDataSetChanged();
			}

			if (progress.isShowing()) {
				progress.dismiss();
			}

			super.onPostExecute(result);
		}

	}

	private void updateDialog(int position) {
		final CarTest car = carList.get(position);

		finalCarID = car.getCarId();
		finalChaseNumber = car.getChase();
		finalColorId = car.getCarColor();
		finalLicenceNumber = car.getPlateNumber();
		finalModelId = car.getCarModelId();
		finalTypeId = dataBaseHandler.getBrand(
				dataBaseHandler.getModel(car.getCarModelId()).getParentBrand())
				.getTypeId();
		finalYear = car.getYear();
		finalInsuranceId = car.getInsuranceId() + "";
		editDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		editDialog.setContentView(R.layout.add_car);
		editDialog.setTitle("Update a car");

		Booking.changeWidthHeight(context, editDialog);

		submit = (Button) editDialog.findViewById(R.id.submit);
		brandTV = (TextView) editDialog.findViewById(R.id.brand);
		brandIdTV = (TextView) editDialog.findViewById(R.id.brandId);
		modelTV = (TextView) editDialog.findViewById(R.id.model);
		modelIdTV = (TextView) editDialog.findViewById(R.id.modelId);
		colorNameTV = (TextView) editDialog.findViewById(R.id.color);
		colorIdTV = (TextView) editDialog.findViewById(R.id.colorId);
		dialogYearTV = (TextView) editDialog.findViewById(R.id.year);
		insuranceTV = (TextView) editDialog.findViewById(R.id.insuranceTV);
		insuranceIdTV = (TextView) editDialog.findViewById(R.id.insuranceIdTV);
		errorMessageTV = (TextView) editDialog.findViewById(R.id.errorMsg);

		chassisEt = (EditText) editDialog.findViewById(R.id.chassis);
		memberName = (EditText) editDialog.findViewById(R.id.memberName);
		memberName.setVisibility(View.GONE);

		licenseNumberET = (EditText) editDialog
				.findViewById(R.id.licenseNumber);

		memberName.setText(dataBaseHandler.getUser(1).getName());

		licenseNumberET.setText(car.getPlateNumber());
		brandTV.setText(dataBaseHandler.getBrand(
				dataBaseHandler.getModel(car.getCarModelId()).getParentBrand())
				.getTypeNameEn());
		modelTV.setText(dataBaseHandler.getModel(car.getCarModelId())
				.getTypeNameEn());
		dialogYearTV.setText(car.getYear());
		colorNameTV.setText(dataBaseHandler.getColor(car.getCarColor())
				.getColorEn());
		chassisEt.setText(car.getChase());

		insuranceTV.setText(dataBaseHandler.getInsurance(car.getId())
				.getNameEn());
		init();

		submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				UpdateCar updateCarTask = new UpdateCar(context);
				updateCarTask.execute();

				if (editDialog.isShowing()) {
					editDialog.dismiss();
				}
			}
		});

		editDialog.show();
	}

	public void init() {
		carsBrandList = new ArrayList<HashMap<String, String>>();
		carsModelList = new ArrayList<HashMap<String, String>>();
		carsColorList = new ArrayList<HashMap<String, String>>();
		insuracneCompanyList = new ArrayList<HashMap<String, String>>();

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
					Toast.makeText(context,
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
		dialogYearTV.setOnClickListener(new OnClickListener() {
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

	public class UpdateCar extends AsyncTask<Void, Void, Void> {
		private String updatUrl, eCode;

		public UpdateCar(Context context) {
			finalLicenceNumber = licenseNumberET.getText().toString();

			this.updatUrl = updateCarURL + "carId=" + finalCarID
					+ "&chaseNumber=" + finalChaseNumber + "&color="
					+ finalColorId + "&modelId=" + finalModelId
					+ "&plateNumber=" + finalLicenceNumber + "&typeId="
					+ finalTypeId + "&year=" + finalYear + "&insuranceId="
					+ finalInsuranceId;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progress = ProgressDialog.show(context, "", "Loading...");
		}

		@Override
		protected Void doInBackground(Void... params) {
			Log.e("andoid", updatUrl);
			JSONObject jsonObj = jsonParser.getJSONFromUrl(updatUrl);
			Log.d("Response: ", "> " + jsonObj);
			try {
				eCode = jsonObj.getString("eCode");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// Log.e("carId", carList.get(position).getCarId());
			super.onPostExecute(result);
			if (progress.isShowing()) {
				progress.dismiss();
			}
			if (eCode.equals("0")) {
				Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
				((Activity) context).finish();
				Intent intent = new Intent(context, context.getClass());
				intent.putExtra("fragNum", "6");
				context.startActivity(intent);

			}
		}

	}

	public void showYearDialog() {
		dialog.setContentView(R.layout.add_car_dialog);
		dialog.setCancelable(true);
		dialog.setTitle("Choose a Year");
		Booking.changeWidthHeight(context, dialog);
		lv = (ListView) dialog.findViewById(R.id.addLV);

		String years[] = { "2016", "2015", "2014", "2013", "2012", "2011",
				"2010", "2009", "2008", "2007", "2007", "2005", "2004", "2003",
				"2002", "2001", "2000" };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
				android.R.layout.simple_list_item_1, years);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				dialogYearTV.setText(lv.getItemAtPosition(position).toString());
				finalYear = lv.getItemAtPosition(position).toString();
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	public void showBrandDialog() {
		if (dataBaseHandler.getBrandsCount() == 0) {
			if (isConnectingToInternet()) {
				new BrandAsyncTask().execute();
			} else {
				Toast.makeText(context, "Check your internet connection",
						Toast.LENGTH_LONG).show();
			}

		} else {
			dialog.setContentView(R.layout.add_car_dialog);
			dialog.setCancelable(true);
			dialog.setTitle("Choose a Brand");
			Booking.changeWidthHeight(context, dialog);
			// dialog.setP
			lv = (ListView) dialog.findViewById(R.id.addLV);
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

			ListAdapter adapter = new SimpleAdapter(context, carsBrandList,
					R.layout.single_brand_item, new String[] { "typeId",
							"typeNameEn" }, new int[] { R.id.typeId,
							R.id.typeNameEn });

			lv.setAdapter(adapter);

			lv.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
					finalTypeId = carsBrandList.get(position).get("typeId");
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

	private class BrandAsyncTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progress = ProgressDialog.show(context, "", "Loading...");
		}

		@Override
		protected Void doInBackground(Void... params) {
			carFunctions.saveJSONBrandAndModels();
			return null;
		}

		@Override
		protected void onPostExecute(Void aVoid) {
			super.onPostExecute(aVoid);
			if (progress.isShowing()) {
				progress.dismiss();
			}

			dialog.setContentView(R.layout.add_car_dialog);
			dialog.setCancelable(true);
			dialog.setTitle("Choose a Brand");
			Booking.changeWidthHeight(context, dialog);
			// dialog.setP
			lv = (ListView) dialog.findViewById(R.id.addLV);

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

			ListAdapter adapter = new SimpleAdapter(context, carsBrandList,
					R.layout.single_brand_item, new String[] { "typeId",
							"typeNameEn" }, new int[] { R.id.typeId,
							R.id.typeNameEn });

			lv.setAdapter(adapter);

			lv.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					finalTypeId = carsBrandList.get(position).get("typeId");
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

	public void showModelDialog() {
		dialog.setContentView(R.layout.add_car_dialog);
		dialog.setCancelable(true);
		dialog.setTitle("Choose a Model");
		Booking.changeWidthHeight(context, dialog);
		// dialog.setP
		lv = (ListView) dialog.findViewById(R.id.addLV);

		if (dataBaseHandler.getModelsCount() == 0) {
			if (isConnectingToInternet()) {
				new CarModel().execute();
			} else {
				Toast.makeText(context, "Check your internet connection",
						Toast.LENGTH_LONG).show();
			}
		} else {
			List<Model> modelList = dataBaseHandler.getAllModels();
			carsModelList.clear();
			for (int i = 0; i < modelList.size(); i++) {
				if (modelList.get(i).getParentBrand()
						.equals(brandTV.getText().toString())) {
					HashMap<String, String> model = new HashMap<String, String>();

					model.put("modelId", modelList.get(i).getTypeId());
					model.put("modelNameAr", modelList.get(i).getTypeNameAr());
					model.put("modelNameEn", modelList.get(i).getTypeNameEn());

					carsModelList.add(model);
				}
			}

			if (carsModelList.size() == 0) {
				if (isConnectingToInternet()) {
					for (int i = 0; i < modelList.size(); i++) {
						if (modelList.get(i).getParentBrand()
								.equals(brandTV.getText().toString())) {
							dataBaseHandler.deleteSingleBrand(modelList.get(i)
									.getId());
						}
					}
					new CarModel().execute();
				} else {
					Toast.makeText(context, "Check your internet connection",
							Toast.LENGTH_LONG).show();
				}
			} else {
				ListAdapter adapter = new SimpleAdapter(context, carsModelList,
						R.layout.single_model_item, new String[] { "modelId",
								"modelNameEn" }, new int[] { R.id.modelId,
								R.id.modelNameEn });

				lv.setAdapter(adapter);
				lv.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						finalModelId = carsModelList.get(position).get(
								"modelId");
						String s = ((TextView) view
								.findViewById(R.id.modelNameEn)).getText()
								.toString();
						modelTV.setText(s);
						String sId = ((TextView) view
								.findViewById(R.id.modelId)).getText()
								.toString();
						// modelTV.setText(s);
						modelIdTV.setText(sId);
						dialog.dismiss();
					}
				});

				dialog.show();
			}
		}
	}

	public class CarModel extends AsyncTask<Void, Void, Void> {
		@Override
		public void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			carsModelList.clear();
			progress = ProgressDialog.show(context, "", "Loading...");
		}

		protected Void doInBackground(Void... arg0) {
			// Creating service handler class instance
			String url = URL;
			try {
				JSONObject jsonObj = jsonParser.getJSONFromUrl(url);
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
			ListAdapter adapter = new SimpleAdapter(context, carsModelList,
					R.layout.single_model_item, new String[] { "modelId",
							"modelNameEn" }, new int[] { R.id.modelId,
							R.id.modelNameEn });

			lv.setAdapter(adapter);
			lv.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					finalModelId = carsModelList.get(position).get("modelId");
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
			dialog.show();
		}
	}

	public void showColorDialog() {
		dialog.setContentView(R.layout.add_car_dialog);
		dialog.setCancelable(true);
		dialog.setTitle("Choose a Color");
		Booking.changeWidthHeight(context, dialog);
		// dialog.setP
		lv = (ListView) dialog.findViewById(R.id.addLV);

		if (dataBaseHandler.getColorsCount() == 0) {
			if (isConnectingToInternet()) {
				new CarColor().execute();
			} else {
				Toast.makeText(context, "Check your internet connection",
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
			ListAdapter adapter = new SimpleAdapter(context, carsColorList,
					R.layout.single_color_item,
					new String[] { "id", "colorEn" }, new int[] { R.id.id,
							R.id.colorEn });

			lv.setAdapter(adapter);

			lv.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					finalColorId = carsColorList.get(position).get("id");
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
			progress = ProgressDialog.show(context, "", "Loading...");
		}

		protected Void doInBackground(Void... arg0) {
			// Creating service handler class instance
			String url = colorURL;
			try {
				dataBaseHandler.deleteAllColors();
				JSONObject jsonObj = jsonParser.getJSONFromUrl(url);
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

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// Dismiss the progress dialog
			if (progress.isShowing()) {
				progress.dismiss();
			}
			ListAdapter adapter = new SimpleAdapter(context, carsColorList,
					R.layout.single_color_item,
					new String[] { "id", "colorEn" }, new int[] { R.id.id,
							R.id.colorEn });

			lv.setAdapter(adapter);

			lv.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					finalColorId = carsColorList.get(position).get("id");
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

	public void showInsuranceDialog() {
		dialog.setContentView(R.layout.add_car_dialog);
		dialog.setCancelable(true);
		dialog.setTitle("Choose an Insurance company");
		Booking.changeWidthHeight(context, dialog);
		// dialog.setP
		lv = (ListView) dialog.findViewById(R.id.addLV);

		if (dataBaseHandler.getInsuranceCount() == 0) {
			if (isConnectingToInternet()) {
				new InsuranceCompany().execute();
			} else {
				Toast.makeText(context, "Check your internet connection",
						Toast.LENGTH_LONG).show();
			}
		} else {

			ArrayList<Insurance> insurances = dataBaseHandler.getAllInsurance();
			insuracneCompanyList.clear();

			for (int i = 0; i < insurances.size(); i++) {
				HashMap<String, String> insurance = new HashMap<String, String>();
				insurance.put("id", insurances.get(i).getId() + "");
				insurance.put("nameAr", insurances.get(i).getNameAr());
				insurance.put("nameEn", insurances.get(i).getNameEn());

				insuracneCompanyList.add(insurance);
			}
			ListAdapter adapter = new SimpleAdapter(context,
					insuracneCompanyList, R.layout.single_insurance_item,
					new String[] { "id", "nameEn" }, new int[] {
							R.id.insuranceId, R.id.nameEn });

			lv.setAdapter(adapter);

			lv.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					finalInsuranceId = insuracneCompanyList.get(position).get(
							"id");

					String idC = ((TextView) view
							.findViewById(R.id.insuranceId)).getText()
							.toString();
					insuranceIdTV.setText(idC);
					insuranceTV.setText(dataBaseHandler.getInsurance(
							Integer.parseInt(finalInsuranceId)).getNameEn());
					dialog.dismiss();
				}
			});
			dialog.show();
		}
	}

	public class InsuranceCompany extends AsyncTask<Void, Void, Void> {

		@Override
		public void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			// carsColorList.clear();
			insuracneCompanyList.clear();
			progress = ProgressDialog.show(context, "", "Loading...");
		}

		protected Void doInBackground(Void... arg0) {
			// Creating service handler class instance
			String url = insuranceURL;
			try {
				// dBCT.deleteAllColors();
				JSONObject jsonObj = jsonParser.getJSONFromUrl(url);
				Log.d("Response: ", "> " + jsonObj);
				insuranceCompany = jsonObj.getString("insuranceCompany");
				JSONArray insuranceJsonObject = new JSONArray(insuranceCompany);
				for (int i = 0; i < insuranceJsonObject.length(); i++) {
					JSONObject c = insuranceJsonObject.getJSONObject(i);
					id = c.getString("id");
					nameAr = c.getString("nameAr");
					nameEn = c.getString("nameEn");

					HashMap<String, String> insurance = new HashMap<String, String>();
					insurance.put("id", id);
					insurance.put("nameAr", nameAr);
					insurance.put("nameEn", nameEn);

					insuracneCompanyList.add(insurance);
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
			ListAdapter adapter = new SimpleAdapter(context,
					insuracneCompanyList, R.layout.single_insurance_item,
					new String[] { "id", "nameEn" }, new int[] {
							R.id.insuranceId, R.id.nameEn });

			lv.setAdapter(adapter);

			lv.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					String nameEn = ((TextView) view.findViewById(R.id.nameEn))
							.getText().toString();
					String idC = ((TextView) view
							.findViewById(R.id.insuranceId)).getText()
							.toString();
					// dataBaseHandler.getInsurance(
					// Integer.parseInt(insuracneCompanyList.get(position)
					// .get("id"))).getNameEn()

					// insuranceTV.setText("sa3eedoof " + finalInsuranceId);
					insuranceIdTV.setText(idC);
					dialog.dismiss();
				}
			});
			dialog.show();
		}
	}

	public boolean isConnectingToInternet() {
		ConnectivityManager connectivity = (ConnectivityManager) context
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