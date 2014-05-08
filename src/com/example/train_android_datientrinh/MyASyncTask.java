package com.example.train_android_datientrinh;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.MyContentProvider;
import com.example.database.MySQLiteHelper;
import com.example.database.user_table;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class MyASyncTask extends AsyncTask<String, Void, Void> {
	Activity context;
	String result;

	public MyASyncTask(Activity context) {
		this.context = context;
	}

	protected void onPreExecute() {
		super.onPreExecute();	
		
	}

	@Override
	protected Void doInBackground(String... params) {
		for (int i = 0; i < 99; i++) {
			for (String url : params) {
				DefaultHttpClient client = new DefaultHttpClient();
				HttpGet request = new HttpGet(url);
				try {
					HttpResponse response = client.execute(request);
					InputStream inputStream = response.getEntity().getContent();
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(inputStream));
					StringBuilder stringBuilder = new StringBuilder();
					String line = "";
					while ((line = reader.readLine()) != null) {
						stringBuilder.append(line);

					}
					result = stringBuilder.toString();
					addToDatabase();
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	private void addToDatabase() {
		try {
			JSONObject jsonObject = new JSONObject(result);
			JSONArray jsonArray = jsonObject.getJSONArray("results");
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject c = jsonArray.getJSONObject(i);
				JSONObject user = c.getJSONObject("user");
				String gender = user.getString("gender");
				JSONObject name = user.getJSONObject("name");
				String title = name.getString("title");
				String first = name.getString("first");
				String mail = user.getString("email");
				String phone = user.getString("phone");
				String picture = user.getString("picture");
				User userobj = new User();
				userobj.setGender(gender);
				userobj.setMail(mail);
				userobj.setName(title + "." + first);
				userobj.setPhone(phone);
				userobj.setPicture(picture);

				// Add user to database
				addUsertoDb(userobj);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void addUsertoDb(User userobj) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(user_table.NAME_COL, userobj.getName());
		contentValues.put(user_table.GENDER_COL, userobj.getGender());
		contentValues.put(user_table.EMAIL_COL, userobj.getMail());
		contentValues.put(user_table.PHONE_COL, userobj.getPhone());
		contentValues.put(user_table.PICTURE_COL, userobj.getPicture());
		context.getContentResolver().insert(MyContentProvider.USER_URI, contentValues);
		
	}

}
