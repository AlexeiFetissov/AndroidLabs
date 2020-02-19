package com.example.androidlabs;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyOpener extends SQLiteOpenHelper {

    protected final static String DATABASE_NAME = "MyDatabaseFile";
    protected final static int VERSION_NUM = 1;
    public final static String TABLE_NAME = "Messages";
    public final static String COL_MESSAGE = "MESSAGE";
    public final static String COL_SENT = "isSent";
    public final static String COL_RECEIVED = "isReceived";
    public final static String COL_ID = "_id";

    public MyOpener(Activity ctx)
    {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }
    @Override
    public void onCreate(SQLiteDatabase db){

        Log.d("onCreate", "");
        // put spacaes between SQL and Java strings
        db.execSQL(" CREATE TABLE " + TABLE_NAME + "( "
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_MESSAGE + " TEXT, "
                + COL_SENT + " TEXT, "
                + COL_RECEIVED + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        Log.i("Datbase upgrade", "Old version: " + oldVersion + "New version: " + newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){
        Log.i("Datbase downgrade", "Old version: " + oldVersion + "New version: " + newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        onCreate(db);
    }
}
