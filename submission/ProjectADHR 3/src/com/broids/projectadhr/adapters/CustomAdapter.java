package com.broids.projectadhr.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.broids.projectadhr.R;
import com.broids.projectadhr.ui.HomeScreenActivity;
import com.broids.projectadhr.ui.ServicesActivity;

public class CustomAdapter extends BaseAdapter {

	String[] result;
	Context context;
	int[] imageId;
	private static LayoutInflater inflater = null;

	public CustomAdapter(HomeScreenActivity mainActivity,
			String[] prgmNameList, int[] prgmImages) {
		result = prgmNameList;
		context = mainActivity;
		imageId = prgmImages;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public int getCount() {
		return result.length;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public class Holder {
		TextView tv;
		ImageView img;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		Holder holder = new Holder();
		View rowView;

		rowView = inflater.inflate(R.layout.category_list_row, null);
		holder.tv = (TextView) rowView.findViewById(R.id.textView1);
		holder.img = (ImageView) rowView.findViewById(R.id.imageView1);

		holder.tv.setText(result[position]);
		holder.img.setImageResource(imageId[position]);

		/*rowView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
			}
		});*/

		return rowView;
	}

}