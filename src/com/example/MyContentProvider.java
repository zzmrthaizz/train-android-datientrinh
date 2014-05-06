package com.example;

import com.example.database.MySQLiteHelper;
import com.example.database.user_table;

import android.R.id;
import android.R.integer;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

public class MyContentProvider extends ContentProvider{
	private MySQLiteHelper db;
	//Locate AUTHORITY
	static String AUTHORITY = "com.example.MyContentProvider";
	//Define the URI
	public static final Uri USER_URI = Uri.parse("content://"+AUTHORITY+"/"+user_table.TABLE_USER);
	//Define Urimatch
	public static final int USERS = 1;
	public static final int USER_ID =2;
	static UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
	static {
		URI_MATCHER.addURI(AUTHORITY, "User", USERS);
		URI_MATCHER.addURI(AUTHORITY, "User/#", USER_ID);
	}
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		
		int uriType = URI_MATCHER.match(uri);
		int delCount = 0;
		switch (uriType) {
		case USER_ID:
			String userID = uri.getLastPathSegment();
			//TODO Define delete in DB
			break;

		default:
			break;
		}
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		int	uriType = URI_MATCHER.match(uri);
		long id = 0;
		switch (uriType) {
		case USERS:
			id = db.insertUser(values);
			getContext().getContentResolver().notifyChange(uri, null);
			return Uri.parse("User"+"/"+id);

		default:
			throw new IllegalArgumentException("Unknow uri: "+uri);
		}
	}

	@Override
	public boolean onCreate() {
		db = new MySQLiteHelper(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
			int uriType = URI_MATCHER.match(uri);
			
			Cursor cursor = null;
			
			switch (uriType) {
			case USERS:
				cursor = db.getAllUser();
				cursor.setNotificationUri(getContext().getContentResolver(), uri);
				return cursor;
			case USER_ID:
				cursor = db.getUser();
				cursor.setNotificationUri(getContext().getContentResolver(), uri);
				return cursor;

			default:
				throw new IllegalArgumentException("Unkown uri: "+uri);
			}
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

}
