package ma.projet.arrosageintellegentv2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.util.List;

import ma.projet.arrosageintellegentv2.beans.Plantage;
import ma.projet.arrosageintellegentv2.ui.espacevert.EspacevertViewModel;

public class DetailPlantageActivity extends AppCompatActivity {
    private long espace_id;
    private long zone_id;
    private ImageView id___image;
    private TextView id___libelle;
    private TextView id___racine;
    private TextView id___type;
    private TextView id___idf;
    private static final String TAG = "DetailPlantageActivity";
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_detail_plantage);
        Intent i = getIntent();
        espace_id = i.getLongExtra("espace_id", -1);
        zone_id = i.getLongExtra("zone_id", -1);
        Log.i(TAG, String.valueOf(espace_id));
        Log.i(TAG, String.valueOf(zone_id));
        System.out.println("ssss1");
        id___idf = findViewById(R.id.id___idf);
        id___libelle = findViewById(R.id.id___libelle);
        id___racine = findViewById(R.id.id___racine);
        id___type = findViewById(R.id.id___type);
        id___image = findViewById(R.id.id___image);
      List<Plantage> plantages =  EspacevertViewModel.getZoneDetails(espace_id, zone_id).getPlantages();
        System.out.println(plantages);
      Log.i(TAG, EspacevertViewModel.getZoneDetails(espace_id, zone_id).toString());
        id___idf.setText(plantages.get(0).getPlante().getId()+"");
       id___libelle.setText("Nom : " +plantages.get(0).getPlante().getLibelle());
        id___racine.setText("Racine : " +plantages.get(0).getPlante().getRacine());
        System.out.println(plantages.get(0).getPlante().getRacine());
        id___type.setText(" type : vegetables");
        Glide.with(this)
                .load(plantages.get(0).getPlante().getImage()) // image url
                .placeholder(R.drawable.ic_launcher_background) // any placeholder to load at start
                .error(R.drawable.ic_launcher_background)  // any image in case of error
                .override(200, 200) // resizing
                .centerCrop()
                .into(id___image);

    }
}