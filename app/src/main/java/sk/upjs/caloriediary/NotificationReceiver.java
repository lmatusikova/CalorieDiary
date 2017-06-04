package sk.upjs.caloriediary;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * co to robi
 */
public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service = new Intent(context, NotificationAlarmService.class);
        context.startService(service);
    }
}
