package org.android.tiffinseva.networking;

import org.android.tiffinseva.AppConstants;
import org.android.tiffinseva.utils.AppSharedPrefRepository;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static org.android.tiffinseva.ApiConstant.BASE_URL;

public class TTSRetrofitClient {
    private static volatile Retrofit retrofitInstance;

    private TTSRetrofitClient() {

    }

    public static Retrofit getInstance() {
        if (retrofitInstance == null) {
            synchronized (TTSRetrofitClient.class) {
                if (retrofitInstance == null) {
                    retrofitInstance = new retrofit2.Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .client(httpLoggingInterceptor())
                            .build();
                }
            }
        }
        return retrofitInstance;
    }

    private static OkHttpClient httpLoggingInterceptor() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder.addInterceptor(new Interceptor() {
            @NotNull
            @Override
            public Response intercept(@NotNull Chain chain) throws IOException {
                Request.Builder builder = chain.request().newBuilder();
                builder.header(AppConstants.KEY_TOKEN, AppSharedPrefRepository.getInstance().getString(AppConstants.KEY_TOKEN, ""));
                return chain.proceed(builder.build());
            }
        });
        okHttpClientBuilder.addInterceptor(httpLoggingInterceptor);
        return okHttpClientBuilder.build();
    }
}
