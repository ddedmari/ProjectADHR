package com.broids.projectadhr.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.broids.projectadhr.R;
import com.broids.projectadhr.adapters.MyEnrollmentArrayAdapter;
import com.broids.projectadhr.db.DBHelper;
import com.broids.projectadhr.models.HealthUser;
import com.broids.projectadhr.models.LoanHistory;
import com.broids.projectadhr.models.MyEnrollmentItem;
import com.broids.projectadhr.utils.AadhaarUtil;
import com.broids.projectadhr.utils.ServiceType;

public class MyEnrollmentsActivity extends Activity {

	private ListView lvMyEnrollments;
	private ArrayList<MyEnrollmentItem> data = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_enrollments);
		
		lvMyEnrollments = (ListView) findViewById(R.id.lvMyEnrollments);
		
		DBHelper dbHelper = new DBHelper(this);
		ArrayList<HealthUser> healthData = dbHelper.getAllHealthDoseHistory(AadhaarUtil.mCurrentaadhaarID);
		ArrayList<LoanHistory> loans = dbHelper.getAllUserLoans(AadhaarUtil.mCurrentaadhaarID);
		
		data = new ArrayList<>();
		for(HealthUser healthUser : healthData) {
			MyEnrollmentItem item = new MyEnrollmentItem();
			item.setName(healthUser.getDotsAadhaar() + " " + healthUser.getTreatmentType());
			item.setAttributeOne(healthUser.getEnrollmentDate());
			item.setAttributeTwo(healthUser.getNextDoseDate());
			item.setServiceType(ServiceType.HEALTH);
			
			data.add(item);
		}
		
		for(LoanHistory loanHistory: loans) {
			MyEnrollmentItem item = new MyEnrollmentItem();
			item.setName(loanHistory.getName());
			item.setAttributeOne(loanHistory.getEnrollmentDate());
			item.setAttributeTwo(loanHistory.getAmount());
			item.setAttributeThree(loanHistory.getRoi());
			item.setAttributeFour(loanHistory.getTenure());
			
			item.setServiceType(ServiceType.LOAN);
			
			data.add(item);
		}
		
		MyEnrollmentArrayAdapter adapter = new MyEnrollmentArrayAdapter(this, data);
		lvMyEnrollments.setAdapter(adapter);
	}
}
