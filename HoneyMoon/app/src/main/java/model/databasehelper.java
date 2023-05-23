package model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class databasehelper extends SQLiteOpenHelper {
    public static final String database = "honey.db";
    public static final String tablename  = "users";
    public static final String emailcol = "email";
    public static final String passwordcol = "password";
    public static final String gendercol = "gender";


    public databasehelper(@Nullable Context context) {
        super(context,database,null,1 );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(" create table " + tablename + " (EMAIL INTEGER PRIMARY KEY AUTOINCREMENT,PASSWORD TEXT,GENDER TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + tablename);
        onCreate(db);
    }
}
