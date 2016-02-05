package com.example.sample;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

public class Bootalarm extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		// For our recurring task, we'll just display a message

		SharedPreferences sharedpreferences = context.getSharedPreferences("time", Context.MODE_PRIVATE);
		String timeforalarm = sharedpreferences.getString("time for alarm", null);
		Calendar cal = Calendar.getInstance();

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date d1 = null;
		try {
			d1 = (Date) formatter.parse(timeforalarm);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cal.setTime(d1);

		AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
		Intent intents = new Intent(context, AlarmReceiver.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intents, PendingIntent.FLAG_UPDATE_CURRENT);

		alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);

		 

	}

}
