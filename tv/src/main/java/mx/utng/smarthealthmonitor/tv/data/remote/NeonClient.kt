package mx.utng.smarthealthmonitor.tv.data.remote

import mx.utng.smarthealthmonitor.tv.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

object NeonClient {
    private val BASE_URL = "https://${BuildConfig.NEON_HOST}/"
 
    val AUTH_HEADER  = "Bearer ${BuildConfig.NEON_API_KEY}"
    val CONN_STRING  = "postgresql://${BuildConfig.NEON_USER}:${BuildConfig.NEON_PASSWORD}@${BuildConfig.NEON_HOST}/${BuildConfig.NEON_DB}?sslmode=require"
 
    val api: NeonApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
            .client(OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
                .hostnameVerifier { _, _ -> true }
                .build())
            .build()
            .create(NeonApiService::class.java)
    }
}
