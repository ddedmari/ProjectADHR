package com.broids.projectadhr.ui;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.broids.projectadhr.R;
import com.broids.projectadhr.db.DBHelper;
import com.broids.projectadhr.utils.AadhaarUtil;

public class EnrollHealthUserActivity extends Activity {
	private static final int CAMERA_PIC_REQUEST = 1337;
	private static Uri outputFileUri;

	ImageView profilePic, prescriptionPic;
	EditText name, address;
	Spinner chooseType;
	Button btn_Capture, btn_Enroll;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_enroll_health_user);
		
		getActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>"+getString(R.string.app_name)+" </font>"));
		
		profilePic = (ImageView) findViewById(R.id.img_profilepic);
		prescriptionPic = (ImageView) findViewById(R.id.img_prescription);

		name = (EditText) findViewById(R.id.editText_enroll_name);
		address = (EditText) findViewById(R.id.editText_enroll_address);

		chooseType = (Spinner) findViewById(R.id.spinner_TBType);
		btn_Capture = (Button) findViewById(R.id.btn_takePhoto);
		btn_Enroll = (Button) findViewById(R.id.btn_enroll);

		name.setText(AadhaarUtil.mCurrentUserKYC.getPoi().getName());

		StringBuilder builder = new StringBuilder();
		builder.append(AadhaarUtil.mCurrentUserKYC.getPoa().getHouse());
		builder.append("  ");
		builder.append(AadhaarUtil.mCurrentUserKYC.getPoa().getStreet());
		builder.append("  ");
		builder.append(AadhaarUtil.mCurrentUserKYC.getPoa().getVtc());
		builder.append("  ");
		builder.append(AadhaarUtil.mCurrentUserKYC.getPoa().getPo());
		builder.append("  ");
		builder.append(AadhaarUtil.mCurrentUserKYC.getPoa().getDist());
		builder.append("  ");
		builder.append(AadhaarUtil.mCurrentUserKYC.getPoa().getState());
		builder.append("  ");
		builder.append(AadhaarUtil.mCurrentUserKYC.getPoa().getPc());
		builder.append("  ");

		address.setText(builder.toString());

		profilePic.setImageDrawable(new BitmapDrawable(getResources(),
				BitmapFactory.decodeByteArray(
						AadhaarUtil.mCurrentUserKYC.getPhoto(), 0,
						AadhaarUtil.mCurrentUserKYC.getPhoto().length)));

		btn_Enroll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				if (null == byteArray) {
					Toast.makeText(EnrollHealthUserActivity.this, "Please take a photo first", Toast.LENGTH_LONG).show();
				}
				
				Calendar calendar = Calendar.getInstance();
				System.out.println("Current time => " + calendar.getTime());

				SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
				String formattedDate = df.format(calendar.getTime());

				Date today = calendar.getTime();

				calendar.add(Calendar.DAY_OF_YEAR, 3);
				Date tomorrow = calendar.getTime();

				DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
				String nextDate = dateFormat.format(tomorrow);

				System.out.println("name " + name.getText().toString());
				System.out.println("address " + address.getText().toString());
				DBHelper dbHelper = new DBHelper(EnrollHealthUserActivity.this);
				boolean successValue = dbHelper.insertAndEnrollHealthUser(
						AadhaarUtil.mCurrentUserKYC.getAadhaar(), "-1", "Y",
						"Not Available", "Not Available", nextDate, chooseType.getSelectedItem().toString(),
						byteArray );
				
				System.out.println("*** TB Insert success: " + successValue);
				
				Intent intent = new Intent();
				intent.setClass(EnrollHealthUserActivity.this,TakeDoseActivity.class);
				intent.putExtra("value1", "-1");
				intent.putExtra("value2", "Y");
				intent.putExtra("value3", "Not Available");
				intent.putExtra("value4", "Not Available");
				intent.putExtra("value5", nextDate);
				intent.putExtra("value6", chooseType.getSelectedItem().toString());
				intent.putExtra("value7", byteArray);
				
				startActivity(intent);
			}
		});

		btn_Capture.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent cameraIntent = new Intent(
						android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
				startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
			}
		});
	}

	byte[] byteArray =null;
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CAMERA_PIC_REQUEST && resultCode == RESULT_OK) {
			// do something to get the bitmap from the uri for example
			try {
				// Bitmap bitmap =
				// MediaStore.Images.Media.getBitmap(this.getContentResolver(),
				// outputFileUri);
				Bitmap photo = (Bitmap) data.getExtras().get("data");
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				photo.compress(Bitmap.CompressFormat.PNG, 50, stream);
				byteArray = stream.toByteArray();
				prescriptionPic.setImageBitmap(photo);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
