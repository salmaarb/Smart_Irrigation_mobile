package ma.projet.arrosageintellegentv2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.statistic, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                // Handle the Up button click
                onBackPressed();
                return true;

            case R.id.action_settings:
                // Handle Settings menu item click
                return true;

            // Add cases for other menu items as needed

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arrosage);
        arossageList=findViewById(R.id.arossageList);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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