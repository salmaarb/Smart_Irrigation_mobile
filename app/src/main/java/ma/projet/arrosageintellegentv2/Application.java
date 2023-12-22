package ma.projet.arrosageintellegentv2;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        scheduleAlarm();
    }

    private void scheduleAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        // Modify this line to use PendingIntent.getService
        Intent alarmIntent = new Intent(this, NotificationService.class);
        PendingIntent pendingIntent = PendingIntent.getService(
                this,
                0,
                alarmIntent,
                PendingIntent.FLAG_IMMUTABLE // Use FLAG_UPDATE_CURRENT to update the PendingIntent
        );

        // Specify the interval in milliseconds (1 minute)
        long INTERVAL = 1 * 60 * 1000;

        // Cancel any existing alarms with the same PendingIntent
        alarmManager.cancel(pendingIntent);

        // Set the repeating alarm
        alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis(),
                INTERVAL,
                pendingIntent
        );
    }
}
