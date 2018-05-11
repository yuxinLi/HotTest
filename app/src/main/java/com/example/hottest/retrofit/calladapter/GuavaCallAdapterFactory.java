package com.example.hottest.retrofit.calladapter;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;

/**
 * 描    述：
 * 作    者：liyx@13322.com
 * 时    间：2018/5/11
 */
public class GuavaCallAdapterFactory extends CallAdapter.Factory {


    public static GuavaCallAdapterFactory create() {
        return new GuavaCallAdapterFactory();
    }

    @Override
    public CallAdapter<?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        if (getRawType(returnType) != ListenableFuture.class) {
            return null;
        }
        if (!(returnType instanceof ParameterizedType)) {
            throw new IllegalStateException("ListenableFuture return type must be parameterized"
                    + " as ListenableFuture<Foo> or ListenableFuture<? extends Foo>");
        }
        final Type responseType = getParameterUpperBound(0, (ParameterizedType) returnType);

        return new CallAdapter<ListenableFuture<?>>() {
            @Override
            public Type responseType() {
                return responseType;
            }

            @Override
            public <R> ListenableFuture<?> adapt(final Call<R> call) {
                ListeningExecutorService executorService = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());
                ListenableFuture<R> listenableFuture = executorService.submit(new Callable<R>() {
                    @Override
                    public R call() throws Exception {
                        return call.execute().body();
                    }
                });
                return listenableFuture;
            }
        };
    }
}
