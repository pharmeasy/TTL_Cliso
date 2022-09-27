package com.example.e5322.thyrosoft.SqliteDb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.GetProductsRecommendedResModel;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Offline_Woe.db";
    public static final String TABLE_NAME = "woe";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "BARCODES";
    public static final String COL_3 = "WOEJSON";
    public static final String COL_4 = "ERROR";

    //Recommended Product
    public static final String TABLE_RECO_PRODUCT = "Reco_Product";
    public static final String COL_TESTS_ASKED = "TestsAsked";
    public static final String COL_TESTS_RECOMMENDED = "TestsRecommended";
    public static final String COL_TESTS_RECO_DISPLAY_NAME = "TestsRecoDisplayName";
    public static final String COL_RECOMMENDATION_MSG = "RecommendationMsg";
    public static final String COL_TEST_PACKAGE_LIST = "TestsPackage";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,BARCODES TEXT,WOEJSON TEXT,ERROR TEXT)");
        db.execSQL("create table " + TABLE_RECO_PRODUCT + "(TestsAsked TEXT,TestsRecommended TEXT,TestsRecoDisplayName TEXT,RecommendationMsg TEXT,TestsPackage TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECO_PRODUCT);
        onCreate(db);
    }

    public void insertRecoProductData(GetProductsRecommendedResModel.ProductListDTO productListDTO) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_TESTS_ASKED, !GlobalClass.isNull(productListDTO.getTestsAsked()) ? productListDTO.getTestsAsked() : "");
        contentValues.put(COL_TESTS_RECOMMENDED, !GlobalClass.isNull(productListDTO.getTestsRecommended()) ? productListDTO.getTestsRecommended() : "");
        contentValues.put(COL_TESTS_RECO_DISPLAY_NAME, !GlobalClass.isNull(productListDTO.getTestsRecoDisplayName()) ? productListDTO.getTestsRecoDisplayName() : "");
        contentValues.put(COL_RECOMMENDATION_MSG, !GlobalClass.isNull(productListDTO.getRecommendationMsg()) ? productListDTO.getRecommendationMsg() : "");
        contentValues.put(COL_TEST_PACKAGE_LIST, String.valueOf(GlobalClass.CheckArrayList(productListDTO.getTestsPackageList()) ? productListDTO.getTestsPackageList() : ""));
        database.insert(TABLE_RECO_PRODUCT, null, contentValues);

    }

    public Cursor getProductData(String test_code) {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("select * from " + TABLE_RECO_PRODUCT + " WHERE TestsAsked = " + "'" + test_code + "'", null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public void deleteAll(String tablename) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("delete from " + tablename);
    }

    public boolean insertData(String barcodes, String woejson) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, barcodes);
        contentValues.put(COL_3, woejson);


        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        return res;
    }

    public Cursor selectData(String barcodes) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT " + COL_3 + " FROM " + TABLE_NAME + " WHERE BARCODES = " + "'" + barcodes.trim() + "'", null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public boolean updateData(String barcodes, String woejson) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_3, woejson);

        db.update(TABLE_NAME, contentValues, "BARCODES = ?", new String[]{barcodes});
        return true;
    }

    public boolean updateDataeRROR(String barcodes, String error) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_4, error);

        db.update(TABLE_NAME, contentValues, "BARCODES = ?", new String[]{barcodes});
        return true;
    }


    public boolean updateBArcodeColumn(String barcodes, String woejson) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, barcodes);

        db.update(TABLE_NAME, contentValues, "WOEJSON = ?", new String[]{woejson});
        return true;
    }

    public boolean deleteData(String barcodes) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, COL_2 + "=?", new String[]{barcodes}) > 0;
    }

    public void open() throws SQLException {
        SQLiteDatabase db = getWritableDatabase();
    }

    public void clearTable() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("delete from " + TABLE_NAME);
    }

    public int getProfilesCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }
}
