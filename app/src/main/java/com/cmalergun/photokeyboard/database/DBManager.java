package com.cmalergun.photokeyboard.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.Html;
import android.util.Log;


import com.cmalergun.photokeyboard.R;

import java.util.ArrayList;

/**
 * Created by Maihan Nijat on 2016-12-30.
 */

public class DBManager {

    private final Context pContext;
    private DbHelper dbHelper;
    private SQLiteDatabase sqliteDb;
    private Cursor cursor;

    public DBManager(Context context) {
        if(sqliteDb != null) { sqliteDb.close(); }
        this.pContext = context;
        dbHelper = new DbHelper(pContext, getDatabaseName());
        sqliteDb = dbHelper.openDB();

        Log.d("Create Database", "Database");
    }

    /**
     * The method returns the list of words to appear as suggestions.
     * @return wordList
     */
    public ArrayList<String> getAllRow(String str, String subType) {
        ArrayList<String> wordList = new ArrayList<>();
        try {

            queryString(str, subType);

            cursor.moveToFirst();
            if (!cursor.isAfterLast()) {
                do {
                    String word = cursor.getString(0);
                    word = String.valueOf(Html.fromHtml(String.valueOf(word)));
                    wordList.add(word);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("DB ERROR", e.toString());
        }
        return wordList;
    }

    private void queryString(String str, String subType) {
        switch (subType) {
            case "english":
                cursor = sqliteDb.rawQuery("SELECT " + getWordColumnName() + " FROM " + getEnglishTableName() + " WHERE " + getWordColumnName()
                        + " LIKE '" + str + "%' AND " + getFreqColumnName() + " > 10 ORDER BY " + getFreqColumnName() + " DESC LIMIT 10", null);
                break;
            case "pashto":
                cursor = sqliteDb.rawQuery("SELECT " + getWordColumnName() + " FROM " + getPashtoTableName() + " WHERE " + getWordColumnName()
                        + " LIKE '" + str + "%' AND " + getFreqColumnName() + " > 10 ORDER BY " + getFreqColumnName() + " DESC LIMIT 10", null);
                break;
            case "farsi":
                cursor = sqliteDb.rawQuery("SELECT " + getWordColumnName() + " FROM " + getFarsiTableName() + " WHERE " + getWordColumnName()
                        + " LIKE '" + str + "%' AND " + getFreqColumnName() + " > 10 ORDER BY " + getFreqColumnName() + " DESC LIMIT 10", null);
                break;
            default:
                break;
        }
    }

    public void delete(String str, String subType) {
        String query = "";
        switch (subType) {
            case "english":
                query = ("DELETE FROM " + getEnglishTableName() + " WHERE " + getWordColumnName()
                        + " = \"" + str + "\"");
                break;
            case "pashto":
                query = ("DELETE FROM " + getPashtoTableName() + " WHERE " + getWordColumnName()
                        + " = '" + str + "'");
                break;
            case "farsi":
                query = ("DELETE FROM " + getFarsiTableName() + " WHERE " + getWordColumnName()
                        + " = '" + str + "'");
                break;
            default:
                break;
        }

        sqliteDb.execSQL(query);
    }

    /**
     * The method adds the new words into database to use it in suggestions
     */
    public void insertNewRecord(String str, String subType) {

        String tableName = "";

        switch (subType) {
            case "en_US":
                tableName = getEnglishTableName();
                break;
            case "fa_AF":
                tableName = getFarsiTableName();
                break;
            case "ps_AF":
                tableName = getPashtoTableName();
                break;
            default:
                break;
        }

        if(!tableName.equals("")) {
            String insertQuery = "INSERT INTO " + tableName
                    + "(" + getFreqColumnName() + ", " + getWordColumnName() + ") VALUES ('" + 1 + "', '" + str + "' )";
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.execSQL(insertQuery);
        }
    }

    /**
     * The method adds the new words into database to use it in suggestions
     */
    public void updateRecord(String str, Integer freq, String subType) {

        String tableName = "";

        switch (subType) {
            case "en_US":
                tableName = getEnglishTableName();
                break;
            case "fa_AF":
                tableName = getFarsiTableName();
                break;
            case "ps_AF":
                tableName = getPashtoTableName();
                break;
            default:
                break;
        }

        if(!tableName.equals("")) {

            String insertQuery = "UPDATE " + tableName
                    + " SET " + getFreqColumnName() + "= " + (freq + 1) + " WHERE word = '" + str + "'";
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.execSQL(insertQuery);
        }
    }

    public Integer getWordFrequency(String str, String subType) {

        String tableName = "";

        switch (subType) {
            case "en_US":
                tableName = getEnglishTableName();
                break;
            case "fa_AF":
                tableName = getFarsiTableName();
                break;
            case "ps_AF":
                tableName = getPashtoTableName();
                break;
            default:
                break;
        }

        Integer freq = 0;

        if(!tableName.equals("")) {

            try {

                cursor = sqliteDb.rawQuery("SELECT " + getFreqColumnName() + " FROM " + tableName + " WHERE " + getWordColumnName()
                        + " = '" + str + "'", null);

                cursor.moveToFirst();
                if (!cursor.isAfterLast()) {
                    do {
                        freq = cursor.getInt(0);
                    } while (cursor.moveToNext());
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("DB ERROR", e.toString());
            }
        }
        return freq;
    }

    // The following methods return the database and column names from string.xml.

    private String getDatabaseName() {
        return pContext.getResources().getString(R.string.database_name);
    }

    private String getEnglishTableName() {
        return pContext.getResources().getString(R.string.en_table_name);
    }

    private String getPashtoTableName() {
        return pContext.getResources().getString(R.string.pa_table_name);
    }

    private String getFarsiTableName() {
        return pContext.getResources().getString(R.string.fa_table_name);
    }

    private String getFreqColumnName() {
        return pContext.getResources().getString(R.string.freq_column);
    }

    private String getWordColumnName() {
        return pContext.getResources().getString(R.string.word_column);
    }

    /**
     * Close database connection.
     */
    public void close() {
        if(cursor != null) { cursor.close(); }
        if(sqliteDb != null) { sqliteDb.close(); }
    }
}