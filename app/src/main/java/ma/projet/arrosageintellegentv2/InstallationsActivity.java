package ma.projet.arrosageintellegentv2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import ma.projet.arrosageintellegentv2.adapters.InstallationsAdapter;
import ma.projet.arrosageintellegentv2.beans.Installation;
import ma.projet.arrosageintellegentv2.ui.espacevert.EspacevertViewModel;

public class InstallationsActivity extends AppCompatActivity {
    private static final String TAG = "GrandeursActivity";
    private long espace_id;
    private long zone_id;
    private ListView InstallationList;
    private List<Installation> installations=new ArrayList<>();
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
        setContentView(R.layout.activity_installations);
        InstallationList=findViewById(R.id.InstallationList);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();
        espace_id =  i.getLongExtra("espace_id", -1);
        zone_id = i.getLongExtra("zone_id", -1);
        Log.i("dzdzdz", String.valueOf(espace_id));
        Log.i("dzdzdz", String.valueOf(zone_id));
        List<Installation> installations = EspacevertViewModel.getZoneDetails(espace_id, zone_id).getInstallations();
        Log.i("dzdzdz", String.valueOf(installations));
        int xmlFile = R.layout.iteam_installation;
        InstallationsAdapter adapter = new InstallationsAdapter(InstallationsActivity.this, xmlFile, installations);
        InstallationList.setAdapter(adapter);
    }
}