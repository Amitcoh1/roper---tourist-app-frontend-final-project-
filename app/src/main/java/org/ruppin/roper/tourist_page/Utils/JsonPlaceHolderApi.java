package org.ruppin.roper.tourist_page.Utils;



import org.ruppin.roper.tourist_page.Models.Business;
import org.ruppin.roper.tourist_page.Models.GooglePlace;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface JsonPlaceHolderApi {

    @GET("business_service/nearByBusinessesByType")
    Call<List<Business>> getBusinesses(
            @Query("radius") Double radius,
            @Query("longitude") String lng,
            @Query("latitude") String lat,
            @Query("type") String type
    );
    @GET
    Call<GooglePlace> getPlaces(@Url String url);

    @GET
    Call<GooglePlace> nextPage(
            @Query("pagetoken") String token,
            @Query("key") String key
    );
}
