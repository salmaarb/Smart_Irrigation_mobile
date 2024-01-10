package ma.projet.arrosageintellegentv2.ui.espacevert;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ma.projet.arrosageintellegentv2.beans.Arrosage;
import ma.projet.arrosageintellegentv2.beans.Boitier;
import ma.projet.arrosageintellegentv2.beans.EspaceVert;
import ma.projet.arrosageintellegentv2.beans.Grandeur;
import ma.projet.arrosageintellegentv2.beans.Installation;
import ma.projet.arrosageintellegentv2.beans.Plantage;
import ma.projet.arrosageintellegentv2.beans.Plante;
import ma.projet.arrosageintellegentv2.beans.Zone;
import ma.projet.arrosageintellegentv2.networking.ApiClient;
import ma.projet.arrosageintellegentv2.networking.ApiInterface;
import ma.projet.arrosageintellegentv2.utils.Crendentials;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EspacevertViewModel extends AndroidViewModel {

    static MutableLiveData<List<EspaceVert>> mEspaceVert = new MutableLiveData<>();

    private static final String TAG = "EspaceVertViewModel";
    SharedPreferences sp;



    public EspacevertViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<EspaceVert>> getEspace() {
        return mEspaceVert;
    }
    String name = null;String role = null;

    void init() throws JSONException {
        sp = getApplication().getSharedPreferences("login_data", Context.MODE_PRIVATE);
        String loginData = sp.getString("login_data", "");

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        try {
            // Supposez que loginData est au format JSON, ajustez-le en fonction de votre structure
            JSONObject userData = new JSONObject(loginData);


            if (userData.has("user")) {
                JSONObject userObject = userData.getJSONObject("user");

                if (userObject.has("username")) {
                    name = userObject.getString("username");

                }
                if (userObject.has("role")) {
                    role = userObject.getString("role");

                }
            }
        Call<List<EspaceVert>> call = apiInterface.getespace(name);


        call.enqueue(new Callback<List<EspaceVert>>() {
            @Override
            public void onResponse(Call call, Response response) {
                //Log.d(TAG, "onResponse: " + response);
                if ("ADMIN".equalsIgnoreCase(role))
                {
                    try {
                        if (userData.has("espacesVerts")) {

                            try {

                                    JSONArray espacesVertsArray = userData.getJSONArray("espacesVerts");
System.out.println(espacesVertsArray);
                                List<EspaceVert> espaceVertList = new ArrayList<>();

                                for (int i = 0; i < espacesVertsArray.length(); i++) {
                                    JSONObject espaceVertObject = espacesVertsArray.getJSONObject(i);

                                    EspaceVert espaceVert = new EspaceVert();
                                    espaceVert.setId(espaceVertObject.getInt("id"));
                                    espaceVert.setLibelle(espaceVertObject.getString("libelle"));
                                    espaceVert.setImage(espaceVertObject.getString("image"));

                                    if (espaceVertObject.has("zones")) {
                                        JSONArray zonesArray = espaceVertObject.getJSONArray("zones");

                                        List<Zone> zoneList = new ArrayList<>();

                                        for (int j = 0; j < zonesArray.length(); j++) {
                                            JSONObject zoneObject = zonesArray.getJSONObject(j);

                                            Zone zone = new Zone();
                                            zone.setId(zoneObject.getInt("id"));
                                            zone.setLibelle(zoneObject.getString("libelle"));
                                            zone.setSuperficie((float) zoneObject.getDouble("superficie"));
                                            zone.setImage(zoneObject.getString("image"));

                                            // Parse and set Plantages
                                            if (zoneObject.has("plantages")) {
                                                JSONArray plantagesArray = zoneObject.getJSONArray("plantages");
                                                List<Plantage> plantageList = new ArrayList<>();

                                                for (int k = 0; k < plantagesArray.length(); k++) {
                                                    JSONObject plantageObject = plantagesArray.getJSONObject(k);

                                                    Plantage plantage = new Plantage();
                                                    plantage.setId(plantageObject.getInt("id"));
                                                    plantage.setDate(plantageObject.getString("date"));
                                                    plantage.setNombre(plantageObject.getInt("nombre"));

                                                    // Parse and set Plante
                                                    JSONObject planteObject = plantageObject.getJSONObject("plante");
                                                    Plante plante = new Plante();
                                                    plante.setId(planteObject.getInt("id"));
                                                    plante.setLibelle(planteObject.getString("libelle"));
                                                    plante.setImage(planteObject.getString("image"));
                                                    // Parse and set other properties for Plante

                                                    plantage.setPlante(plante);
                                                    plantageList.add(plantage);
                                                }

                                                zone.setPlantages(plantageList);
                                            }
                                            if (zoneObject.has("grandeurs")) {
                                                JSONArray grandeursArray = zoneObject.getJSONArray("grandeurs");
                                                List<Grandeur> grandeurList = new ArrayList<>();

                                                for (int k = 0; k < grandeursArray.length(); k++) {
                                                    JSONObject grandeurObject = grandeursArray.getJSONObject(k);

                                                    Grandeur grandeur = new Grandeur();
                                                    grandeur.setId(grandeurObject.getInt("id"));
                                                    grandeur.setTemperature((float) grandeurObject.getDouble("temperature"));
                                                    grandeur.setHumidity((float) grandeurObject.getDouble("humidity"));
                                                    grandeur.setDateTime(grandeurObject.getString("dateTime"));

                                                    // Add the grandeur to the list
                                                    grandeurList.add(grandeur);
                                                }

                                                zone.setGrandeurs(grandeurList);
                                            }
                                            if (zoneObject.has("installations")) {
                                                JSONArray installationsArray = zoneObject.getJSONArray("installations");
                                                List<Installation> installationList = new ArrayList<>();

                                                for (int k = 0; k < installationsArray.length(); k++) {
                                                    JSONObject installationObject = installationsArray.getJSONObject(k);

                                                    Installation installation = new Installation();
                                                    installation.setId(installationObject.getInt("id"));
                                                    installation.setDateDebut(installationObject.getString("dateDebut"));
                                                    installation.setDateFin(installationObject.optString("dateFin", null)); // Use optString to handle null values

                                                    // Parse and set Boitier
                                                    JSONObject boitierObject = installationObject.getJSONObject("boitier");
                                                    Boitier boitier = new Boitier();
                                                    boitier.setId(boitierObject.getInt("id"));
                                                    boitier.setRef(boitierObject.getString("ref"));
                                                    boitier.setType(boitierObject.getString("type"));
                                                    boitier.setCode(boitierObject.getString("code"));
                                                    boitier.setImage(boitierObject.getString("image"));
                                                    // Parse and set other properties for Boitier

                                                    installation.setBoitier(boitier);
                                                    installationList.add(installation);
                                                }

                                                zone.setInstallations(installationList);
                                            }
                                            if (zoneObject.has("arrosages")) {
                                                JSONArray arrosagesArray = zoneObject.getJSONArray("arrosages");
                                                List<Arrosage> arrosageList = new ArrayList<>();

                                                for (int k = 0; k < arrosagesArray.length(); k++) {
                                                    JSONObject arrosageObject = arrosagesArray.getJSONObject(k);

                                                    Arrosage arrosage = new Arrosage();
                                                    arrosage.setId(arrosageObject.getInt("id"));
                                                    arrosage.setDate(arrosageObject.getString("date"));
                                                    arrosage.setLitresEau((float) arrosageObject.getDouble("litresEau"));

                                                    arrosageList.add(arrosage);
                                                }

                                                zone.setArrosages(arrosageList);
                                            }

                                            zoneList.add(zone);
                                        }

                                        espaceVert.setZones(zoneList);
                                    }

                                    espaceVertList.add(espaceVert);
                                }
                                mEspaceVert.setValue((List<EspaceVert>) espaceVertList);


                            } catch (JSONException e) {
                                e.printStackTrace(); // Handle JSONException
                            }
                        }} catch (Exception e) {
                        throw new RuntimeException(e);
                    }


                    System.out.println("salmaa");
                    Log.d(TAG, "getValeu(à: " + mEspaceVert.getValue());

                }else
                {
                    mEspaceVert.setValue((List<EspaceVert>) response.body());
                    Log.d(TAG, "getValeu(à: " + mEspaceVert.getValue());

                }

            }

               @Override
            public void onFailure(Call call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getLocalizedMessage());
            }
        });
    } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Zone> getZonesByEspaceId(long espace_id) {
        for (EspaceVert e : mEspaceVert.getValue()) {
            if (e.getId() == espace_id) {
                return e.getZones();
            }
        }
        return null;
    }


    public static  Zone getZoneDetails(long espace_id,long zone_id) {
        for (Zone z : getZonesByEspaceId(espace_id)) {
            if (z.getId() == zone_id) {
                return z;
            }
        }
        return null;
    }
    public List<Installation> getZoneBoitier(long id){

        for(EspaceVert e: mEspaceVert.getValue()) {
            for(Zone z: e.getZones()) {
                if(z.getId() == id) {
                    return z.getInstallations();
                }
            }
        }

        return null;
    }
    public List<Plantage> getZonePlantes( long id){

        for(EspaceVert e: mEspaceVert.getValue()) {
            for(Zone z: e.getZones()) {
                if(z.getId() == id) {
                    return z.getPlantages();
                }
            }
        }

        return null;
    }
    public List<Grandeur> getZoneGrandeurs(long id){

        for(EspaceVert e: mEspaceVert.getValue()) {
            for(Zone z: e.getZones()) {
                if(z.getId() == id) {
                    return z.getGrandeurs();
                }
            }
        }

        return null;
    }
    public List<Arrosage> getZoneArrosage(long id){

        for(EspaceVert e: mEspaceVert.getValue()) {
            for(Zone z: e.getZones()) {
                if(z.getId() == id) {
                    return z.getArrosages();
                }
            }
        }

        return null;
    }



}
