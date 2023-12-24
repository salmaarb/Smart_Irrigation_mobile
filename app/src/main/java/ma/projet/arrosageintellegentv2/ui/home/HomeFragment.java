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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

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
                updateGraph(binding.graphTemperature, generateRandomSensorData(sensorData.size()));
            }
        });

        homeViewModel.getHumidityData().observe(getViewLifecycleOwner(), new Observer<List<SensorData>>() {
            @Override
            public void onChanged(List<SensorData> sensorData) {
                updateGraph1(binding.graphHumidity, generateRandomSensorData(sensorData.size()));
            }
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        homeViewModel.startRealTimeUpdates();
    }

    @Override
    public void onPause() {
        super.onPause();
        homeViewModel.stopRealTimeUpdates();
    }

    private List<SensorData> generateRandomSensorData(int count) {
        List<SensorData> randomData = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < count; i++) {
            double randomTemperature = 20 + (random.nextDouble() * 10);
            double randomHumidity = 40 + (random.nextDouble() * 20);

            SensorData randomSensorData = new SensorData(randomTemperature, randomHumidity);
            randomData.add(randomSensorData);
        }

        return randomData;
    }

    private void updateGraph(GraphView graph, List<SensorData> sensorData) {
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
        for (int i = 0; i < sensorData.size(); i++) {
            SensorData measure = sensorData.get(i);
            series.appendData(new DataPoint(i, measure.getTemperature()), true, sensorData.size());
        }

        series.setColor(Color.RED);
        graph.removeAllSeries();
        graph.addSeries(series);
        customizeGraph(graph);
    }

    private void updateGraph1(GraphView graph, List<SensorData> sensorData) {
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
        for (int i = 0; i < sensorData.size(); i++) {
            SensorData measure = sensorData.get(i);
            series.appendData(new DataPoint(i, measure.getHumidity()), true, sensorData.size());
        }

        graph.removeAllSeries();
        graph.addSeries(series);
        customizeGraph1(graph);
    }

    private void customizeGraph1(GraphView graph) {
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter());
        graph.setTitle("Graph Humidity");
        graph.setTitleColor(Color.BLUE);
    }

    private void customizeGraph(GraphView graph) {
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter());
        graph.setTitle("Graph Temperature");
        graph.setTitleColor(Color.RED);
    }

    private class DateAsXAxisLabelFormatter extends DefaultLabelFormatter {
        private final List<Date> sequentialTimeValues;
        private final SimpleDateFormat dateFormat;

        public DateAsXAxisLabelFormatter() {
            sequentialTimeValues = generateSequentialTimeValues(10);
            dateFormat = new SimpleDateFormat("HH:mm:ss", Locale.FRENCH);
        }

        private List<Date> generateSequentialTimeValues(int count) {
            List<Date> timeValues = new ArrayList<>();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());

            for (int i = 0; i < count; i++) {
                calendar.add(Calendar.MINUTE, 1);
                timeValues.add(calendar.getTime());
            }
            return timeValues;
        }

        @Override
        public String formatLabel(double value, boolean isValueX){
            if (isValueX) {
                int index = (int) value % sequentialTimeValues.size();
                return dateFormat.format(sequentialTimeValues.get(index));
            } else {
                return super.formatLabel(value, isValueX);
            }
        }
    }


}