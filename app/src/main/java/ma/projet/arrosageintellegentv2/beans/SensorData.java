package ma.projet.arrosageintellegentv2.beans;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class SensorData {
    @SerializedName("id")
    private long id;

    @SerializedName("temperature")
    private double temperature;

    @SerializedName("humidite")
    private double humidity;

    @SerializedName("date")
    private Date date;

    // Ajoutez les getters et les setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
