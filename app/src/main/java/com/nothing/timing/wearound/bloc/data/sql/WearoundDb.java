package com.nothing.timing.wearound.bloc.data.sql;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class WearoundDb extends SQLiteOpenHelper {

    private static final String TAG = "WearoundDB";

    private SQLiteDatabase db = getWritableDatabase();
    private Query query;

    public WearoundDb(Context context, String table) {
        super(context, SqlNames.DATABASE_NAME, null, 1);

        query = new Query();
        query.setTable(table);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        if (query == null) {

            query = new Query();
        }

        db.execSQL(query.createTable(SqlNames.getFavouritesTableName()));
        db.execSQL(query.createTable(SqlNames.getHistoryTableName()));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(query.drop());
    }

    public boolean insertRow(String shopName,
                             String address,
                             String latitude,
                             String longitude,
                             String shopId,
                             String imgUrl,
                             String image) {

        try {
            Cursor action = db.rawQuery(query.insert(),
                    new String[] { shopName,address,latitude,longitude, shopId, imgUrl, image });
            action.moveToFirst();
            action.close();

        } catch( SQLiteException e ) {

            return false;
        }

        return true;
    }

    public Cursor selectData(String column) {

        return db.rawQuery(query.select(column), null);
    }

    public boolean recordExists(String shop) {

        try {
            Cursor c = db.rawQuery(query.selectCount(shop), null);

           // Log.e(TAG, "shop count is " + c.getCount());

            c.moveToFirst();

            if (c.getInt(0) > 0) {

                return true;
            }
            c.close();
            return false;

        } catch (SQLiteException e) {

            Log.e(TAG, "Exception caught -> " + e);
            return false;
        }
    }

    public boolean delData() {

        try {
            Cursor cursor = db.rawQuery(query.delete(), null);
            cursor.moveToFirst();
            cursor.close();

        } catch (SQLiteException e) {

            return false;
        }

        return true;
    }

    public boolean delRow(String where) {

        try {
            Cursor action = db.rawQuery(query.delRow(),new String[] { where });
            action.moveToFirst();
            action.close();

        }  catch( SQLiteException e ) {

            return false;
        }

        return true;
    }
}
