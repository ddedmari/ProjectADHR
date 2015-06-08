package com.broids.projectadhr.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.broids.projectadhr.R;
import com.broids.projectadhr.adapters.CustomAdapter;
import com.broids.projectadhr.utils.AadhaarUtil;

public class HomeScreenActivity extends Activity {
	GridView mGridView;
	public static String[] categories;
	public static int[] images = { R.drawable.goverment, R.drawable.loan,
			R.drawable.agriculture, R.drawable.medical, R.drawable.education,
			R.drawable.social };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_categories);
		if (!TextUtils.isEmpty(AadhaarUtil.mCurrentUserName)) {
			getActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>"+AadhaarUtil.mCurrentUserName+" </font>"));
		}
		/*
		if (null != AadhaarUtil.photo) {
			getActionBar().setIcon(new BitmapDrawable(getResources(),BitmapFactory.decodeByteArray(AadhaarUtil.photo, 0, AadhaarUtil.photo.length)));
		}
		*/
		categories = getResources().getStringArray(R.array.categories);

		mGridView = (GridView) findViewById(R.id.gridView1);
		mGridView.setAdapter(new CustomAdapter(this, categories, images));
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				switch (images[position]) {
				case R.drawable.goverment:

					break;
				case R.drawable.loan:
					Intent intent = new Intent();
					intent.setClass(HomeScreenActivity.this, ServicesActivity.class);
					startActivity(intent);
					break;
				case R.drawable.agriculture:

					break;
				case R.drawable.medical:
					Intent healthServicesIntent = new Intent();
					healthServicesIntent.setClass(HomeScreenActivity.this, HealthServicesActivity.class);
					startActivity(healthServicesIntent);
					break;
				case R.drawable.education:

					break;
				case R.drawable.social:

					break;
				default:
					break;
				}
			}
		});
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);// Menu Resource, Menu
		MenuItem item_logout = menu.findItem(R.id.menu_logout);
		MenuItem item_login = menu.findItem(R.id.menu_login);
		
		MenuItem item_my_profile = menu.findItem(R.id.menu_my_profile);
		if (null != AadhaarUtil.photo) {
			item_my_profile.setIcon(new BitmapDrawable(getResources(),BitmapFactory.decodeByteArray(AadhaarUtil.photo, 0, AadhaarUtil.photo.length)));
		}
		
		if (AadhaarUtil.mCurrentUser == AadhaarUtil.USER_CUSTOMER) {
			item_login.setVisible(false);
			item_logout.setVisible(true);
		} else if (AadhaarUtil.mCurrentUser == AadhaarUtil.USER_PARTNER) {
			item_logout.setVisible(false);
			item_login.setVisible(true);
		}
		
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_login:
			Intent intent = new Intent();
			intent.setClass(HomeScreenActivity.this, LoginActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
			return true;
			
		case R.id.menu_logout:
			Intent intent_logout = new Intent();
			intent_logout.setClass(HomeScreenActivity.this, LoginActivity.class);
			intent_logout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent_logout);
			finish();
			return true;
		case R.id.menu_my_profile:
			Intent profileIntent = new Intent(HomeScreenActivity.this, MyProfileActivity.class);;
			startActivity(profileIntent);
			return true;
		default:
			return super.onOptionsItemSelected(item);

		}
	}

}