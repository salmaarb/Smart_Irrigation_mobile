package ma.projet.arrosageintellegentv2.ui.home;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import ma.projet.arrosageintellegentv2.LoginActivity;
import ma.projet.arrosageintellegentv2.R;
import ma.projet.arrosageintellegentv2.beans.SensorData;
import ma.projet.arrosageintellegentv2.databinding.FragmentHomeBinding;
import ma.projet.arrosageintellegentv2.networking.ApiClient;
import ma.projet.arrosageintellegentv2.networking.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    int salma=0;

    private FragmentHomeBinding binding;
    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {




        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();



        // Initialiser le ViewModel
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        // Observer les changements dans les données de température
        homeViewModel.getTemperatureData().observe(getViewLifecycleOwner(), new Observer<List<SensorData>>() {
            @Override
            public void onChanged(List<SensorData> sensorData) {
                // Mettre à jour le graphique de température avec les nouvelles données
                updateGraph(binding.graphTemperature, sensorData);

            }
        });

        // Observer les changements dans les données d'humidité
        homeViewModel.getHumidityData().observe(getViewLifecycleOwner(), new Observer<List<SensorData>>() {
            @Override
            public void onChanged(List<SensorData> sensorData) {
                // Mettre à jour le graphique d'humidité avec les nouvelles données
                updateGraph1(binding.graphHumidity, sensorData);
            }
        });

        return root;
    }
    @Override
    public void onResume() {
        super.onResume();
        // Commencer les mises à jour en temps réel lorsque le fragment est visible
        homeViewModel.startRealTimeUpdates();

    }

    @Override
    public void onPause() {
        super.onPause();
        // Arrêter les mises à jour en temps réel lorsque le fragment n'est pas visible
        homeViewModel.stopRealTimeUpdates();
    }
    private void updateGraph(GraphView graph, List<SensorData> sensorData) {
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
        for (int i = 0; i < sensorData.size(); i++) {
            SensorData mesure = sensorData.get(i);
            // Utilisez le temps comme X, et la température comme Y
            series.appendData(new DataPoint(i, mesure.getTemperature()), true, sensorData.size());

         /*   if (mesure.getTemperature() > 90) {
                salma = 1;
            } else {
                salma = 2;
            }*/
        }

      /*   if (salma == 1) {
            makenotifications();
        }*/
        series.setColor(android.graphics.Color.RED);

        // Effacer les séries existantes et ajouter la nouvelle série
        graph.removeAllSeries();
        graph.addSeries(series);
        // Personnaliser l'apparence des graphiques si nécessaire
        customizeGraph(graph);
    }
    public void makenotifications(){
        String channelID="CHANNEL_ID_NOTIFICATION";
        NotificationCompat.Builder builder=new NotificationCompat.Builder(requireContext(),channelID);
        builder.setSmallIcon(R.drawable.notif)
                .setContentTitle("Degree temperature")
                .setContentText("the temperature has increased 90")
                .setAutoCancel(true).setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManager notificationManager = (NotificationManager) requireContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
            NotificationChannel notificationChannel=notificationManager.getNotificationChannel(channelID);
            if(notificationChannel==null){
                int importanace =NotificationManager.IMPORTANCE_HIGH;
                notificationChannel=new NotificationChannel(channelID,"some description",importanace);
                notificationChannel.setLightColor(Color.GREEN);
                notificationChannel.enableVibration(true);
                notificationManager.createNotificationChannel(notificationChannel);
            }


        }

        notificationManager.notify(0,builder.build());



    }
    private void updateGraph1(GraphView graph, List<SensorData> sensorData) {
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
        for (int i = 0; i < sensorData.size(); i++) {
            SensorData mesure = sensorData.get(i);
            // Utilisez le temps comme X, et la température comme Y
            series.appendData(new DataPoint(i, mesure.getHumidity()), true, sensorData.size());
        }
        // Effacer les séries existantes et ajouter la nouvelle série
       graph.removeAllSeries();
        graph.addSeries(series);

        // Personnaliser l'apparence des graphiques si nécessaire
        customizeGraph1(graph);
    } private void customizeGraph1(GraphView graph) {
        // Personnaliser l'apparence des graphiques si nécessaire
        // ...
        // Par exemple, personnalisez les labels de l'axe X pour afficher l'heure au format HH:mm:ss
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter());
        graph.setTitle("graphe humidite");
        graph.setTitleColor(Color.BLUE);
    }
    private void customizeGraph(GraphView graph) {
        // Personnaliser l'apparence des graphiques si nécessaire
        // ...
        // Par exemple, personnalisez les labels de l'axe X pour afficher l'heure au format HH:mm:ss
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter());
   graph.setTitle("graphe temperature");
        graph.setTitleColor(Color.RED);

    }

    private class DateAsXAxisLabelFormatter extends DefaultLabelFormatter {
        private final SimpleDateFormat dateFormat;

        public DateAsXAxisLabelFormatter() {
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

            Call<List<SensorData>> humidityCall = apiInterface.getSensorData();
            humidityCall.enqueue(new Callback<List<SensorData>>() {
                @Override
                public void onResponse(Call<List<SensorData>> call, Response<List<SensorData>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        for (int i = 0; i < response.body().size(); i++) {
                            List<SensorData> mesure = response.body();
                            // Utilisez le temps comme X, et la température comme Y
                        System.out.print(mesure.get(2));

                        }
                    } else {
                        // Handle errors
                    }
                }

                @Override
                public void onFailure(Call<List<SensorData>> call, Throwable t) {
                    // Handle errors
                }
            });
            dateFormat = new SimpleDateFormat("HH:mm:ss", Locale.FRENCH);
        }

        @Override
        public String formatLabel(double value, boolean isValueX) {
            if (isValueX) {
                return dateFormat.format(new Date((long) value));
            } else {
                return super.formatLabel(value, isValueX);
            }
        }
    }
}
