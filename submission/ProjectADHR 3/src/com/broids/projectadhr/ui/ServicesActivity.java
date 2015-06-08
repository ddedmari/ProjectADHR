package com.broids.projectadhr.ui;

import java.util.ArrayList;

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
import android.widget.ListView;

import com.broids.projectadhr.R;
import com.broids.projectadhr.adapters.LoanArrayAdapter;
import com.broids.projectadhr.models.LoanItem;
import com.broids.projectadhr.utils.AadhaarUtil;
import com.broids.projectadhr.utils.Constants;

public class ServicesActivity extends Activity {

	private ListView lvServices;
	private LoanArrayAdapter mAdapter;
	private ArrayList<LoanItem> arrLoanItems;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_services);
		if (!TextUtils.isEmpty(AadhaarUtil.mCurrentUserName)) {
			getActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>"+AadhaarUtil.mCurrentUserName+" </font>"));
		}
		/*if (null != AadhaarUtil.photo) {
			getActionBar().setIcon(new BitmapDrawable(getResources(),BitmapFactory.decodeByteArray(AadhaarUtil.photo, 0, AadhaarUtil.photo.length)));
		}*/
		lvServices = (ListView) findViewById(R.id.lvServices);

		
		arrLoanItems = new ArrayList<LoanItem>();
		if (AadhaarUtil.mCurrentUserKYC.getPoa().getState().equalsIgnoreCase(("Karnataka"))) {
			arrLoanItems.add(new LoanItem(1, "Karnataka Self Employment Scheme Loan", 10.0f));
			arrLoanItems.add(new LoanItem(2, "Karnataka Handicraft Artisan Loan", 13.0f));
			arrLoanItems.add(new LoanItem(3, "Karnataka Farmers Loan", 11.0f));
		}
		else if (AadhaarUtil.mCurrentUserKYC.getPoa().getState().equalsIgnoreCase(("Gujarat"))) {
			arrLoanItems.add(new LoanItem(4, "Dr.P.G.Solanki Loan Assistance", 15.0f));
			arrLoanItems.add(new LoanItem(5, "Dr.Baba Saheb Ambedkar Loan", 11.60f));
			arrLoanItems.add(new LoanItem(6, "Gujarat Govt Agriculture Loan", 9.0f));
		}
		
		arrLoanItems.add(new LoanItem(7, "Home Loan", 15.0f));
		arrLoanItems.add(new LoanItem(8, "Education Loan", 6.0f));

		mAdapter = new LoanArrayAdapter(this, arrLoanItems);

		lvServices.setAdapter(mAdapter);

		lvServices.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Intent serviceDetailsIntent = new Intent(ServicesActivity.this,
						ServiceDetailsActivity.class);
				serviceDetailsIntent.putExtra(Constants.EXTRA_LOAN_ID,
						arrLoanItems.get(position).getLoanID());
				serviceDetailsIntent.putExtra(Constants.EXTRA_LOAN_NAME,
						arrLoanItems.get(position).getLoanName());
				serviceDetailsIntent.putExtra(Constants.EXTRA_LOAN_ROI,
						arrLoanItems.get(position).getRateOfInterest());
				
				startActivity(serviceDetailsIntent);
			}
		});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_login:
			Intent intent = new Intent();
			intent.setClass(ServicesActivity.this, LoginActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
			finish();
			startActivity(intent);

			return true;
			
		case R.id.menu_logout:
			Intent intent_logout = new Intent();
			intent_logout.setClass(ServicesActivity.this, LoginActivity.class);
			intent_logout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
			finish();
			startActivity(intent_logout);

			return true;
		case R.id.menu_my_profile:
			Intent profileIntent = new Intent(ServicesActivity.this, MyProfileActivity.class);;
			startActivity(profileIntent);
			return true;
		default:
			return super.onOptionsItemSelected(item);

		}
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);// Menu Resource, Menu
		MenuItem item_logout = menu.findItem(R.id.menu_logout);
		MenuItem item_login = menu.findItem(R.id.menu_login);

		if (AadhaarUtil.mCurrentUser == AadhaarUtil.USER_CUSTOMER) {
			item_login.setVisible(false);
			item_logout.setVisible(true);
		} else if (AadhaarUtil.mCurrentUser == AadhaarUtil.USER_PARTNER) {
			item_logout.setVisible(false);
			item_login.setVisible(true);
		}
		
		MenuItem item_my_profile = menu.findItem(R.id.menu_my_profile);
		if (null != AadhaarUtil.photo) {
			item_my_profile.setIcon(new BitmapDrawable(getResources(),BitmapFactory.decodeByteArray(AadhaarUtil.photo, 0, AadhaarUtil.photo.length)));
		}
		return true;
	}

}
