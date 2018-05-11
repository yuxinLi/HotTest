package com.example.hottest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.hottest.retrofit.Contributor;
import com.example.hottest.retrofit.UserService;
import com.example.hottest.retrofit.UserServiceGlava;
import com.example.hottest.retrofit.calladapter.GuavaCallAdapterFactory;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defaultMehtod();
//                glava();
            }
        });
    }

    private void glava() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(GuavaCallAdapterFactory.create())
                .build();

        UserServiceGlava userService = retrofit.create(UserServiceGlava.class);
        ListenableFuture<List<Contributor>> contributor = userService.getContributor("square", "retrofit");
        Futures.addCallback(contributor, new FutureCallback<List<Contributor>>() {
            @Override
            public void onSuccess(@Nullable List<Contributor> result) {
                Log.w(TAG , result.toString());
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });

    }


    private void defaultMehtod() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserService userService = retrofit.create(UserService.class);
        Call<List<Contributor>> contributor = userService.getContributor("square", "retrofit");
        contributor.enqueue(new Callback<List<Contributor>>() {
            @Override
            public void onResponse(Call<List<Contributor>> call, Response<List<Contributor>> response) {
                Log.w(TAG , response.body().toString());
            }

            @Override
            public void onFailure(Call<List<Contributor>> call, Throwable t) {

            }
        });

    }


}


