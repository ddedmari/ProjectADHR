package com.broids.projectadhr.net;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.aadhaarconnect.bridge.capture.model.auth.AuthCaptureData;
import com.aadhaarconnect.bridge.gateway.model.AuthResponse;
import com.broids.projectadhr.R;
import com.broids.projectadhr.ui.ConfirmationActivity;
import com.broids.projectadhr.ui.HomeScreenActivity;
import com.broids.projectadhr.ui.LoginActivity;
import com.broids.projectadhr.utils.GsonSerializerUtil;

public class AadhaarAuthAsyncTask extends AsyncTask<String, Void, AuthResponse> {
	protected static final String CONTENT_HEADER_ACCEPT = "Accept";
	protected static final String CONTENT_TYPE_APPLICATION_JSON = "application/json";
	protected static final String CONTENT_HEADER_TYPE = "Content-Type";
	protected static final int CONNECTION_TIMEOUT = 10000;
	protected static final int SOCKET_TIMEOUT = 60000;
	protected ProgressDialog mDialog;
	protected Context mContext;

	AuthCaptureData authData;
	String mCallerType;
	public AadhaarAuthAsyncTask(Context context, AuthCaptureData authCaptureData, String caller) {
		this.authData = authCaptureData;
		this.mContext = context;
		mCallerType = caller;
	}

	@Override
	protected void onPreExecute() {
		Log.d("AadhaarDemo", " pre execute async");
		mDialog = new ProgressDialog(mContext);
		mDialog.setCancelable(true);
		mDialog.setMessage("Authenticating with server ..");
		mDialog.show();
	}

	@Override
	protected void onPostExecute(AuthResponse response) {
		if ((this.mDialog != null) && this.mDialog.isShowing()) {
			this.mDialog.dismiss();
		}
		/*
		 * AlertDialog alertDialog = new AlertDialog.Builder(ctx).create();
		 * alertDialog.setTitle("Status");
		 * 
		 * String msg = "Auth Status: " + (response.isSuccess() ? "success" :
		 * "fail") + (null != response.getReferenceCode() ? "\nRefcode: " +
		 * response.getReferenceCode() : ""); alertDialog.setMessage(msg);
		 * alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
		 * public void onClick(DialogInterface dialog, int which) {
		 * dialog.dismiss(); } });
		 * 
		 * alertDialog.show();
		 */

		if (response.isSuccess()) {
			if (mCallerType.equals("TYPE_DOSE")) {
				Toast.makeText(mContext,mContext.getString(R.string.thank_you_remember_to_take_your_next_dose_on_time_),
						Toast.LENGTH_LONG).show();
				
				Intent intent_logout = new Intent();
				intent_logout.setClass(mContext, LoginActivity.class);
				intent_logout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
				mContext.startActivity(intent_logout);
				
			}else if (mCallerType.equals("TYPE_TRANSACTION")) {
				Intent intent_logout = new Intent();
				intent_logout.setClass(mContext, ConfirmationActivity.class);
				intent_logout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
				mContext.startActivity(intent_logout);
			}
			
		} else {
			Toast.makeText(mContext,mContext.getString(R.string.authentication_failed),
					Toast.LENGTH_LONG).show();
		}
	}

	@Override
	protected AuthResponse doInBackground(String... params) {
		HttpParams httpparams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpparams,
				CONNECTION_TIMEOUT);
		HttpConnectionParams.setSoTimeout(httpparams, SOCKET_TIMEOUT);

		HttpClient httpclient = getHttpClient(httpparams);
		HttpPost httppost = new HttpPost(params[0]);
		httppost.setHeader(CONTENT_HEADER_TYPE, CONTENT_TYPE_APPLICATION_JSON);
		httppost.setHeader(CONTENT_HEADER_ACCEPT, CONTENT_TYPE_APPLICATION_JSON);

		try {
			StringEntity entity = new StringEntity(
					GsonSerializerUtil.marshall(authData));
			entity.setContentType(CONTENT_TYPE_APPLICATION_JSON);
			httppost.setEntity(entity);
		} catch (UnsupportedEncodingException e) {
			Log.e("DEMOAPP", "Error while communicating with the server", e);

		}

		try {
			HttpResponse response = httpclient.execute(httppost);
			String responseContent = EntityUtils.toString(response.getEntity());
			if (response != null
					&& response.getStatusLine().getStatusCode() == 200) {
				Log.d("RESPONSE", responseContent);
				return GsonSerializerUtil.unmarshal(responseContent,
						AuthResponse.class);
			} else {
				return buildErrorResponse();
			}
		} catch (Exception e) {
			Log.e("COMMUNICATION_ERROR",
					"Error while communicating with the server. Check connectivity.",
					e);
			return buildErrorResponse();
		}
	}

	private HttpClient getHttpClient(HttpParams params) {
		try {
			KeyStore trustStore = KeyStore.getInstance(KeyStore
					.getDefaultType());
			trustStore.load(null, null);

			SSLSocketFactory sf = new TrustAllCertsSSLSocketFactory(trustStore);
			sf.setHostnameVerifier(SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);

			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

			SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
			registry.register(new Scheme("https", sf, 443));

			ClientConnectionManager ccm = new ThreadSafeClientConnManager(
					params, registry);
			DefaultHttpClient client = new DefaultHttpClient(ccm, params);
			client.setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler(
					0, false));
			return client;
		} catch (Exception e) {
			return new DefaultHttpClient();
		}
	}

	private AuthResponse buildErrorResponse() {
		AuthResponse response = new AuthResponse();
		response.setSuccess(false);
		return response;
	}

	public static class TrustAllCertsSSLSocketFactory extends SSLSocketFactory {
		SSLContext sslContext = SSLContext.getInstance("TLS");

		public TrustAllCertsSSLSocketFactory(KeyStore truststore)
				throws NoSuchAlgorithmException, KeyManagementException,
				KeyStoreException, UnrecoverableKeyException {
			super(truststore);
			TrustManager tm = new X509TrustManager() {
				public void checkClientTrusted(X509Certificate[] chain,
						String authType) throws CertificateException {
				}

				public void checkServerTrusted(X509Certificate[] chain,
						String authType) throws CertificateException {
				}

				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			};
			sslContext.init(null, new TrustManager[] { tm }, null);
		}

		@Override
		public Socket createSocket(Socket socket, String host, int port,
				boolean autoClose) throws IOException, UnknownHostException {
			return sslContext.getSocketFactory().createSocket(socket, host,
					port, autoClose);
		}

		@Override
		public Socket createSocket() throws IOException {
			return sslContext.getSocketFactory().createSocket();
		}
	}
}