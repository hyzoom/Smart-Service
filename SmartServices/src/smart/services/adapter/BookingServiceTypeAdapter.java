package smart.services.adapter;

import info.androidhive.slidingmenu.R;

import java.util.ArrayList;

import smart.services.handler.DataBaseHandler;
import smart.services.model.ServiceType;
import smart.services.model.Setting;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class BookingServiceTypeAdapter extends ArrayAdapter<ServiceType>{

	private Context context;
	private ArrayList<ServiceType> serviceTypeList = new ArrayList<ServiceType>();
	
	private LayoutInflater inflator;
	private DataBaseHandler dataBaseHandler;
	private Setting setting;
	private TextView serviceTypeNameTV;
	
	public BookingServiceTypeAdapter(Context context, int textViewResourceId, ArrayList<ServiceType> serviceTypeList) {
		super(context, textViewResourceId, serviceTypeList);
		this.serviceTypeList = serviceTypeList;
		this.context = context;
		inflator = LayoutInflater.from(context);
	}
	
	@SuppressLint("ViewHolder") @Override
	public View getView(int position, View convertView, ViewGroup parent) {
		dataBaseHandler = new DataBaseHandler(context);
		setting= dataBaseHandler.getSetting();
		
		View view = inflator.inflate(R.layout.service_type_adapter, parent, false);
		serviceTypeNameTV = (TextView) view.findViewById(R.id.serviceTypeNameTV);
		
		if (setting.getDuration() == 0) {
			serviceTypeNameTV.setText(serviceTypeList.get(position).getTypeNameEn());
		} else {
			serviceTypeNameTV.setText(serviceTypeList.get(position).getTypeNameAr());
		}
		return view;
	}
}
