package ma.projet.arrosageintellegentv2.ui.home;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import ma.projet.arrosageintellegentv2.R;
import ma.projet.arrosageintellegentv2.beans.SensorData;
import ma.projet.arrosageintellegentv2.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        homeViewModel.getTemperatureData().observe(getViewLifecycleOwner(), new Observer<List<SensorData>>() {
            @Override
            public void onChanged(List<SensorData> sensorData) {
                updateGraph(binding.graphTemperature, sensorData);
            }
        });

        homeViewModel.getHumidityData().observe(getViewLifecycleOwner(), new Observer<List<SensorData>>() {
            @Override
            public void onChanged(List<SensorData> sensorData) {
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
        homeViewModel.stopRealTimeUpdates();
    }
    private void updateGraph(GraphView graph, List<SensorData> sensorData) {
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
        for (int i = 0; i < sensorData.size(); i++) {
            SensorData mesure = sensorData.get(i);
            System.out.println(mesure.getDate());

            series.appendData(new DataPoint(i, mesure.getTemperature()), true, sensorData.size());

        }
        series.setColor(Color.RED);

        graph.removeAllSeries();
        graph.addSeries(series);
        customizeGraph(graph);
    }
    private void updateGraph1(GraphView graph, List<SensorData> sensorData) {
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();

        for (int i = 0; i < sensorData.size(); i++) {
            SensorData mesure = sensorData.get(i);
            series.appendData(new DataPoint(i, mesure.getHumidity()), true, sensorData.size());

        }
        graph.removeAllSeries();
        graph.addSeries(series);
        customizeGraph1(graph);
    }
    private void customizeGraph(GraphView graph) {
        graph.setTitle("Graph Temperature");

        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter());
    }
    private void customizeGraph1(GraphView graph) {

        graph.setTitle("Graph Humidity");

        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter());
    }


    private class DateAsXAxisLabelFormatter extends DefaultLabelFormatter {
        private final SimpleDateFormat dateFormat;

        public DateAsXAxisLabelFormatter() {
            dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss 'GMT' yyyy", Locale.getDefault());
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

