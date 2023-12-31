package ma.projet.arrosageintellegentv2;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import ma.projet.arrosageintellegentv2.beans.SensorData;
import ma.projet.arrosageintellegentv2.networking.ApiClient;
import ma.projet.arrosageintellegentv2.networking.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationService extends Service {

    private static final String TAG = "NotificationService";
    private List<SensorData> temperatureData;
    private boolean hasChecked = false;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Service created");
     //   temperatureData = new ArrayList<>(); // Initialize temperatureData
      //  hasChecked = false;
      //  setInitialSize();
    }
/*
    private void setInitialSize() {
        if (temperatureData.isEmpty()) {
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

            Call<List<SensorData>> temperatureCall = apiInterface.getSensorData();
            temperatureCall.enqueue(new Callback<List<SensorData>>() {
                @Override
                public void onResponse(Call<List<SensorData>> call, Response<List<SensorData>> response) {
                    if (response.isSuccessful()) {
                        List<SensorData> newTemperatureData = response.body();
                        if (newTemperatureData != null) {
                            temperatureData.addAll(newTemperatureData);
                            Log.d(TAG, "Initial size set: " + temperatureData.size());
                        } else {
                            Log.d(TAG, "newTemperatureData is null");
                        }
                    } else {
                        Log.d(TAG, "Response not successful. Code: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<List<SensorData>> call, Throwable t) {
                    Log.e(TAG, "Failed to fetch temperature data", t);
                }
            });
        }
    }
*/
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Service started");

        // Implement background processing logic here
        checkDatabaseAndSendNotifications();

        // Return START_STICKY to ensure the service restarts if it's killed by the system
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Service destroyed");
    }

    private void checkDatabaseAndSendNotifications() {

            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

            Call<List<SensorData>> temperatureCall = apiInterface.getSensorData();
            temperatureCall.enqueue(new Callback<List<SensorData>>() {
                @Override
                public void onResponse(Call<List<SensorData>> call, Response<List<SensorData>> response) {
                    if (response.isSuccessful()) {
                        List<SensorData> newTemperatureData = response.body();

                        if (newTemperatureData != null) {
                            // Check if any new records with temperature > 90
                            for (SensorData newMesure : newTemperatureData) {
                                if ( newMesure.getTemperature() > 100) {
                                    makenotifications(newMesure.getTemperature());
                                    break;
                                }
                            }

                            // Update temperature data
                           // temperatureData.addAll(newTemperatureData);
                        } else {
                            Log.d(TAG, "newTemperatureData is null");
                        }
                    } else {
                        Log.d(TAG, "Response not successful. Code: " + response.code());
                    }

                //    hasChecked = true;
                    stopSelf();
                }

                @Override
                public void onFailure(Call<List<SensorData>> call, Throwable t) {
                    Log.e(TAG, "Failed to fetch temperature data", t);
                   // hasChecked = true;
                    stopSelf();
                }
            });
        }


    private boolean containsTemperatureData(SensorData newMesure) {
        if (temperatureData != null) {
            for (SensorData existingMesure : temperatureData) {
                // Compare based on unique ID
                if (existingMesure.getId() == newMesure.getId()) {
                    return true;
                }
            }
        }
        return false;
    }

    private void makenotifications(double temperature) {
        String channelID = "CHANNEL_ID_NOTIFICATION";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelID);
        builder.setSmallIcon(R.drawable.notif)
                .setContentTitle("Degree temperature")
                .setContentText("The temperature has exceedeed value "+ temperature)

                .setAutoCancel(true).setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = notificationManager.getNotificationChannel(channelID);
            if (notificationChannel == null) {
                int importance = NotificationManager.IMPORTANCE_HIGH;
                notificationChannel = new NotificationChannel(channelID, "Some description", importance);
                notificationChannel.setLightColor(Color.GREEN);
                notificationChannel.enableVibration(true);
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }

        notificationManager.notify(0, builder.build());
    }

    @Override
    public IBinder onBind(Intent intent) {
        // We don't need to bind to this service, so return null
        return null;
    }
}
