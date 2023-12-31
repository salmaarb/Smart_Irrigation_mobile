package ma.projet.arrosageintellegentv2.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.time.LocalDateTime;

public class Grandeur implements Parcelable{
    private int id;
    private float temperature;
    private float humidity;
    private String dateTime;

    public Grandeur() {
    }
    private float value;

    // Ajoutez un constructeur et les autres méthodes nécessaires

    protected Grandeur(Parcel in) {
        value = in.readFloat();
        // Lisez les autres champs nécessaires depuis le Parcel
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(value);
        // Écrivez les autres champs dans le Parcel
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Grandeur> CREATOR = new Parcelable.Creator<Grandeur>() {
        @Override
        public Grandeur createFromParcel(Parcel in) {
            return new Grandeur(in);
        }

        @Override
        public Grandeur[] newArray(int size) {
            return new Grandeur[size];
        }
    };

    // Ajoutez les getters et setters nécessaires et d'autres méthodes si besoin


    public Grandeur(int id, float temperature, float humidity, String dateTime) {
        this.id = id;
        this.temperature = temperature;
        this.humidity = humidity;
        this.dateTime = dateTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public float getHumidity() {
        return humidity;
    }

    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return "Grandeur{" +
                "id=" + id +
                ", temperature=" + temperature +
                ", humidity=" + humidity +
                ", dateTime='" + dateTime + '\'' +
                '}';
    }
}
