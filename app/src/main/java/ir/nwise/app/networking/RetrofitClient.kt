package ir.nwise.app.networking

import android.content.Context
import android.net.ConnectivityManager
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import ir.nwise.app.common.Config
import ir.nwise.app.domain.NetworkManager
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

fun providesGsonConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl(Config.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()
}

fun provideOkHttpClient(connectivityManager: NetworkManager, context: Context): OkHttpClient {
    val cacheSize = (5 * 1024 * 1024).toLong()

    val myCache = Cache(context.cacheDir, cacheSize)


    val builder = OkHttpClient.Builder()
        .readTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .connectTimeout(15, TimeUnit.SECONDS)
        // Specify the cache we created earlier.
        .cache(myCache)
        // Add an Interceptor to the OkHttpClient.
        .addInterceptor { chain ->
            // Get the request from the chain.
            var request = chain.request()
            val url = request.url.newBuilder()
                .addQueryParameter("key", Config.API_KEY)
                .build()
            request = request.newBuilder()
                .url(url)
                .build()
            /*
            *  Leveraging the advantage of using Kotlin,
            *  we initialize the request and change its header depending on whether
            *  the device is connected to Internet or not.
            */
            request = if (connectivityManager.hasNetwork())
            /*
            *  If there is Internet, get the cache that was stored 5 seconds ago.
            *  If the cache is older than 5 seconds, then discard it,
            *  and indicate an error in fetching the response.
            *  The 'max-age' attribute is responsible for this behavior.
            */
                request.newBuilder().header("Cache-Control", "public, max-age=" + 5).build()
            else
            /*
            *  If there is no Internet, get the cache that was stored 7 days ago.
            *  If the cache is older than 7 days, then discard it,
            *  and indicate an error in fetching the response.
            *  The 'max-stale' attribute is responsible for this behavior.
            *  The 'only-if-cached' attribute indicates to not retrieve new data; fetch the cache only instead.
            */
                request.newBuilder().header(
                    "Cache-Control",
                    "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7
                ).build()


            // End of if-else statement

            // Add the modified request to the chain.
            chain.proceed(request)
        }
    val logging = HttpLoggingInterceptor()
    logging.level = HttpLoggingInterceptor.Level.BODY
    builder.addNetworkInterceptor(logging)  // <-- this is the important line!
    return builder.build()
}

fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)