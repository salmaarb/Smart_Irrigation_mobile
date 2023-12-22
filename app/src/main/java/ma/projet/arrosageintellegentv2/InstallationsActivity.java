package ma.projet.arrosageintellegentv2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_installations);
        InstallationList=findViewById(R.id.InstallationList);

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