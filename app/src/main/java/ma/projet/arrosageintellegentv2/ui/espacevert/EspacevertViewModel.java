package ma.projet.arrosageintellegentv2.ui.espacevert;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import ma.projet.arrosageintellegentv2.beans.Arrosage;
import ma.projet.arrosageintellegentv2.beans.EspaceVert;
import ma.projet.arrosageintellegentv2.beans.Grandeur;
import ma.projet.arrosageintellegentv2.beans.Installation;
import ma.projet.arrosageintellegentv2.beans.Plantage;
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


    void init() {
        sp = getApplication().getSharedPreferences("login_data", Context.MODE_PRIVATE);

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        String loginData = sp.getString("login_data", "");
        try {
            // Supposez que loginData est au format JSON, ajustez-le en fonction de votre structure
            JSONObject userData = new JSONObject(loginData);

            // Récupérez les informations nécessaires du JSON

            String name = userData.getString("username");


        Call<List<EspaceVert>> call = apiInterface.getespace(name);

        call.enqueue(new Callback<List<EspaceVert>>() {
            @Override
            public void onResponse(Call call, Response response) {
                //Log.d(TAG, "onResponse: " + response);
                mEspaceVert.setValue((List<EspaceVert>) response.body());
                Log.d(TAG, "getValeu(à: " + mEspaceVert.getValue());
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getLocalizedMessage());
            }
        });
    } catch (JSONException e) {
        Log.e(TAG, "Error parsing login data JSON", e);
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
