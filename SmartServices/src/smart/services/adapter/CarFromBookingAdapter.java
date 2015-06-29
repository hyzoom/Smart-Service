package smart.services.adapter;

import info.androidhive.slidingmenu.R;

import java.util.ArrayList;
import java.util.HashMap;

import smart.services.handler.DataBaseHandler;
import smart.services.model.CarTest;
import smart.services.model.Setting;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class CarFromBookingAdapter extends ArrayAdapter<CarTest> {
	private Context context;
	private TextView brandNameTV, modelNameTV, yearTV, licenseNumberTV,
			colorTV;
	private LinearLayout editLayout, deleteLayout;
	private ArrayList<CarTest> carList = new ArrayList<CarTest>();
	private LayoutInflater inflator;
	private DataBaseHandler dataBaseHandler;
	private Setting setting;

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

	public CarFromBookingAdapter(Context context, int textViewResourceId,
			ArrayList<CarTest> carList) {
		super(context, textViewResourceId, carList);
		this.carList = carList;
		this.context = context;
		inflator = LayoutInflater.from(context);
	}

	@SuppressLint({ "ViewHolder", "InflateParams" })
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		dataBaseHandler = new DataBaseHandler(context);
		setting= dataBaseHandler.getSetting();

		View view = null;

		if (setting.getDuration() == 0) {
			view = inflator.inflate(R.layout.car_list_adapter, parent, false);
		} else {
			view = inflator.inflate(R.layout.dialog_car_lisr_adapter_ar, parent, false);
		}
		brandNameTV = (TextView) view.findViewById(R.id.brand_name);
		modelNameTV = (TextView) view.findViewById(R.id.model_text_view);
		yearTV = (TextView) view.findViewById(R.id.year_textView);
		licenseNumberTV = (TextView) view
				.findViewById(R.id.licence_number_text_view);
		colorTV = (TextView) view.findViewById(R.id.color_textView);

		if (setting.getDuration() == 0) {
			editLayout = (LinearLayout) view.findViewById(R.id.edit_layout);
			deleteLayout = (LinearLayout) view.findViewById(R.id.delete_layout);
			
			deleteLayout.setVisibility(View.GONE);
			editLayout.setVisibility(View.GONE);

			
			brandNameTV.setText(dataBaseHandler.getBrand(
					dataBaseHandler.getModel(
							carList.get(position).getCarModelId())
							.getParentBrand()).getTypeNameEn());
			modelNameTV.setText(dataBaseHandler.getModel(
					carList.get(position).getCarModelId()).getTypeNameEn());
			yearTV.setText(carList.get(position).getYear());

			licenseNumberTV.setText(carList.get(position).getPlateNumber());

			colorTV.setText(dataBaseHandler.getColor(
					carList.get(position).getCarColor()).getColorEn());
		} else {
			brandNameTV.setText(dataBaseHandler.getBrand(
					dataBaseHandler.getModel(
							carList.get(position).getCarModelId())
							.getParentBrand()).getTypeNameAr());
			modelNameTV.setText(dataBaseHandler.getModel(
					carList.get(position).getCarModelId()).getTypeNameAr());
			yearTV.setText(carList.get(position).getYear());

			licenseNumberTV.setText(carList.get(position).getPlateNumber());

			colorTV.setText(dataBaseHandler.getColor(
					carList.get(position).getCarColor()).getColorAr());
		}
		
		return view;
	}
}