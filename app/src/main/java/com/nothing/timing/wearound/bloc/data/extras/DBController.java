package com.nothing.timing.wearound.bloc.data.extras;

import android.database.Cursor;
import android.util.Log;

import com.nothing.timing.wearound.bloc.data.sql.SqlNames;
import com.nothing.timing.wearound.bloc.data.sql.WearoundDb;

import java.util.ArrayList;
import java.util.Arrays;

public class DBController {

     private static final String TAG = "Database Controller";

     private WearoundDb db;

     public DBController(WearoundDb db) {

        this.db = db;
    }

    public ArrayList<String> getStringColumn(String columnName) {

        ArrayList<String> stringColumn = new ArrayList<>();
        Cursor cursor = db.selectData(columnName);

        while (cursor.moveToNext()) {

            stringColumn.add(cursor.getString(0));
        }
        cursor.close();

        return stringColumn;
    }

//     public ArrayList<byte[]> getImages() {
//
//         ArrayList<byte[]> imgColumn = new ArrayList<>();
//
//         Cursor cursor = db.selectData(SqlNames.IMAGES);
//
//         while (cursor.moveToNext()) {
//
//             imgColumn.add(cursor.getBlob(0));
//         }
//         cursor.close();
//
//         return imgColumn;
//     }


}
