package com.cmalergun.photokeyboard.database;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

class DbHelper extends SQLiteOpenHelper {

    private static String DATAB_PATH;
    private SQLiteDatabase sqlDatabase;
    public final Context context;
    private static String DBASE_NAME;

    DbHelper(Context context, String databaseName) {
        super(context, databaseName, null, 1);
        this.context = context;

        String packageName = context.getPackageName();
        DATAB_PATH = String.format("//data//data//%s//databases//", packageName);
        DBASE_NAME = databaseName;
        openDB();
    }

    private void createDB() {
        boolean dbExist = checkDB();
        if (!dbExist) {
            this.getReadableDatabase();
            try {
                copyDB();
            } catch (IOException e) {
                Log.e(this.getClass().toString(), "Copying error");
                throw new Error("Error copying database!");
            }
        } else {
            Log.i(this.getClass().toString(), "Database already exists");
        }
    }


    private void copyDB() throws IOException {
        InputStream externalDbStream = context.getAssets().open(DBASE_NAME);
        String outFileName = DATAB_PATH + DBASE_NAME;

        OutputStream localDbStream = new FileOutputStream(outFileName);

        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = externalDbStream.read(buffer)) > 0) {
            localDbStream.write(buffer, 0, bytesRead);
        }

        localDbStream.close();
        externalDbStream.close();

    }

    private boolean checkDB() {
        SQLiteDatabase checkDb = null;
        try {
            String path = DATAB_PATH + DBASE_NAME;
            checkDb = SQLiteDatabase.openDatabase(path, null,SQLiteDatabase.OPEN_READONLY);
        } catch (SQLException e) {
            Log.e(this.getClass().toString(), "Error while checking db");
        }

        if (checkDb != null) {
            checkDb.close();
        }
        return checkDb != null;
    }

    SQLiteDatabase openDB() throws SQLException {
        String path = DATAB_PATH + DBASE_NAME;
        if (sqlDatabase == null) {
            createDB();
            sqlDatabase = SQLiteDatabase.openDatabase(path, null,SQLiteDatabase.OPEN_READWRITE);
        }
        return sqlDatabase;
    }

    @Override
    public synchronized void close() {
        if (sqlDatabase != null) {
            sqlDatabase.close();
        }
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}