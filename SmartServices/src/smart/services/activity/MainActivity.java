package smart.services.activity;

import info.androidhive.slidingmenu.R;

import java.util.ArrayList;

import smart.services.adapter.NavDrawerListAdapter;
import smart.services.fragment.AboutUs;
import smart.services.fragment.AddCar;
import smart.services.fragment.Booking;
import smart.services.fragment.ComplainFragment;
import smart.services.fragment.ContactUs;
import smart.services.fragment.El7a2ona;
import smart.services.fragment.Maintenance;
import smart.services.fragment.ManageBookings;
import smart.services.fragment.ManageCarInfo;
import smart.services.fragment.Offers;
import smart.services.fragment.Services;
import smart.services.fragment.Settings;
import smart.services.handler.DataBaseHandler;
import smart.services.model.NavDrawerItem;
import smart.services.model.Setting;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.ActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class MainActivity extends Activity {
	Fragment fragment = null;
	FragmentManager fragmentManager = getFragmentManager();
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private DataBaseHandler dataBaseHandler;
	// nav drawer title
	// private CharSequence mDrawerTitle;
	Boolean exit = false;
	// used to store app title
	private CharSequence mTitle;

	// slide menu items
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;

	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;

	private Setting setting;

	private MenuItem arabicItem = new MenuItem() {

		@Override
		public MenuItem setVisible(boolean visible) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public MenuItem setTitleCondensed(CharSequence title) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public MenuItem setTitle(int title) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public MenuItem setTitle(CharSequence title) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public MenuItem setShowAsActionFlags(int actionEnum) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setShowAsAction(int actionEnum) {
			// TODO Auto-generated method stub

		}

		@Override
		public MenuItem setShortcut(char numericChar, char alphaChar) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public MenuItem setOnMenuItemClickListener(
				OnMenuItemClickListener menuItemClickListener) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public MenuItem setOnActionExpandListener(
				OnActionExpandListener listener) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public MenuItem setNumericShortcut(char numericChar) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public MenuItem setIntent(Intent intent) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public MenuItem setIcon(int iconRes) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public MenuItem setIcon(Drawable icon) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public MenuItem setEnabled(boolean enabled) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public MenuItem setChecked(boolean checked) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public MenuItem setCheckable(boolean checkable) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public MenuItem setAlphabeticShortcut(char alphaChar) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public MenuItem setActionView(int resId) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public MenuItem setActionView(View view) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public MenuItem setActionProvider(ActionProvider actionProvider) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean isVisible() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isEnabled() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isChecked() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isCheckable() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isActionViewExpanded() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean hasSubMenu() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public CharSequence getTitleCondensed() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CharSequence getTitle() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public SubMenu getSubMenu() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int getOrder() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public char getNumericShortcut() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public ContextMenuInfo getMenuInfo() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int getItemId() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public Intent getIntent() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Drawable getIcon() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int getGroupId() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public char getAlphabeticShortcut() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getActionView() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public ActionProvider getActionProvider() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean expandActionView() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean collapseActionView() {
			// TODO Auto-generated method stub
			return false;
		}
	};

	private MenuItem englishItem = new MenuItem() {

		@Override
		public MenuItem setVisible(boolean visible) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public MenuItem setTitleCondensed(CharSequence title) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public MenuItem setTitle(int title) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public MenuItem setTitle(CharSequence title) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public MenuItem setShowAsActionFlags(int actionEnum) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setShowAsAction(int actionEnum) {
			// TODO Auto-generated method stub

		}

		@Override
		public MenuItem setShortcut(char numericChar, char alphaChar) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public MenuItem setOnMenuItemClickListener(
				OnMenuItemClickListener menuItemClickListener) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public MenuItem setOnActionExpandListener(
				OnActionExpandListener listener) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public MenuItem setNumericShortcut(char numericChar) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public MenuItem setIntent(Intent intent) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public MenuItem setIcon(int iconRes) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public MenuItem setIcon(Drawable icon) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public MenuItem setEnabled(boolean enabled) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public MenuItem setChecked(boolean checked) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public MenuItem setCheckable(boolean checkable) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public MenuItem setAlphabeticShortcut(char alphaChar) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public MenuItem setActionView(int resId) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public MenuItem setActionView(View view) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public MenuItem setActionProvider(ActionProvider actionProvider) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean isVisible() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isEnabled() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isChecked() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isCheckable() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isActionViewExpanded() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean hasSubMenu() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public CharSequence getTitleCondensed() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CharSequence getTitle() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public SubMenu getSubMenu() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int getOrder() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public char getNumericShortcut() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public ContextMenuInfo getMenuInfo() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int getItemId() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public Intent getIntent() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Drawable getIcon() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int getGroupId() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public char getAlphabeticShortcut() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getActionView() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public ActionProvider getActionProvider() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean expandActionView() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean collapseActionView() {
			// TODO Auto-generated method stub
			return false;
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		dataBaseHandler = new DataBaseHandler(this.getApplicationContext());

		// mTitle = mDrawerTitle = getTitle();

		// nav drawer icons from resources
		navMenuIcons = getResources()
				.obtainTypedArray(R.array.nav_drawer_icons);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
		int width = getResources().getDisplayMetrics().widthPixels * 3 / 4;
		DrawerLayout.LayoutParams params = (android.support.v4.widget.DrawerLayout.LayoutParams) mDrawerList
				.getLayoutParams();
		params.width = width;
		mDrawerList.setLayoutParams(params);

		navDrawerItems = new ArrayList<NavDrawerItem>();

		if (dataBaseHandler.getSettingsCount() == 0) {
			setting = new Setting();
			setting.setDuration(0);
			dataBaseHandler.addSetting(setting);
			navMenuTitles = getResources().getStringArray(
					R.array.nav_drawer_items);
		} else {
			setting = dataBaseHandler.getSetting();

			if (setting.getDuration() == 1) {
				ViewCompat.setLayoutDirection(getWindow().getDecorView(),
						ViewCompat.LAYOUT_DIRECTION_RTL);
				navMenuTitles = getResources().getStringArray(
						R.array.arabic_nav_drawer_items);

			} else {
				// load slide menu items
				navMenuTitles = getResources().getStringArray(
						R.array.nav_drawer_items);
			}
		}

		navDrawerItems.add(new NavDrawerItem("Welcome,\n "
				+ dataBaseHandler.getUser(1).getName(), R.drawable.smarty1));

		navDrawerItems.add(new NavDrawerItem(navMenuTitles[11],
				R.drawable.icon0));

		navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons
				.getResourceId(1, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons
				.getResourceId(2, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons
				.getResourceId(3, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons
				.getResourceId(4, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons
				.getResourceId(5, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[6], navMenuIcons
				.getResourceId(6, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[7], navMenuIcons
				.getResourceId(7, -1)));
		navDrawerItems
				.add(new NavDrawerItem(navMenuTitles[8], R.drawable.trans));
		navDrawerItems
				.add(new NavDrawerItem(navMenuTitles[9], R.drawable.trans));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[10],
				R.drawable.trans));

		// Recycle the typed array
		navMenuIcons.recycle();

		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

		// setting the nav drawer list adapter
		adapter = new NavDrawerListAdapter(getApplicationContext(),
				navDrawerItems);
		mDrawerList.setAdapter(adapter);

		// enabling action bar app icon and behaving it as toggle button
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		// getActionBar().setTitle("Hazem");
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, // nav menu toggle icon
				R.string.app_name, // nav drawer open - description for
									// accessibility
				R.string.app_name // nav drawer close - description for
									// accessibility
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle("Smart Service");// mTitle
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle("Smart Service");// mDrawerTitle
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		if (savedInstanceState == null) {
			// on first time display view for first nav item
			Bundle extras = getIntent().getExtras();
			if (extras != null) {
				String fragNum = extras.getString("fragNum");
				if (fragNum.equals("4")) {
					displayView(Integer.parseInt(fragNum));
					Booking.showSubmitDialog(this,
							"" + extras.getString("address"));
				} else if (fragNum.equals("6")) {
					displayView(Integer.parseInt(fragNum));
				} else if (fragNum.equals("7")) {
					displayView(Integer.parseInt(fragNum));
				} else {
					displayView(1);
				}
			} else {
				displayView(1);
			}
		}
	}

	/**
	 * Slide menu item click listener
	 * */
	private class SlideMenuClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// display view for selected nav drawer item
			displayView(position);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);

		arabicItem = menu.findItem(R.id.action_arabic);
		englishItem = menu.findItem(R.id.action_english);

		if (setting.getDuration() == 1) {
			arabicItem.setVisible(false);
		} else {
			englishItem.setVisible(false);
		}

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action bar actions click
		switch (item.getItemId()) {
		case R.id.action_settings:
			fragment = new Settings();
			newFrag(fragment, 0);
			return true;

		case R.id.action_arabic:
			dataBaseHandler.updateSetting(1);
			Intent intent = getIntent();
			finish();
			startActivity(intent);
			return true;

		case R.id.action_english:
			dataBaseHandler.updateSetting(0);
			Intent intent1 = getIntent();
			finish();
			startActivity(intent1);
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}

	}

	/* *
	 * Called when invalidateOptionsMenu() is triggered
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		// MenuItem bedMenuItem = menu.findItem(R.id.action_settings);
		// bedMenuItem.setTitle();
		return super.onPrepareOptionsMenu(menu);
	}

	/**
	 * Displaying fragment view for selected nav drawer list item
	 * */
	private void displayView(int position) {
		// update the main content by replacing fragments

		switch (position) {
		case 0:
			fragment = new Settings();
			break;
		case 1:
			fragment = new Settings();
			break;
		case 2:
			fragment = new Services();
			break;
		case 3:
			fragment = new Booking();
			break;
		case 4:
			fragment = new ManageBookings();
			break;
		case 5:
			fragment = new AddCar();
			break;
		case 6:
			fragment = new ManageCarInfo();
			break;
		case 7:
			fragment = new Offers();
			break;
		case 8:
			fragment = new El7a2ona();
			break;
		case 9:
			fragment = new Maintenance();
			break;
		case 10:
			fragment = new ComplainFragment();
			break;
		case 11:
			fragment = new ContactUs();
			break;

		default:
			break;
		}
		newFrag(fragment, position);
	}

	public void newFrag(Fragment fragment, int position) {
		if (fragment != null) {
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment).commit();
			// update selected item and title, then close the drawer
			mDrawerList.setItemChecked(position, true);
			// mDrawerList.setSelection(position);
			setTitle("Smart Service");// navMenuTitles[position]
			mDrawerLayout.closeDrawer(mDrawerList);
		} else {
			// error in creating fragment
			Log.e("MainActivity", "Error in creating fragment");
		}
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	public void onBackPressed() {
		if (exit) {
			MainActivity.this.finish();
		} else {
			displayView(1);
			Toast.makeText(this, "Press Double Click to Exit.",
					Toast.LENGTH_SHORT).show();
			exit = true;
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					exit = false;
				}
			}, (long) (0.5 * 1000));
		}
	}
}
