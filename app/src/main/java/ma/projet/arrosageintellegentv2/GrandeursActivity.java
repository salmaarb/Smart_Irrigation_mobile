package ma.projet.arrosageintellegentv2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import ma.projet.arrosageintellegentv2.adapters.ArosageAdapter;
import ma.projet.arrosageintellegentv2.adapters.GrandeurAdapter;
import ma.projet.arrosageintellegentv2.beans.Arrosage;
import ma.projet.arrosageintellegentv2.beans.Grandeur;
import ma.projet.arrosageintellegentv2.ui.espacevert.EspacevertViewModel;

public class GrandeursActivity extends AppCompatActivity {
    private static final String TAG = "GrandeursActivity";
    private long espace_id;
    private long zone_id;
    private ListView GrandeurList;
    private List<Grandeur> grandeurs=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grandeurs);
        GrandeurList=findViewById(R.id.GrandeurList);

        Intent i = getIntent();
        espace_id =  i.getLongExtra("espace_id", -1);
        zone_id = i.getLongExtra("zone_id", -1);
        Log.i("dzdzdz", String.valueOf(espace_id));
        Log.i("dzdzdz", String.valueOf(zone_id));
       List<Grandeur> grandeurs = EspacevertViewModel.getZoneDetails(espace_id, zone_id).getGrandeurs();
        int xmlFile = R.layout.iteam_grandeur;
        GrandeurAdapter adapter = new GrandeurAdapter(GrandeursActivity.this, xmlFile, grandeurs);
        GrandeurList.setAdapter(adapter);
    }
}