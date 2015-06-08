package com.broids.projectadhr.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.broids.projectadhr.models.HealthUser;
import com.broids.projectadhr.models.LoanHistory;

public class DBHelper extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "samadhaar.db";

	public static final String TABLE_NAME_HEALTH_USERS = "health_users";

	public static final String HEALTH_USERS_COLUMN_ID = "_id";
	public static final String HEALTH_USERS_COLUMN_UID = "uid";
	public static final String HEALTH_USERS_COLUMN_DOTS_AADHAAR = "dots_aadhaar";
	public static final String HEALTH_USERS_COLUMN_IS_ACTIVE = "is_active";
	public static final String HEALTH_USERS_COLUMN_ENROLLMENT_DATE = "enrollment_date";
	public static final String HEALTH_USERS_COLUMN_LAST_DOSE_DATE = "last_dose_date";
	public static final String HEALTH_USERS_COLUMN_NEXT_DOSE_DATE = "next_dose_date";
	public static final String HEALTH_USERS_COLUMN_TREATMENT_TYPE = "treatment_type";
	public static final String HEALTH_USERS_COLUMN_PHOTO_URI = "photo_bitmap";

	public static final String TABLE_NAME_SERVICES = "services";
	public static final String SERVICES_COLUMN_SERVICE_ID = "_id";
	public static final String SERVICES_COLUMN_SERVICE_NAME = "name";

	public static final String TABLE_NAME_LOANS = "loans";
	public static final String LOAN_COLUMN_ID = "_id";
	public static final String LOAN_COLUMN_UID = "uid";
	public static final String LOAN_COLUMN_NAME = "name";
	public static final String LOAN_COLUMN_AMOUNT = "amount";
	public static final String LOAN_COLUMN_ROI = "roi";
	public static final String LOAN_COLUMN_TENURE = "tenure";
	public static final String LOAN_COLUMN_ENTROLLMENT_DATE = "enrollmentDate";

	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		StringBuilder createScript1 = new StringBuilder()

				// Health Users Table
				.append("create table if not exists ").append(TABLE_NAME_HEALTH_USERS).append(" ( ").append(HEALTH_USERS_COLUMN_ID).append(" integer primary key, ").append(HEALTH_USERS_COLUMN_UID)
				.append(" text, ").append(HEALTH_USERS_COLUMN_DOTS_AADHAAR).append(" text, ").append(HEALTH_USERS_COLUMN_IS_ACTIVE).append(" text, ").append(HEALTH_USERS_COLUMN_ENROLLMENT_DATE)
				.append(" text, ").append(HEALTH_USERS_COLUMN_LAST_DOSE_DATE).append(" text, ").append(HEALTH_USERS_COLUMN_NEXT_DOSE_DATE).append(" text, ").append(HEALTH_USERS_COLUMN_TREATMENT_TYPE)
				.append(" text, ").append(HEALTH_USERS_COLUMN_PHOTO_URI).append(" blob); ");

		StringBuilder createScript2 = new StringBuilder().append("create table if not exists ").append(TABLE_NAME_SERVICES).append(" ( ").append(SERVICES_COLUMN_SERVICE_ID)
				.append(" integer primary key, ").append(SERVICES_COLUMN_SERVICE_NAME).append(" text); ");

		StringBuilder createScript3 = new StringBuilder().append("create table if not exists ").append(TABLE_NAME_LOANS).append(" ( ").append(LOAN_COLUMN_ID).append(" integer primary key, ")
				.append(LOAN_COLUMN_NAME).append(" text, ").append(LOAN_COLUMN_UID).append(" text, ").append(LOAN_COLUMN_AMOUNT).append(" text, ").append(LOAN_COLUMN_ROI).append(" text, ")
				.append(LOAN_COLUMN_TENURE).append(" text, ").append(LOAN_COLUMN_ENTROLLMENT_DATE).append(" text); ");

		db.execSQL(createScript1.toString());
		db.execSQL(createScript2.toString());
		db.execSQL(createScript3.toString());
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_HEALTH_USERS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_SERVICES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_LOANS);
		onCreate(db);
	}

	public boolean insertAndEnrollHealthUser(String uid, String dots_aadhaar, String is_active, String enrollment_date, String last_dose_date, String next_dose_date, String treatment_type,
			byte[] photo_uri) {
		
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put(HEALTH_USERS_COLUMN_UID, uid);
		contentValues.put(HEALTH_USERS_COLUMN_DOTS_AADHAAR, dots_aadhaar);
		contentValues.put(HEALTH_USERS_COLUMN_IS_ACTIVE, is_active);
		contentValues.put(HEALTH_USERS_COLUMN_ENROLLMENT_DATE, enrollment_date);
		contentValues.put(HEALTH_USERS_COLUMN_LAST_DOSE_DATE, last_dose_date);
		contentValues.put(HEALTH_USERS_COLUMN_NEXT_DOSE_DATE, next_dose_date);
		contentValues.put(HEALTH_USERS_COLUMN_TREATMENT_TYPE, treatment_type);
		//contentValues.put(HEALTH_USERS_COLUMN_PHOTO_URI, photo_uri);
		contentValues.put(HEALTH_USERS_COLUMN_PHOTO_URI, "");

		long returnValue = db.insert(TABLE_NAME_HEALTH_USERS, null, contentValues);
		return (returnValue > 0);
	}

	public HealthUser getHealthUserDetails(String id) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor res = db.rawQuery("select * from " + TABLE_NAME_HEALTH_USERS + " where " + HEALTH_USERS_COLUMN_UID + " = '" + id + "'  ORDER BY _id DESC", null);

		if (res != null && res.moveToFirst()) {
			HealthUser user = new HealthUser();
			user.set_id(res.getLong(res.getColumnIndex(HEALTH_USERS_COLUMN_UID)));
			user.setDotsAadhaar(res.getString(res.getColumnIndex(HEALTH_USERS_COLUMN_DOTS_AADHAAR)));
			user.setActive(res.getString(res.getColumnIndex(HEALTH_USERS_COLUMN_IS_ACTIVE)));
			user.setEnrollmentDate(res.getString(res.getColumnIndex(HEALTH_USERS_COLUMN_ENROLLMENT_DATE)));
			user.setLastDoseDate(res.getString(res.getColumnIndex(HEALTH_USERS_COLUMN_LAST_DOSE_DATE)));
			user.setNextDoseDate(res.getString(res.getColumnIndex(HEALTH_USERS_COLUMN_NEXT_DOSE_DATE)));
			user.setTreatmentType(res.getString(res.getColumnIndex(HEALTH_USERS_COLUMN_TREATMENT_TYPE)));
			user.setPhotoURI(res.getBlob((res.getColumnIndex(HEALTH_USERS_COLUMN_PHOTO_URI))));

			return user;
		}

		return null;
	}

	public boolean isHealthUserEnrolled(String uid) {

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor res = db.rawQuery("select * from " + TABLE_NAME_HEALTH_USERS + " where " + HEALTH_USERS_COLUMN_UID + " = '" + uid + "'", null);
		return (res.getCount() > 0);
	}

	public ArrayList<HealthUser> getAllHealthDoseHistory(String uid) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor res = db.rawQuery("select * from " + TABLE_NAME_HEALTH_USERS + " where " + HEALTH_USERS_COLUMN_UID + " = '" + uid + "'", null);
		ArrayList<HealthUser> arrDoseHistory = new ArrayList<>();
		res.moveToFirst();
		while (res.isAfterLast() == false) {
			HealthUser healthUserDose = new HealthUser();
			healthUserDose.set_id(res.getLong(res.getColumnIndex(HEALTH_USERS_COLUMN_ID)));
			healthUserDose.setUid(res.getString(res.getColumnIndex(HEALTH_USERS_COLUMN_DOTS_AADHAAR)));
			healthUserDose.setDotsAadhaar(res.getString(res.getColumnIndex(HEALTH_USERS_COLUMN_DOTS_AADHAAR)));
			healthUserDose.setActive(res.getString(res.getColumnIndex(HEALTH_USERS_COLUMN_IS_ACTIVE)));
			healthUserDose.setEnrollmentDate(res.getString(res.getColumnIndex(HEALTH_USERS_COLUMN_ENROLLMENT_DATE)));
			healthUserDose.setLastDoseDate(res.getString(res.getColumnIndex(HEALTH_USERS_COLUMN_LAST_DOSE_DATE)));
			healthUserDose.setNextDoseDate(res.getString(res.getColumnIndex(HEALTH_USERS_COLUMN_NEXT_DOSE_DATE)));
			healthUserDose.setTreatmentType(res.getString(res.getColumnIndex(HEALTH_USERS_COLUMN_TREATMENT_TYPE)));
			healthUserDose.setPhotoURI(res.getBlob(res.getColumnIndex(HEALTH_USERS_COLUMN_PHOTO_URI)));

			arrDoseHistory.add(healthUserDose);

			res.moveToNext();
		}

		return arrDoseHistory;
	}
	
	

	// ========================== LOAN ====================================

	public boolean insertLoan(String uid, String name, String string, String string2, String string3, String enrollmentDate) {

		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues contentValues = new ContentValues();

		contentValues.put(LOAN_COLUMN_UID, uid);
		contentValues.put(LOAN_COLUMN_NAME, name);
		contentValues.put(LOAN_COLUMN_AMOUNT, string);
		contentValues.put(LOAN_COLUMN_ROI, string2);
		contentValues.put(LOAN_COLUMN_TENURE, string3);
		contentValues.put(LOAN_COLUMN_ENTROLLMENT_DATE, enrollmentDate);

		db.insert(TABLE_NAME_LOANS, null, contentValues);

		return true;

	}

	public ArrayList<LoanHistory> getAllUserLoans(String uid) {

		SQLiteDatabase db = this.getReadableDatabase();

		Cursor res = db.rawQuery("select * from " + TABLE_NAME_LOANS + " where " + LOAN_COLUMN_UID + " = '" + uid + "'", null);

		ArrayList<LoanHistory> arrLoanHistory = new ArrayList<>();

		res.moveToFirst();

		while (res.isAfterLast() == false) {

			LoanHistory loanHistory = new LoanHistory();

			loanHistory.set_id(res.getLong(res.getColumnIndex(LOAN_COLUMN_UID)));
			loanHistory.setName(res.getString(res.getColumnIndex(LOAN_COLUMN_NAME)));
			loanHistory.setAmount(res.getString(res.getColumnIndex(LOAN_COLUMN_AMOUNT)));
			loanHistory.setRoi(res.getString(res.getColumnIndex(LOAN_COLUMN_ROI)));
			loanHistory.setTenure(res.getString(res.getColumnIndex(LOAN_COLUMN_TENURE)));
			loanHistory.setEnrollmentDate(res.getString(res.getColumnIndex(LOAN_COLUMN_ENTROLLMENT_DATE)));

			arrLoanHistory.add(loanHistory);

			res.moveToNext();

		}

		return arrLoanHistory;

	}
}
