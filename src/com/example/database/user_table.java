package com.example.database;

import android.R.integer;
import android.R.string;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class user_table {
	// table
	public final static String TABLE_USER = "User";
	// column
	public final static String ID_COL = "_id";
	public final static String NAME_COL = "Name";
	public final static String EMAIL_COL = "Email";
	public final static String PHONE_COL = "Phone";
	public final static String GENDER_COL = "Gender";
	public final static String PICTURE_COL = "Picture";
	// create SQL Statement
	static String creat_table_user = "create table " + TABLE_USER + " ( "
			+ ID_COL + " integer primary key, " + NAME_COL + " text, "
			+ EMAIL_COL + " text, " + PHONE_COL + " text, " + GENDER_COL
			+ " text, "+PICTURE_COL+ " text );";

	public static void onCreate(SQLiteDatabase db) {
		db.execSQL(creat_table_user);
	}

	public static void onUpgrade(SQLiteDatabase db, int oldVersion,
			int newVersion) {
		Log.w(user_table.class.getName(), "Upgrading database from version "
				+ oldVersion + " to " + newVersion
				+ ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS "+TABLE_USER);
		onCreate(db);
	}
}
