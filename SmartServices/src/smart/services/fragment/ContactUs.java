package smart.services.fragment;

import info.androidhive.slidingmenu.R;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class ContactUs extends Fragment implements OnClickListener {

	private Button facebookButton, twitterButton, googleButton, youtubeButton,
			instagramButton;

	public ContactUs() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.contact_us, container, false);
		facebookButton = (Button) view.findViewById(R.id.facbookButton);
		twitterButton = (Button) view.findViewById(R.id.twitterButton);
		googleButton = (Button) view.findViewById(R.id.googleButton);
		youtubeButton = (Button) view.findViewById(R.id.youtubeButton);
		instagramButton = (Button) view.findViewById(R.id.instgramButton);

		facebookButton.setOnClickListener(this);
		twitterButton.setOnClickListener(this);
		googleButton.setOnClickListener(this);
		youtubeButton.setOnClickListener(this);
		instagramButton.setOnClickListener(this);

		return view;
	}

	@Override
	public void onClick(View v) {
		if (!isConnectingToInternet()) {
			Toast.makeText(getActivity(), "Check your internet connection",
					Toast.LENGTH_LONG).show();
			return;
		}

		String url = null;

		switch (v.getId()) {
		case R.id.facbookButton:
			url = "https://www.google.com";
			break;

		case R.id.twitterButton:
			url = "https://www.google.com";
			break;

		case R.id.googleButton:
			url = "https://www.google.com";
			break;

		case R.id.youtubeButton:
			url = "https://www.google.com";
			break;

		case R.id.instgramButton:
			url = "https://www.google.com";
			break;
		}

		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(url));
		startActivity(i);
	}

	public boolean isConnectingToInternet() {
		ConnectivityManager connectivity = (ConnectivityManager) getActivity()
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
