package ma.projet.arrosageintellegentv2;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ma.projet.arrosageintellegentv2.beans.Grandeur;
import ma.projet.arrosageintellegentv2.beans.SensorData;
import ma.projet.arrosageintellegentv2.beans.Zone;
import ma.projet.arrosageintellegentv2.networking.ApiClient;
import ma.projet.arrosageintellegentv2.networking.ApiInterface;
import ma.projet.arrosageintellegentv2.ui.espacevert.EspacevertViewModel;
import ma.projet.arrosageintellegentv2.ui.home.HomeFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GrandeursActivity extends AppCompatActivity {

    private static final String TAG = "GrandeursActivity";
    private long espace_id;
    private long zone_id;
    HomeFragment home;
    Spinner spinner;
    GraphView graphTemperature;
    GraphView graphHumidity;
    private final MutableLiveData<List<SensorData>> temperatureData = new MutableLiveData<>();
    private final MutableLiveData<List<SensorData>> humidityData = new MutableLiveData<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grandeurs);
        mHandler.postDelayed(mUpdateRunnable, UPDATE_INTERVAL);
        graphTemperature = findViewById(R.id.graphTemperature);
        graphHumidity = findViewById(R.id.graphHumidity);
        home = new HomeFragment();
        Intent i = getIntent();
        espace_id = i.getLongExtra("espace_id", -1);
        zone_id = i.getLongExtra("zone_id", -1);
        EspacevertViewModel viewModel = new ViewModelProvider(this).get(EspacevertViewModel.class);
        Zone zoneDetails = viewModel.getZoneDetails(espace_id, zone_id);
        if (zoneDetails != null) {
            List<Grandeur> grandeurs = EspacevertViewModel.getZoneDetails(espace_id, zone_id).getGrandeurs();
            if (grandeurs != null) {
                System.out.println(grandeurs);
                fetchDataTemperature();
                fetchDataHumidity();
                updateGraph(graphTemperature, temperatureData.getValue());
                updateGraph1(graphHumidity, humidityData.getValue());
            } else {
                Log.e(TAG, "Grandeurs list is null");
            }
        } else {
            Log.e(TAG, "ZoneDetails is null");
        }
    }
    private Handler mHandler = new Handler();
    private static final int UPDATE_INTERVAL = 10 * 1000; // 30 secondes

    private Runnable mUpdateRunnable = new Runnable() {
        @Override
        public void run() {
            // Répétez le code de mise à jour ici
            fetchDataTemperature();
            fetchDataHumidity();

            // Postez à nouveau le Runnable après l'intervalle d'actualisation
            mHandler.postDelayed(this, UPDATE_INTERVAL);
        }
    };


    private void fetchDataTemperature() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<List<SensorData>> temperatureCall = apiInterface.getSensorData(zone_id);
        temperatureCall.enqueue(new Callback<List<SensorData>>() {
            @Override
            public void onResponse(Call<List<SensorData>> call, Response<List<SensorData>> response) {
                if (response.isSuccessful() && response.body() != null) {

                    temperatureData.setValue(response.body());
                    // Update graph after fetching data
                    updateGraph(graphTemperature, temperatureData.getValue());
                } else {
                    Log.e(TAG, "Error fetching temperature data");
                }
            }

            @Override
            public void onFailure(Call<List<SensorData>> call, Throwable t) {
                Log.e(TAG, "Failed to fetch temperature data", t);
            }
        });
    }

    private void fetchDataHumidity() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<List<SensorData>> humidityCall = apiInterface.getSensorData(zone_id);
        humidityCall.enqueue(new Callback<List<SensorData>>() {
            @Override
            public void onResponse(Call<List<SensorData>> call, Response<List<SensorData>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    System.out.println(response.body());
                    humidityData.setValue(response.body());
                    // Update graph after fetching data
                    System.out.println(humidityData.getValue());
                    updateGraph1(graphHumidity, humidityData.getValue());

                } else {
                    Log.e(TAG, "Error fetching humidity data");
                }
            }

            @Override
            public void onFailure(Call<List<SensorData>> call, Throwable t) {
                Log.e(TAG, "Failed to fetch humidity data", t);
            }
        });
    }

    private void updateGraph(GraphView graph, List<SensorData> sensorData) {
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
        if (sensorData != null && !sensorData.isEmpty()) {
            for (int i = 0; i < sensorData.size(); i++) {
                SensorData mesure = sensorData.get(i);
                System.out.println("asefergt");
                System.out.println(mesure.getTemperature());
                series.appendData(new DataPoint(i, mesure.getTemperature()), true, sensorData.size());
            }
        }
        series.setColor(Color.RED);
        graph.removeAllSeries();
        graph.addSeries(series);
        customizeGraph(graph);
    }
    private void updateGraph1(GraphView graph, List<SensorData> sensorData) {
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
        if (sensorData != null && !sensorData.isEmpty()) {
            for (int i = 0; i < sensorData.size(); i++) {
                SensorData mesure = sensorData.get(i);
                System.out.println("asefergt");

                System.out.println(mesure.getHumidity());
                series.appendData(new DataPoint(i, mesure.getHumidity()), true, sensorData.size());
            }
        }
        series.setColor(Color.BLUE);
        graph.removeAllSeries();
        graph.addSeries(series);
        customizeGraph(graph);
    }

    private void customizeGraph(GraphView graph) {
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter());
    }

    private class DateAsXAxisLabelFormatter extends DefaultLabelFormatter {
        private final SimpleDateFormat dateFormat;

        public DateAsXAxisLabelFormatter() {
            dateFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
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
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Arrêtez le minuteur lorsqu'il n'est plus nécessaire
        mHandler.removeCallbacks(mUpdateRunnable);
    }

}
