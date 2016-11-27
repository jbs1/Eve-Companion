package com.jbs.evecompanion;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


class DBhelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "eve_comp.db";
    private static final int DATABASE_VERSION = 2;
    private static final String CHAR_TABLE = "char_table";
    private static final String CHAR_TABLE_COL0 = "ID";
    private static final String CHAR_TABLE_COL1 = "ACCESS_TOKEN";
    private static final String CHAR_TABLE_COL2 = "REFRESH_TOKEN";
    private static final String CHAR_TABLE_COL3 = "VALID_UNITL";
    private static final String CHAR_TABLE_COL4 = "CHAR_ID";
    private static final String CHAR_TABLE_COL5 = "CHAR_NAME";


    DBhelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+CHAR_TABLE+" ("+CHAR_TABLE_COL0+" INTEGER PRIMARY KEY AUTOINCREMENT,"+CHAR_TABLE_COL1+" TEXT,"+CHAR_TABLE_COL2+" TEXT,"+CHAR_TABLE_COL3+" LONG,"+CHAR_TABLE_COL4+" INTEGER UNIQUE,"+CHAR_TABLE_COL5+" TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL();
    }

    void resetDB(){
        SQLiteDatabase db =this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS "+CHAR_TABLE);
        this.onCreate(db);
    }

    String getTableAsString(String tableName) {
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
        ctv.put(CHAR_TABLE_COL1,acctk);
        ctv.put(CHAR_TABLE_COL2,reftk);
        ctv.put(CHAR_TABLE_COL3,valid);
        ctv.put(CHAR_TABLE_COL4,charid);
        ctv.put(CHAR_TABLE_COL5,charname);
        long result = db.insert(CHAR_TABLE, null, ctv);
        return result != -1;
    }

    String access_token(int charid){
        SQLiteDatabase db =this.getWritableDatabase();
        Cursor chars=db.rawQuery("SELECT * FROM " + CHAR_TABLE + " WHERE CHAR_ID=" + Integer.toString(charid) ,null);


        JSONObject c = new JSONObject();

        if(chars.moveToFirst()){
            do {
                try {
                    c.put("access", chars.getString(1));
                    c.put("refresh", chars.getString(2));
                    c.put("valid", chars.getInt(3));
                    c.put("id", chars.getInt(4));
                    c.put("name", chars.getString(5));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } while (chars.moveToNext());
        } else {
            return null;
        }
        chars.close();

        try {
            if(System.currentTimeMillis()<c.getInt("valid")){
                return c.getString("access");
            } else {
                //get new access token with a refreshtoken class
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }

    
    JSONArray get_all_chars(){
        JSONArray arr=new JSONArray();
        SQLiteDatabase db =this.getWritableDatabase();
        Cursor chars=db.rawQuery("SELECT * FROM " + CHAR_TABLE,null);

        if(chars.moveToFirst()){
            do {
                JSONObject c = new JSONObject();

                try {
                    c.put("name", chars.getString(5));
                    c.put("id", chars.getInt(4));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                arr.put(c);
            } while (chars.moveToNext());
        }
        chars.close();
        return arr;
    }
}
