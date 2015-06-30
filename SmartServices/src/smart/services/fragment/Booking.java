package smart.services.fragment;

import info.androidhive.slidingmenu.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import smart.services.adapter.BookingServiceTypeAdapter;
import smart.services.adapter.CarFromBookingAdapter;
import smart.services.functions.BookingFunctions;
import smart.services.functions.CarFunctions;
import smart.services.handler.DataBaseHandler;
import smart.services.handler.JSONParser;
import smart.services.model.Brand;
import smart.services.model.CarTest;
import smart.services.model.Model;
import smart.services.model.ServiceType;
import smart.services.model.Setting;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Booking extends Fragment {
	private static String bookingURL = "http://smarty-trioplus.rhcloud.com/supportCenter/booking?";
	JSONParser jP = new JSONParser();

	EditText commentTxt;
	ListView lv;
	private DatePicker datePicker;
	private TimePicker timePicker;
	Button submitDate;
	SwipeRefreshLayout swipeView;
	int currYear, currMonth, currDay;
	ArrayList<HashMap<String, String>> carsList;

	String finalCarId, finalTypeId, dateFromTV, finalDate, finalHour,
			finalComment, list, typeId, typeNameAr, typeNameEn, eCode, eDesc,
			msg_en, msg_ar, userCarsDTOs, carId, chaseNumber, modelIdFromCars,
			plateNumber, year, carId0;
	Button submit;
	ArrayList<HashMap<String, String>> serviceTypeList;

	private ProgressDialog pDialog;

	private ImageView chooseCarArIV, chooseCarEnIV, serviceTypeEnIV,
			serviceTypeArIV, bookingTypeEnIV, bookingTypeArIV;
	private TextView titleTV, carTV, carIdTV, serviceTypeTV, serviceTypeIdTV,
			bookingTimeTV, errorMsg0;

	private ProgressDialog progress;
	private DataBaseHandler databaseHandler;
	private Setting setting;
	private CarFunctions carFunctions;
	private BookingFunctions bookingFunctions;
	private Dialog dialog;
	static Dialog dialog_;
	private String selectedCarId, bookingTime;

	public Booking() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		databaseHandler = new DataBaseHandler(getActivity());
		carFunctions = new CarFunctions(getActivity());
		bookingFunctions = new BookingFunctions(getActivity());
		setting = databaseHandler.getSetting();
		dialog = new Dialog(getActivity());
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		View rootView = inflater.inflate(R.layout.bookings, container, false);

		// Image views
		chooseCarEnIV = (ImageView) rootView.findViewById(R.id.chooseCarEnIV);
		chooseCarArIV = (ImageView) rootView.findViewById(R.id.chooseCarArIV);
		serviceTypeEnIV = (ImageView) rootView
				.findViewById(R.id.serviceTypeEnIV);
		serviceTypeArIV = (ImageView) rootView
				.findViewById(R.id.serviceTypeArIV);
		bookingTypeEnIV = (ImageView) rootView
				.findViewById(R.id.bookingTypeEnIV);
		bookingTypeArIV = (ImageView) rootView
				.findViewById(R.id.bookingTypeArIV);

		// TextViews
		titleTV = (TextView) rootView.findViewById(R.id.titleTV);

		carTV = (TextView) rootView.findViewById(R.id.carTV);
		carIdTV = (TextView) rootView.findViewById(R.id.modelIdTV);
		serviceTypeTV = (TextView) rootView.findViewById(R.id.serviceTypeTV);
		serviceTypeIdTV = (TextView) rootView
				.findViewById(R.id.serviceTypeIdTV);
		bookingTimeTV = (TextView) rootView.findViewById(R.id.bookingTimeTV);
		databaseHandler = new DataBaseHandler(getActivity());
		submit = (Button) rootView.findViewById(R.id.submitB);
		commentTxt = (EditText) rootView.findViewById(R.id.commentTxt);
		errorMsg0 = (TextView) rootView.findViewById(R.id.errorMsg0);
		serviceTypeList = new ArrayList<HashMap<String, String>>();
		init();
		submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				addBooking();
			}
		});
		return rootView;
	}

	public void init() {
		if (setting.getDuration() == 0) {
			chooseCarArIV.setVisibility(View.GONE);
			serviceTypeArIV.setVisibility(View.GONE);
			bookingTypeArIV.setVisibility(View.GONE);

			titleTV.setText(getResources().getString(
					R.string.add_booking_title_en));
			carTV.setHint(getResources().getString(
					R.string.booking_choose_car_en));
			serviceTypeTV.setHint(getResources().getString(
					R.string.booking_service_type_en));
			bookingTimeTV.setHint(getResources().getString(
					R.string.booking_time_en));

			commentTxt.setHint(getResources().getString(
					R.string.booking_comment_en));

			submit.setText(getResources().getString(R.string.booking_submet_en));

		} else {
			chooseCarEnIV.setVisibility(View.GONE);
			serviceTypeEnIV.setVisibility(View.GONE);
			bookingTypeEnIV.setVisibility(View.GONE);

			titleTV.setText(getResources().getString(
					R.string.add_booking_title_ar));
			carTV.setHint(getResources().getString(
					R.string.booking_choose_car_ar));
			serviceTypeTV.setHint(getResources().getString(
					R.string.booking_service_type_ar));
			bookingTimeTV.setHint(getResources().getString(
					R.string.booking_time_ar));

			commentTxt.setHint(getResources().getString(
					R.string.booking_comment_ar));

			submit.setText(getResources().getString(R.string.booking_submet_ar));
		}

		carTV.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showCarsDialog();
			}
		});
		serviceTypeTV.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showServiceDialog();
			}
		});
		bookingTimeTV.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showBookingTimeDialog();
			}
		});
	}

	public void addBooking() {
		finalCarId = carIdTV.getText().toString();
		finalTypeId = serviceTypeIdTV.getText().toString();
		dateFromTV = bookingTimeTV.getText().toString();
		finalComment = commentTxt.getText().toString();
		if (carTV.getText().toString().equals("")
				|| serviceTypeTV.getText().toString().equals("")
				|| dateFromTV.equals("") || finalComment.equals("")) {
			if (carTV.getText().toString().equals("")) {
				Toast.makeText(getActivity(), "Please select a Car",
						Toast.LENGTH_LONG).show();
			} else if (serviceTypeTV.getText().toString().equals("")) {
				Toast.makeText(getActivity(), "Please select a Service Type",
						Toast.LENGTH_LONG).show();
			} else if (dateFromTV.equals("")) {
				Toast.makeText(getActivity(), "Please select a date",
						Toast.LENGTH_LONG).show();
			} else if (finalComment.equals("")) {
				Toast.makeText(getActivity(), "Please enter a comment",
						Toast.LENGTH_LONG).show();
			}
		} else {
			finalDate = dateFromTV.substring(0, dateFromTV.indexOf(" "));
			finalHour = dateFromTV.substring(dateFromTV.lastIndexOf(" ") + 1,
					dateFromTV.length());
			if (carFunctions.isConnectingToInternet()) {
				new AddBooking(finalCarId, finalTypeId, finalDate, finalHour,
						finalComment).execute();
			} else {
				errorMsg0.setText("Check your internet connection");
			}
		}

		System.out.println("finalComment >" + finalComment);
	}

	@SuppressLint("InlinedApi")
	@SuppressWarnings("deprecation")
	public void showCarsDialog() {
		if (databaseHandler.getCarsCount() == 0) {
			if (carFunctions.isConnectingToInternet()) {
				new CarsAsyncTask().execute();
			} else {
				Toast.makeText(getActivity(), "Check your internet connection",
						Toast.LENGTH_LONG).show();
			}
		} else {
			if (setting.getDuration() == 0) {
				dialog.setContentView(R.layout.manage_car_info);
			} else {
				dialog.setContentView(R.layout.dialog_car_list_ar);
			}

			lv = (ListView) dialog.findViewById(R.id.carsTestList);
			swipeView = (SwipeRefreshLayout) dialog
					.findViewById(R.id.swipeOffersCarInfo);
			swipeView
					.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
						@Override
						public void onRefresh() {
							swipeView.setRefreshing(true);
							Toast.makeText(getActivity(), "No Updates",
									Toast.LENGTH_LONG).show();
						}
					});
			swipeView.setColorScheme(android.R.color.holo_blue_bright,
					android.R.color.holo_green_light,
					android.R.color.holo_orange_light,
					android.R.color.holo_red_light);
			dialog.setCancelable(true);

			dialog.setTitle("Choose a car");

			Booking.changeWidthHeight(getActivity(), dialog);

			carsList = databaseHandler.getUserCars(databaseHandler.getUser(1)
					.getUserId());

			CarFromBookingAdapter adapter = new CarFromBookingAdapter(
					getActivity(), R.layout.car_list_adapter,
					databaseHandler.getAllCarTest());
			// AddCarAdapter adapter = new AddCarAdapter(carsList,
			// getActivity());
			lv.setAdapter(adapter);

			lv.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {

					CarTest selectedCar = databaseHandler.getCar(carsList.get(position).get("carId"));

					String currModelId = selectedCar.getCarModelId();
					Model model = databaseHandler.getModel(currModelId);
					Brand brand = databaseHandler.getBrand(model
							.getParentBrand());

					if (setting.getDuration() == 0) {
						carTV.setText(brand.getTypeNameEn() + "   "
								+ model.getTypeNameEn() + "   "
								+ selectedCar.getPlateNumber());
					} else {
						carTV.setText(brand.getTypeNameAr() + "   "
								+ model.getTypeNameAr() + "   "
								+ selectedCar.getPlateNumber());
					}

					selectedCarId = selectedCar.getCarId();
					carIdTV.setText(selectedCarId);
					if (dialog.isShowing()) {
						dialog.dismiss();
					}
				}
			});

			dialog.show();
		}
	}

	@SuppressWarnings("deprecation")
	public static void changeWidthHeight(Context c, Dialog dialog) {
		Window window = dialog.getWindow();
		Display display = ((Activity) c).getWindowManager().getDefaultDisplay();
		window.setLayout(display.getWidth() * 5 / 6,
				display.getHeight() * 3 / 4);
	}

	@SuppressWarnings("deprecation")
	public static void changeWidthHeightDim(Context c, Dialog dialog) {
		Window window = dialog.getWindow();
		Display display = ((Activity) c).getWindowManager().getDefaultDisplay();
		window.setLayout(display.getWidth() * 5 / 6,
				display.getHeight() * 1 / 2);
	}

	public static void showSubmitDialog(Context c, String address) {
		dialog_ = new Dialog(c);
		dialog_.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog_.setContentView(R.layout.submit_message);
		changeWidthHeightDim(c, dialog_);
		TextView mesg = (TextView) dialog_.findViewById(R.id.mesg);
		Button close = (Button) dialog_.findViewById(R.id.close);
		mesg.setText(address);
		dialog_.show();
		close.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog_.dismiss();
			}
		});
	}

	private class CarsAsyncTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progress = ProgressDialog.show(Booking.this.getActivity(), "",
					"Loading...");
		}

		@Override
		protected Void doInBackground(Void... params) {
			carFunctions.saveJSONUserCars();
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

			if (setting.getDuration() == 0) {
				dialog.setContentView(R.layout.manage_car_info);
			} else {
				dialog.setContentView(R.layout.dialog_car_list_ar);
			}

			lv = (ListView) dialog.findViewById(R.id.carsTestList);
			swipeView = (SwipeRefreshLayout) dialog
					.findViewById(R.id.swipeOffersCarInfo);
			swipeView
					.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
						@Override
						public void onRefresh() {
							swipeView.setRefreshing(true);
							Toast.makeText(getActivity(), "No Updates",
									Toast.LENGTH_LONG).show();
						}
					});
			swipeView.setColorScheme(android.R.color.holo_blue_bright,
					android.R.color.holo_green_light,
					android.R.color.holo_orange_light,
					android.R.color.holo_red_light);
			dialog.setCancelable(true);
			dialog.setTitle("Choose a car");
			Booking.changeWidthHeight(getActivity(), dialog);

			carsList = databaseHandler.getUserCars(databaseHandler.getUser(1)
					.getUserId());

			CarFromBookingAdapter adapter = new CarFromBookingAdapter(
					getActivity(), R.layout.car_list_adapter,
					databaseHandler.getAllCarTest());
			// AddCarAdapter adapter = new AddCarAdapter(carsList,
			// getActivity());
			lv.setAdapter(adapter);

			lv.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {

					CarTest selectedCar = databaseHandler.getCar(carsList.get(
							position).get("carId"));

					String currModelId = selectedCar.getCarModelId();
					Model model = databaseHandler.getModel(currModelId);
					Brand brand = databaseHandler.getBrand(model
							.getParentBrand());

					if (setting.getDuration() == 0) {
						carTV.setText(brand.getTypeNameEn() + "   "
								+ model.getTypeNameEn() + "   "
								+ selectedCar.getPlateNumber());
					} else {
						carTV.setText(brand.getTypeNameAr() + "   "
								+ model.getTypeNameAr() + "   "
								+ selectedCar.getPlateNumber());
					}

					selectedCarId = selectedCar.getCarId();
					carIdTV.setText(selectedCarId);
					if (dialog.isShowing()) {
						dialog.dismiss();
					}
				}
			});

			dialog.show();
		}
	}

	public void showServiceDialog() {
		if (databaseHandler.getServiceTypesCount() == 0) {
			if (carFunctions.isConnectingToInternet()) {
				new ServiceTypeAsyncTask().execute();
			} else {
				Toast.makeText(getActivity(), "Check your internet connection",
						Toast.LENGTH_LONG).show();
			}
		} else {
			if (setting.getDuration() == 0) {
				dialog.setContentView(R.layout.service_type_dialog);
			} else {
				dialog.setContentView(R.layout.dialog_service_type_ar);
			}

			dialog.setCancelable(true);
			dialog.setTitle("Choose Service Type");
			Booking.changeWidthHeight(getActivity(), dialog);

			final List<ServiceType> typeList = databaseHandler.getAllServiceTypes();

			BookingServiceTypeAdapter bookingServiceTypeAdapter = new BookingServiceTypeAdapter(
					getActivity(), R.layout.dialog_service_type_ar,
					databaseHandler.getAllServiceTypes());

			lv = (ListView) dialog.findViewById(R.id.serviceTypeLV);
			lv.setAdapter(bookingServiceTypeAdapter);

			lv.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {

					if (setting.getDuration() == 0) {
						serviceTypeTV.setText(typeList.get(position).getTypeNameEn());
					} else {
						serviceTypeTV.setText(typeList.get(position).getTypeNameAr());
					}

					serviceTypeIdTV.setText(typeList.get(position).getTypeId());

					dialog.dismiss();
				}
			});

			swipeView = (SwipeRefreshLayout) dialog
					.findViewById(R.id.swipeDialogST);
			swipeView
					.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
						@Override
						public void onRefresh() {
							swipeView.setRefreshing(true);
							if (carFunctions.isConnectingToInternet()) {
								new ServiceTypeAsyncTask().execute();
							} else {
								Toast.makeText(getActivity(),
										"Check your internet connection",
										Toast.LENGTH_LONG).show();
							}
							swipeView.setRefreshing(false);
						}
					});

			dialog.show();
		}

	}

	private class ServiceTypeAsyncTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			serviceTypeList.clear();
			progress = ProgressDialog.show(Booking.this.getActivity(), "",
					"Loading...");
		}

		@Override
		protected Void doInBackground(Void... params) {
			bookingFunctions.saveJSONUserServiceType();
			return null;
		}

		@Override
		protected void onPostExecute(Void aVoid) {
			super.onPostExecute(aVoid);
			if (progress.isShowing()) {
				progress.dismiss();
			}

			if (setting.getDuration() == 0) {
				dialog.setContentView(R.layout.service_type_dialog);
			} else {
				dialog.setContentView(R.layout.dialog_service_type_ar);
			}

			dialog.setCancelable(true);
			dialog.setTitle("Choose Service Type");
			Booking.changeWidthHeight(getActivity(), dialog);

			final List<ServiceType> typeList = databaseHandler.getAllServiceTypes();

			BookingServiceTypeAdapter bookingServiceTypeAdapter = new BookingServiceTypeAdapter(
					getActivity(), R.layout.dialog_service_type_ar,
					databaseHandler.getAllServiceTypes());

			lv = (ListView) dialog.findViewById(R.id.serviceTypeLV);
			lv.setAdapter(bookingServiceTypeAdapter);

			lv.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {

					if (setting.getDuration() == 0) {
						serviceTypeTV.setText(typeList.get(position).getTypeNameEn());
					} else {
						serviceTypeTV.setText(typeList.get(position).getTypeNameAr());
					}

					serviceTypeIdTV.setText(typeList.get(position).getTypeId());

					dialog.dismiss();
				}
			});

			swipeView = (SwipeRefreshLayout) dialog
					.findViewById(R.id.swipeDialogST);
			swipeView
					.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
						@Override
						public void onRefresh() {
							swipeView.setRefreshing(true);
							if (carFunctions.isConnectingToInternet()) {
								new ServiceTypeAsyncTask().execute();
							} else {
								Toast.makeText(getActivity(),
										"Check your internet connection",
										Toast.LENGTH_LONG).show();
							}
							swipeView.setRefreshing(false);
						}
					});

			dialog.show();
		}
	}

	public void showBookingTimeDialog() {
		dialog.setContentView(R.layout.booking_time_dialog);
		datePicker = (DatePicker) dialog.findViewById(R.id.dpResult);
		timePicker = (TimePicker) dialog.findViewById(R.id.timePicker);
		submitDate = (Button) dialog.findViewById(R.id.submitDate);

		dialog.setCancelable(true);
		dialog.setTitle("Choose booking time");
		Booking.changeWidthHeight(getActivity(), dialog);
		dialog.show();
		final Calendar c = Calendar.getInstance();
		currYear = c.get(Calendar.YEAR);
		currMonth = c.get(Calendar.MONTH);
		currDay = c.get(Calendar.DAY_OF_MONTH);
		datePicker.init(currYear, currMonth, currDay, null);

		timePicker.setCurrentHour(c.get(Calendar.HOUR));
		timePicker.setCurrentMinute(c.get(Calendar.MINUTE));
		submitDate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int month = datePicker.getMonth() + 1;
				int day = datePicker.getDayOfMonth();
				int year = datePicker.getYear();

				String date = "" + day + "-" + month + "-" + year;

				String hour = timePicker.getCurrentHour() + ":"
						+ timePicker.getCurrentMinute();
				if (carFunctions.acceptTime(year, month, day,
						timePicker.getCurrentHour(),
						timePicker.getCurrentMinute())) {
					bookingTime = date + "    " + hour;
					bookingTimeTV.setText(bookingTime);
					dialog.dismiss();
				} else {
					Toast.makeText(getActivity(), "This Time is invalid",
							Toast.LENGTH_LONG).show();
				}

			}
		});

	}

	public class AddBooking extends AsyncTask<Void, Void, Void> {
		String carmodelid = "";
		String bookingTypeId = "";
		String date = "";
		String time = "";
		String comment = "";

		public AddBooking() {
		}

		public AddBooking(String carmodelid, String bookingTypeId, String date,
				String time, String comment) {
			this.carmodelid = carmodelid;
			this.bookingTypeId = bookingTypeId;
			this.date = date;
			this.time = time;
			this.comment = comment;
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
			String url = bookingURL + "carmodelid=" + this.carmodelid
					+ "&bookingTypeId=" + this.bookingTypeId + "&date="
					+ this.date + "&time=" + this.time + "&comment="
					+ this.comment;
			try {
				JSONObject jsonObj = jP.getJSONFromUrl(url);
				Log.d("Response: ", "> " + jsonObj);

				eCode = jsonObj.getString("eCode");
				eDesc = jsonObj.getString("eDesc");
				msg_en = jsonObj.getString("msg_en");
				msg_ar = jsonObj.getString("msg_ar");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			errorMsg0.setText(eDesc);
			if (eCode.equals("0")) {
				getActivity().finish();
				Intent intent = new Intent(getActivity(), getActivity()
						.getClass());
				intent.putExtra("fragNum", "4");
				intent.putExtra("address", msg_en);
				getActivity().startActivity(intent);
				// dBCT.addTriple(new CarTest(brandTV.getText().toString(),
				// this.finalCarId, modelTV.getText().toString(),
				// this.finalYear, this.finalColor, this.finalChase));
			}
			if (pDialog.isShowing()) {
				pDialog.dismiss();
			}
		}
	}

}
