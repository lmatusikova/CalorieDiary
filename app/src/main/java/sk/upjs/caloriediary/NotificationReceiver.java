package sk.upjs.caloriediary;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.Calendar;

/**
 * https://karanbalkar.com/2013/07/tutorial-41-using-alarmmanager-and-broadcastreceiver-in-android/
 * when broadcast is detected, start service NotificationAlarmService
 */
public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent)
    {
       Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 14);
        calendar.set(Calendar.MINUTE, 22);
        calendar.set(Calendar.SECOND, 0);

        if(System.currentTimeMillis() == calendar.getTimeInMillis()) {
            Intent intent1 = new Intent(context, MainActivity.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent1, 0);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            builder.setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.apple2)
                    .setContentTitle("Calorie Diary")
                    .setContentText("Today you do not enter the data.");
            Notification notification = builder.build();
            notification.flags |= Notification.FLAG_AUTO_CANCEL;

            NotificationManager manager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
            manager.notify(0, notification);
        }
    }
}
