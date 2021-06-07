package com.example.valet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHandler extends SQLiteOpenHelper {

    private static String TAG = "DatabaseHandler";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ValetDatabase";
    static SQLiteDatabase db;

    //******************************************************************
    private static final String USER_INFO = "USER_INFO";

    private static final String NAME = "NAME";
    private static final String PASSWORD = "PASSWORD";
    private static final String E_MAIL = "E_MAIL";
    private static final String NUMBER = "NUMBER";
    private static final String CAR_TYPE = "CAR_TYPE";
    private static final String CAR_MODEL = "CAR_MODEL";
    private static final String CAR_COLOR = "CAR_COLOR";
    private static final String CAR_LOT = "CAR_LOT";
    private static final String CURRENT_PAGE = "CURRENT_PAGE";

    //******************************************************************
    private static final String PARKING_INFO = "PARKING_INFO";

    private static final String LOCATION = "LOCATION";
    private static final String CAPTAIN_NAME = "CAPTAIN_NAME";
    private static final String CAPTAIN_NUMBER = "CAPTAIN_NUMBER";
    private static final String TIME_OF_PARKING = "TIME_OF_PARKING";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE_USER_INFO = "CREATE TABLE " + USER_INFO + "("
                + NAME + " TEXT,"
                + PASSWORD + " TEXT,"
                + E_MAIL + " TEXT,"
                + NUMBER + " TEXT,"
                + CAR_TYPE + " TEXT,"
                + CAR_MODEL + " TEXT,"
                + CAR_COLOR + " TEXT,"
                + CAR_LOT + " TEXT,"
                + CURRENT_PAGE + " TEXT" + ")";
        db.execSQL(CREATE_TABLE_USER_INFO);

        String CREATE_TABLE_ParkingInfo = "CREATE TABLE " + PARKING_INFO + "("
                + LOCATION + " TEXT,"
                + CAPTAIN_NAME + " TEXT,"
                + CAPTAIN_NUMBER + " TEXT,"
                + TIME_OF_PARKING + " TEXT" + ")";
        db.execSQL(CREATE_TABLE_ParkingInfo);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        try {
            //db.execSQL("ALTER TABLE SETTINGS_TABLE ADD " + USER_INFO + " TEXT");
        } catch (Exception e) {
            Log.e("upgrade", "USER NO");
        }
    }

    public void addParkingInfo(String location , String captainName , String captainNum , String timeOfPark ) {
        deletePARKINFO();

        db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(LOCATION, location);
        contentValues.put(CAPTAIN_NAME, captainName);
        contentValues.put(CAPTAIN_NUMBER, captainNum);
        contentValues.put(TIME_OF_PARKING, timeOfPark);

        db.insert(PARKING_INFO, null, contentValues);
        db.close();
    }

    public void addUserInfo(String name , String password , String Email , String number ,  String carType , String carModel , String carColor , String carNo , String currentPage) {
        deleteUSERINFO();

        db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(NAME, name);
        contentValues.put(PASSWORD, password);
        contentValues.put(E_MAIL, Email);
        contentValues.put(NUMBER, number);
        contentValues.put(CAR_TYPE, carType);
        contentValues.put(CAR_MODEL, carModel);
        contentValues.put(CAR_COLOR, carColor);
        contentValues.put(CAR_LOT, carNo);
        contentValues.put(CURRENT_PAGE, currentPage);

        db.insert(USER_INFO, null, contentValues);
        db.close();
    }

    public String getName() {
        String name = null;
        String selectQuery = "SELECT NAME FROM " + USER_INFO;
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                name = cursor.getString(0);

            } while (cursor.moveToNext());
        }
        return name;
    }

    public Clients getUSER_INFO() {
        Clients settings = new Clients();
        String selectQuery = "SELECT * FROM " + USER_INFO;
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                settings.setUserName(cursor.getString(0));
                settings.setPassword(cursor.getString(1));
                settings.setE_mail(cursor.getString(2));
                settings.setPhoneNumber(cursor.getString(3));
                settings.setCarType(cursor.getString(4));
                settings.setCarModel(cursor.getString(5));
                settings.setCarColor(cursor.getString(6));
                settings.setCarLot(cursor.getString(7));
                settings.setCurrentPage(cursor.getString(8));

            } while (cursor.moveToNext());
        }
        return settings;
    }

    public Captains getParkingInfo() {
        Captains settings = new Captains();
        String selectQuery = "SELECT * FROM " + PARKING_INFO;
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                settings.setCaptain_rate(cursor.getString(0)); // for location
                settings.setCaptainName(cursor.getString(1));
                settings.setCaptainNumber(cursor.getString(2));
                settings.setClientName(cursor.getString(3)); // for time of park

            } while (cursor.moveToNext());
        }
        return settings;
    }

    public void updateCurrentPage(String current) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(CURRENT_PAGE, current);
        db.update(USER_INFO, values, null, null);
    }


    public void deleteUSERINFO() {
        db = this.getWritableDatabase();
        db.delete(USER_INFO, null, null);
        db.close();
    }

    public void deletePARKINFO() {
        db = this.getWritableDatabase();
        db.delete(PARKING_INFO, null, null);
        db.close();
    }

}
