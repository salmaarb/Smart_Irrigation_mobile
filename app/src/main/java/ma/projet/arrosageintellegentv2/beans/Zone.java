package ma.projet.arrosageintellegentv2.beans;

import android.app.Notification;

import java.io.Serializable;
import java.util.List;

public class Zone implements Serializable {
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Zone{" +
                "id=" + id +
                ", libelle='" + libelle + '\'' +
                ", superficie=" + superficie +
                ", image='" + image + '\'' +
                ", type=" + type +
                ", plantages=" + plantages +
                ", arrosages=" + arrosages +
                ", installations=" + installations +
                ", grandeurs=" + grandeurs +
                ", notifications=" + notifications +
                '}';
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public float getSuperficie() {
        return superficie;
    }

    public void setSuperficie(float superficie) {
        this.superficie = superficie;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public SolType getType() {
        return type;
    }

    public void setType(SolType type) {
        this.type = type;
    }

    public List<Plantage> getPlantages() {
        return plantages;
    }

    public void setPlantages(List<Plantage> plantages) {
        this.plantages = plantages;
    }

    public List<Arrosage> getArrosages() {
        return arrosages;
    }

    public void setArrosages(List<Arrosage> arrosages) {
        this.arrosages = arrosages;
    }

    public List<Installation> getInstallations() {
        return installations;
    }

    public void setInstallations(List<Installation> installations) {
        this.installations = installations;
    }

    public List<Grandeur> getGrandeurs() {
        return grandeurs;
    }

    public void setGrandeurs(List<Grandeur> grandeurs) {
        this.grandeurs = grandeurs;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }

    public Zone(long id, String libelle, float superficie, String image, SolType type, List<Plantage> plantages, List<Arrosage> arrosages, List<Installation> installations, List<Grandeur> grandeurs, List<Notification> notifications) {
        this.id = id;
        this.libelle = libelle;
        this.superficie = superficie;
        this.image = image;
        this.type = type;
        this.plantages = plantages;
        this.arrosages = arrosages;
        this.installations = installations;
        this.grandeurs = grandeurs;
        this.notifications = notifications;
    }

    private String libelle;
    private float superficie;
    private String image;
    private SolType type;
    private List<Plantage> plantages;
    private List<Arrosage> arrosages;
    private List<Installation> installations;
    private List<Grandeur> grandeurs;
    private List<Notification> notifications;
}
