package ma.projet.arrosageintellegentv2.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ma.projet.arrosageintellegentv2.beans.SensorData;
import ma.projet.arrosageintellegentv2.networking.ApiClient;
import ma.projet.arrosageintellegentv2.networking.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<List<SensorData>> temperatureData;
    private final MutableLiveData<List<SensorData>> humidityData;

    private ScheduledExecutorService executorService;

    public HomeViewModel() {
        temperatureData = new MutableLiveData<>();
        humidityData = new MutableLiveData<>();

        executorService = Executors.newSingleThreadScheduledExecutor();

        fetchDataTemperature();
        fetchDataHumidity();
    }

    public LiveData<List<SensorData>> getTemperatureData() {
        return temperatureData;
    }

    public LiveData<List<SensorData>> getHumidityData() {
        return humidityData;
    }

    public void stopRealTimeUpdates() {
        // Shutdown the executor service
        executorService.shutdown();

        // Reinitialize the executor service if needed
        executorService = Executors.newSingleThreadScheduledExecutor();
    }

    private void fetchDataTemperature() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<List<SensorData>> temperatureCall = apiInterface.getSensorData();
        temperatureCall.enqueue(new Callback<List<SensorData>>() {
            @Override
            public void onResponse(Call<List<SensorData>> call, Response<List<SensorData>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    System.out.println("sss");
                    System.out.println(temperatureData);

                    temperatureData.setValue(response.body());
                } else {
                    // Handle errors
                }
            }

            @Override
            public void onFailure(Call<List<SensorData>> call, Throwable t) {
                // Handle errors
            }
        });
    }

    private void fetchDataHumidity() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<List<SensorData>> humidityCall = apiInterface.getSensorData();
        humidityCall.enqueue(new Callback<List<SensorData>>() {
            @Override
            public void onResponse(Call<List<SensorData>> call, Response<List<SensorData>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    humidityData.setValue(response.body());
                } else {
                    // Handle errors
                }
            }

            @Override
            public void onFailure(Call<List<SensorData>> call, Throwable t) {
            }
        });
    }

    public void startRealTimeUpdates() {
        executorService.scheduleAtFixedRate(this::fetchDataTemperature, 0, 10, TimeUnit.SECONDS);

        executorService.scheduleAtFixedRate(this::fetchDataHumidity, 0, 10, TimeUnit.SECONDS);
    }
}