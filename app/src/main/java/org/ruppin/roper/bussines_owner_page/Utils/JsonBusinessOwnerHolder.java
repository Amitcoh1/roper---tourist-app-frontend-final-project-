package org.ruppin.roper.bussines_owner_page.Utils;

import org.ruppin.roper.bussines_owner_page.Models.BusinessBO;
import org.ruppin.roper.bussines_owner_page.Models.BusinessOwner;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Url;

public interface JsonBusinessOwnerHolder
{
    @GET
    Call<BusinessOwner> getBusinessOwner(@Url String url);
    @PUT("business_owner_service/businessOwner")
    Call<BusinessOwner> updateBOinfo(@Body BusinessOwner businessOwner);
    @POST
    Call<BusinessBO> addBusiness(@Url String url, @Body BusinessBO business);
    @HTTP(method = "DELETE", path = "business_service/business", hasBody = true)
    Call<BusinessBO> deleteBusiness(@Body BusinessBO business);
    @PUT("business_service/business")
    Call<BusinessBO> updateBsns(@Body ArrayList<BusinessBO> business);
    @GET
    Call<ArrayList<BusinessBO>> getBussinesses(@Url String url);
    @GET
    Call<BusinessBO> getBsns(@Url String url);
}

