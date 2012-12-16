package com.rarnu.zoe.love2.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.rarnu.zoe.love2.R;
import com.rarnu.zoe.love2.TodoActivity;
import com.rarnu.zoe.love2.api.LovingYouApi;
import com.rarnu.zoe.love2.common.Consts;

public class NotificationUtils {

	public static void doHint(Context context, String hintStr) {
		LovingYouApi.saveLog(context, "RTC", "Notification");
		
		NotificationManager manager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		try {
			manager.cancel(Consts.NOTIFY_ID);
		} catch (Exception e) {

		}
		Notification n = new Notification(R.drawable.ic_small, context
				.getResources().getString(R.string.hint_task),
				System.currentTimeMillis());

		n.defaults |= Notification.DEFAULT_SOUND;
		n.defaults |= Notification.DEFAULT_LIGHTS;
		n.ledARGB = 0xff00ff00;
		n.ledOnMS = 300;
		n.ledOffMS = 1000;
		n.flags |= Notification.FLAG_SHOW_LIGHTS;

		Intent inMain = new Intent(context, TodoActivity.class);
		inMain.setAction(Consts.NOTIFY_ACTION);
		inMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		PendingIntent pMain = PendingIntent.getActivity(context, 0, inMain, 0);
		n.setLatestEventInfo(context,
				context.getResources().getString(R.string.todo_hint), hintStr,
				pMain);
		manager.notify(Consts.NOTIFY_ID, n);
	}
}
