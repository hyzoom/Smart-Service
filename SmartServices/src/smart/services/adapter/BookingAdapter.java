package smart.services.adapter;

import info.androidhive.slidingmenu.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import smart.services.fragment.Booking;
import smart.services.functions.BookingFunctions;
import smart.services.handler.DataBaseHandler;
import smart.services.handler.JSONParser;
import smart.services.model.BookingModel;
import smart.services.model.Brand;
import smart.services.model.CarTest;
import smart.services.model.Model;
import smart.services.model.ServiceType;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

@SuppressLint("ViewHolder")
public class BookingAdapter extends ArrayAdapter<BookingModel> {

	JSONParser jP = new JSONParser();

	private Context context;
	private List<BookingModel> bookingModels;
	private LayoutInflater inflator;
	private DataBaseHandler dataBaseHandler;
	private TextView carTV, serviceTV, bookingTimeTV;
	private ListView lv;

	private BookingFunctions bookingFunctions;

	private ProgressDialog pDialog;
	private ProgressDialog progress;

	private String toServerBookingId, toServerCarModelId,
			toServerBookingTypeId, toServerDate, toServerTime, toServerComment;

	String finalCarId, finalTypeId, dateFromTV, finalDate, finalHour,
			finalComment, list, typeId, typeNameAr, typeNameEn, eCode, eDesc,
			msg_en, msg_ar, userCarsDTOs, carId, chaseNumber, modelIdFromCars,
			plateNumber, year, carId0;

	ArrayList<HashMap<String, String>> serviceTypeList;
	SwipeRefreshLayout swipeView;
	private DatePicker datePicker;
	private TimePicker timePicker;
	Button submitDate;
	EditText commentTxt;

