package com.broids.projectadhr.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.broids.projectadhr.db.DBHelper;
import com.broids.projectadhr.models.HealthUser;
import com.broids.projectadhr.net.AadhaarHealthAuthAsyncTask;
import com.broids.projectadhr.utils.AadhaarUtil;
import com.google.gson.Gson;

public class TakeDoseActivity extends Activity {
	
	TextView lastDose, nextDose;
	ImageView bioAuth;
	EditText aadhaarNumber;
	public static final int QRCODE_REQUEST = 1000;

	public static final int AADHAAR_CONNECT_AUTH_REQUEST = 1001;
	private static final String BASE_URL = "https://ac.khoslalabs.com/hackgate/hackathon";

	private ImageView qrCodeScanner;
	
	String value1, value2, value3, value4, value5, value6;
	byte[] value7 =null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_take_dose);
		qrCodeScanner = (ImageView) findViewById(R.id.barcode);

		
		DBHelper dbHelper = new DBHelper(TakeDoseActivity.this);
		
		HealthUser user = dbHelper.getHealthUserDetails(AadhaarUtil.mCurrentaadhaarID);
		
		lastDose = (TextView) findViewById(R.id.text_lastDoze);
		nextDose = (TextView) findViewById(R.id.text_nextDoze);
		bioAuth = (ImageView) findViewById(R.id.img_biometric);
		
		aadhaarNumber = (EditText) findViewById(R.id.doats_aadhaar_number);
		
		lastDose.setText(user.getLastDoseDate());
		nextDose.setText(user.getNextDoseDate());
		
		Bundle extras = getIntent().getExtras();
		if(extras != null) {
			value1 = extras.getString("value1");
			value2 = extras.getString("value2");
			value3 = extras.getString("value3");
			value4 = extras.getString("value4");
			value5 = extras.getString("value5");
			value6 = extras.getString("value6");
			value7 = extras.getByteArray("value7");
			
		}
		
		bioAuth.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				authenticate_BioMetric();
			}
		});
		
	}
	
	public void scanUsingQRCode(View v) {
		Intent intent = new Intent("com.google.zxing.client.android.SCAN");
		intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
		try {
			startActivityForResult(intent, QRCODE_REQUEST);
		} catch (Exception e) {
			showToast("No QR Code scanning modules found.", Toast.LENGTH_LONG);
		}
	}
	
	public void authenticate_BioMetric() {
		if (TextUtils.isEmpty(aadhaarNumber.getText().toString())) {
			showToast(
					"Invalid Aadhaar Number. Please enter a valid Aadhaar Number",
					Toast.LENGTH_LONG);
			return;
		}

		AuthCaptureRequest authCaptureRequest = new AuthCaptureRequest();
		authCaptureRequest.setAadhaar(aadhaarNumber.getText().toString());
		authCaptureRequest.setModality(Modality.biometric);
		authCaptureRequest.setModalityType(ModalityType.fp);
		authCaptureRequest.setNumOffingersToCapture(2);
		authCaptureRequest.setCertificateType(CertificateType.preprod);

		Location loc = new Location();
		loc.setType(LocationType.pincode);
		loc.setPincode("560076");
		authCaptureRequest.setLocation(loc);

		Intent i = new Intent();
		i = new Intent("com.aadhaarconnect.bridge.action.AUTHCAPTURE");
		i.putExtra("REQUEST", new Gson().toJson(authCaptureRequest));
		try {
			startActivityForResult(i, AADHAAR_CONNECT_AUTH_REQUEST);
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
		if (resultCode == RESULT_OK
				&& requestCode == AADHAAR_CONNECT_AUTH_REQUEST && data != null) {
			String responseStr = data.getStringExtra("RESPONSE");
			final AuthCaptureData authCaptureData = new Gson().fromJson(
					responseStr, AuthCaptureData.class);
			
			AadhaarHealthAuthAsyncTask authAsyncTask = new AadhaarHealthAuthAsyncTask(TakeDoseActivity.this,
					authCaptureData,"TYPE_DOSE", value1, value2, value3, value4, value5, value6, value7);
			authAsyncTask.execute(BASE_URL + "/auth");
			return;
		}
		
		if (requestCode == QRCODE_REQUEST && resultCode == RESULT_OK
				&& data != null) {
			String contents = data.getStringExtra("SCAN_RESULT");
			if (!TextUtils.isEmpty(contents)) {
				String aadhaar = readValue(contents, "uid");
				aadhaarNumber.setText(aadhaar);
				qrCodeScanner.setImageResource(R.drawable.qrcode_green);
			} else {
				qrCodeScanner.setImageResource(R.drawable.qrcode_gray);
			}
			return;
		}
	}
	
	// HELPER METHODS
		private String readValue(String contents, String dataName) {
			String[] keys;
			if (dataName.contains(",")) {
				keys = dataName.split(",");
			} else {
				keys = new String[] { dataName };
			}
			String value = "";
			for (String key : keys) {
				int startIndex = contents.indexOf(key + "=");
				if (startIndex >= 0) {
					int endIndex = contents.indexOf("\"", startIndex + key.length()
							+ 1 + 1);
					if (endIndex >= 0) {
						value += " ";
						value += contents.substring(startIndex + key.length() + 1,
								endIndex).replaceAll("\"", "");
					}
				}
			}
			return value.trim();
		}
}
