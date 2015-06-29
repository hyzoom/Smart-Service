package smart.services.adapter;

import info.androidhive.slidingmenu.R;

import java.util.List;

import smart.services.model.ServiceNumber;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ServiceNumberAdapter extends ArrayAdapter<ServiceNumber> {

	List<ServiceNumber> serviceNumber;
	Context context;
	LayoutInflater inflator;
	Animation pulse;

	public ServiceNumberAdapter(Context context, int resource,
			List<ServiceNumber> serviceNumber) {
		super(context, resource, serviceNumber);
		this.serviceNumber = serviceNumber;
		this.context = context;
		inflator = LayoutInflater.from(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = inflator.inflate(R.layout.service_number_adapter, parent,
				false);

		final TextView serviceNumberTV = (TextView) view
				.findViewById(R.id.serviceNumberTV);
		Button callButton = (Button) view.findViewById(R.id.callButton);
		pulse = AnimationUtils.loadAnimation(context, R.anim.pulse);
		callButton.startAnimation(pulse);
		serviceNumberTV.setText(serviceNumber.get(position).getServiceNumber());

		callButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent callIntent = new Intent(Intent.ACTION_CALL);
				callIntent.setData(Uri.parse("tel:"
						+ serviceNumberTV.getText().toString()));
				try {
					context.startActivity(callIntent);
				} catch (android.content.ActivityNotFoundException ex) {
					Toast.makeText(context, "your Activity is not found",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		return view;
	}
}
