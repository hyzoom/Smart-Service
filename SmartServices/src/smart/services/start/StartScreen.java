package smart.services.start;

import info.androidhive.slidingmenu.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class StartScreen extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start_screen);
	}

	public void getVerifCode(View v) {
		Intent intent = new Intent(StartScreen.this,
				smart.services.start.VerificationCode.class);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
	}

	public void signIn(View v) {
		Intent intent = new Intent(StartScreen.this,
				smart.services.start.SignIn.class);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
	}

	public void signUp(View v) {
		Intent intent = new Intent(StartScreen.this,
				smart.services.start.VerificationCode.class);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
	}

	public void forget(View v) {
		Intent intent = new Intent(StartScreen.this,
				smart.services.start.ForgetPassWord.class);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
	}
}
