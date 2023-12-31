package ma.projet.arrosageintellegentv2.networking;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    //private static final String BASE_URL = "http://192.168.56.1:8091";
    private static final String BASE_URL = "http://192.168.1.23:8080";

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
    public static ApiInterface getUserService() {
        ApiInterface APIservice = getClient().create(ApiInterface.class);

        return APIservice;
    }
}

