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

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        try {
            //db.execSQL("ALTER TABLE SETTINGS_TABLE ADD " + USER_INFO + " TEXT");
        } catch (Exception e) {
            Log.e("upgrade", "USER NO");
        }
    }

    public void addUserInfo(String name , String password , String Email , String number ,  String carType , String carModel , String carColor , String carNo , String currentPage) {
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

            } while (cursor.moveToNext());
        }
        return settings;
    }

}
