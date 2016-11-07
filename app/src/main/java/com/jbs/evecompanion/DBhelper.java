package com.jbs.evecompanion;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


class DBhelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "eve_comp.db";
    private static final int DATABASE_VERSION = 2;
    private static final String AUTH_TABLE = "auth_table";
    private static final String AUTH_TABLE_COL1 = "ID";
    private static final String AUTH_TABLE_COL2 = "ACCESS_TOKEN";
    private static final String AUTH_TABLE_COL3 = "REFRESH_TOKEN";
    private static final String AUTH_TABLE_COL4 = "VALID_UNITL";
    private static final String AUTH_TABLE_COL5 = "CHAR_ID";
    private static final String AUTH_TABLE_COL6 = "CHAR_NAME";


    DBhelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+AUTH_TABLE+" ("+AUTH_TABLE_COL1+" INTEGER PRIMARY KEY AUTOINCREMENT,"+AUTH_TABLE_COL2+" TEXT,"+AUTH_TABLE_COL3+" TEXT,"+AUTH_TABLE_COL4+" LONG,"+AUTH_TABLE_COL5+" INTEGER UNIQUE,"+AUTH_TABLE_COL6+" TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL();
    }

    public void resetDB(){
        SQLiteDatabase db =this.getWritableDatabase();
        db.execSQL("DROP TABLE "+AUTH_TABLE);
        this.onCreate(db);
    }

    public String getTableAsString(String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();
        String tableString = String.format("Table %s:\n", tableName);
        Cursor allRows  = db.rawQuery("SELECT * FROM " + tableName, null);
        if (allRows.moveToFirst() ){
            String[] columnNames = allRows.getColumnNames();
            do {
                for (String name: columnNames) {
                    tableString += String.format("%s: %s\n", name,
                            allRows.getString(allRows.getColumnIndex(name)));
                }
                tableString += "\n";

            } while (allRows.moveToNext());
        }
        allRows.close();
        return tableString;
    }


    boolean insert_char(String acctk, String reftk, long valid, int charid, String charname) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues ctv = new ContentValues();
        ctv.put(AUTH_TABLE_COL2,acctk);
        ctv.put(AUTH_TABLE_COL3,reftk);
        ctv.put(AUTH_TABLE_COL4,valid);
        ctv.put(AUTH_TABLE_COL5,charid);
        ctv.put(AUTH_TABLE_COL6,charname);
        long result = db.insert(AUTH_TABLE, null, ctv);
        return result != -1;
    }
}
