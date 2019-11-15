package org.ruppin.roper.Utils;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.Base64;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Utils {

    /*Endocing a back authentication header with a given username and password.*/
    public static String BasicAuthorizationEncoder(String username, String password) throws Exception {
        String userCredentials = username + ":" + password;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userCredentials.getBytes()));
            return basicAuth;
        }
        else
        {
            throw new Exception("Cant create the basic auth");
        }
    }

    /*Initialize retrofit with url and basic authentication header.*/
    public static Retrofit retrofitInit(String url,String username, String password)
    {
        Gson gson = new GsonBuilder().serializeNulls().create();

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request newRequest = null;
                        try {
                            newRequest = chain.request().newBuilder()
                                    .addHeader("Authorization", Utils.BasicAuthorizationEncoder(username, password))
                                    .build();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return chain.proceed(newRequest);
                    }
                })
                .addInterceptor(loggingInterceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();

        return retrofit;

    }
}
