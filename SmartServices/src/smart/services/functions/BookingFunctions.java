package smart.services.functions;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import smart.services.handler.DataBaseHandler;
import smart.services.handler.JSONParser;
import smart.services.model.BookingModel;
import smart.services.model.ServiceType;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class BookingFunctions {

	private Context context;
	private DataBaseHandler dataBaseHandler;
	private JSONParser jsonParser;
	static String msg_en;

	public BookingFunctions(Context context) {
		this.context = context;
		dataBaseHandler = new DataBaseHandler(context);
		jsonParser = new JSONParser();
	}

	/**
	 * get user bookings from json
	 */
	public ArrayList<BookingModel> saveJSONBookings() {
		ArrayList<BookingModel> bookingModels = new ArrayList<BookingModel>();

		String userId = dataBaseHandler.getUser(1).getUserId();
		String URL = "http://smarty-trioplus.rhcloud.com/supportCenter/bookingHistory?userId="
				+ userId;

		JSONObject jsonObj = jsonParser.getJSONFromUrl(URL);
		Log.d("Response: ", "> " + jsonObj);

		try {
			String bookingDTOs = jsonObj.getString("bookingDTOs");
			JSONArray historyJsonObject = new JSONArray(bookingDTOs);
			for (int i = 0; i < historyJsonObject.length(); i++) {
				JSONObject c = historyJsonObject.getJSONObject(i);

				userId = c.getString("userId");

				BookingModel bookingModel = new BookingModel();
				bookingModel.setBookingId(c.getString("bookingId"));
				bookingModel.setBookingDate(c.getString("bookingDate"));
				bookingModel.setBookingTime(c.getString("bookingTime"));
				bookingModel.setCarId(c.getString("carId"));
				bookingModel.setConfirmationTime(c
						.getString("confirmationTime"));
				bookingModel.setCreation(c.getString("creation"));
				bookingModel.setDescription(c.getString("description"));
				bookingModel.setReason(c.getString("reason"));
				bookingModel.setState(c.getString("status"));
				bookingModel.setUserId(c.getString("userId"));
				bookingModel.setMsisdn(c.getString("msisdn"));
				bookingModel.setChaseNumber(c.getString("chaseNumber"));
				bookingModel.setComment(c.getString("description"));

				JSONObject bookingType = c.getJSONObject("bookingType");
				Log.d("Response: ", "> " + bookingType);

				try {
					ServiceType serviceType = dataBaseHandler
							.getServiceType(bookingType.getString("typeId"));
					bookingModel.setBookingType(serviceType.getTypeId());
				} catch (Exception e) {
					ServiceType serviceType = new ServiceType();
					serviceType.setTypeId(bookingType.getString("typeId"));
					serviceType.setTypeNameAr(bookingType
							.getString("typeNameAr"));
					serviceType.setTypeNameEn(bookingType
							.getString("typeNameEn"));
					dataBaseHandler.addServiceType(serviceType);
					bookingModel.setBookingType(serviceType.getTypeId());
				}

				bookingModels.add(bookingModel);
				// if you want to save bookings
				// dataBaseHandler.addBooking(bookingModel);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return bookingModels;
	}

	/**
	 * get service types from json and save them into database
	 */
	public void saveJSONUserServiceType() {
		String URL = "http://smarty-trioplus.rhcloud.com/supportCenter/bookingTypeList";

		JSONObject jsonObj = jsonParser.getJSONFromUrl(URL);
		Log.d("Response: ", "> " + jsonObj);

		try {
			String serviceTypeList = jsonObj.getString("list");

			JSONArray carsJsonObject = new JSONArray(serviceTypeList);

			for (int i = 0; i < carsJsonObject.length(); i++) {
				JSONObject jsonObject = carsJsonObject.getJSONObject(i);

				try {
					ServiceType serviceType = dataBaseHandler
							.getServiceType(jsonObject.getString("typeId"));
					Log.d("asd", serviceType.getTypeId());
				} catch (Exception e) {
					String typeId = jsonObject.getString("typeId");
					String typeNameAr = jsonObject.getString("typeNameAr");
					String typeNameEn = jsonObject.getString("typeNameEn");

					ServiceType serviceType = new ServiceType();
					serviceType.setTypeId(typeId);
					serviceType.setTypeNameAr(typeNameAr);
					serviceType.setTypeNameEn(typeNameEn);

					dataBaseHandler.addServiceType(serviceType);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * delete booking from server
	 */
	public String deleteBooking(String bookingId) {
		String URL = "http://smarty-trioplus.rhcloud.com/supportCenter/bookingDelete?bookingId="
				+ bookingId;

		String eCode = null;

		JSONObject jsonObj = jsonParser.getJSONFromUrl(URL);
		Log.d("Response: ", "> " + jsonObj);

		try {
			eCode = jsonObj.getString("eCode");

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return eCode;
	}

	/**
	 * update booking from server
	 */
	public String updateBooking(String bookingId, String carModelId,
			String bookingTypeId, String date, String time, String comment) {
		// String URL =
		// "http://smarty-trioplus.rhcloud.com/supportCenter/bookingUpdate?bookingId="+
		// bookingId
		// + "&carmodelid=" + carModelId + "&bookingTypeId=" + bookingTypeId +
		// "&date=" + date
		// + "&time=" + time + "&comment=" + comment;

		String URL = "http://smarty-trioplus.rhcloud.com/supportCenter/bookingUpdate?bookingId="
				+ bookingId
				+ "&carmodelid="
				+ carModelId
				+ "&bookingTypeId="
				+ bookingTypeId
				+ "&date="
				+ date
				+ "&time="
				+ time
				+ "&comment=" + comment;

		String eCode = null;

		JSONObject jsonObj = jsonParser.getJSONFromUrl(URL);
		Log.d("Response: ", "> " + jsonObj);

		try {
			eCode = jsonObj.getString("eCode");
			msg_en = jsonObj.getString("msg_en");

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return eCode;
	}

	public static String getResp() {
		return msg_en;
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

}
