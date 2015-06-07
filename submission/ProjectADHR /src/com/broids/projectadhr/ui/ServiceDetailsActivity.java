package com.broids.projectadhr.ui;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aadhaarconnect.bridge.capture.model.auth.AuthCaptureData;
import com.aadhaarconnect.bridge.capture.model.auth.AuthCaptureRequest;
import com.aadhaarconnect.bridge.capture.model.common.Location;
import com.aadhaarconnect.bridge.capture.model.common.LocationType;
import com.aadhaarconnect.bridge.capture.model.common.request.CertificateType;
import com.aadhaarconnect.bridge.capture.model.common.request.Modality;
import com.aadhaarconnect.bridge.capture.model.common.request.ModalityType;
import com.broids.projectadhr.R;
import com.broids.projectadhr.models.LoanHistory;
import com.broids.projectadhr.net.AadhaarConfirmationAuth;
import com.broids.projectadhr.utils.AadhaarUtil;
import com.broids.projectadhr.utils.Constants;
import com.google.gson.Gson;

public class ServiceDetailsActivity extends Activity {

	private TextView lblServiceTitle, lblAgreementDescription;
	private EditText txtAmount, txtRateOfInterest, txtTenure;
	private Button btnCalculate, btnAgree;
	StringBuilder summaryMessage ;
	boolean isCalculated ;
	public static final int AADHAAR_FINAL_BIOMETRIC = 2002;
	private static final String BASE_URL = "https://ac.khoslalabs.com/hackgate/hackathon";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.service_details);
		isCalculated = false;
		
		if (!TextUtils.isEmpty(AadhaarUtil.mCurrentUserName)) {
			getActionBar().setTitle(AadhaarUtil.mCurrentUserName);	
		}
		
		/*if (null != AadhaarUtil.photo) {
			getActionBar().setIcon(new BitmapDrawable(getResources(),BitmapFactory.decodeByteArray(AadhaarUtil.photo, 0, AadhaarUtil.photo.length)));
		}*/
		
		lblServiceTitle = (TextView) findViewById(R.id.lblServiceTitle);
		lblAgreementDescription = (TextView) findViewById(R.id.lblAgreementDescription);
		lblAgreementDescription.setText("");
		
		txtAmount = (EditText) findViewById(R.id.txtAmount);
		txtAmount.requestFocus();
		txtRateOfInterest = (EditText) findViewById(R.id.txtRateOfInterest);
		txtTenure = (EditText) findViewById(R.id.txtTenure);
		btnCalculate = (Button) findViewById(R.id.btnCalculate);
		btnAgree = (Button) findViewById(R.id.btnAgree);
		
		
		if (AadhaarUtil.mCurrentUser == AadhaarUtil.USER_CUSTOMER) {
			btnCalculate.setVisibility(View.VISIBLE);
			btnAgree.setVisibility(View.VISIBLE);
		} else if (AadhaarUtil.mCurrentUser == AadhaarUtil.USER_PARTNER) {
			btnAgree.setVisibility(View.GONE);
			btnCalculate.setVisibility(View.VISIBLE);
		}
		
		Bundle extras = getIntent().getExtras();
		if(extras != null) {
			lblServiceTitle.setText(extras.getString(Constants.EXTRA_LOAN_NAME));
			txtRateOfInterest.setText(String.valueOf(extras.getFloat(Constants.EXTRA_LOAN_ROI)));
			txtRateOfInterest.setEnabled(false);
		}
		
		btnAgree.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if (isCalculated) {
					authenticate_BioMetric();
				}else {
					Toast.makeText(ServiceDetailsActivity.this, "Please calculate the amount first", Toast.LENGTH_LONG).show();
				}
			}
		});
		
		btnCalculate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				isCalculated = true;
				
				float amount = 0.0f;
				try {
					amount = Float.parseFloat(txtAmount.getText().toString());
				} catch(Exception ex) {
					ex.printStackTrace();
				}
				
				float tenure = 0.0f;
				try {
					tenure = Float.parseFloat(txtTenure.getText().toString());
				} catch (Exception ex) {
					ex.printStackTrace();
				}

				float roi = 0.0f;
				try {
					roi = Float.parseFloat(txtRateOfInterest.getText().toString());
				} catch (Exception ex) {
					ex.printStackTrace();
				}

				summaryMessage = new StringBuilder("Summary:\n\n  Total Amount: INR ");
				summaryMessage.append(amount);
				summaryMessage.append("\n   Tenure: ");
				summaryMessage.append(tenure);
				summaryMessage.append("\n   Percentage: ");
				summaryMessage.append(roi);
				
				float interest = (amount * tenure * roi) / (12 * 100);
				summaryMessage.append("\n   Interest: ");
				summaryMessage.append(interest);
				
				lblAgreementDescription.setText(summaryMessage);
				AadhaarUtil.mCurrentTransactionSummary = summaryMessage.toString();
				
				
		/*		Calendar calendar = Calendar.getInstance();

				SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
				String formattedDate = df.format(calendar.getTime());*/
				
				
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
				String currentDateandTime = sdf.format(new Date());
				
				history = new LoanHistory();
				history.setAmount(String.valueOf(amount));
				history.setEnrollmentDate(currentDateandTime);
				history.setName(lblServiceTitle.getText().toString());
				history.setRoi(String.valueOf(roi));
				history.setTenure(String.valueOf(tenure));
				history.setUid(AadhaarUtil.mCurrentaadhaarID);
			}
		});
	}
	
	LoanHistory history;
	
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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_login:
			Intent intent = new Intent();
			intent.setClass(ServiceDetailsActivity.this, LoginActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
			return true;
			
		case R.id.menu_logout:
			Intent intent_logout = new Intent();
			intent_logout.setClass(ServiceDetailsActivity.this, LoginActivity.class);
			intent_logout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent_logout);
			finish();
			return true;
		case R.id.menu_my_profile:
			Intent profileIntent = new Intent(ServiceDetailsActivity.this, MyProfileActivity.class);;
			startActivity(profileIntent);
			return true;
		default:
			return super.onOptionsItemSelected(item);

		}
	}
	
	
	public void authenticate_BioMetric() {
		if (TextUtils.isEmpty(AadhaarUtil.mCurrentaadhaarID)) {
			showToast(
					"Invalid Aadhaar Number. Please enter a valid Aadhaar Number",
					Toast.LENGTH_LONG);
			return;
		}

		AuthCaptureRequest authCaptureRequest = new AuthCaptureRequest();
		authCaptureRequest.setAadhaar(AadhaarUtil.mCurrentaadhaarID);
		authCaptureRequest
				.setModality(Modality.biometric);
		authCaptureRequest
				.setModalityType(ModalityType.fp);
		authCaptureRequest.setNumOffingersToCapture(2);
		authCaptureRequest
				.setCertificateType(CertificateType.preprod);

		Location loc = new Location();
		loc.setType(LocationType.pincode);
		loc.setPincode("560076");
		authCaptureRequest.setLocation(loc);

		Intent i = new Intent();
		i = new Intent("com.aadhaarconnect.bridge.action.AUTHCAPTURE");
		i.putExtra("REQUEST",
				new Gson().toJson(authCaptureRequest));
		try {
			startActivityForResult(i,AADHAAR_FINAL_BIOMETRIC);
		} catch (Exception e) {
			System.out.println("");
			e.printStackTrace();
		}

	}
	
	private void showToast(String text, int duration) {
		Toast toast = Toast.makeText(this, text, duration);
		toast.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (resultCode == RESULT_OK && requestCode == AADHAAR_FINAL_BIOMETRIC && data != null) {
			String responseStr = data.getStringExtra("RESPONSE");
			final AuthCaptureData authCaptureData = new Gson().fromJson(
					responseStr, AuthCaptureData.class);
			
			AadhaarConfirmationAuth authAsyncTask = new AadhaarConfirmationAuth(this,
					authCaptureData,history);
			authAsyncTask.execute(BASE_URL + "/auth");
			return;
		}
	}
}
