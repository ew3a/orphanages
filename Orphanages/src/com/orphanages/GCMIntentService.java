package com.orphanages;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;
import com.orphanages.objects.JSONParser;
import com.orphanages.objects.Public;

public class GCMIntentService extends GCMBaseIntentService {

	public static int notificationId = 1;
	public static boolean notificationReceived;

	public GCMIntentService() {
		super("254179858176");
	}

	@Override
	protected void onError(Context context, String regId) {
		// TODO Auto-generated method stub
		Log.e("", "error registration id : " + regId);
	}

	@Override
	protected void onMessage(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Log.i("Message", "GCM received message");
		try {
			handleMessage(context, intent);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void onRegistered(Context context, String regId) {
		// TODO Auto-generated method stub
		// Log.e("", "registration id : "+regId);
		handleRegistration(context, regId);
	}

	@Override
	protected void onUnregistered(Context context, String regId) {
		// TODO Auto-generated method stub

	}

	private void handleMessage(Context context, Intent intent)
			throws IOException, ClassNotFoundException {
		// TODO Auto-generated method stub

		String notiType = intent.getStringExtra("type");

		long when = System.currentTimeMillis(); // notification time

		Notification notification = null;

		Uri defaultSound = RingtoneManager
				.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

		if ((notiType).equals("friendRequest")) {
			// friend request
			String nameOfRequestor = intent.getStringExtra("nameOfRequestor");

			Intent yesReceive = new Intent();
			yesReceive.setAction("accept");
			yesReceive.putExtra("friendName", nameOfRequestor);
			yesReceive.putExtra("type", intent.getStringExtra("index"));

//			Intent intent1 = new Intent(context,
//					AcceptOrRejectFriendRequest.class);
//			intent1.putExtra("friendName", nameOfRequestor);
//			intent1.putExtra("type", intent.getStringExtra("index"));
//			
//			PendingIntent pintent1 = PendingIntent.getActivity(context,
//					0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
//
//			Intent notificationIntent1 = new Intent(context,
//					NotificationServiceForAccept.class);
//			notificationIntent1.putExtra("friendName", nameOfRequestor);
//			notificationIntent1
//					.putExtra("type", intent.getStringExtra("index"));
//
//			PendingIntent pendingIntent1 = PendingIntent.getService(context, 0,
//					notificationIntent1, PendingIntent.FLAG_UPDATE_CURRENT);
//
//			Intent notificationIntent2 = new Intent(context,
//					NotificationServiceForReject.class);

//			PendingIntent pendingIntent2 = PendingIntent.getService(context, 0,
//					notificationIntent2, PendingIntent.FLAG_UPDATE_CURRENT);

			NotificationManager notificationManager = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);

			PowerManager pm = (PowerManager) context
					.getSystemService(Context.POWER_SERVICE);
			WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
					| PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
			wl.acquire();

//			Notification notification1 = new NotificationCompat.Builder(context)
//					.setContentTitle(nameOfRequestor)
//					.setContentText(
//							"You have a friend request from " + nameOfRequestor)
//					.setSubText("Click to respond..")
//					.setContentIntent(pintent1)
//					.setSmallIcon(R.drawable.ic_launcher)
//					.setLights(Color.YELLOW, 3000, 1000).setWhen(when)
//					.setAutoCancel(true).setNumber(notificationId)
//					.setSound(defaultSound)
//					.setVibrate(new long[] { 100L, 100L, 200L, 500L })
//					.addAction(R.drawable.right, "Accept", pendingIntent1)
//					.addAction(R.drawable.wrong, "Reject", pendingIntent2)
//					.build();

//			notificationManager.notify(0, notification1);
//			notificationReceived = true;
//			notification1.defaults |= Notification.DEFAULT_SOUND;
//			notification1.defaults |= Notification.DEFAULT_VIBRATE;
//			// notification.ledARGB = 0xff00ff00;
//			notification1.ledARGB = 0xffffff00;
//			notification1.ledOnMS = 6000;
//			notification1.ledOffMS = 1000;
//			notification1.flags |= Notification.FLAG_SHOW_LIGHTS;
//			notification1.vibrate = new long[] { 100L, 100L, 200L, 500L };
//			wl.release();

		}

		else if ((notiType).equals("friendRequestResponse")) {
			// friend response
			String nameOfResponder = intent.getStringExtra("nameOfResponder");
			String message = intent.getStringExtra("message");

			Intent notificationIntent1 = new Intent(context, getClass());
			PendingIntent pendingIntent1 = PendingIntent.getActivity(context,
					0, notificationIntent1, PendingIntent.FLAG_UPDATE_CURRENT);

			NotificationManager notificationManager = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);

			PowerManager pm = (PowerManager) context
					.getSystemService(Context.POWER_SERVICE);
			WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
					| PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
			wl.acquire();

			notification = new NotificationCompat.Builder(context)
					.setContentTitle(nameOfResponder).setContentText(message)
					.setContentIntent(pendingIntent1)
					.setSmallIcon(R.drawable.ic_launcher).setAutoCancel(true)
					.setLights(Color.YELLOW, 3000, 1000).setWhen(when)
					.setNumber(notificationId).setSound(defaultSound)
					.setVibrate(new long[] { 100L, 100L, 200L, 500L }).build();

			notificationManager.notify(++notificationId, notification);
			notificationReceived = true;
			notification.defaults |= Notification.DEFAULT_SOUND;
			notification.defaults |= Notification.DEFAULT_VIBRATE;
			// notification.ledARGB = 0xff00ff00;
			notification.ledARGB = 0xffffff00;
			notification.ledOnMS = 6000;
			notification.ledOffMS = 1000;
			notification.flags |= Notification.FLAG_SHOW_LIGHTS;
			notification.vibrate = new long[] { 100L, 100L, 200L, 500L };
			wl.release();

		}

		else {
			if ((notiType).equals("eventRequest")) {
				String eventname = intent.getStringExtra("eventname");
				String admin = intent.getStringExtra("admin");
				String eventtype = intent.getStringExtra("eventType");
				String date = intent.getStringExtra("when");
				String location = intent.getStringExtra("location");
				String place = intent.getStringExtra("place");
				String address = intent.getStringExtra("address");
				String id = intent.getStringExtra("eventid");
				
				String title = "";
				if (eventtype.equals("2")) {
					title = "a Meeting Event";
				} else {
					if (eventtype.equals("3")) {
						title = "an on-the-spot meeting Event";

					} else {
						title = "an outing Event";

					}
				}
				String message = admin + " has invited you to " + eventname;

//				Intent notificationIntent = new Intent(context,
//						EventDetailsFromNotification.class);
//				// set intent so it does not start a new activity
//				notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
//						| Intent.FLAG_ACTIVITY_SINGLE_TOP);
//				PendingIntent intentt = PendingIntent.getActivity(context, 0,
//						notificationIntent, 0);
//				notification.setLatestEventInfo(context, title, message,
//						intentt);

			}
		}
	}

	private void handleRegistration(Context context, String regId) {
//		new Public(context);
//		// TODO Auto-generated method stub
//		Public.regid = regId;
		putStringInSharedPreference("regid", regId);
		new UpdateRegid().execute();

	}

	public void putStringInSharedPreference(String tag, String value) {
		getSharedPreferences("eplanner", MODE_PRIVATE).edit()
				.putString(tag, value).commit();
	}

	private class UpdateRegid extends AsyncTask<String, Void, String> {
		protected String doInBackground(String... s) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("username", getSharedPreferences(
					"eplanner", Context.MODE_PRIVATE).getString("name", "")));
			params.add(new BasicNameValuePair("regid", getSharedPreferences(
					"eplanner", Context.MODE_PRIVATE).getString("regid", "")));
			JSONObject json = new JSONParser().getJSONFromUrl(Public.url
					+ "regid.php", params);
			try {
				int success = json.getInt("success");
				if (success == 1) {
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
	}
}
