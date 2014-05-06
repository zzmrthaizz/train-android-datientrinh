package com.example.adapter;

import java.util.zip.Inflater;

import com.example.train_android_datientrinh.R;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MyCursorAdapter extends CursorAdapter{
	LayoutInflater inflater;
	public MyCursorAdapter(Context context, Cursor c) {
		super(context, c);
		inflater = LayoutInflater.from(context);
	}

	@Override
	public void bindView(View arg0, Context arg1, Cursor arg2) {
		
		TextView name = (TextView) arg0.findViewById(R.id.tvname);
		TextView gender = (TextView) arg0.findViewById(R.id.tvgender);
		TextView mail = (TextView) arg0.findViewById(R.id.tvmail);
		TextView phone = (TextView) arg0.findViewById(R.id.tvphone);
		ImageView picture = (ImageView) arg0.findViewById(R.id.img_avatar);
		
		name.setText(arg2.getString(1));
		mail.setText(arg2.getString(2));
		phone.setText(arg2.getString(3));
		gender.setText(arg2.getString(4));
		UrlImageViewHelper.setUrlDrawable(picture, arg2.getString(5));
	}

	@Override
	public View newView(Context arg0, Cursor arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.item_user, arg2, false);
	}

}
