package smart.services.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import info.androidhive.slidingmenu.R;
import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

public class OfferAdapter extends BaseAdapter implements ListAdapter {
	private ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
	private Context context;
	TextView expire, titleEn, descEn;
	HashMap<String, String> offers;

	public OfferAdapter(ArrayList<HashMap<String, String>> offersList,
			Context context) {
		this.list = offersList;
		this.context = context;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		offers = list.get(position);
		View view = convertView;
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.single_offer_item, null);
		}
		expire = (TextView) view.findViewById(R.id.expire);
		titleEn = (TextView) view.findViewById(R.id.titleEn);
		descEn = (TextView) view.findViewById(R.id.descEn);

		expire.setText(offers.get("expireTime"));
		titleEn.setText(offers.get("titleEn"));
		descEn.setText(offers.get("descEn"), TextView.BufferType.SPANNABLE);

		return view;
	}

}
