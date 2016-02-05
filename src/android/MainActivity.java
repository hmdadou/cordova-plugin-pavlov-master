package com.example.sample;

import java.text.DateFormat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.apache.cordova.DroidGap;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

public class MainActivity extends DroidGap {
	private PendingIntent pendingIntent;

	String SENDER_ID = "1096194542840";
	AsyncTask<Void, Void, Void> mRegisterTask;

	@SuppressLint("NewApi")
	public void getnoti() {

		generateNotification(getApplicationContext(), "Alarm Notification",
				"your alram run");

		PowerManager pm = (PowerManager) getApplicationContext()
				.getSystemService(Context.POWER_SERVICE);

		WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
				| PowerManager.ACQUIRE_CAUSES_WAKEUP
				| PowerManager.ON_AFTER_RELEASE, "MyLock");

		wl.acquire(10000);
		WakeLock wl_cpu = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
				"MyCpuLock");

		wl_cpu.acquire(10000);

	}

	public void getstartalarm(String value) {

		Intent alarmIntent = new Intent(MainActivity.this, AlarmReceiver.class);
		pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),
				12345, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
		String[] h1 = value.split(":");

		int hour = Integer.parseInt(h1[0]);
		int minute = Integer.parseInt(h1[1]);
		int second = Integer.parseInt(h1[2]);

		int temp;
		temp = second + (60 * minute) + (3600 * hour);
		start(temp);

	}

	@SuppressLint("NewApi")
	private void generateNotification(Context context, String name,
			String message) {
		Uri alarmSound = RingtoneManager
				.getDefaultUri(RingtoneManager.TYPE_ALARM);

		// Creates an explicit intent for an Activity in your app
		Intent resultIntent = new Intent(context, MainActivity.class);

		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		// Adds the back stack for the Intent (but not the Intent itself)
		stackBuilder.addParentStack(MainActivity.class);
		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(resultIntent);

		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(123,
				PendingIntent.FLAG_CANCEL_CURRENT);
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				context)
				// .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
				// .setLights(Color.YELLOW, 3000, 3000)
				.setSmallIcon(R.drawable.ic_launcher).setContentTitle(name)
				.setDefaults(Notification.DEFAULT_LIGHTS).setSound(alarmSound)
				.setContentText(message);

		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(123, mBuilder.build());
	}

	public void start(int interval) {

		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
		PendingIntent pendingIntent = PendingIntent
				.getBroadcast(MainActivity.this, 0, intent,
						PendingIntent.FLAG_UPDATE_CURRENT);

		Toast.makeText(getApplicationContext(),
				"alarm setup will launch in " + interval + " second", 0).show();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.SECOND, interval);
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
				pendingIntent);
		SharedPreferences sharedpreferences = getSharedPreferences("time",
				Context.MODE_PRIVATE);
		Editor editor = sharedpreferences.edit();
		editor.putString("time for alarm", dateFormat.format(cal.getTime()));
		editor.putInt("difference", interval);
		editor.commit();

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.setBooleanProperty("showTitle", true);
		super.onCreate(savedInstanceState);
		super.init();
		super.loadUrl("file:///android_asset/www/index.html");
		appView.addJavascriptInterface(this, "MyCls");

	}
}
