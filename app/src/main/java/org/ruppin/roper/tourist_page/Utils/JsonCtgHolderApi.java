package org.ruppin.roper.tourist_page.Utils;

import org.ruppin.roper.tourist_page.Models.Categories;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface JsonCtgHolderApi {
    @GET
    Call<List<Categories>> getCtgrs(@Url String url);
}
