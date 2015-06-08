package com.broids.projectadhr.utils;

import com.aadhaarconnect.bridge.gateway.model.Kyc;
import com.aadhaarconnect.bridge.gateway.model.OtpResponse;

public class AadhaarUtil {

	public static int mCurrentUser = 1;
	public static final int USER_CUSTOMER = 1;
	public static final int USER_PARTNER = 2;

	public static String mCurrentUserName = "";
	public static String mCurrentaadhaarID = "";
	public static String mCurrentTransactionSummary = "";
	public static byte[] photo = null;
	public static Kyc mCurrentUserKYC = null;

	public interface OnTaskCompleted {
		void onTaskCompleted(OtpResponse response);
	}

	public static String TYPE_ACTIVE = "Active TB";
	public static String TYPE_MDR = "MDR TB";
	public static String TYPE_XDR  = "XDR TB";
}
