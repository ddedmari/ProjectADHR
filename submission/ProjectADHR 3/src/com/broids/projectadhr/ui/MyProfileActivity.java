package com.broids.projectadhr.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.broids.projectadhr.R;
import com.broids.projectadhr.utils.AadhaarUtil;

public class MyProfileActivity extends Activity {

	private ImageView img_logo;
	private TextView lblCustomerName, lblCustomerAddress;
	private Button btnMyEnrollments;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_profile);

		btnMyEnrollments = (Button) findViewById(R.id.btnMyEnrollments);
		img_logo = (ImageView) findViewById(R.id.img_logo);
		lblCustomerName = (TextView) findViewById(R.id.lblCustomerName);
		lblCustomerAddress = (TextView) findViewById(R.id.lblCustomerAddress);

		lblCustomerName.setText(AadhaarUtil.mCurrentUserName);

		StringBuilder builder = new StringBuilder();
		builder.append(AadhaarUtil.mCurrentUserKYC.getPoa().getHouse());
		builder.append("  ");
		builder.append(AadhaarUtil.mCurrentUserKYC.getPoa().getStreet());
		builder.append("\n");
		builder.append(AadhaarUtil.mCurrentUserKYC.getPoa().getVtc());
		builder.append("  ");
		builder.append(AadhaarUtil.mCurrentUserKYC.getPoa().getPo());
		builder.append("\n");
		builder.append(AadhaarUtil.mCurrentUserKYC.getPoa().getDist());
		builder.append("\n");
		builder.append(AadhaarUtil.mCurrentUserKYC.getPoa().getState());
		builder.append(" - ");
		builder.append(AadhaarUtil.mCurrentUserKYC.getPoa().getPc());
		builder.append("\n");

		lblCustomerAddress.setText(builder.toString());
		
		img_logo.setImageDrawable(new BitmapDrawable(getResources(),BitmapFactory.decodeByteArray(AadhaarUtil.photo, 0, AadhaarUtil.photo.length)));

		btnMyEnrollments.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent myEnrollmentsIntent = new Intent();
				myEnrollmentsIntent.setClass(MyProfileActivity.this, MyEnrollmentsActivity.class);
				startActivity(myEnrollmentsIntent);
			}
			
		});
	}
}
