package com.broids.projectadhr.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.broids.projectadhr.R;
import com.broids.projectadhr.models.LoanItem;

public class LoanArrayAdapter extends ArrayAdapter<LoanItem> {
	private Context mContext;
	private ArrayList<LoanItem> mData;

	public LoanArrayAdapter(Context context, ArrayList<LoanItem> data) {
		super(context, 0, data);
		mContext = context;
		mData = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_service_name, parent, false);
			holder = new ViewHolder();
			holder.lblItemServiceName = (TextView) convertView.findViewById(R.id.lblItemServiceName);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.lblItemServiceName.setText(mData.get(position).getLoanName());

		return convertView;
	}

	private static class ViewHolder {
		TextView lblItemServiceName;
	}
}
