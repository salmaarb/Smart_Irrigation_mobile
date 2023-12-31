package ma.projet.arrosageintellegentv2;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ma.projet.arrosageintellegentv2.R;
import ma.projet.arrosageintellegentv2.beans.AppUser;
import ma.projet.arrosageintellegentv2.beans.EspaceVert;
import ma.projet.arrosageintellegentv2.beans.LoginResponse;
import ma.projet.arrosageintellegentv2.beans.reponse;
import ma.projet.arrosageintellegentv2.networking.ApiClient;
import ma.projet.arrosageintellegentv2.networking.ApiInterface;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.google.gson.Gson;
import com.onesignal.Continue;
import com.onesignal.OneSignal;
import com.onesignal.debug.LogLevel;

public class LoginActivity extends AppCompatActivity {
    private EditText username;
    private EditText password;
    private Button button;
    public static SharedPreferences sharedPreferences;
    public static SharedPreferences sharedPreferences1;
  //  public static SharedPreferences sharref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Verbose Logging set to help debug issues, remove before releasing your app.

        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
    //    sharref = getSharedPreferences("access_token", Context.MODE_PRIVATE);

      sharedPreferences = getSharedPreferences("login_data", Context.MODE_PRIVATE);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        Button loginBtn = findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(username.getText().toString()) || TextUtils.isEmpty(password.getText().toString())) {
                    Toast.makeText(LoginActivity.this, "Username / Password Required", Toast.LENGTH_LONG).show();
                } else {
                    login();
                }
            }
        });

      //  button=findViewById(R.id.btnNotications);
  /*      if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.S_V2)
        {
            if(ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.ACCESS_NOTIFICATION_POLICY)
            != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(LoginActivity.this,new String[]{Manifest.permission.ACCESS_NOTIFICATION_POLICY},101);
            }
        }*/
  /*      button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makenotifications();
            }
        });*/
    }
    public void makenotifications(){
        String channelID="CHANNEL_ID_NOTIFICATION";
        NotificationCompat.Builder builder=new NotificationCompat.Builder(getApplicationContext(),channelID);
        builder.setSmallIcon(R.drawable.notif)
   .setContentTitle("Notification title")
   .setContentText("Some text for notification here")
   .setAutoCancel(true).setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManager notificationManager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
            NotificationChannel notificationChannel=notificationManager.getNotificationChannel(channelID);
            if(notificationChannel==null){
                int importanace =NotificationManager.IMPORTANCE_HIGH;
                notificationChannel=new NotificationChannel(channelID,"some description",importanace);
                notificationChannel.setLightColor(Color.GREEN);
                notificationChannel.enableVibration(true);
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }
notificationManager.notify(0,builder.build());
    }
    private void login() {
        String usernameStr = username.getText().toString();
        String passwordStr = password.getText().toString();
        // Create a Map for the login data
        Map<String, String> loginData = new HashMap<>();
        loginData.put("username", usernameStr);
        loginData.put("password", passwordStr);
        System.out.println(loginData);
        System.out.println(passwordStr);
        // Make a POST request to the "/login" endpoint
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> loginResponseCall = apiInterface.Authentifcate(loginData);
        loginResponseCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // reponse loginResponse = response.body();
                    try {
                        String responseBodyString = response.body().string();
                        Gson gson = new Gson();
                        reponse loginResponse = gson.fromJson(responseBodyString, reponse.class);
                        System.out.println(loginResponse);
                        if (loginResponse != null) {
                            // Extraire la liste des espaces verts
                            List<EspaceVert> espacesVerts = loginResponse.getEspacesVerts();
                            System.out.println(espacesVerts);
                            if (espacesVerts != null && !espacesVerts.isEmpty()) {
                                // Imprimer ou traiter la liste des espaces verts selon vos besoins
                                for (EspaceVert espaceVert : espacesVerts) {
                                    System.out.println("Espace Vert: " + espaceVert.getLibelle());
                                }
                            } else {
                                System.out.println("Liste des espaces verts est vide ou nulle.");
                            }
                            // Reste du code pour sauvegarder d'autres informations ou effectuer d'autres actions
                            System.out.println(responseBodyString);
                            SharedPreferences.Editor myEditor = sharedPreferences.edit();
                            myEditor.putString("login_data", responseBodyString);
                            myEditor.apply();
                            Log.i("login_data", responseBodyString);
                            Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(LoginActivity.this, StatisticActivity.class);
                            startActivity(intent);
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }


            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("LoginActivity", "Login API Call Failed: " + t.getMessage(), t);
                Toast.makeText(LoginActivity.this, "Login API Call Failed: " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }
}
