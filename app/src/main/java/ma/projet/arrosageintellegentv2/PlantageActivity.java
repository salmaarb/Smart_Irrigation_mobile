package ma.projet.arrosageintellegentv2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import ma.projet.arrosageintellegentv2.adapters.PlantageAdapter;
import ma.projet.arrosageintellegentv2.beans.Plantage;
import ma.projet.arrosageintellegentv2.ui.espacevert.EspacevertViewModel;

public class PlantageActivity  extends AppCompatActivity {
    private static final String TAG = "PlantageActivity";
    private long espace_id;
    private long zone_id;
    private ImageView id__photo;

    private ListView plantageList;
    private List<Plantage> plantages=new ArrayList<>();
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
        setContentView(R.layout.activity_plantage);
        plantageList=findViewById(R.id.plantageList);

        Intent i = getIntent();
        espace_id =  i.getLongExtra("espace_id", -1);
        zone_id = i.getLongExtra("zone_id", -1);
        Log.i("dzdzdz", String.valueOf(espace_id));
        Log.i("dzdzdz", String.valueOf(zone_id));
        List<Plantage> plantages = EspacevertViewModel.getZoneDetails(espace_id, zone_id).getPlantages();
        int xmlFile = R.layout.activity_detail_plantage;
        PlantageAdapter adapter = new PlantageAdapter(PlantageActivity.this, xmlFile, plantages);
        plantageList.setAdapter(adapter);


        plantageList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
               OpenZonesActivity(espace_id,zone_id);
                //  OpenZonesActivity(zone.);
            }

        });
    }
    private void OpenZonesActivity(long espace_id,long zone_id) {
        Log.i(TAG, "OpenZonesActivity: id = " + zone_id);
        Intent intent = new Intent(this, DetailPlantageActivity.class);
        intent.putExtra("espace_id",espace_id);
        intent.putExtra("zone_id",zone_id);
        System.out.println("sss");
        startActivity(intent);

    }

}