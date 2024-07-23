package com.mobdeve.s11.group07.mco3.wanderfriend

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import java.security.cert.CertificateException
import javax.net.ssl.*

object RetrofitInstance {
    private const val BASE_URL = "https://restcountries.com/"

    private val okHttpClient: OkHttpClient
        get() {
            return try {
                val trustAllCerts = arrayOf<TrustManager>(
                    object : X509TrustManager {
                        override fun checkClientTrusted(chain: Array<java.security.cert.X509Certificate?>?, authType: String?) {}

                        override fun checkServerTrusted(chain: Array<java.security.cert.X509Certificate?>?, authType: String?) {}

                        override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate?>? = arrayOf()
                    }
                )

                val sslContext = SSLContext.getInstance("SSL")
                sslContext.init(null, trustAllCerts, java.security.SecureRandom())

                val sslSocketFactory = sslContext.socketFactory

                OkHttpClient.Builder()
                    .sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
                    .hostnameVerifier { _, _ -> true }
                    .build()
            } catch (e: Exception) {
                throw RuntimeException(e)
            }
        }

    val api: RestCountriesApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RestCountriesApi::class.java)
    }
}