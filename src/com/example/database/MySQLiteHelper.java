package com.example.database;

import com.example.train_android_datientrinh.User;

import com.example.database.user_table;
import android.R.integer;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteHelper extends SQLiteOpenHelper{
	static String DATABASE_NAME = "user.db";
	static int DATABASE_VESTION = 1;

	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VESTION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		user_table.onCreate(db);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		user_table.onUpgrade(db, oldVersion, newVersion);
	}
	public long insertUser(ContentValues contentValues) {
		SQLiteDatabase db = this.getWritableDatabase();
		long rowID = db.insert(user_table.TABLE_USER, null, contentValues);
		db.close();
		return rowID;
	}

	public Cursor getAllUser() {
		SQLiteDatabase db = this.getReadableDatabase();
		String query = "select * from "+ user_table.TABLE_USER;
		Cursor cursor = db.rawQuery(query, null);
		return cursor;
	}

	public Cursor getUser() {
		// TODO define get user
		return null;
	}
}
