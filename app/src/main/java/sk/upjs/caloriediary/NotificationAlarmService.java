package sk.upjs.caloriediary;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * co to robi
 */
public class NotificationAlarmService extends Service{

    private NotificationManager manager;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        manager = (NotificationManager) this.getApplicationContext().getSystemService(this.getApplicationContext().NOTIFICATION_SERVICE);
        Intent intent2 = new Intent(this.getApplicationContext(),MainActivity.class);
        intent2.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP| Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingNotificationIntent = PendingIntent.getActivity( this.getApplicationContext(),0, intent2,PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(NotificationAlarmService.this);
        builder.setContentIntent(pendingNotificationIntent)
                .setSmallIcon(R.drawable.apple2)
                .setContentTitle("Calorie Diary")
                .setContentText("Today you do not enter the data.");
        Notification notification = builder.getNotification();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        manager.notify(001, notification);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
