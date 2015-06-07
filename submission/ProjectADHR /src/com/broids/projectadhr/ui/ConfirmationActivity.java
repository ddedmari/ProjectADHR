package com.broids.projectadhr.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
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
import com.broids.projectadhr.net.AadhaarConfirmationAuth;
import com.broids.projectadhr.utils.AadhaarUtil;
import com.google.gson.Gson;

public class ConfirmationActivity extends Activity {

	TextView text_summary;
	Button btn_Done;
	
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_transactionsummary);

		if (!TextUtils.isEmpty(AadhaarUtil.mCurrentUserName)) {
			getActionBar().setTitle(AadhaarUtil.mCurrentUserName);	
		}
		
		/*if (null != AadhaarUtil.photo) {
			getActionBar().setIcon(new BitmapDrawable(getResources(),BitmapFactory.decodeByteArray(AadhaarUtil.photo, 0, AadhaarUtil.photo.length)));
		}*/
		
		text_summary = (TextView) findViewById(R.id.text_details);
		text_summary.setText(AadhaarUtil.mCurrentTransactionSummary);

		btn_Done = (Button) findViewById(R.id.btnDone);
		btn_Done.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				authenticate_BioMetric();
				
				Intent intent_logout = new Intent();
				intent_logout.setClass(ConfirmationActivity.this, LoginActivity.class);
				intent_logout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent_logout);
				finish();
			}
		});

	};

	

	private void showToast(String text, int duration) {
		Toast toast = Toast.makeText(this, text, duration);
		toast.show();
	}

	/*@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (resultCode == RESULT_OK && requestCode == AADHAAR_FINAL_BIOMETRIC && data != null) {
			String responseStr = data.getStringExtra("RESPONSE");
			final AuthCaptureData authCaptureData = new Gson().fromJson(
					responseStr, AuthCaptureData.class);
			AadhaarConfirmationAuth authAsyncTask = new AadhaarConfirmationAuth(this,
					authCaptureData);
			authAsyncTask.execute(BASE_URL + "/auth");
			return;
		}
	}*/
}
