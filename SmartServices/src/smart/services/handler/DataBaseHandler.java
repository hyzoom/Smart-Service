package smart.services.handler;

import java.util.ArrayList;
import java.util.HashMap;

import smart.services.model.BookingModel;
import smart.services.model.Brand;
import smart.services.model.CarTest;
import smart.services.model.Color;
import smart.services.model.Complain;
import smart.services.model.Insurance;
import smart.services.model.Member;
import smart.services.model.Model;
import smart.services.model.Offer;
import smart.services.model.Service;
import smart.services.model.ServiceNumber;
import smart.services.model.ServiceType;
import smart.services.model.Setting;
import smart.services.model.SignUpInfo;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHandler extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "carTest0";

	// Contacts table name
	private static final String TABLE_SETTING = "setting";
	private static final String TABLE_USER = "user";
	private static final String TABLE_REGISTRATION = "regestration";
	private static final String TABLE_CAR_TEST = "carTestData0";
	private static final String TABLE_BRAND = "brand";
	private static final String TABLE_MODEL = "model";
	private static final String TABLE_COLOR = "color";
	private static final String TABLE_SERVICE = "service";
	private static final String TABLE_SERVICE_NUMBER = "serviceNumber";
	private static final String TABLE_SERVICE_TYPE = "serviceType";
	private static final String TABLE_OFFER = "offer";
	private static final String TABLE_BOOKING = "booking";
	private static final String TABLE_COMPLAIN = "complain";
	private static final String TABLE_INSURANCE = "insurance";

	// Contacts Table Setting Columns
	private static final String CURRENT_DURATION = "duration";

	// Contacts Table Columns names
	private static final String KEY_ID = "Id";
	private static final String USER_ID = "userId";
	private static final String EMAIL = "email";
	private static final String LOYALTYID = "loyaltyId";
	private static final String MSISDN = "msisdn";
	private static final String NAME = "name";

	// Registration
	private static final String VER_CODE = "verCode";
	private static final String MOB_NUM = "mobNum";
	private static final String PASSWORD = "password";
	private static final String ADDRESS = "address";
	private static final String IS_LOGGED_OUT = "isLoggedOut";

	// Contacts Table Columns car
	private static final String CAR_ID = "carId";
	private static final String CAR_BRAND = "carBrand";
	private static final String CAR_MODEL_ID = "carModelId";
	private static final String CAR_MODEL = "carModel";
	private static final String YEAR = "year";
	private static final String CAR_COLOR = "carColor";
	private static final String CHASE = "chase";
	private static final String PLATE_NUMBER = "plateNumber";
	private static final String INSURANCE_ID = "insuranceId";

	// Brand and Model table columns
	private static final String TYPE_ID = "chase";
	private static final String TYPE_NAME_IN_AR = "typeNameAr";
	private static final String TYPE_NAME_IN_EN = "typeNameEn";
	private static final String BRAND_CAR_MODEL = "carModels";
	private static final String MODEL_PARENT_BRAND = "parentBrand";

	// Color table columns
	private static final String COLOR_ID = "chase";
	private static final String COLOR_NAME_IN_AR = "typeNameAr";
	private static final String COLOR_NAME_IN_EN = "typeNameEn";

	// New add from here sa3eed
	// Service table columns
	private static final String SERVICE_ID = "service_id";
	private static final String SERVICE_DESC_IN_AR = "desc_ar";
	private static final String SERVICE_DESC_IN_EN = "desc_en";
	private static final String SERVICE_TITLE_IN_AR = "title_ar";
	private static final String SERVICE_TITLE_IN_EN = "title_en";

	// Service Number table columns
	private static final String SERVICE_NUMBER_ID = "service_number_id";
	private static final String SERVICE_NUMBER = "service_number";

	// Service table columns
	private static final String SERVICE_TYPE_ID = "service_type_id";

	// Service table columns
	private static final String OFFER_ID = "offer_id";
	private static final String OFFER_DESC_IN_AR = "desc_ar";
	private static final String OFFER_DESC_IN_EN = "desc_en";
	private static final String EXPIRE_TIME = "expire_time";
	private static final String EXPIRE = "expire";
	private static final String OFFER_TYPE = "offer_type";
	private static final String STATUS = "status";
	private static final String OFFER_TITLE_AR = "title_ar";
	private static final String OFFER_TITLE_En = "title_en";

	// Booking table
	private static final String BOOKING_ID = "booking_id";
	private static final String BOOKING_DATE = "booking_DATE";
	private static final String BOOKING_TIME = "booking_TIME";
	private static final String BOOKING_CAR_ID = "booking_car_id";
	private static final String CONFIRMATION_TIME = "confirmation_time";
	private static final String CREATION = "creation";
	private static final String DESCRIPTION = "description";
	private static final String REASON = "reason";
	private static final String BOOKING_TYPE = "booking_type";
	private static final String COMPLAIN_TEXT = "complain_text";

	// Insurance Table Attributes
	private static final String NAME_AR = "name_ar";
	private static final String NAME_EN = "name_en";

	public DataBaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		String CREATE_SETTING_TABLE = "CREATE TABLE " + TABLE_SETTING+ "(" + KEY_ID
				+ " INTEGER PRIMARY KEY," + CURRENT_DURATION + " INTEGER)";
		
		String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "(" + KEY_ID
				+ " INTEGER PRIMARY KEY," + USER_ID + " TEXT," + EMAIL
				+ " TEXT," + LOYALTYID + " TEXT," + MSISDN + " TEXT," + NAME
				+ " TEXT)";

		String CREATE_REGISTRATION_TABLE = "CREATE TABLE " + TABLE_REGISTRATION
				+ "(" + KEY_ID + " INTEGER PRIMARY KEY," + VER_CODE + " TEXT,"
				+ MOB_NUM + " TEXT," + NAME + " TEXT," + EMAIL + " TEXT,"
				+ PASSWORD + " TEXT," + ADDRESS + " TEXT," + IS_LOGGED_OUT
				+ " TEXT)";

		String CREATE_CAR_TABLE = "CREATE TABLE " + TABLE_CAR_TEST + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + CAR_ID + " TEXT,"
				+ CAR_BRAND + " TEXT," + CAR_MODEL_ID + " TEXT," + CAR_MODEL
				+ " TEXT," + YEAR + " TEXT," + CAR_COLOR + " TEXT," + CHASE
				+ " TEXT," + PLATE_NUMBER + " TEXT," + INSURANCE_ID
				+ " INTEGER," + USER_ID + " TEXT)";

		String CREATE_BRAND_TABLE = "CREATE TABLE " + TABLE_BRAND + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + TYPE_ID + " TEXT,"
				+ TYPE_NAME_IN_AR + " TEXT," + TYPE_NAME_IN_EN + " TEXT,"
				+ BRAND_CAR_MODEL + " TEXT)";

		String CREATE_MODEL_TABLE = "CREATE TABLE " + TABLE_MODEL + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + TYPE_ID + " TEXT,"
				+ TYPE_NAME_IN_AR + " TEXT," + TYPE_NAME_IN_EN + " TEXT,"
				+ MODEL_PARENT_BRAND + " TEXT)";

		String CREATE_COLOR_TABLE = "CREATE TABLE " + TABLE_COLOR + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + COLOR_ID + " TEXT,"
				+ COLOR_NAME_IN_AR + " TEXT," + COLOR_NAME_IN_EN + " TEXT)";

		String CREATE_SERVICE_TABLE = "CREATE TABLE " + TABLE_SERVICE + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + SERVICE_ID + " TEXT,"
				+ SERVICE_DESC_IN_AR + " TEXT," + SERVICE_DESC_IN_EN + " TEXT,"
				+ SERVICE_TITLE_IN_AR + " TEXT," + SERVICE_TITLE_IN_EN
				+ " TEXT)";

		String CREATE_SERVICE_NUMBER_TABLE = "CREATE TABLE "
				+ TABLE_SERVICE_NUMBER + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
				+ SERVICE_NUMBER_ID + " TEXT," + SERVICE_NUMBER + " TEXT,"
				+ SERVICE_ID + " TEXT)";

		String CREATE_SERVICE_TYPE_TABLE = "CREATE TABLE " + TABLE_SERVICE_TYPE
				+ "(" + KEY_ID + " INTEGER PRIMARY KEY," + SERVICE_TYPE_ID
				+ " TEXT," + TYPE_NAME_IN_AR + " TEXT," + TYPE_NAME_IN_EN
				+ " TEXT)";

		String CREATE_OFFER_TABLE = "CREATE TABLE " + TABLE_OFFER + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + OFFER_ID + " TEXT,"
				+ OFFER_DESC_IN_AR + " TEXT," + OFFER_DESC_IN_EN + " TEXT,"
				+ EXPIRE_TIME + " TEXT," + EXPIRE + " TEXT," + OFFER_TYPE
				+ " TEXT," + STATUS + " TEXT," + OFFER_TITLE_AR + " TEXT,"
				+ OFFER_TITLE_En + " TEXT)";

		String CREATE_BOOKING_TABLE = "CREATE TABLE " + TABLE_BOOKING + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + BOOKING_ID + " TEXT,"
				+ BOOKING_DATE + " TEXT," + BOOKING_TIME + " TEXT,"
				+ BOOKING_CAR_ID + " TEXT," + CONFIRMATION_TIME + " TEXT,"
				+ CREATION + " TEXT," + DESCRIPTION + " TEXT," + REASON
				+ " TEXT," + STATUS + " TEXT," + USER_ID + " TEXT," + MSISDN
				+ " TEXT," + CHASE + " TEXT," + BOOKING_TYPE + " TEXT)";

		String CREATE_COMPLAIN_TABLE = "CREATE TABLE " + TABLE_COMPLAIN + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + USER_ID + " TEXT,"
				+ SERVICE_TYPE_ID + " INTEGER," + COMPLAIN_TEXT + " TEXT)";

		String CREATE_INSURANCE_TABLE = "CREATE TABLE " + TABLE_INSURANCE + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + NAME_AR + " TEXT,"
				+ NAME_EN + " TEXT)";

		db.execSQL(CREATE_SETTING_TABLE);
		db.execSQL(CREATE_USER_TABLE);
		db.execSQL(CREATE_REGISTRATION_TABLE);
		db.execSQL(CREATE_CAR_TABLE);
		db.execSQL(CREATE_BRAND_TABLE);
		db.execSQL(CREATE_MODEL_TABLE);
		db.execSQL(CREATE_COLOR_TABLE);
		db.execSQL(CREATE_SERVICE_TABLE);
		db.execSQL(CREATE_SERVICE_NUMBER_TABLE);
		db.execSQL(CREATE_SERVICE_TYPE_TABLE);//
		db.execSQL(CREATE_OFFER_TABLE);
		db.execSQL(CREATE_BOOKING_TABLE);
		db.execSQL(CREATE_COMPLAIN_TABLE);
		db.execSQL(CREATE_INSURANCE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SETTING);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_REGISTRATION);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CAR_TEST);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_BRAND);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MODEL);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_COLOR);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SERVICE);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SERVICE_NUMBER);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SERVICE_TYPE);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_OFFER);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKING);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPLAIN);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_INSURANCE);

		// Create tables again
		onCreate(db);
	}

	public int getSettingsCount() {
		String countQuery = "SELECT  * FROM " + TABLE_SETTING;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int x = 0;
		x = cursor.getCount();
		cursor.close();
		db.close();
		return x;
	}
	
	public void addSetting(Setting setting) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		values.put(CURRENT_DURATION, setting.getDuration());
		
		// Inserting Row
		db.insert(TABLE_SETTING, null, values);
		db.close(); // Closing database connection
	}
	
	public Setting getSetting() {
		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = "SELECT  * FROM " + TABLE_SETTING + " WHERE "
				+ KEY_ID + " = " + 1;

		Cursor c = db.rawQuery(selectQuery, null);

		if (c != null)
			c.moveToFirst();

		Setting setting = new Setting();
		setting.setId(c.getInt(c.getColumnIndex(KEY_ID)));
		setting.setDuration(c.getInt(c.getColumnIndex(CURRENT_DURATION)));
		
		// return contact
		return setting;
	}
	
	public int updateSetting(int duration) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		values.put(CURRENT_DURATION, duration);
		// values.put(CAR_MODEL, mem.getCarModel());

		String id = 1 +"";
		return db.update(TABLE_SETTING, values, KEY_ID + " = ?",
				new String[] { id });
	}
	
	
	// Adding new User
	public void addUser(Member member) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		values.put(USER_ID, member.getUserId());
		values.put(EMAIL, member.getEmail());
		values.put(LOYALTYID, member.getLoyaltyId());
		values.put(MSISDN, member.getMsisdn());
		values.put(NAME, member.getName());
		// Inserting Row
		db.insert(TABLE_USER, null, values);
		db.close(); // Closing database connection
	}

	public Member getUser(int id) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_USER, new String[] { KEY_ID, USER_ID,
				EMAIL, LOYALTYID, MSISDN, NAME }, KEY_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		Member memb = new Member(Integer.parseInt(cursor.getString(0)),
				cursor.getString(1), cursor.getString(2), cursor.getString(3),
				cursor.getString(4), cursor.getString(5));
		// return contact
		return memb;
	}

	public int getUsersCount() {
		String countQuery = "SELECT  * FROM " + TABLE_USER;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int x = 0;
		x = cursor.getCount();
		cursor.close();
		db.close();
		return x;
	}

	public int updateUserInfo(Member mem, String id) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		values.put(MSISDN, mem.getMsisdn());
		values.put(NAME, mem.getName());
		values.put(EMAIL, mem.getEmail());
		// values.put(CAR_MODEL, mem.getCarModel());

		return db.update(TABLE_USER, values, KEY_ID + " = ?",
				new String[] { id });
	}

	public void deleteAllUsers() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_USER, KEY_ID + " >= 1", null);
		db.close();
	}

	// Adding new triple
	public void addRegister(SignUpInfo info) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(VER_CODE, info.getVerCode());
		values.put(MOB_NUM, info.getMobNum());
		values.put(NAME, info.getName());
		values.put(EMAIL, info.getEmail());
		values.put(PASSWORD, info.getPassword());
		values.put(ADDRESS, info.getAddress());
		values.put(IS_LOGGED_OUT, info.getIsLoggedOut());
		// Inserting Row
		db.insert(TABLE_REGISTRATION, null, values);
		db.close(); // Closing database connection
	}

	public int updateRegisterAddress(String address, String id) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		values.put(ADDRESS, address);
		// values.put(CAR_MODEL, mem.getCarModel());

		return db.update(TABLE_REGISTRATION, values, KEY_ID + " = ?",
				new String[] { id });
	}

	public SignUpInfo getRegister(int id) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_REGISTRATION, new String[] { KEY_ID,
				VER_CODE, MOB_NUM, NAME, EMAIL, PASSWORD, ADDRESS,
				IS_LOGGED_OUT }, KEY_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		SignUpInfo info = new SignUpInfo(Integer.parseInt(cursor.getString(0)),
				cursor.getString(1), cursor.getString(2), cursor.getString(3),
				cursor.getString(4), cursor.getString(5), cursor.getString(6),
				cursor.getString(7));
		// return contact
		return info;
	}

	public int getRegistersCount() {
		String countQuery = "SELECT  * FROM " + TABLE_REGISTRATION;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int x = 0;
		x = cursor.getCount();
		cursor.close();
		db.close();
		return x;
	}

	public void deleteAllRegisters() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_REGISTRATION, KEY_ID + " >= 1", null);
		db.close();
	}

	// //////////////////////////////////////////////////////////////////////////////////
	// Adding new Car
	public void addCar(CarTest carTest) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		values.put(CAR_ID, carTest.getCarId());
		values.put(CAR_BRAND, carTest.getCarBrand());
		values.put(CAR_MODEL_ID, carTest.getCarModelId());
		values.put(CAR_MODEL, carTest.getCarModel());
		values.put(YEAR, carTest.getYear());
		values.put(CAR_COLOR, carTest.getCarColor());
		values.put(CHASE, carTest.getChase());
		values.put(PLATE_NUMBER, carTest.getPlateNumber());
		values.put(INSURANCE_ID, carTest.getInsuranceId());
		values.put(USER_ID, carTest.getUserId());

		// Inserting Row
		db.insert(TABLE_CAR_TEST, null, values);
		db.close(); // Closing database connection
	}

	public CarTest getCar(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = "SELECT  * FROM " + TABLE_CAR_TEST + " WHERE "
				+ KEY_ID + " = " + id;

		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor != null)
			cursor.moveToFirst();
		CarTest carTest = new CarTest();

		carTest.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
		carTest.setCarId(cursor.getString(cursor.getColumnIndex(CAR_ID)));
		carTest.setCarBrand(cursor.getString(cursor.getColumnIndex(CAR_BRAND)));
		carTest.setCarModelId(cursor.getString(cursor
				.getColumnIndex(CAR_MODEL_ID)));
		carTest.setCarModel(cursor.getString(cursor.getColumnIndex(CAR_MODEL)));
		carTest.setYear(cursor.getString(cursor.getColumnIndex(YEAR)));
		carTest.setCarColor(cursor.getString(cursor.getColumnIndex(CAR_COLOR)));
		carTest.setChase(cursor.getString(cursor.getColumnIndex(CHASE)));
		carTest.setPlateNumber(cursor.getString(cursor
				.getColumnIndex(PLATE_NUMBER)));
		carTest.setInsuranceId(cursor.getInt(cursor
				.getColumnIndex(INSURANCE_ID)));
		carTest.setUserId(cursor.getString(cursor.getColumnIndex(USER_ID)));

		// return contact
		return carTest;
	}

	public CarTest getCar(String carId) {
		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = "SELECT  * FROM " + TABLE_CAR_TEST + " WHERE "
				+ CAR_ID + " = " + carId;

		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor != null)
			cursor.moveToFirst();

		CarTest carTest = new CarTest();

		carTest.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
		carTest.setCarId(cursor.getString(cursor.getColumnIndex(CAR_ID)));
		carTest.setCarBrand(cursor.getString(cursor.getColumnIndex(CAR_BRAND)));
		carTest.setCarModelId(cursor.getString(cursor
				.getColumnIndex(CAR_MODEL_ID)));
		carTest.setCarModel(cursor.getString(cursor.getColumnIndex(CAR_MODEL)));
		carTest.setYear(cursor.getString(cursor.getColumnIndex(YEAR)));
		carTest.setCarColor(cursor.getString(cursor.getColumnIndex(CAR_COLOR)));
		carTest.setChase(cursor.getString(cursor.getColumnIndex(CHASE)));
		carTest.setPlateNumber(cursor.getString(cursor
				.getColumnIndex(PLATE_NUMBER)));
		carTest.setInsuranceId(cursor.getInt(cursor
				.getColumnIndex(INSURANCE_ID)));
		carTest.setUserId(cursor.getString(cursor.getColumnIndex(USER_ID)));

		// return contact
		return carTest;
	}

	// get All Cars
	public ArrayList<HashMap<String, String>> getAllCars() {
		ArrayList<HashMap<String, String>> carsList = new ArrayList<HashMap<String, String>>();

		String selectQuery = "SELECT  * FROM " + TABLE_CAR_TEST;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				CarTest car = this.getCar(c.getInt(c.getColumnIndex(KEY_ID)));
				HashMap<String, String> cars = new HashMap<String, String>();

				cars.put("carId", car.getCarId());
				cars.put("carBrand", car.getCarBrand());
				cars.put("carModelId", car.getCarModelId());
				cars.put("carModel", car.getCarModel());
				cars.put("year", car.getYear());
				cars.put("carColor", car.getCarColor());
				cars.put("chase", car.getChase());
				cars.put("plate", car.getPlateNumber());
				cars.put("insurance", car.getInsuranceId() + "");
				cars.put("userId", car.getUserId());

				carsList.add(cars);

			} while (c.moveToNext());
		}
		return carsList;
	}

	// public ArrayList<HashMap<String, String>> getAllCars_() {
	// ArrayList<HashMap<String, String>> carsList = new
	// ArrayList<HashMap<String, String>>();
	//
	// String selectQuery = "SELECT  * FROM " + TABLE_CAR_TEST;
	//
	// SQLiteDatabase db = this.getReadableDatabase();
	// Cursor c = db.rawQuery(selectQuery, null);
	//
	// // looping through all rows and adding to list
	// if (c.moveToFirst()) {
	// do {
	// CarTest car = this.getCar(c.getInt(c.getColumnIndex(KEY_ID)));
	// HashMap<String, String> cars = new HashMap<String, String>();
	//
	// String currModelId = car.getCarModelId();
	// Model model = this.getModel(currModelId);
	// Brand brand = this.getBrand(model.getParentBrand());
	// Color color = this.getColor(car.getCarColor());
	//
	// cars.put("carId", car.getCarId());
	// cars.put("carBrand", brand.getTypeNameEn());
	// cars.put("carModelId", car.getCarModelId());
	// cars.put("carModel", model.getTypeNameEn());
	// cars.put("year", car.getYear());
	// cars.put("carColor", color.getColorEn());
	// cars.put("chase", car.getChase());
	// cars.put("plate", car.getPlateNumber());
	// cars.put("insurance", car.getInsuranceName());
	// cars.put("userId", car.getUserId());
	//
	// carsList.add(cars);
	//
	// } while (c.moveToNext());
	// }
	// return carsList;
	// }

	// get All Cars
	public ArrayList<CarTest> getAllCarTest() {
		ArrayList<CarTest> carsList = new ArrayList<CarTest>();

		String selectQuery = "SELECT  * FROM " + TABLE_CAR_TEST;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				CarTest car = this.getCar(c.getInt(c.getColumnIndex(KEY_ID)));

				carsList.add(car);

			} while (c.moveToNext());
		}
		return carsList;
	}

	// get user Cars
	public ArrayList<HashMap<String, String>> getUserCars(String userId) {
		ArrayList<HashMap<String, String>> carsList = new ArrayList<HashMap<String, String>>();

		String selectQuery = "SELECT  * FROM " + TABLE_CAR_TEST + " WHERE "
				+ USER_ID + " = " + userId;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				CarTest car = this.getCar(c.getInt(c.getColumnIndex(KEY_ID)));
				HashMap<String, String> cars = new HashMap<String, String>();

				cars.put("carId", car.getCarId());
				cars.put("carBrand", car.getCarBrand());
				cars.put("carModelId", car.getCarModelId());
				cars.put("carModel", car.getCarModel());
				cars.put("year", car.getYear());
				cars.put("carColor", car.getCarColor());
				cars.put("chase", car.getChase());
				cars.put("plate", car.getPlateNumber());
				cars.put("insurance", car.getInsuranceId() + "");
				cars.put("userId", car.getUserId());

				if (userId.equals(car.getUserId())) {
					carsList.add(cars);
				}

			} while (c.moveToNext());
		}
		return carsList;
	}

	public int updateCar(CarTest car, int id) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		// values.put(CAR_ID, car.getCarId());
		values.put(CAR_BRAND, car.getCarBrand());
		values.put(CAR_MODEL_ID, car.getCarModelId());
		values.put(CAR_MODEL, car.getCarModel());
		values.put(YEAR, car.getYear());
		values.put(CAR_COLOR, car.getCarColor());
		values.put(CHASE, car.getChase());
		values.put(PLATE_NUMBER, car.getPlateNumber());
		values.put(INSURANCE_ID, car.getInsuranceId());

		return db.update(TABLE_CAR_TEST, values, CAR_ID + " = ?",
				new String[] { String.valueOf(id) });
	}

	public int updateCarId(String carId, int id) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(CAR_ID, carId);
		return db.update(TABLE_CAR_TEST, values, KEY_ID + " = ?",
				new String[] { String.valueOf(id) });
	}

	public int getCarsCount() {
		String countQuery = "SELECT  * FROM " + TABLE_CAR_TEST;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int x = 0;
		x = cursor.getCount();
		cursor.close();
		db.close();
		return x;
	}

	public void deleteAllCars() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_CAR_TEST, KEY_ID + " >= 1", null);
		db.close();
	}

	public void deleteSingleCar(int carId) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_CAR_TEST, KEY_ID + " = ?",
				new String[] { String.valueOf(carId) });
		db.close();
	}

	// ///////////////////////// //////////////////////////////////

	// Brand Methods
	// Adding new brand
	public void addBrand(Brand brand) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		values.put(TYPE_ID, brand.getTypeId());
		values.put(TYPE_NAME_IN_AR, brand.getTypeNameAr());
		values.put(TYPE_NAME_IN_EN, brand.getTypeNameEn());
		values.put(BRAND_CAR_MODEL, brand.getCarModels());
		// Inserting Row
		db.insert(TABLE_BRAND, null, values);
		db.close(); // Closing database connection
	}

	public Brand getBrand(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = "SELECT  * FROM " + TABLE_BRAND + " WHERE "
				+ KEY_ID + " = " + id;

		Cursor c = db.rawQuery(selectQuery, null);

		if (c != null)
			c.moveToFirst();

		Brand brand = new Brand();
		brand.setId(c.getInt(c.getColumnIndex(KEY_ID)));
		brand.setTypeId(c.getString(c.getColumnIndex(TYPE_ID)));
		brand.setTypeNameAr(c.getString(c.getColumnIndex(TYPE_NAME_IN_AR)));
		brand.setTypeNameEn(c.getString(c.getColumnIndex(TYPE_NAME_IN_EN)));
		brand.setCarModels(c.getString(c.getColumnIndex(BRAND_CAR_MODEL)));

		// return contact
		return brand;
	}

	public Brand getBrand(String id) {
		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = "SELECT  * FROM " + TABLE_BRAND + " WHERE "
				+ TYPE_ID + " = " + id;

		Cursor c = db.rawQuery(selectQuery, null);

		if (c != null)
			c.moveToFirst();

		Brand brand = new Brand();
		brand.setId(c.getInt(c.getColumnIndex(KEY_ID)));
		brand.setTypeId(c.getString(c.getColumnIndex(TYPE_ID)));
		brand.setTypeNameAr(c.getString(c.getColumnIndex(TYPE_NAME_IN_AR)));
		brand.setTypeNameEn(c.getString(c.getColumnIndex(TYPE_NAME_IN_EN)));
		brand.setCarModels(c.getString(c.getColumnIndex(BRAND_CAR_MODEL)));

		// return contact
		return brand;
	}

	// get All Brands
	public ArrayList<Brand> getAllBrands() {
		ArrayList<Brand> brandList = new ArrayList<Brand>();

		String selectQuery = "SELECT  * FROM " + TABLE_BRAND;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				Brand brand = new Brand();
				brand.setId(c.getInt(c.getColumnIndex(KEY_ID)));
				brand.setTypeId(c.getString(c.getColumnIndex(TYPE_ID)));
				brand.setTypeNameAr(c.getString(c
						.getColumnIndex(TYPE_NAME_IN_AR)));
				brand.setTypeNameEn(c.getString(c
						.getColumnIndex(TYPE_NAME_IN_EN)));
				brand.setCarModels(c.getString(c
						.getColumnIndex(BRAND_CAR_MODEL)));

				brandList.add(brand);

			} while (c.moveToNext());
		}
		return brandList;
	}

	public int getBrandsCount() {
		String countQuery = "SELECT  * FROM " + TABLE_BRAND;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int x = 0;
		x = cursor.getCount();
		cursor.close();
		db.close();
		return x;
	}

	public void deleteAllBrands() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_BRAND, KEY_ID + " >= 1", null);
		db.close();
	}

	public void deleteSingleBrand(int brandId) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_BRAND, KEY_ID + " = ?",
				new String[] { String.valueOf(brandId) });
		db.close();
	}

	// ///////////////////////////////////////////////////
	// Model Methods
	// Adding new Model
	public void addModel(Model model) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		values.put(TYPE_ID, model.getTypeId());
		values.put(TYPE_NAME_IN_AR, model.getTypeNameAr());
		values.put(TYPE_NAME_IN_EN, model.getTypeNameEn());
		values.put(MODEL_PARENT_BRAND, model.getParentBrand());

		// Inserting Row
		db.insert(TABLE_MODEL, null, values);
		db.close(); // Closing database connection
	}

	public Model getModel(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = "SELECT  * FROM " + TABLE_MODEL + " WHERE "
				+ KEY_ID + " = " + id;

		Cursor c = db.rawQuery(selectQuery, null);

		if (c != null)
			c.moveToFirst();

		Model model = new Model();
		model.setId(c.getInt(c.getColumnIndex(KEY_ID)));
		model.setTypeId(c.getString(c.getColumnIndex(TYPE_ID)));
		model.setTypeNameAr(c.getString(c.getColumnIndex(TYPE_NAME_IN_AR)));
		model.setTypeNameEn(c.getString(c.getColumnIndex(TYPE_NAME_IN_EN)));
		model.setParentBrand(c.getString(c.getColumnIndex(MODEL_PARENT_BRAND)));

		// return contact
		return model;
	}

	public Model getModel(String id) {
		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = "SELECT  * FROM " + TABLE_MODEL + " WHERE "
				+ TYPE_ID + " = " + id;

		Cursor c = db.rawQuery(selectQuery, null);

		if (c != null)
			c.moveToFirst();

		Model model = new Model();
		model.setId(c.getInt(c.getColumnIndex(KEY_ID)));
		model.setTypeId(c.getString(c.getColumnIndex(TYPE_ID)));
		model.setTypeNameAr(c.getString(c.getColumnIndex(TYPE_NAME_IN_AR)));
		model.setTypeNameEn(c.getString(c.getColumnIndex(TYPE_NAME_IN_EN)));
		model.setParentBrand(c.getString(c.getColumnIndex(MODEL_PARENT_BRAND)));

		// return contact
		return model;
	}

	// get All Models
	public ArrayList<Model> getAllModels() {
		ArrayList<Model> modelList = new ArrayList<Model>();

		String selectQuery = "SELECT  * FROM " + TABLE_MODEL;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				Model model = new Model();
				model.setId(c.getInt(c.getColumnIndex(KEY_ID)));
				model.setTypeId(c.getString(c.getColumnIndex(TYPE_ID)));
				model.setTypeNameAr(c.getString(c
						.getColumnIndex(TYPE_NAME_IN_AR)));
				model.setTypeNameEn(c.getString(c
						.getColumnIndex(TYPE_NAME_IN_EN)));
				model.setParentBrand(c.getString(c
						.getColumnIndex(MODEL_PARENT_BRAND)));

				modelList.add(model);

			} while (c.moveToNext());
		}
		return modelList;
	}

	public int getModelsCount() {
		String countQuery = "SELECT  * FROM " + TABLE_MODEL;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int x = 0;
		x = cursor.getCount();
		cursor.close();
		db.close();
		return x;
	}

	public void deleteAllModels() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_MODEL, KEY_ID + " >= 1", null);
		db.close();
	}

	public void deleteSingleModel(int modelId) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_BRAND, KEY_ID + " = ?",
				new String[] { String.valueOf(modelId) });
		db.close();
	}

	// ///////////////////////////////////////////////////
	// Color Methods
	// Adding new Color
	public void addColor(Color color) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		values.put(COLOR_ID, color.getColorId());
		values.put(COLOR_NAME_IN_AR, color.getColorAr());
		values.put(COLOR_NAME_IN_EN, color.getColorEn());

		// Inserting Row
		db.insert(TABLE_COLOR, null, values);
		db.close(); // Closing database connection
	}

	public Color getColor(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = "SELECT  * FROM " + TABLE_COLOR + " WHERE "
				+ KEY_ID + " = " + id;

		Cursor c = db.rawQuery(selectQuery, null);

		if (c != null)
			c.moveToFirst();

		Color color = new Color();
		color.setId(c.getInt(c.getColumnIndex(COLOR_ID)));
		color.setColorAr(c.getString(c.getColumnIndex(COLOR_NAME_IN_AR)));
		color.setColorEn(c.getString(c.getColumnIndex(COLOR_NAME_IN_EN)));

		// return contact
		return color;
	}

	public Color getColor(String colorId) {
		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = "SELECT  * FROM " + TABLE_COLOR + " WHERE "
				+ COLOR_ID + " = " + colorId;

		Cursor c = db.rawQuery(selectQuery, null);

		if (c != null)
			c.moveToFirst();

		Color color = new Color();
		color.setId(c.getInt(c.getColumnIndex(COLOR_ID)));
		color.setColorAr(c.getString(c.getColumnIndex(COLOR_NAME_IN_AR)));
		color.setColorEn(c.getString(c.getColumnIndex(COLOR_NAME_IN_EN)));

		// return contact
		return color;
	}

	// get All Models
	public ArrayList<Color> getAllColors() {
		ArrayList<Color> colorList = new ArrayList<Color>();

		String selectQuery = "SELECT  * FROM " + TABLE_COLOR;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				Color color = new Color();
				color.setId(c.getInt(c.getColumnIndex(KEY_ID)));
				color.setColorId(c.getString(c.getColumnIndex(COLOR_ID)));
				color.setColorAr(c.getString(c.getColumnIndex(COLOR_NAME_IN_AR)));
				color.setColorEn(c.getString(c.getColumnIndex(COLOR_NAME_IN_EN)));

				colorList.add(color);

			} while (c.moveToNext());
		}
		return colorList;
	}

	public int getColorsCount() {
		String countQuery = "SELECT  * FROM " + TABLE_COLOR;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int x = 0;
		x = cursor.getCount();
		cursor.close();
		db.close();
		return x;
	}

	public void deleteAllColors() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_COLOR, KEY_ID + " >= 1", null);
		db.close();
	}

	public void deleteSingleColor(int colorId) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_COLOR, KEY_ID + " = ?",
				new String[] { String.valueOf(colorId) });
		db.close();
	}

	public void addService(Service service) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		values.put(SERVICE_ID, service.getServiceId());
		values.put(SERVICE_DESC_IN_AR, service.getDescAr());
		values.put(SERVICE_DESC_IN_EN, service.getDescEn());
		values.put(SERVICE_TITLE_IN_AR, service.getTitleAr());
		values.put(SERVICE_TITLE_IN_EN, service.getTitleEn());

		// Inserting Row
		db.insert(TABLE_SERVICE, null, values);
		db.close(); // Closing database connection
	}

	public Service getService(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = "SELECT  * FROM " + TABLE_SERVICE + " WHERE "
				+ KEY_ID + " = " + id;

		Cursor c = db.rawQuery(selectQuery, null);

		if (c != null)
			c.moveToFirst();

		Service service = new Service();
		service.setId(c.getInt(c.getColumnIndex(KEY_ID)));
		service.setServiceId(c.getString(c.getColumnIndex(SERVICE_ID)));
		service.setDescAr(c.getString(c.getColumnIndex(SERVICE_DESC_IN_AR)));
		service.setDescEn(c.getString(c.getColumnIndex(SERVICE_DESC_IN_EN)));
		service.setTitleAr(c.getString(c.getColumnIndex(SERVICE_TITLE_IN_AR)));
		service.setTitleEn(c.getString(c.getColumnIndex(SERVICE_TITLE_IN_EN)));

		// return contact
		return service;
	}

	// get All Service
	public ArrayList<Service> getAllService() {
		ArrayList<Service> serviceList = new ArrayList<Service>();

		String selectQuery = "SELECT  * FROM " + TABLE_SERVICE;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				Service service = new Service();
				service.setId(c.getInt(c.getColumnIndex(KEY_ID)));
				service.setServiceId(c.getString(c.getColumnIndex(SERVICE_ID)));
				service.setDescAr(c.getString(c
						.getColumnIndex(SERVICE_DESC_IN_AR)));
				service.setDescEn(c.getString(c
						.getColumnIndex(SERVICE_DESC_IN_EN)));
				service.setTitleAr(c.getString(c
						.getColumnIndex(SERVICE_TITLE_IN_AR)));
				service.setTitleEn(c.getString(c
						.getColumnIndex(SERVICE_TITLE_IN_EN)));

				serviceList.add(service);

			} while (c.moveToNext());
		}
		return serviceList;
	}

	public int getServicesCount() {
		String countQuery = "SELECT  * FROM " + TABLE_SERVICE;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int x = 0;
		x = cursor.getCount();
		cursor.close();
		db.close();
		return x;
	}

	public void deleteAllService() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_SERVICE, KEY_ID + " >= 1", null);
		db.close();
		deleteAllServiceNumbers();
	}

	public void deleteSingleService(int serviceId) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_SERVICE, KEY_ID + " = ?",
				new String[] { String.valueOf(serviceId) });
		db.close();
	}

	// Service Number Methods
	// Adding new Service Number
	public void addServiceNumber(ServiceNumber serviceNumber) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		values.put(SERVICE_NUMBER_ID, serviceNumber.getServiceNumberId());
		values.put(SERVICE_NUMBER, serviceNumber.getServiceNumber());
		values.put(SERVICE_ID, serviceNumber.getServiceId());

		// Inserting Row
		db.insert(TABLE_SERVICE_NUMBER, null, values);
		db.close(); // Closing database connection
	}

	public ServiceNumber getServiceNumber(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = "SELECT  * FROM " + TABLE_SERVICE_NUMBER
				+ " WHERE " + KEY_ID + " = " + id;

		Cursor c = db.rawQuery(selectQuery, null);

		if (c != null)
			c.moveToFirst();

		ServiceNumber serviceNumber = new ServiceNumber();
		serviceNumber.setId(c.getInt(c.getColumnIndex(KEY_ID)));
		serviceNumber.setServiceNumberId(c.getString(c
				.getColumnIndex(SERVICE_NUMBER_ID)));
		serviceNumber.setServiceNumber(c.getString(c
				.getColumnIndex(SERVICE_NUMBER)));
		serviceNumber.setServiceId(c.getString(c.getColumnIndex(SERVICE_ID)));

		// return contact
		return serviceNumber;
	}

	// get All Service Numbers for service
	public ArrayList<ServiceNumber> getAllServicNumbers(String serviceId) {
		ArrayList<ServiceNumber> serviceNumberList = new ArrayList<ServiceNumber>();

		String selectQuery = "SELECT  * FROM " + TABLE_SERVICE_NUMBER;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				ServiceNumber serviceNumber = new ServiceNumber();
				serviceNumber.setId(c.getInt(c.getColumnIndex(KEY_ID)));
				serviceNumber.setServiceNumberId(c.getString(c
						.getColumnIndex(SERVICE_NUMBER_ID)));
				serviceNumber.setServiceNumber(c.getString(c
						.getColumnIndex(SERVICE_NUMBER)));
				serviceNumber.setServiceId(c.getString(c
						.getColumnIndex(SERVICE_ID)));

				if (serviceNumber.getServiceId().equals(serviceId)) {
					serviceNumberList.add(serviceNumber);
				}

			} while (c.moveToNext());
		}
		return serviceNumberList;
	}

	public int getServiceNumbersCount() {
		String countQuery = "SELECT  * FROM " + TABLE_SERVICE_NUMBER;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int x = 0;
		x = cursor.getCount();
		cursor.close();
		db.close();
		return x;
	}

	public void deleteAllServiceNumbers() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_SERVICE_NUMBER, KEY_ID + " >= 1", null);
		db.close();
	}

	public void deleteSingleServiceNumber(int serviceId) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_SERVICE_NUMBER, KEY_ID + " = ?",
				new String[] { String.valueOf(serviceId) });
		db.close();
	}

	// Service Type Methods
	// Adding new Service Type
	public void addServiceType(ServiceType serviceType) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		values.put(SERVICE_TYPE_ID, serviceType.getTypeId());
		values.put(TYPE_NAME_IN_AR, serviceType.getTypeNameAr());
		values.put(TYPE_NAME_IN_EN, serviceType.getTypeNameEn());

		// Inserting Row
		db.insert(TABLE_SERVICE_TYPE, null, values);
		db.close(); // Closing database connection
	}

	public ServiceType getServiceType(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = "SELECT  * FROM " + TABLE_SERVICE_TYPE + " WHERE "
				+ KEY_ID + " = " + id;

		Cursor c = db.rawQuery(selectQuery, null);

		if (c != null)
			c.moveToFirst();

		ServiceType serviceType = new ServiceType();
		serviceType.setId(c.getInt(c.getColumnIndex(KEY_ID)));
		serviceType.setTypeId(c.getString(c.getColumnIndex(SERVICE_TYPE_ID)));
		serviceType
				.setTypeNameAr(c.getString(c.getColumnIndex(TYPE_NAME_IN_AR)));
		serviceType
				.setTypeNameEn(c.getString(c.getColumnIndex(TYPE_NAME_IN_EN)));

		// return contact
		return serviceType;
	}

	public ServiceType getServiceType(String id) {
		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = "SELECT  * FROM " + TABLE_SERVICE_TYPE + " WHERE "
				+ SERVICE_TYPE_ID + " = " + id;

		Cursor c = db.rawQuery(selectQuery, null);

		if (c != null)
			c.moveToFirst();

		ServiceType serviceType = new ServiceType();
		serviceType.setId(c.getInt(c.getColumnIndex(KEY_ID)));
		serviceType.setTypeId(c.getString(c.getColumnIndex(SERVICE_TYPE_ID)));
		serviceType
				.setTypeNameAr(c.getString(c.getColumnIndex(TYPE_NAME_IN_AR)));
		serviceType
				.setTypeNameEn(c.getString(c.getColumnIndex(TYPE_NAME_IN_EN)));

		// return contact
		return serviceType;
	}

	// get All Service Types for service
	public ArrayList<ServiceType> getAllServiceTypes() {
		ArrayList<ServiceType> serviceTypeList = new ArrayList<ServiceType>();

		String selectQuery = "SELECT  * FROM " + TABLE_SERVICE_TYPE;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				ServiceType serviceType = new ServiceType();
				serviceType.setId(c.getInt(c.getColumnIndex(KEY_ID)));
				serviceType.setTypeId(c.getString(c
						.getColumnIndex(SERVICE_TYPE_ID)));
				serviceType.setTypeNameAr(c.getString(c
						.getColumnIndex(TYPE_NAME_IN_AR)));
				serviceType.setTypeNameEn(c.getString(c
						.getColumnIndex(TYPE_NAME_IN_EN)));

				serviceTypeList.add(serviceType);
			} while (c.moveToNext());
		}
		return serviceTypeList;
	}

	public int getServiceTypesCount() {
		String countQuery = "SELECT  * FROM " + TABLE_SERVICE_TYPE;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int x = 0;
		x = cursor.getCount();
		cursor.close();
		db.close();
		return x;
	}

	public void deleteAllServiceTypes() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_SERVICE_TYPE, KEY_ID + " >= 1", null);
		db.close();
	}

	public void deleteSingleServiceType(int serviceTypeId) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_SERVICE_TYPE, KEY_ID + " = ?",
				new String[] { String.valueOf(serviceTypeId) });
		db.close();
	}

	// Service offer Methods
	public void addOffer(Offer offer) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		values.put(OFFER_ID, offer.getOfferId());
		values.put(OFFER_DESC_IN_AR, offer.getDescAr());
		values.put(OFFER_DESC_IN_EN, offer.getDescEn());
		values.put(EXPIRE_TIME, offer.getExpireTime());
		values.put(EXPIRE, offer.getExpire());
		values.put(OFFER_TYPE, offer.getOfferType());
		values.put(STATUS, offer.getStatus());
		values.put(OFFER_TITLE_AR, offer.getTitleAr());
		values.put(OFFER_TITLE_En, offer.getTitleEn());

		// Inserting Row
		db.insert(TABLE_OFFER, null, values);
		db.close(); // Closing database connection
	}

	public Offer getOffer(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = "SELECT  * FROM " + TABLE_OFFER + " WHERE "
				+ KEY_ID + " = " + id;

		Cursor c = db.rawQuery(selectQuery, null);

		if (c != null)
			c.moveToFirst();

		Offer offer = new Offer();
		offer.setId(c.getInt(c.getColumnIndex(KEY_ID)));
		offer.setOfferId(c.getString(c.getColumnIndex(OFFER_ID)));
		offer.setDescAr(c.getString(c.getColumnIndex(OFFER_DESC_IN_AR)));
		offer.setDescEn(c.getString(c.getColumnIndex(OFFER_DESC_IN_EN)));
		offer.setExpireTime(c.getString(c.getColumnIndex(EXPIRE_TIME)));
		offer.setExpire(c.getString(c.getColumnIndex(EXPIRE)));
		offer.setOfferType(c.getString(c.getColumnIndex(OFFER_TYPE)));
		offer.setStatus(c.getString(c.getColumnIndex(STATUS)));
		offer.setTitleAr(c.getString(c.getColumnIndex(OFFER_TITLE_AR)));
		offer.setTitleEn(c.getString(c.getColumnIndex(OFFER_TITLE_En)));

		// return contact
		return offer;
	}

	// get All offers
	public ArrayList<Offer> getAllOffers() {
		ArrayList<Offer> offerList = new ArrayList<Offer>();

		String selectQuery = "SELECT  * FROM " + TABLE_OFFER;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				Offer offer = new Offer();
				offer.setId(c.getInt(c.getColumnIndex(KEY_ID)));
				offer.setOfferId(c.getString(c.getColumnIndex(OFFER_ID)));
				offer.setDescAr(c.getString(c.getColumnIndex(OFFER_DESC_IN_AR)));
				offer.setDescEn(c.getString(c.getColumnIndex(OFFER_DESC_IN_EN)));
				offer.setExpireTime(c.getString(c.getColumnIndex(EXPIRE_TIME)));
				offer.setExpire(c.getString(c.getColumnIndex(EXPIRE)));
				offer.setOfferType(c.getString(c.getColumnIndex(OFFER_TYPE)));
				offer.setStatus(c.getString(c.getColumnIndex(STATUS)));
				offer.setTitleAr(c.getString(c.getColumnIndex(OFFER_TITLE_AR)));
				offer.setTitleEn(c.getString(c.getColumnIndex(OFFER_TITLE_En)));

				offerList.add(offer);
			} while (c.moveToNext());
		}
		return offerList;
	}

	public int getOffersCount() {
		String countQuery = "SELECT  * FROM " + TABLE_OFFER;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int x = 0;
		x = cursor.getCount();
		cursor.close();
		db.close();
		return x;
	}

	public void deleteAllOffers() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_OFFER, KEY_ID + " >= 1", null);
		db.close();
	}

	public void deleteSingleOffer(int offerId) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_OFFER, KEY_ID + " = ?",
				new String[] { String.valueOf(offerId) });
		db.close();
	}

	// Service booking Methods
	public void addBooking(BookingModel bookingModel) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		values.put(BOOKING_ID, bookingModel.getBookingId());
		values.put(BOOKING_DATE, bookingModel.getBookingDate());
		values.put(BOOKING_TIME, bookingModel.getBookingTime());
		values.put(BOOKING_CAR_ID, bookingModel.getCarId());
		values.put(CONFIRMATION_TIME, bookingModel.getConfirmationTime());
		values.put(CREATION, bookingModel.getCreation());
		values.put(DESCRIPTION, bookingModel.getDescription());
		values.put(REASON, bookingModel.getReason());
		values.put(STATUS, bookingModel.getState());
		values.put(USER_ID, bookingModel.getUserId());
		values.put(MSISDN, bookingModel.getMsisdn());
		values.put(CHASE, bookingModel.getChaseNumber());
		values.put(BOOKING_TYPE, bookingModel.getBookingType());

		// Inserting Row
		db.insert(TABLE_BOOKING, null, values);
		db.close(); // Closing database connection
	}

	public BookingModel getBooking(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = "SELECT  * FROM " + TABLE_BOOKING + " WHERE "
				+ KEY_ID + " = " + id;

		Cursor c = db.rawQuery(selectQuery, null);

		if (c != null)
			c.moveToFirst();

		BookingModel bookingModel = new BookingModel();
		bookingModel.setId(c.getInt(c.getColumnIndex(KEY_ID)));
		bookingModel.setBookingId(c.getString(c.getColumnIndex(BOOKING_ID)));
		bookingModel
				.setBookingDate(c.getString(c.getColumnIndex(BOOKING_DATE)));
		bookingModel
				.setBookingTime(c.getString(c.getColumnIndex(BOOKING_TIME)));
		bookingModel.setCarId(c.getString(c.getColumnIndex(BOOKING_CAR_ID)));
		bookingModel.setConfirmationTime(c.getString(c
				.getColumnIndex(CONFIRMATION_TIME)));
		bookingModel.setCreation(c.getString(c.getColumnIndex(CREATION)));
		bookingModel.setDescription(c.getString(c.getColumnIndex(DESCRIPTION)));
		bookingModel.setReason(c.getString(c.getColumnIndex(REASON)));
		bookingModel.setState(c.getString(c.getColumnIndex(STATUS)));
		bookingModel.setUserId(c.getString(c.getColumnIndex(USER_ID)));
		bookingModel.setMsisdn(c.getString(c.getColumnIndex(MSISDN)));
		bookingModel.setChaseNumber(c.getString(c.getColumnIndex(CHASE)));
		bookingModel
				.setBookingType(c.getString(c.getColumnIndex(BOOKING_TYPE)));

		// return contact
		return bookingModel;
	}

	// get All booking
	public ArrayList<BookingModel> getAllBookings() {
		ArrayList<BookingModel> bookingList = new ArrayList<BookingModel>();

		String selectQuery = "SELECT  * FROM " + TABLE_BOOKING;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				BookingModel bookingModel = new BookingModel();
				bookingModel.setId(c.getInt(c.getColumnIndex(KEY_ID)));
				bookingModel.setBookingId(c.getString(c
						.getColumnIndex(BOOKING_ID)));
				bookingModel.setBookingDate(c.getString(c
						.getColumnIndex(BOOKING_DATE)));
				bookingModel.setBookingTime(c.getString(c
						.getColumnIndex(BOOKING_TIME)));
				bookingModel.setCarId(c.getString(c
						.getColumnIndex(BOOKING_CAR_ID)));
				bookingModel.setConfirmationTime(c.getString(c
						.getColumnIndex(CONFIRMATION_TIME)));
				bookingModel
						.setCreation(c.getString(c.getColumnIndex(CREATION)));
				bookingModel.setDescription(c.getString(c
						.getColumnIndex(DESCRIPTION)));
				bookingModel.setReason(c.getString(c.getColumnIndex(REASON)));
				bookingModel.setState(c.getString(c.getColumnIndex(STATUS)));
				bookingModel.setUserId(c.getString(c.getColumnIndex(USER_ID)));
				bookingModel.setMsisdn(c.getString(c.getColumnIndex(MSISDN)));
				bookingModel
						.setChaseNumber(c.getString(c.getColumnIndex(CHASE)));
				bookingModel.setBookingType(c.getString(c
						.getColumnIndex(BOOKING_TYPE)));

				bookingList.add(bookingModel);
			} while (c.moveToNext());
		}
		return bookingList;
	}

	// get All booking
	public ArrayList<BookingModel> getUserBookings(String userId) {
		ArrayList<BookingModel> bookingList = new ArrayList<BookingModel>();

		String selectQuery = "SELECT  * FROM " + TABLE_BOOKING;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				BookingModel bookingModel = new BookingModel();
				bookingModel.setId(c.getInt(c.getColumnIndex(KEY_ID)));
				bookingModel.setBookingId(c.getString(c
						.getColumnIndex(BOOKING_ID)));
				bookingModel.setBookingDate(c.getString(c
						.getColumnIndex(BOOKING_DATE)));
				bookingModel.setBookingTime(c.getString(c
						.getColumnIndex(BOOKING_TIME)));
				bookingModel.setCarId(c.getString(c
						.getColumnIndex(BOOKING_CAR_ID)));
				bookingModel.setConfirmationTime(c.getString(c
						.getColumnIndex(CONFIRMATION_TIME)));
				bookingModel
						.setCreation(c.getString(c.getColumnIndex(CREATION)));
				bookingModel.setDescription(c.getString(c
						.getColumnIndex(DESCRIPTION)));
				bookingModel.setReason(c.getString(c.getColumnIndex(REASON)));
				bookingModel.setState(c.getString(c.getColumnIndex(STATUS)));
				bookingModel.setUserId(c.getString(c.getColumnIndex(USER_ID)));
				bookingModel.setMsisdn(c.getString(c.getColumnIndex(MSISDN)));
				bookingModel
						.setChaseNumber(c.getString(c.getColumnIndex(CHASE)));
				bookingModel.setBookingType(c.getString(c
						.getColumnIndex(BOOKING_TYPE)));

				if (userId.equals(bookingModel.getUserId())) {
					bookingList.add(bookingModel);
				}
			} while (c.moveToNext());
		}
		return bookingList;
	}

	public int getBookingCount() {
		String countQuery = "SELECT  * FROM " + TABLE_BOOKING;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int x = 0;
		x = cursor.getCount();
		cursor.close();
		db.close();
		return x;
	}

	public void deleteAllBookings() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_BOOKING, KEY_ID + " >= 1", null);
		db.close();
	}

	public void deleteSingleBooking(int bookingId) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_BOOKING, KEY_ID + " = ?",
				new String[] { String.valueOf(bookingId) });
		db.close();
	}

	// //////////////////////////////////////////////////////////////////
	// Complains
	public void addComplain(Complain complain) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		values.put(SERVICE_TYPE_ID, complain.getServiceType());
		values.put(COMPLAIN_TEXT, complain.getComplain());
		values.put(USER_ID, complain.getUserId());

		// Inserting Row
		db.insert(TABLE_COMPLAIN, null, values);
		db.close(); // Closing database connection
	}

	public Complain getComplain(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = "SELECT  * FROM " + TABLE_COMPLAIN + " WHERE "
				+ KEY_ID + " = " + id;

		Cursor c = db.rawQuery(selectQuery, null);

		if (c != null)
			c.moveToFirst();

		Complain complain = new Complain();
		complain.setId(c.getInt(c.getColumnIndex(KEY_ID)));
		complain.setServiceType(c.getInt(c.getColumnIndex(SERVICE_TYPE_ID)));
		complain.setComplain(c.getString(c.getColumnIndex(COMPLAIN_TEXT)));
		complain.setUserId(c.getString(c.getColumnIndex(USER_ID)));

		// return contact
		return complain;
	}

	public Complain getComplain(String id) {
		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = "SELECT  * FROM " + TABLE_COMPLAIN + " WHERE "
				+ SERVICE_TYPE_ID + " = " + id;

		Cursor c = db.rawQuery(selectQuery, null);

		if (c != null)
			c.moveToFirst();

		Complain complain = new Complain();
		complain.setId(c.getInt(c.getColumnIndex(KEY_ID)));
		complain.setServiceType(c.getInt(c.getColumnIndex(SERVICE_TYPE_ID)));
		complain.setComplain(c.getString(c.getColumnIndex(COMPLAIN_TEXT)));
		complain.setUserId(c.getString(c.getColumnIndex(USER_ID)));

		// return contact
		return complain;
	}

	// get All service
	public ArrayList<Complain> getAllComplain() {
		ArrayList<Complain> complainList = new ArrayList<Complain>();

		String selectQuery = "SELECT  * FROM " + TABLE_COMPLAIN;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				Complain complain = new Complain();
				complain.setId(c.getInt(c.getColumnIndex(KEY_ID)));
				complain.setServiceType(c.getInt(c
						.getColumnIndex(SERVICE_TYPE_ID)));
				complain.setComplain(c.getString(c
						.getColumnIndex(COMPLAIN_TEXT)));
				complain.setUserId(c.getString(c.getColumnIndex(USER_ID)));

				complainList.add(complain);
			} while (c.moveToNext());
		}
		return complainList;
	}

	public int getComplainCount() {
		String countQuery = "SELECT  * FROM " + TABLE_COMPLAIN;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int x = 0;
		x = cursor.getCount();
		cursor.close();
		db.close();
		return x;
	}

	public void deleteAllComplains() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_COMPLAIN, KEY_ID + " >= 1", null);
		db.close();
	}

	public void deleteSingleComplain(int complainId) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_COMPLAIN, KEY_ID + " = ?",
				new String[] { String.valueOf(complainId) });
		db.close();
	}

	// //////////////////////////////////////////////////////////////////Insurance
	public void addInsurance(Insurance insurance) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		values.put(NAME_AR, insurance.getNameAr());
		values.put(NAME_EN, insurance.getNameEn());

		// Inserting Row
		db.insert(TABLE_INSURANCE, null, values);
		db.close(); // Closing database connection
	}

	public Insurance getInsurance(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = "SELECT  * FROM " + TABLE_INSURANCE + " WHERE "
				+ KEY_ID + " = " + id;

		Cursor c = db.rawQuery(selectQuery, null);

		if (c != null)
			c.moveToFirst();

		Insurance insurance = new Insurance();
		insurance.setId(c.getInt(c.getColumnIndex(KEY_ID)));
		insurance.setNameAr(c.getString(c.getColumnIndex(NAME_AR)));
		insurance.setNameEn(c.getString(c.getColumnIndex(NAME_EN)));

		// return contact
		return insurance;
	}

	public ArrayList<Insurance> getAllInsurance() {
		ArrayList<Insurance> insuranceList = new ArrayList<Insurance>();

		String selectQuery = "SELECT  * FROM " + TABLE_INSURANCE;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				Insurance insurance = new Insurance();
				insurance.setId(c.getInt(c.getColumnIndex(KEY_ID)));
				insurance.setNameAr(c.getString(c.getColumnIndex(NAME_AR)));
				insurance.setNameEn(c.getString(c.getColumnIndex(NAME_EN)));

				insuranceList.add(insurance);
			} while (c.moveToNext());
		}
		return insuranceList;
	}

	public int getInsuranceCount() {
		String countQuery = "SELECT  * FROM " + TABLE_INSURANCE;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int x = 0;
		x = cursor.getCount();
		cursor.close();
		db.close();
		return x;
	}

	public void deleteAllInsurance() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_INSURANCE, KEY_ID + " >= 1", null);
		db.close();
	}

	public void deleteSingleInsurance(int insuranceId) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_INSURANCE, KEY_ID + " = ?",
				new String[] { String.valueOf(insuranceId) });
		db.close();
	}

}
