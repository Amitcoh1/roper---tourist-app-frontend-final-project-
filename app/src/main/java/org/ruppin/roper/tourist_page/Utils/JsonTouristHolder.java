package org.ruppin.roper.tourist_page.Utils;
import org.ruppin.roper.tourist_page.Models.Tourist;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Url;

public interface JsonTouristHolder {
    @GET
    Call<Tourist> getTourist(@Url String url);
    @PUT("tourist_service/tourist")
    Call<Tourist> updateTouristInfo(@Body Tourist tourist);
    @PUT
    Call<Tourist> addQuestToTourist(@Url String url , @Body Tourist tourist);
    @HTTP(method = "DELETE",hasBody = true)
    Call<Tourist> deleteQuestFromTourist(@Url String url , @Body Tourist tourist);
}
