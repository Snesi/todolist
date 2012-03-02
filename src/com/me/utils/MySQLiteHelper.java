package com.me.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "todolistDBv1";
	private static final int DATABASE_VERSION = 1;
	// Database creation sql statement
	private static final String DATABASE_CREATE = "CREATE TABLE todolist " +
			"(_id INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ "task TEXT NOT NULL, "
			+ "state INTEGER NOT NULL);";

	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Not implemented
	}

}
