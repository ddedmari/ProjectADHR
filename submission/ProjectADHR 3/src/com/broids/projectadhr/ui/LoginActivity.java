package com.broids.projectadhr.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.aadhaarconnect.bridge.capture.model.auth.AuthCaptureData;
import com.aadhaarconnect.bridge.capture.model.auth.AuthCaptureRequest;
import com.aadhaarconnect.bridge.capture.model.common.ConsentType;
import com.aadhaarconnect.bridge.capture.model.common.Location;
import com.aadhaarconnect.bridge.capture.model.common.LocationType;
import com.aadhaarconnect.bridge.capture.model.common.request.CertificateType;
import com.aadhaarconnect.bridge.capture.model.common.request.Modality;
import com.aadhaarconnect.bridge.capture.model.common.request.ModalityType;
import com.aadhaarconnect.bridge.capture.model.kyc.KycCaptureData;
import com.aadhaarconnect.bridge.capture.model.kyc.KycCaptureRequest;
import com.aadhaarconnect.bridge.capture.model.otp.OtpCaptureData;
import com.aadhaarconnect.bridge.capture.model.otp.OtpCaptureRequest;
import com.aadhaarconnect.bridge.capture.model.otp.OtpChannel;
import com.aadhaarconnect.bridge.gateway.model.OtpResponse;
import com.broids.projectadhr.R;
import com.broids.projectadhr.models.LoanItem;
import com.broids.projectadhr.net.AadhaarAuthAsyncTask;
import com.broids.projectadhr.net.AadhaarKYCAsyncTask;
import com.broids.projectadhr.net.AadhaarOTPAsyncTask;
import com.broids.projectadhr.utils.AadhaarUtil;
import com.broids.projectadhr.utils.AadhaarUtil.OnTaskCompleted;
import com.google.gson.Gson;

public class LoginActivity extends Activity implements OnTaskCompleted {

	public static final int QRCODE_REQUEST = 1000;
	public static final int AADHAAR_CONNECT_AUTH_REQUEST = 1001;
	public static final int AADHAAR_BIO_KYC = 1002;
	public static final int AADHAAR_OTP_AUTH_REQUEST = 1003;
	public static final int AADHAAR_OTP_KYC_REQUEST = 1004;
	private static final String BASE_URL = "https://ac.khoslalabs.com/hackgate/hackathon";

	private EditText aadhaarEditTextView;
	private ImageView qrCodeScanner, btn_OTP, btn_BioMetric;
	RadioGroup radio_userType;

	// private Button btn_auth;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		AadhaarUtil.mCurrentUserName = "";
		AadhaarUtil.mCurrentaadhaarID = "";
		AadhaarUtil.mCurrentTransactionSummary = "";
		AadhaarUtil.photo = null;
		
		aadhaarEditTextView = (EditText) findViewById(R.id.aadhaar_number);
		qrCodeScanner = (ImageView) findViewById(R.id.barcode);

//		btn_OTP = (ImageView) findViewById(R.id.img_otp);
		btn_BioMetric = (ImageView) findViewById(R.id.img_biometric);
		radio_userType = (RadioGroup) findViewById(R.id.rdgLoginType);
		
		AadhaarUtil.mCurrentUser = AadhaarUtil.USER_CUSTOMER;
		
		radio_userType
		.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				RadioButton rb = (RadioButton) findViewById(checkedId);

				switch (rb.getText().toString()) {
				case "Customer":
					AadhaarUtil.mCurrentUser = AadhaarUtil.USER_CUSTOMER;
					break;
				case "Partner":
					AadhaarUtil.mCurrentUser = AadhaarUtil.USER_PARTNER;
					break;
				default:
					break;
				}
			}
		});

//		btn_OTP.setOnClickListener(clickListner);
		btn_BioMetric.setOnClickListener(clickListner);

		/*
		 * btn_auth = (Button) findViewById(R.id.auth_button);
		 * btn_auth.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { authenticate(); } });
		 */
	}

	OnClickListener clickListner = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
//			case R.id.img_otp:
//				authenticate_OTP();
//				break;

			case R.id.img_biometric:
