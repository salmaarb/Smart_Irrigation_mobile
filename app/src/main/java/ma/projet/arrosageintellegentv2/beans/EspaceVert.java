package ma.projet.arrosageintellegentv2.beans;

import java.util.List;

public class EspaceVert {
    private long id;
    private String libelle;
    private String image;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "EspaceVert{" +
                "id=" + id +
                ", libelle='" + libelle + '\'' +
                ", image='" + image + '\'' +
                ", zones=" + zones +
                '}';
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<Zone> getZones() {
        return zones;
    }

    public void setZones(List<Zone> zones) {
        this.zones = zones;
    }

    public EspaceVert(long id, String libelle, String image, List<Zone> zones) {
        this.id = id;
        this.libelle = libelle;
        this.image = image;
        this.zones = zones;
    }

    private List<Zone> zones;
}
