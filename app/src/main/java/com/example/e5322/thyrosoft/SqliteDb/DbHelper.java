package com.example.e5322.thyrosoft.SqliteDb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    private static String CREATE_TABLE1;

    //    Database name
    public static String DATABASE_NAME = "Offline_Woe";

    //    Talble name
    public static final String TABLE1_NAME = "woe";

    //      Fields for table
    public static final String ID = "id";
    public static final String BARCODES = "barcodes";
    public static final String WOEJSON = "woeJson";

    //      Required resorces to manage database
    private ContentValues cValues;
    private SQLiteDatabase dataBase = null;
    private Cursor cursor;

    public DbHelper(Context context) {
        super(context, context.getExternalFilesDir(null).getAbsolutePath()
                + "/" + DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        CREATE_TABLE1 = "CREATE TABLE " + TABLE1_NAME + "(" + ID
                + " INTEGER PRIMARY KEY autoincrement, " + BARCODES
                + " TEXT, " + WOEJSON + " TEXT)";


        db.execSQL(CREATE_TABLE1);
        System.out
                .println("Table is created...........................!");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE1_NAME);

        onCreate(db);
    }

    public void inserRecord(String barcodes, String woeJson) {

        dataBase = getWritableDatabase();
        cValues = new ContentValues();

        cValues.put(BARCODES, barcodes);
        cValues.put(WOEJSON, woeJson);

        // insert data into database
        long rghj = dataBase.insert(TABLE1_NAME, null, cValues);

        dataBase.close();
    }

    public void updateRecord(String barcodes, String woeJson) {

        dataBase = getWritableDatabase();

        cValues = new ContentValues();

        cValues.put(BARCODES, barcodes);
        cValues.put(WOEJSON, woeJson);
//    Update data from database table
        dataBase.update(DbHelper.TABLE1_NAME, cValues,
                null, null);

        dataBase.close();
    }

    public Cursor selectRecords() {

        dataBase = getReadableDatabase();

//    Getting data from database table
        cursor = dataBase.rawQuery("select * from " + TABLE1_NAME, null);
        return cursor;
    }

    public Cursor deleteRecord(String strBarcodes) {

        dataBase = getReadableDatabase();

//    Getting data from database table
        cursor = dataBase.rawQuery("delete from " + TABLE1_NAME+"where "+BARCODES+ "="+strBarcodes, null);
        return cursor;

//    Deleting all records from database table
    }

}
