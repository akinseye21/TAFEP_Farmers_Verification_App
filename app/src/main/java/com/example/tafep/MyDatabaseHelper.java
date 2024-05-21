package com.example.tafep;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private Context context;
    private static final String DATABASE_NAME = "Verification.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "verification_table";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_TAFEP = "tafep";
    private static final String COLUMN_FULLNAME = "fullname";
    private static final String COLUMN_PHONE = "phone_number";
    private static final String COLUMN_EMRGENCY_PHONE = "emergency_phone_number";
    private static final String COLUMN_CREATED_BY = "created_by";
    private static final String COLUMN_LGA = "location_lga";
    private static final String COLUMN_FARM_ADDRESS = "farm_address";
    private static final String COLUMN_WARD = "ward";
    private static final String COLUMN_FARM_TYPE = "farm_type";
    private static final String COLUMN_FARM_SIZE = "farm_size";
    private static final String COLUMN_CROP_CULTIVATED = "crop_cultivated";
    private static final String COLUMN_LAT_1 = "lat1";
    private static final String COLUMN_LAT_2 = "lat2";
    private static final String COLUMN_LAT_3 = "lat3";
    private static final String COLUMN_LAT_4 = "lat4";
    private static final String COLUMN_LONG_1 = "long1";
    private static final String COLUMN_LONG_2 = "long2";
    private static final String COLUMN_LONG_3 = "long3";
    private static final String COLUMN_LONG_4 = "long4";
    private static final String COLUMN_PICTURE_1 = "picture1";
    private static final String COLUMN_PICTURE_2 = "picture2";

    public MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +
                " (" +COLUMN_ID + " INTEGER PRIMARY KEY, "+
                COLUMN_TAFEP + " TEXT, "+
                COLUMN_FULLNAME + " TEXT, "+
                COLUMN_PHONE + " TEXT, "+
                COLUMN_EMRGENCY_PHONE + " TEXT, "+
                COLUMN_CREATED_BY + " TEXT, "+
                COLUMN_LGA + " TEXT, "+
                COLUMN_FARM_ADDRESS + " TEXT, "+
                COLUMN_WARD + " TEXT, "+
                COLUMN_FARM_TYPE + " TEXT, "+
                COLUMN_FARM_SIZE + " TEXT, "+
                COLUMN_CROP_CULTIVATED + " TEXT, "+
                COLUMN_LAT_1 + " TEXT, "+
                COLUMN_LAT_2 + " TEXT, "+
                COLUMN_LAT_3 + " TEXT, "+
                COLUMN_LAT_4 + " TEXT, "+
                COLUMN_LONG_1 + " TEXT, "+
                COLUMN_LONG_2 + " TEXT, "+
                COLUMN_LONG_3 + " TEXT, "+
                COLUMN_LONG_4 + " TEXT, "+
                COLUMN_PICTURE_1 + " BLOB, "+
                COLUMN_PICTURE_2 + " BLOB);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(" DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(db);
    }

    public void addToTable(String tafep, String fullname, String phone, String emergencynum, String createdby, String lga,
                           String farmaddress, String ward, String farmtype, String farmsize, String cropcultivated,
                           String lat1, String lat2, String lat3, String lat4, String long1,
                           String long2, String long3, String long4,
                           byte[] picture1, byte[] picture2){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_TAFEP, tafep);
        cv.put(COLUMN_FULLNAME, fullname);
        cv.put(COLUMN_PHONE, phone);
        cv.put(COLUMN_EMRGENCY_PHONE, emergencynum);
        cv.put(COLUMN_CREATED_BY, createdby);
        cv.put(COLUMN_LGA, lga);
        cv.put(COLUMN_FARM_ADDRESS, farmaddress);
        cv.put(COLUMN_WARD, ward);
        cv.put(COLUMN_FARM_TYPE, farmtype);
        cv.put(COLUMN_FARM_SIZE, farmsize);
        cv.put(COLUMN_CROP_CULTIVATED, cropcultivated);
        cv.put(COLUMN_LAT_1, lat1);
        cv.put(COLUMN_LAT_2, lat2);
        cv.put(COLUMN_LAT_3, lat3);
        cv.put(COLUMN_LAT_4, lat4);
        cv.put(COLUMN_LONG_1, long1);
        cv.put(COLUMN_LONG_2, long2);
        cv.put(COLUMN_LONG_3, long3);
        cv.put(COLUMN_LONG_4, long4);
        cv.put(COLUMN_PICTURE_1, picture1);
        cv.put(COLUMN_PICTURE_2, picture2);

        long result = db.insert(TABLE_NAME, null, cv);
        if(result == -1){
            System.out.println("Failed to save information");
            Toast.makeText(context, "Failed to save information", Toast.LENGTH_SHORT).show();
        }else{
            System.out.println("Successfully saved information");
            Toast.makeText(context, "Information saved successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    public Cursor readAllData(){
        String query = "SELECT * FROM "+TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }

        return cursor;
    }

    public void deleteRecord(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE _id = "+id);
    }

    public int getRecordCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        int count = 0;

        try {
            // Execute a query to get the count
            Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME, null);

            if (cursor.moveToFirst()) {
                count = cursor.getInt(0); // The count is at index 0
            }

            cursor.close();
        } finally {
            db.close();
        }

        return count;
    }
}
