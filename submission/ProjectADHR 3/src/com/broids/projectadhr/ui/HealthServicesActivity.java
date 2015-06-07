package com.broids.projectadhr.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.broids.projectadhr.R;
import com.broids.projectadhr.db.DBHelper;
import com.broids.projectadhr.models.HealthUser;
import com.broids.projectadhr.utils.AadhaarUtil;

public class HealthServicesActivity extends Activity {

	private ListView lvHealthServices;

	String[] healthServices = new String[] { "RNTCP-DOTS Seva",
			"Pulse Polio Abhiyan" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_health_services);
		lvHealthServices = (ListView) findViewById(R.id.lvHealthServices);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1,
				healthServices);
		lvHealthServices.setAdapter(adapter);

		lvHealthServices.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				if (position == 0) {
					if (!TextUtils.isEmpty(AadhaarUtil.mCurrentaadhaarID)) {
						DBHelper dbHelper = new DBHelper(
								HealthServicesActivity.this);

						boolean userExistsInDb = dbHelper
								.isHealthUserEnrolled(AadhaarUtil.mCurrentaadhaarID);

						if (!userExistsInDb) {
							Intent healthServiceDetailsIntent = new Intent(
									HealthServicesActivity.this,
									EnrollHealthUserActivity.class);
							startActivity(healthServiceDetailsIntent);
						} else {
							HealthUser user = dbHelper
									.getHealthUserDetails(AadhaarUtil.mCurrentaadhaarID);

							Intent healthServiceDetailsIntent = new Intent(
									HealthServicesActivity.this,
									TakeDoseActivity.class);
							startActivity(healthServiceDetailsIntent);

						}
					} else {
						Toast.makeText(HealthServicesActivity.this,
								R.string.please_login_again_, Toast.LENGTH_LONG)
								.show();
						Intent intent_logout = new Intent();
						intent_logout.setClass(HealthServicesActivity.this,
								LoginActivity.class);
						intent_logout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
								| Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent_logout);
						finish();
					}
				}else {
					Toast.makeText(HealthServicesActivity.this,
							R.string.other_services_will_be_supported_in_future_, Toast.LENGTH_LONG)
							.show();
				}

			}
		});
	}
}
