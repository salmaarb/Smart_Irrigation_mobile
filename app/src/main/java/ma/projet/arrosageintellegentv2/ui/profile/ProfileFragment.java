package ma.projet.arrosageintellegentv2.ui.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import ma.projet.arrosageintellegentv2.R;
import ma.projet.arrosageintellegentv2.beans.AppUser;
import ma.projet.arrosageintellegentv2.beans.EspaceVert;
import ma.projet.arrosageintellegentv2.networking.ApiClient;
import ma.projet.arrosageintellegentv2.networking.ApiInterface;
import ma.projet.arrosageintellegentv2.utils.Crendentials;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class ProfileFragment extends Fragment {

    private ProfileViewModel mViewModel;
    SharedPreferences sp;
    TextView txt_id, txt_name, txt_role, txt_address, txt_phone;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    private static final String TAG = "ProfileFragment";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        sp = requireActivity().getSharedPreferences("login_data", Context.MODE_PRIVATE);

        txt_id = view.findViewById(R.id.txt_id);
        txt_name = view.findViewById(R.id.txt_name);
        txt_role = view.findViewById(R.id.txt_role);
        txt_address = view.findViewById(R.id.txt_address);
        txt_phone = view.findViewById(R.id.txt_phone);

        String loginData = sp.getString("login_data", "");

        try {
            JSONObject userData = new JSONObject(loginData);

            if (userData.has("user")) {
                JSONObject userObject = userData.getJSONObject("user");

                if (userObject.has("id")) {
                    String id = userObject.getString("id");
                    txt_id.setText(id);

                }

                if (userObject.has("username")) {
                    String name = userObject.getString("username");
                    txt_name.setText(name);

                }

                if (userObject.has("role")) {
                    String role = userObject.getString("role");

                    txt_role.setText(role);
                }

                if (userObject.has("address")) {
                    String address = userObject.getString("address");
                    txt_address.setText(address);

                }

                if (userObject.has("phone")) {
                    String phone = userObject.getString("phone");  txt_phone.setText(phone);

                }


        }

        return view;
    } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }}