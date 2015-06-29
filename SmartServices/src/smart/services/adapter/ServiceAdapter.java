package smart.services.adapter;

import info.androidhive.slidingmenu.R;

import java.util.List;

import smart.services.handler.DataBaseHandler;
import smart.services.model.Service;
import smart.services.model.Setting;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ServiceAdapter extends ArrayAdapter<Service> {

	private List<Service> service;
	private Context context;
	private LayoutInflater inflator;
	private DataBaseHandler databaseHandler;
	private Setting setting;
	private TextView serviceIDTV, serviceDescArTV, serviceDescEnTV, serviceTitleArTV, serviceTitleEnTV;

	public ServiceAdapter(Context context, int resource, List<Service> service) {
		super(context, resource, service);
		this.service = service;
		this.context = context;
		inflator = LayoutInflater.from(context);
		databaseHandler = new DataBaseHandler(context);
		setting = databaseHandler.getSetting();
	}

	@SuppressLint("ViewHolder") 
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = inflator.inflate(R.layout.service_adapter, parent, false);

		serviceIDTV = (TextView) view.findViewById(R.id.serviceIdTV);
		serviceDescArTV = (TextView) view.findViewById(R.id.descArTV);
		serviceDescEnTV = (TextView) view.findViewById(R.id.descEnTV);
		serviceTitleArTV = (TextView) view.findViewById(R.id.titleArTV);
		serviceTitleEnTV = (TextView) view.findViewById(R.id.titleEnTV);

		ListView serviceNumberLV = (ListView) view
				.findViewById(R.id.serviceNumberListView);

		serviceIDTV.setText(service.get(position).getServiceId());
		serviceDescArTV.setText(service.get(position).getDescAr());
		serviceDescEnTV.setText(service.get(position).getDescEn());
		serviceTitleArTV.setText(service.get(position).getTitleAr());
		serviceTitleEnTV.setText(service.get(position).getTitleEn());
		
		if (setting.getDuration() == 1) {
			serviceDescArTV.setVisibility(View.VISIBLE);
			serviceDescEnTV.setVisibility(View.GONE);
			
			serviceTitleArTV.setVisibility(View.VISIBLE);
			serviceTitleEnTV.setVisibility(View.GONE);
		}
		
		

		ServiceNumberAdapter serviceNumberAdapter = new ServiceNumberAdapter(
				context, R.layout.service_number_adapter,
				databaseHandler.getAllServicNumbers(service.get(position)
						.getServiceId()));

		serviceNumberLV.setAdapter(serviceNumberAdapter);
		getListViewSize(serviceNumberLV);
		return view;
	}

	public void getListViewSize(ListView myListView) {
		ListAdapter myListAdapter = myListView.getAdapter();
		if (myListAdapter == null) {
			// do nothing return null
			return;
		}
		// set listAdapter in loop for getting final size
		int totalHeight = 0;
		for (int size = 0; size < myListAdapter.getCount(); size++) {
			View listItem = myListAdapter.getView(size, null, myListView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight()
					+ myListView.getDividerHeight();
		}

		// setting listview item in adapter
		ViewGroup.LayoutParams params = myListView.getLayoutParams();
		params.height = totalHeight
				+ (myListView.getDividerHeight() * (myListAdapter.getCount() - 1));
		myListView.setLayoutParams(params);
	}

}
