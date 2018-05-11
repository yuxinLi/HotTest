package com.example.hottest.retrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * 描    述：
 * 作    者：liyx@13322.com
 * 时    间：2018/5/11
 */
public interface UserService {

    @GET("repos/{owner}/{repo}/contributors")
    Call<List<Contributor>> getContributor(
            @Path("owner") String owner ,
            @Path("repo") String repo);

}
