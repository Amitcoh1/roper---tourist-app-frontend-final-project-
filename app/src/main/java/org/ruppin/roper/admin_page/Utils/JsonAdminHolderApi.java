package org.ruppin.roper.admin_page.Utils;
import org.ruppin.roper.admin_page.AdminDto;
import org.ruppin.roper.admin_page.Person;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Url;

public interface  JsonAdminHolderApi {
    @GET
    Call<AdminDto> getSettings(@Url String url);
    @PUT("settings_service/settings")
    Call<AdminDto> updtSettings(@Body AdminDto adminDto);
    @GET
    Call<Person> getUser(@Url String url);
    @PUT("person_service/user")
    Call<Person> updtAdminProfile(@Body Person person);
}
