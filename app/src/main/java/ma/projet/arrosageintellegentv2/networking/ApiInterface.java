package ma.projet.arrosageintellegentv2.networking;

import java.util.List;
import java.util.Map;

import ma.projet.arrosageintellegentv2.beans.AppUser;
import ma.projet.arrosageintellegentv2.beans.EspaceVert;
import ma.projet.arrosageintellegentv2.beans.LoginResponse;
import ma.projet.arrosageintellegentv2.beans.SensorData;
import ma.projet.arrosageintellegentv2.beans.reponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface ApiInterface {
    @Headers({ "Content-Type: application/json;charset=UTF-8"})

    @POST("/api/login")
    Call<ResponseBody> Authentifcate(@Body Map<String, String> loginData);
    @GET("/api/mesures/m/All")
  Call <List<SensorData> >getSensorData();

    @GET("/api/mesures/m/{zone_id}")
    Call <List<SensorData> >getSensorData(@Path("zone_id") long zone_id);
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("/api/farmer")
    Call<AppUser> getUser(@Query("id") String id, @Header("Authorization") String auth);


    //@Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("/api/farmer/espaces_verts/{username}")
    Call<List<EspaceVert>> getespace(@Path("username") String username);
  /*  @GET("/api/products")
    Call<List<Product>> getProducts();

    @GET("/products/{id}")
    Call<Product> getProduct(@Path("id") int id);

    @GET("/products/{id}/sellers")
    Call<Seller> getProductSellers(@Path("id") int id);
*/
}
