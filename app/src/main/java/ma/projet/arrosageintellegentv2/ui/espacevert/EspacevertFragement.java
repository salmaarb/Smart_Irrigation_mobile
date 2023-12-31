package ma.projet.arrosageintellegentv2.ui.espacevert;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import ma.projet.arrosageintellegentv2.R;
import ma.projet.arrosageintellegentv2.StatisticActivity;
import ma.projet.arrosageintellegentv2.ZoneActivity;
import ma.projet.arrosageintellegentv2.adapters.EspaceVertAdapter;
import ma.projet.arrosageintellegentv2.beans.EspaceVert;
import ma.projet.arrosageintellegentv2.databinding.FragmentEspacevertBinding;

public class EspacevertFragement extends Fragment {

    private static final String TAG = "EsapcevertFragment";
    private FragmentEspacevertBinding binding;
    private ListView list;
    SharedPreferences sp;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        sp = requireActivity().getSharedPreferences("login_data", Context.MODE_PRIVATE);

        binding = FragmentEspacevertBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
      //  String loginData = sp.getString("login_data", "");



        // instantiate ViewModel
        EspacevertViewModel mEspacevertViewModel = new ViewModelProvider(this).get(EspacevertViewModel.class);

        // reception initial les données
        try {
            mEspacevertViewModel.init();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        // instantiate the adapter
        EspaceVertAdapter adapter = new EspaceVertAdapter((StatisticActivity) getActivity());
        list = root.findViewById(R.id.EspacevertList);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                OpenZonesActivity(mEspacevertViewModel.getEspace().getValue().get(position));
            }
        });
        list.setAdapter(adapter);

        // set Observer of the DataLive (which is products)
        mEspacevertViewModel.getEspace().observe(getActivity(), new Observer<List<EspaceVert>>(){

            @Override
            public void onChanged(List<EspaceVert> espaceVerts) {
                Log.i(TAG, "onchanged in observer is called!");
                Log.i("data", espaceVerts.toString());
                adapter.setObjects(espaceVerts);
                adapter.notifyDataSetChanged();
            }
        });

        return root;
    }

    private void OpenZonesActivity(EspaceVert espaceVert) {
        Log.i(TAG, "OpenZonesActivity: id = " + espaceVert.getId());
        Intent intent = new Intent(getActivity(), ZoneActivity.class);

        intent.putExtra("espace_id", espaceVert.getId());

        startActivity(intent);
    }

}