package com.broids.projectadhr.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.broids.projectadhr.R;
import com.broids.projectadhr.models.MyEnrollmentItem;
import com.broids.projectadhr.utils.ServiceType;

public class MyEnrollmentArrayAdapter extends ArrayAdapter<MyEnrollmentItem> {
	private Context mContext;
	private ArrayList<MyEnrollmentItem> mData;

	public MyEnrollmentArrayAdapter(Context context, ArrayList<MyEnrollmentItem> data) {
		super(context, 0, data);
		mContext = context;
		mData = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_my_enrollment, parent, false);
			holder = new ViewHolder();
			holder.lblItemMyEnrollment = (TextView) convertView.findViewById(R.id.lblItemMyEnrollment);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		StringBuilder displayText = new StringBuilder();
		if(mData.get(position).getServiceType() == ServiceType.LOAN) {
			displayText.append("Loan name: ").append(mData.get(position).getName());
			displayText.append("\nEnrollment Date: ").append(mData.get(position).getAttributeOne());
			displayText.append("\nAmount: ").append(mData.get(position).getAttributeTwo());
			displayText.append("\nRate Of Interst: ").append(mData.get(position).getAttributeThree());
			displayText.append("\nTenure: ").append(mData.get(position).getAttributeFour());
			
		} else if(mData.get(position).getServiceType() == ServiceType.HEALTH) {
			displayText.append("Name: ").append(mData.get(position).getName());
			displayText.append("\nEnrollment Date: ").append(mData.get(position).getAttributeOne());
			displayText.append("\nNext Dose Date: ").append(mData.get(position).getAttributeTwo());
		}
		
		holder.lblItemMyEnrollment.setText(displayText.toString());

		return convertView;
	}

	private static class ViewHolder {
		TextView lblItemMyEnrollment;
	}
}
