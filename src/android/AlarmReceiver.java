package com.example.sample;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

	@SuppressLint("NewApi")
	@Override
	public void onReceive(Context context, Intent intent) {

		// For our recurring task, we'll just display a message
		Toast.makeText(context, "Hello this is alaram", Toast.LENGTH_SHORT)
				.show();

		SharedPreferences sharedpreferences = context.getSharedPreferences(
				"time", Context.MODE_PRIVATE);
		int interval = sharedpreferences.getInt("difference", -1);

		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(context.ALARM_SERVICE);
		Intent intents = new Intent(context, AlarmReceiver.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
				intents, PendingIntent.FLAG_UPDATE_CURRENT);

		Toast.makeText(context,
				"alarm will again launch in " + interval + " second", 0).show();

		// Getting current time and add the seconds in it
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.SECOND, interval);
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

		alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
				pendingIntent);

		Editor editor = sharedpreferences.edit();
		editor.putString("time for alarm", dateFormat.format(cal.getTime()));
		editor.putInt("difference", interval);
		editor.commit();

		generateNotification(context, "Alarm Notification", "your alram run");

		PowerManager pm = (PowerManager) context
				.getSystemService(Context.POWER_SERVICE);

		WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
				| PowerManager.ACQUIRE_CAUSES_WAKEUP
				| PowerManager.ON_AFTER_RELEASE, "MyLock");

		wl.acquire(10000);
		WakeLock wl_cpu = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
				"MyCpuLock");

		wl_cpu.acquire(10000);

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

}
