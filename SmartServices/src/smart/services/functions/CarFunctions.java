package smart.services.functions;

import java.util.Calendar;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import smart.services.handler.DataBaseHandler;
import smart.services.handler.JSONParser;
import smart.services.model.Brand;
import smart.services.model.CarTest;
import smart.services.model.Color;
import smart.services.model.Insurance;
import smart.services.model.Model;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class CarFunctions {

	private Context context;
	private DataBaseHandler dataBaseHandler;
	private JSONParser jsonParser;

	public CarFunctions(Context context) {
		this.context = context;
		dataBaseHandler = new DataBaseHandler(context);
		jsonParser = new JSONParser();
	}

	// /////////////////////json////////////////////
	// /////////////////get all cars ///////////////

	/**
	 * get cars from json and save them into database
	 */
	public void saveJSONUserCars() {
		String currentUserId = dataBaseHandler.getUser(1).getUserId();
		String URL = "http://smarty-trioplus.rhcloud.com/supportCenter/getUserCars?userId="
				+ currentUserId;

		dataBaseHandler.deleteAllCars();

		JSONObject jsonObj = jsonParser.getJSONFromUrl(URL);
		Log.d("Response: ", "> " + jsonObj);

		try {
			String userCarsDTOs = jsonObj.getString("userCarsDTOs");

			JSONArray carsJsonObject = new JSONArray(userCarsDTOs);

			for (int i = 0; i < carsJsonObject.length(); i++) {
				JSONObject jsonObject = carsJsonObject.getJSONObject(i);

				String carId = jsonObject.getString("carId");
				String chaseNumber = jsonObject.getString("chaseNumber");
				String modelIdFromCars = jsonObject.getString("modelId");
				String plateNumber = jsonObject.getString("plateNumber");
				String year = jsonObject.getString("year");

				JSONObject colorJsonObject = jsonObject
						.getJSONObject("carColor");

				CarTest car = new CarTest();

				car.setCarId(carId);
				car.setChase(chaseNumber);
				car.setCarModelId(modelIdFromCars);
				car.setYear(year);
				car.setPlateNumber(plateNumber);
				car.setUserId(dataBaseHandler.getUser(1).getUserId());

				String colorId = colorJsonObject.getString("id");
				String colorNamrAr = colorJsonObject.getString("coloAr");
				String colorNamrEn = colorJsonObject.getString("colorEn");

				try {
					Color color = dataBaseHandler.getColor(colorId);
					car.setCarColor(color.getColorId());
				} catch (Exception e) {
					Color color = new Color();
					color.setColorId(colorId);
					color.setColorEn(colorNamrEn);
					color.setColorAr(colorNamrAr);

					dataBaseHandler.addColor(color);

				}
				car.setCarColor(colorId);
				dataBaseHandler.addCar(car);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * get brands from json and save them into database
	 */
	public void saveJSONBrandAndModels() {
		String URL = "http://smarty-trioplus.rhcloud.com/supportCenter/CarList";

		JSONObject jsonObj = jsonParser.getJSONFromUrl(URL);
		Log.d("Response: ", "> " + jsonObj);

		dataBaseHandler.deleteAllBrands();
		dataBaseHandler.deleteAllModels();

		try {
			dataBaseHandler.deleteAllBrands();

			String carsList = jsonObj.getString("carsList");

			JSONArray carBrand = new JSONArray(carsList);

			for (int i = 0; i < carBrand.length(); i++) {
				JSONObject c = carBrand.getJSONObject(i);
				String typeId = c.getString("typeId");
				String typeNameAr = c.getString("typeNameAr");
				String typeNameEn = c.getString("typeNameEn");
				String carModels = c.getString("carModels");

				Brand brandRow = new Brand();
				brandRow.setTypeId(typeId);
				brandRow.setTypeNameAr(typeNameAr);
				brandRow.setTypeNameEn(typeNameEn);
				brandRow.setCarModels(carModels);
				dataBaseHandler.addBrand(brandRow);

				JSONArray brandModels = new JSONArray(carModels);
				for (int j = 0; j < brandModels.length(); j++) {
					JSONObject modelObj = brandModels.getJSONObject(j);

					Model model = new Model();
					model.setTypeId(modelObj.getString("modelId"));
					model.setTypeNameAr(modelObj.getString("modelNameAr"));
					model.setTypeNameEn(modelObj.getString("modelNameEn"));
					model.setParentBrand(typeId);

					dataBaseHandler.addModel(model);

				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * get cars from json and save them into database
	 */
	public void saveJSONColors() {
		String URL = "http://smarty-trioplus.rhcloud.com/supportCenter/getCarColors";
		dataBaseHandler.deleteAllColors();

		JSONObject jsonObj = jsonParser.getJSONFromUrl(URL);
		Log.d("Response: ", "> " + jsonObj);

		try {
			String userCarsDTOs = jsonObj.getString("userCarsDTOs");

			JSONArray colorJsonObject = new JSONArray(userCarsDTOs);

			for (int i = 0; i < colorJsonObject.length(); i++) {
				JSONObject jsonObject = colorJsonObject.getJSONObject(i);

				Color color = new Color();
				color.setColorId(jsonObject.getString("id"));
				color.setColorAr(jsonObject.getString("coloAr"));
				color.setColorEn(jsonObject.getString("colorEn"));

				dataBaseHandler.addColor(color);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * get insurance from json and save them into database
	 */
	public void saveJSONInsuranceCompany() {
		String URL = "http://smarty-trioplus.rhcloud.com/supportCenter/insuranceCompanyList";

		dataBaseHandler.deleteAllInsurance();
		JSONObject jsonObj = jsonParser.getJSONFromUrl(URL);
		Log.d("Response: ", "> " + jsonObj);

		try {
			String insuranceCompany = jsonObj.getString("insuranceCompany");
			JSONArray insuranceJsonObject = new JSONArray(insuranceCompany);
			for (int i = 0; i < insuranceJsonObject.length(); i++) {
				JSONObject c = insuranceJsonObject.getJSONObject(i);

				String stringId = c.getString("id");
				String nameAr = c.getString("nameAr");
				String nameEn = c.getString("nameEn");

				Insurance insurance = new Insurance();
				insurance.setId(Integer.parseInt(stringId));
				insurance.setNameAr(nameAr);
				insurance.setNameEn(nameEn);

				dataBaseHandler.addInsurance(insurance);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * check if the connection with network is able
	 */
	public boolean isConnectingToInternet() {
		ConnectivityManager connectivity = (ConnectivityManager) (context
				.getSystemService(Context.CONNECTIVITY_SERVICE));
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

	/**
	 * check if time selected before current time or not
	 */
	public boolean acceptTime(int year, int month, int day, int hour, int minute) {
		boolean hasPromise = true;
		Date date = new Date();

		long timestamp = date.getTime();
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(timestamp);

		int currentYear = cal.get(Calendar.YEAR);
		int currentMonth = cal.get(Calendar.MONTH) + 1;
		int currentDay = cal.get(Calendar.DAY_OF_MONTH);

		int currentHour = cal.get(Calendar.HOUR_OF_DAY);
		int currentMinute = cal.get(Calendar.MINUTE);

		// Check if booking if for one month only
		if (year < currentYear) {
			return false;
		}

		if (year > currentYear) {
			if (currentMonth == 12 && year == currentYear + 1 && month == 1) {
			} else {
				return false;
			}
		} else if (year == currentYear) {
			if (month == currentMonth) {
				if (day < currentDay) {
					return false;
				}
			} else if (month == currentMonth + 1) {
				int numberOfDays = day + (31 - currentDay);
				if (numberOfDays > 31) {
					return false;
				}
			} else if (month > currentMonth + 1) {
				return false;
			}
		}

		// Check if booking time is more current
		if (year == currentYear) {
			if (month < currentMonth) {
				return false;
			} else if (month == currentMonth) {
				if (day < currentDay) {
				} else if (day == currentDay) {
					if (hour < currentHour) {
						return false;
					} else if (hour == currentHour) {
						if (minute < currentMinute) {
							return false;
						}
					}
				}
			}
		}

		return hasPromise;
	}
}
