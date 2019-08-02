package com.nothing.timing.wearound.bloc.data.sql;

class Query {

    private String table;

    public void setTable(String table) {

        this.table = table;
    }

    String insert() {

        return "INSERT INTO " + table
                + " (" + SqlNames.SHOP_NAME
                + "," + SqlNames.ADDRESS
                + "," + SqlNames.LATITUDE
                + "," + SqlNames.LONGITUDE
                + "," + SqlNames.SHOP_ID_URL
                + "," + SqlNames.IMG_URL
                + "," + SqlNames.IMAGES + ") " +
                "VALUES(?,?,?,?,?,?,?)";
    }

    String select(String column) {

        return "SELECT " + column +
                " FROM "  + table;
    }

    String selectCount(String shop) {

        return "SELECT EXISTS "
                + "(SELECT " + SqlNames.SHOP_NAME
                + " FROM " + table
                + " WHERE " + SqlNames.SHOP_NAME
                + " = '" + shop +  "' LIMIT 1)";

//        return "SELECT PID FROM "  + table +
//                " WHERE " + SqlNames.SHOP_NAME + "= '" + shop + "'";
    }

    String delete() {

        return "DELETE FROM " + table;
    }

    String delRow() {

        return "DELETE FROM " + table +
                " WHERE " + SqlNames.SHOP_NAME + "= ?";
    }

    String drop() {

        return "DROP TABLE IF EXISTS " + table;
    }

    String createTable(String table) {

        return "CREATE TABLE " + table
                + " (a INTEGER PRIMARY KEY,"
                + SqlNames.SHOP_NAME + " TEXT,"
                + SqlNames.ADDRESS + " TEXT,"
                + SqlNames.LATITUDE + " TEXT,"
                + SqlNames.LONGITUDE +" TEXT,"
                + SqlNames.SHOP_ID_URL + " TEXT,"
                + SqlNames.IMG_URL + " TEXT,"
                + SqlNames.IMAGES + " TEXT)";
    }


}
