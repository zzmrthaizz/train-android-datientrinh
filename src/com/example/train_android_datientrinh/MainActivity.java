package com.example.train_android_datientrinh;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.DefaultedHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.MyContentProvider;
import com.example.adapter.MyCursorAdapter;
import com.example.database.MySQLiteHelper;
import com.example.database.user_table;
import com.example.train_android_datientrinh.User;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.R.integer;
import android.R.string;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

@SuppressLint("NewApi")
public class MainActivity extends Activity implements LoaderCallbacks<Cursor> {

	MyASyncTask getData;
	// Cursorloader
	MyCursorAdapter adapter;
	CursorLoader cursorLoader;
	LoaderManager loaderManager = getLoaderManager();
	int LOADER_ID = 1;
	// Controll
	ListView list;
	Button addButton;
	TextView textView;
	// Http
	String[] url = { "http://api.randomuser.me/" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);


		getControl();
		fillData();
		addEvent();

		registerForContextMenu(list);

	}

	private void addEvent() {
		addButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				doASyncTask();
//				addToDatabase(result);
//				getDataFromNetWork();
			}
		});
		loaderManager.restartLoader(LOADER_ID, null, this);
	}

	protected void doASyncTask() {
		getData = new MyASyncTask(MainActivity.this);
		getData.execute(url);
		
	}

	protected void addToDatabase(String result) {
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

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		int position = info.position;
		switch (item.getItemId()) {
		case R.id.delete:
			adapter.getCursor().moveToPosition(position);
			// getContentResolver().delete(MyContentProvider.USER_URI, where,
			// selectionArgs)
			break;
		case R.id.edit:

			break;
		}
		return super.onContextItemSelected(item);
	}

	private void fillData() {
		adapter = new MyCursorAdapter(this, null);
		list.setAdapter(adapter);
		loaderManager.initLoader(LOADER_ID, null, this);

	}

	private void getControl() {
		list = (ListView) findViewById(R.id.lvUser);
		addButton = (Button) findViewById(R.id.btnadd);
		textView = (TextView) findViewById(R.id.tvIsConnected);
	}

	private void getDataFromNetWork() {
		// Get data from network
		String result = null;
		StringBuilder stringBuilder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet();
		try {
			HttpResponse response = client.execute(request);
			InputStream inputStream = response.getEntity().getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream));
			String line = null;
			if ((line = reader.readLine()) != null) {
				stringBuilder.append(line);
			}
			inputStream.close();
			result = stringBuilder.toString();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Read data and port to Obj
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
		getContentResolver().insert(MyContentProvider.USER_URI, contentValues);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		cursorLoader = new CursorLoader(MainActivity.this,
				MyContentProvider.USER_URI, null, null, null, null);
		return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
		adapter.swapCursor(arg1);

	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		adapter.swapCursor(null);

	}

}