//				authenticate_BioMetric();
				kyc_BioMetric();
				break;

			default:
				break;
			}
		}
	};

	
	private void kyc_BioMetric() {
		Intent i = new Intent();
		i = new Intent("com.aadhaarconnect.bridge.action.KYCCAPTURE");

		KycCaptureRequest kycRequest = new KycCaptureRequest();
		AuthCaptureRequest biometricAuth = new AuthCaptureRequest();
		biometricAuth.setAadhaar(aadhaarEditTextView.getText().toString());
		biometricAuth.setModality(Modality.biometric);
		biometricAuth.setModalityType(ModalityType.fp);
		biometricAuth.setCertificateType(CertificateType.preprod);
		biometricAuth.setNumOffingersToCapture(2);

		com.aadhaarconnect.bridge.capture.model.common.Location loc 
		    = new com.aadhaarconnect.bridge.capture.model.common.Location();
		loc.setType(com.aadhaarconnect.bridge.capture.model.common.LocationType.gps);
		loc.setLatitude(String.valueOf("12.345"));
		loc.setLongitude(String.valueOf("12.87277"));
		loc.setAltitude(String.valueOf("15.9"));

		biometricAuth.setLocation(loc);

		kycRequest.setAuthCaptureRequest(biometricAuth);
		kycRequest.setConsent(ConsentType.Y);

		i.putExtra("REQUEST", new Gson().toJson(kycRequest));

		try {
		    this.startActivityForResult(i, AADHAAR_BIO_KYC);
		} catch (Exception e) {
		    Log.e("ERROR", e.getMessage());
		}
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

	private void authenticate_OTP() {
		Intent i = new Intent();
		i = new Intent("com.aadhaarconnect.bridge.action.OTPCAPTURE");

		String aadhaarNumber = aadhaarEditTextView.getText().toString();

		OtpCaptureRequest otpRequest = new OtpCaptureRequest();
		otpRequest.setAadhaar(aadhaarNumber);
		otpRequest.setCertificateType(CertificateType.preprod);
		otpRequest.setChannel(OtpChannel.SMS);

		com.aadhaarconnect.bridge.capture.model.common.Location loc = new com.aadhaarconnect.bridge.capture.model.common.Location();
		loc.setType(com.aadhaarconnect.bridge.capture.model.common.LocationType.gps);
		loc.setLatitude(String.valueOf("12.345"));
		loc.setLongitude(String.valueOf("12.87277"));
		loc.setAltitude(String.valueOf("15.9"));

		otpRequest.setLocation(loc);
		i.putExtra("REQUEST", new Gson().toJson(otpRequest));

		try {
			this.startActivityForResult(i, AADHAAR_OTP_AUTH_REQUEST);
		} catch (Exception e) {
			Log.e("ERROR", e.getMessage());
		}
	}

	public void authenticate_BioMetric() {
		if (TextUtils.isEmpty(aadhaarEditTextView.getText())) {
			showToast(
					"Invalid Aadhaar Number. Please enter a valid Aadhaar Number",
					Toast.LENGTH_LONG);
			return;
		}

		AuthCaptureRequest authCaptureRequest = new AuthCaptureRequest();
		authCaptureRequest.setAadhaar(aadhaarEditTextView.getText().toString());
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == QRCODE_REQUEST && resultCode == RESULT_OK
				&& data != null) {
			String contents = data.getStringExtra("SCAN_RESULT");
			if (!TextUtils.isEmpty(contents)) {
				String aadhaar = readValue(contents, "uid");
				aadhaarEditTextView.setText(aadhaar);
				qrCodeScanner.setImageResource(R.drawable.qrcode_green);
			} else {
				qrCodeScanner.setImageResource(R.drawable.qrcode_gray);
			}
			return;
		}

		if (resultCode == RESULT_OK
				&& requestCode == AADHAAR_CONNECT_AUTH_REQUEST && data != null) {
			String responseStr = data.getStringExtra("RESPONSE");
			final AuthCaptureData authCaptureData = new Gson().fromJson(
					responseStr, AuthCaptureData.class);
			
			AadhaarAuthAsyncTask authAsyncTask = new AadhaarAuthAsyncTask(this,
					authCaptureData,"TYPE_TRANSACTION");
			authAsyncTask.execute(BASE_URL + "/auth");
			return;
		}
		if (resultCode == RESULT_OK && requestCode == AADHAAR_OTP_AUTH_REQUEST
				&& data != null) {
			String responseStr = data.getStringExtra("RESPONSE");
			final OtpCaptureData response = new Gson().fromJson(responseStr,
					OtpCaptureData.class);

			AadhaarOTPAsyncTask authAsyncTask = new AadhaarOTPAsyncTask(
					LoginActivity.this, response, this);
			authAsyncTask.execute(BASE_URL + "/otp");
			return;
		}
		if (resultCode == RESULT_OK && (requestCode == AADHAAR_BIO_KYC  && data != null)){
			String responseStr = data.getStringExtra("RESPONSE");
			final KycCaptureData authCaptureData = new Gson().fromJson(responseStr, KycCaptureData.class);

			AadhaarKYCAsyncTask authAsyncTask = new AadhaarKYCAsyncTask(this,authCaptureData);
			authAsyncTask.execute(BASE_URL + "/kyc");

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

	private void showToast(String text, int duration) {
		Toast toast = Toast.makeText(this, text, duration);
		toast.show();
	}

	@Override
	public void onTaskCompleted(final OtpResponse response) {
		try {
			if (null != response && response.isSuccess()) {

				final EditText otpText = new EditText(LoginActivity.this);
				otpText.setInputType(InputType.TYPE_CLASS_NUMBER);
				otpText.setPadding(5, 5, 5, 5);

				new AlertDialog.Builder(LoginActivity.this)
				.setTitle("OTP Authentication")
				.setMessage(
						R.string.please_enter_otp_received_on_your_phone)
						.setView(otpText)
						.setPositiveButton(R.string.confirm,
								new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								Editable value = otpText.getText();
								AuthCaptureRequest authCaptureRequest = new AuthCaptureRequest();
								authCaptureRequest
								.setAadhaar(aadhaarEditTextView
										.getText().toString());
								authCaptureRequest
								.setModality(Modality.otp);
								authCaptureRequest.setOtp(value
										.toString());
								authCaptureRequest
								.setCertificateType(CertificateType.preprod);

								Location loc = new Location();
								loc.setType(LocationType.pincode);
								loc.setPincode("560076");
								authCaptureRequest.setLocation(loc);

								Intent i = new Intent();
								i = new Intent(
										"com.aadhaarconnect.bridge.action.AUTHCAPTURE");
								i.putExtra("REQUEST", new Gson()
								.toJson(authCaptureRequest));
								try {
									startActivityForResult(i,
											AADHAAR_CONNECT_AUTH_REQUEST);
								} catch (Exception e) {
									e.printStackTrace();
								}

							}
						})
						.setNegativeButton(R.string.cancel,
								new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								// Do nothing.
							}
						}).show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
