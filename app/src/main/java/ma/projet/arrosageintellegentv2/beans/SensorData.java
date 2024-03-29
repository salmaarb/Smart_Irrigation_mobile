package ma.projet.arrosageintellegentv2.beans;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class SensorData {
    @SerializedName("id")
    private long id;

    @SerializedName("temperature")
    private double temperature;

    @SerializedName("humidity")
    private double humidity;

    @SerializedName("dateTime")
    private Date date;

    public SensorData(double temperature, double humidity) {
        this.temperature = temperature;
        this.humidity = humidity;
    }

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