	public BookingAdapter(Context context, int resource,
			List<BookingModel> bookingModels) {
		super(context, resource, bookingModels);
		this.bookingModels = bookingModels;
		this.context = context;
		inflator = LayoutInflater.from(context);
		dataBaseHandler = new DataBaseHandler(context);
		bookingFunctions = new BookingFunctions(context);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = inflator.inflate(R.layout.booking_adapter, parent, false);

		TextView serviceTypeTV = (TextView) view
				.findViewById(R.id.syrvice_type_textView);
		TextView brantTV = (TextView) view.findViewById(R.id.brand_textView);
		TextView modelTV = (TextView) view.findViewById(R.id.model_textView);
		TextView dateTV = (TextView) view.findViewById(R.id.date_textView);
		TextView timeTV = (TextView) view.findViewById(R.id.time_textView);

		LinearLayout updateLayout = (LinearLayout) view
				.findViewById(R.id.edit_layout);
		LinearLayout deleteLayout = (LinearLayout) view
				.findViewById(R.id.delete_layout);

		CarTest car = dataBaseHandler.getCar(bookingModels.get(position)
				.getCarId());
		Model carModel = dataBaseHandler.getModel(car.getCarModelId());
		Brand carBrand = dataBaseHandler.getBrand(carModel.getParentBrand());

		ServiceType serviceType = dataBaseHandler.getServiceType(bookingModels
				.get(position).getBookingType());
		serviceTypeTV.setText(serviceType.getTypeNameEn());

		modelTV.setText(carModel.getTypeNameEn());
		brantTV.setText(carBrand.getTypeNameEn());
		dateTV.setText(bookingModels.get(position).getBookingDate());
		timeTV.setText(bookingModels.get(position).getBookingTime());

		updateLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				updateCarDialog(position);
			}
		});

		deleteLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				deleteAlertDialog(position);
			}
		});

		return view;
	}

	public void deleteAlertDialog(final int position) {
		final TextView input = new TextView(context);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				context);
		alertDialogBuilder.setTitle("Delete");

		alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					@SuppressLint("DefaultLocale")
					public void onClick(DialogInterface dialog, int id) {

						DeleteBooking deleteBooking = new DeleteBooking(
								position);
						deleteBooking.execute();
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

	public class DeleteBooking extends AsyncTask<Void, Void, Void> {

		int position;
		String eCode = null;

		public DeleteBooking(int position) {
			this.position = position;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progress = ProgressDialog.show(context, "", "Loading...");
		}

		@Override
		protected Void doInBackground(Void... params) {
			eCode = bookingFunctions.deleteBooking(bookingModels.get(position)
					.getBookingId());
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (eCode.equals("0")) {
				bookingModels.remove(position);
				notifyDataSetChanged();
			}

			if (progress.isShowing()) {
				progress.dismiss();
			}
		}

	}

	@SuppressWarnings("deprecation")
	public void updateCarDialog(int pos) {
		final BookingModel bookingModel = bookingModels.get(pos);

		final Dialog dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.bookings);
		dialog.setTitle("Edit a booking");
		Booking.changeWidthHeight(context, dialog);

		final EditText commentET = (EditText) dialog
				.findViewById(R.id.commentTxt);
		Button submitU = (Button) dialog.findViewById(R.id.submitB);

		carTV = (TextView) dialog.findViewById(R.id.carTV);

		CarTest car = dataBaseHandler.getCar(bookingModel.getCarId());
		Model carModel = dataBaseHandler.getModel(car.getCarModelId());
		Brand carBrand = dataBaseHandler.getBrand(carModel.getParentBrand());

		ServiceType serviceType = dataBaseHandler.getServiceType(bookingModel
				.getBookingType());

		carTV.setText(serviceType.getTypeNameEn());

		serviceTV = (TextView) dialog.findViewById(R.id.serviceTypeTV);
		serviceTV.setText(carModel.getTypeNameEn() + "  "
				+ carBrand.getTypeNameEn());

		serviceTypeList = new ArrayList<HashMap<String, String>>();

		bookingTimeTV = (TextView) dialog.findViewById(R.id.bookingTimeTV);
		bookingTimeTV.setText(bookingModel.getBookingDate() + "    "
				+ bookingModel.getBookingTime());
		commentTxt = (EditText) dialog.findViewById(R.id.commentTxt);
		commentTxt.setText(bookingModel.getComment());

		toServerBookingId = bookingModel.getBookingId();
		toServerBookingTypeId = serviceType.getTypeId();
		toServerCarModelId = car.getCarId();
		toServerDate = bookingModel.getBookingDate();
		toServerTime = bookingModel.getBookingTime();
		toServerComment = commentTxt.getText().toString();

		init();

		submitU.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				BookingModel updatedBooking = bookingModel;
				updatedBooking.setBookingTime(bookingTimeTV.getText()
						.toString());
				// updateAlertDialog();
				// notifyDataSetChanged();

				toServerComment = commentTxt.getText().toString();
				// Toast.makeText(context, toServerDate + "  " + toServerTime,
				// Toast.LENGTH_SHORT).show();
				new UpdateBookingAsyncTask(toServerBookingId,
						toServerCarModelId, toServerBookingTypeId,
						toServerDate, toServerTime, toServerComment).execute();
				// System.out.println("toServerBookingId " + toServerBookingId);
				// System.out.println("toServerCarModelId " +
				// toServerCarModelId);
				// System.out.println("toServerBookingTypeId "
				// + toServerBookingTypeId);
				// System.out.println("toServerDate " + toServerDate);
				// System.out.println("toServerTime " + toServerTime);
				// System.out.println("toServerComment " + toServerComment);
				dialog.dismiss();
			}
		});

		dialog.show();
	}

	private class UpdateBookingAsyncTask extends AsyncTask<Void, Void, Void> {

		private String eCode = null;
		private String bookingId, carModelId, bookingTypeId, date, time,
				comment;

		public UpdateBookingAsyncTask(String bookingId, String carModelId,
				String bookingTypeId, String date, String time, String comment) {
			this.bookingId = bookingId;
			this.carModelId = carModelId;
			this.bookingTypeId = bookingTypeId;
			this.date = date;
			this.time = time;
			this.comment = comment;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(context);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			eCode = bookingFunctions.updateBooking(bookingId, carModelId,
					bookingTypeId, date, time, comment);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (pDialog.isShowing()) {
				pDialog.dismiss();
			}
			if (eCode.equals("0")) {

				((Activity) context).finish();
				Intent intent = new Intent(context, context.getClass());
				intent.putExtra("fragNum", "4");
				intent.putExtra("address", BookingFunctions.getResp());
				context.startActivity(intent);
				// Booking.showSubmitDialog(context, );
			} else {
				Toast.makeText(context, "Error!", Toast.LENGTH_LONG).show();
			}
		}

	}

	public void init() {

		carTV.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showServiceDialog();
			}
		});

		serviceTV.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showCarsDialog();
			}
		});
		bookingTimeTV.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showBookingTimeDialog();
			}
		});
	}

	public void showCarsDialog() {
		final Dialog dialog = new Dialog(context);
		dialog.setContentView(R.layout.add_car_booking);
		lv = (ListView) dialog.findViewById(R.id.addCarsTestList);
		dialog.setCancelable(true);
		dialog.setTitle("Choose a car");
		Booking.changeWidthHeight(context, dialog);
		final ArrayList<HashMap<String, String>> carsList = dataBaseHandler
				.getAllCars();

		CarFromBookingAdapter adapter = new CarFromBookingAdapter(context,
				R.layout.car_list_adapter, dataBaseHandler.getAllCarTest());

		// ListAdapter adapter = new SimpleAdapter(context, carsList,
		// R.layout.single_add_car_item_booking, new String[] { "carId",
		// "carBrand", "carModelId", "carModel", "year",
		// "carColor", "chase" }, new int[] { R.id.carId,
		// R.id.carBrand, R.id.carModelId, R.id.carModel,
		// R.id.year, R.id.carColor, R.id.chase });
		lv.setAdapter(adapter);

		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				CarTest car = dataBaseHandler.getCar(carsList.get(position)
						.get("carId"));
				Model model = dataBaseHandler.getModel(car.getCarModelId());
				Brand brand = dataBaseHandler.getBrand(model.getParentBrand());

				toServerCarModelId = car.getCarId();
				serviceTV.setText(brand.getTypeNameEn() + "   "
						+ model.getTypeNameEn());

				dialog.dismiss();
			}
		});
		dialog.show();
	}

	@SuppressWarnings("deprecation")
	public void showServiceDialog() {
		final Dialog dialog = new Dialog(context);
		dialog.setContentView(R.layout.service_type_dialog);
		dialog.setTitle("Choose Service Type");
		Booking.changeWidthHeight(context, dialog);
		lv = (ListView) dialog.findViewById(R.id.serviceTypeLV);

		if (dataBaseHandler.getServiceTypesCount() == 0) {
			if (bookingFunctions.isConnectingToInternet()) {
				new ServiceTypes().execute();
			} else {
				Toast.makeText(context, "Check your internet connection",
						Toast.LENGTH_LONG).show();
			}
		} else {
			List<ServiceType> typeList = dataBaseHandler.getAllServiceTypes();
			serviceTypeList.clear();
			for (int i = 0; i < typeList.size(); i++) {
				HashMap<String, String> serviceType = new HashMap<String, String>();
				serviceType.put("typeId", typeList.get(i).getTypeId());
				serviceType.put("typeNameAr", typeList.get(i).getTypeNameAr());
				serviceType.put("typeNameEn", typeList.get(i).getTypeNameEn());

				serviceTypeList.add(serviceType);
			}

			ListAdapter adapter = new SimpleAdapter(context, serviceTypeList,
					R.layout.single_service_type_item, new String[] { "typeId",
							"typeNameAr", "typeNameEn" }, new int[] {
							R.id.serTypeId, R.id.serTypeNameAr,
							R.id.serTypeNameEn });
			lv.setAdapter(adapter);
		}

		swipeView = (SwipeRefreshLayout) dialog
				.findViewById(R.id.swipeDialogST);
		swipeView
				.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
					@Override
					public void onRefresh() {
						swipeView.setRefreshing(true);
						if (bookingFunctions.isConnectingToInternet()) {
							new ServiceTypes().execute();
						} else {
							Toast.makeText(context,
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
				String serEn = ((TextView) view
						.findViewById(R.id.serTypeNameEn)).getText().toString();

				toServerBookingTypeId = serviceTypeList.get(position).get(
						"typeId");

				carTV.setText(serEn);
				dialog.dismiss();
			}
		});

		dialog.show();
	}

	public class ServiceTypes extends AsyncTask<Void, Void, Void> {

		@Override
		public void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			serviceTypeList.clear();
			pDialog = new ProgressDialog(context);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected Void doInBackground(Void... arg0) {
			bookingFunctions.saveJSONUserServiceType();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// Dismiss the progress dialog
			if (pDialog.isShowing()) {
				pDialog.dismiss();
			}

			List<ServiceType> typeList = dataBaseHandler.getAllServiceTypes();
			serviceTypeList.clear();
			for (int i = 0; i < typeList.size(); i++) {
				HashMap<String, String> serviceType = new HashMap<String, String>();
				serviceType.put("typeId", typeList.get(i).getTypeId());
				serviceType.put("typeNameAr", typeList.get(i).getTypeNameAr());
				serviceType.put("typeNameEn", typeList.get(i).getTypeNameEn());

				serviceTypeList.add(serviceType);
			}

			ListAdapter adapter = new SimpleAdapter(context, serviceTypeList,
					R.layout.single_service_type_item, new String[] { "typeId",
							"typeNameAr", "typeNameEn" }, new int[] {
							R.id.serTypeId, R.id.serTypeNameAr,
							R.id.serTypeNameEn });
			lv.setAdapter(adapter);

		}
	}

	public void showBookingTimeDialog() {
		final Dialog dialog = new Dialog(context);
		dialog.setContentView(R.layout.booking_time_dialog);
		datePicker = (DatePicker) dialog.findViewById(R.id.dpResult);
		timePicker = (TimePicker) dialog.findViewById(R.id.timePicker);
		submitDate = (Button) dialog.findViewById(R.id.submitDate);

		dialog.setCancelable(true);
		dialog.setTitle("Choose booking time");
		Booking.changeWidthHeight(context, dialog);
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		datePicker.init(year, month, day, null);

		timePicker.setCurrentHour(c.get(Calendar.HOUR));
		timePicker.setCurrentMinute(c.get(Calendar.MINUTE));
		submitDate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int month = datePicker.getMonth() + 1;
				String date = "" + datePicker.getDayOfMonth() + "-" + month
						+ "-" + datePicker.getYear();
				String hour = timePicker.getCurrentHour() + ":"
						+ timePicker.getCurrentMinute();

				toServerDate = date;
				toServerTime = hour;
				bookingTimeTV.setText(date + "    " + hour);
				dialog.dismiss();
			}
		});
		dialog.show();
	}

}
