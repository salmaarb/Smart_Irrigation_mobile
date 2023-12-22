package ma.projet.arrosageintellegentv2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.time.zone.ZoneRulesException;
import java.util.ArrayList;
import java.util.List;

import ma.projet.arrosageintellegentv2.adapters.ArosageAdapter;
import ma.projet.arrosageintellegentv2.adapters.ZoneAdapter;
import ma.projet.arrosageintellegentv2.beans.Arrosage;
import ma.projet.arrosageintellegentv2.beans.Zone;
import ma.projet.arrosageintellegentv2.ui.espacevert.EspacevertViewModel;

public class ArrosageActivity extends AppCompatActivity {
    private static final String TAG = "ArrosageActivity";
    private long espace_id;
    private long zone_id;
    private ListView arossageList;
    private  List<Arrosage>arrosages=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arrosage);
        arossageList=findViewById(R.id.arossageList);

        Intent i = getIntent();

        espace_id =  i.getLongExtra("espace_id", -1);
        zone_id = i.getLongExtra("zone_id", -1);
        Log.i("dzdzdz", String.valueOf(espace_id));
        Log.i("dzdzdz", String.valueOf(zone_id));
       List<Arrosage> arrosages = EspacevertViewModel.getZoneDetails(espace_id, zone_id).getArrosages();
        int xmlFile = R.layout.iteam_arossage;
        ArosageAdapter adapter = new ArosageAdapter(ArrosageActivity.this, xmlFile, arrosages);
        arossageList.setAdapter(adapter);
    }
}